package com.yodinfo.seed.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.yodinfo.seed.exception.BusinessException;
import com.yodinfo.seed.bo.AbstractReq;
import com.yodinfo.seed.exception.JsonBodyNotValidException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.beans.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Iterator;

@ControllerAdvice
public class JsonBodyValidatorAdvice implements RequestBodyAdvice {
    private final static JsonSchemaFactory factory = JsonSchemaFactory.byDefault();

    private static final Class[] annos = {
            RequestMapping.class,
            GetMapping.class,
            PostMapping.class,
            DeleteMapping.class,
            PutMapping.class
    };

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        AnnotatedElement element = methodParameter.getAnnotatedElement();
        return Arrays.stream(annos).anyMatch(anno -> anno.isAnnotation() && element.isAnnotationPresent(anno));
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        try {
            return new JsvHttpInputMessage(httpInputMessage, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Object afterBodyRead(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        if (o instanceof AbstractReq) {
            Object params = ((AbstractReq) o).getFilter();
            handleParamsInBody(params); // 处理空值
        }

        return o;
    }

    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }

    private void handleParamsInBody(Object obj) {
        if (obj == null) {
            return;
        }

        BeanInfo beanInfo;

        try {
            beanInfo = Introspector.getBeanInfo(obj.getClass());
        } catch (IntrospectionException e) {
            throw new IllegalStateException(e); // it can't happen here
        }

        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();

            // 过滤class属性
            if (key.equals("class")) {
                continue;
            }

            // 得到property对应的getter方法
            Method getter = property.getReadMethod();
            if (getter.getAnnotation(Transient.class) != null) {
                continue; // 带Transient注解的不校验
            }
            Object tmpValue;
            try {
                tmpValue = getter.invoke(obj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException(e);
            }

            Method setter = property.getWriteMethod();
            try {
                // 处理空字符串
                if (tmpValue instanceof String) {
                    String v = (String) tmpValue;
                    if (StringUtils.isBlank(v)) {
                        setter.invoke(obj, (Object) null);
                    }
                }

                // 过滤数组中空白字符串成员
                if (tmpValue instanceof String[]) {
                    String[] v = (String[]) tmpValue;
                    String[] arr = Arrays.stream(v).filter(StringUtils::isNoneBlank).toArray(String[]::new);
                    setter.invoke(obj, (Object) arr);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    class JsvHttpInputMessage implements HttpInputMessage {
        private HttpHeaders headers;

        private InputStream body;

        private Type type;

        public JsvHttpInputMessage(HttpInputMessage inputMessage, Type type) throws Exception {
            this.headers = inputMessage.getHeaders();
            this.body = inputMessage.getBody();
            this.type = type;
        }

        @Override
        public InputStream getBody() throws IOException {
            InputStream jsvIs = getClass().getResourceAsStream("/jsv/" + this.type.getTypeName() + ".json");
            if (jsvIs != null) {
                JsonNode b = JsonLoader.fromReader(new InputStreamReader(this.body));
                JsonNode v = JsonLoader.fromReader(new InputStreamReader(jsvIs));
                try {
                    JsonSchema schema = factory.getJsonSchema(v);
                    ProcessingReport r = schema.validate(b);
                    if (!r.isSuccess()) {
                        Iterator<ProcessingMessage> i = r.iterator();
                        if (i.hasNext()) {
                            ProcessingMessage pm = i.next();
                            throw new JsonBodyNotValidException(pm.getMessage(), pm.toString());
                        }
                    }
                } catch (ProcessingException e) {
                    e.printStackTrace();
                }

                return IOUtils.toInputStream(b.toString(), "UTF-8");
            } else {
                return this.body;
            }
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }
    }

}
