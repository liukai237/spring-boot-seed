package com.iakuil.bf.common.http;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.VirtualBeanPropertyWriter;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.SimpleBeanPropertyDefinition;
import com.iakuil.bf.common.DictPool;
import com.iakuil.bf.common.annotation.DictType;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 字典注解拦截器
 */
@Component
public class DictAnnotationIntrospector extends JacksonAnnotationIntrospector {

    public DictAnnotationIntrospector() {

    }

    @Override
    public void findAndAddVirtualProperties(MapperConfig<?> config, AnnotatedClass ac, List<BeanPropertyWriter> properties) {
        super.findAndAddVirtualProperties(config, ac, properties);
        for (Iterator<AnnotatedField> iter = ac.fields().iterator(); iter.hasNext(); ) {
            AnnotatedField field = iter.next();
            if (field.hasAnnotation(DictType.class)) {
                DictType dict = field.getAnnotation(DictType.class);
                VirtualBeanPropertyWriter vbpw = new DictNamePropertyWriter();
                AnnotatedMember member = new DictVirtualAnnotatedMember(ac, field, dict.value());
                SimpleBeanPropertyDefinition propDef = SimpleBeanPropertyDefinition.construct(config, member);
                properties.add(vbpw.withConfig(config, ac, propDef, config.constructType(String.class)));
            }
        }
    }

    /**
     * 封装被字典注解的字段信息
     */
    public class DictVirtualAnnotatedMember extends VirtualAnnotatedMember {
        protected AnnotatedField dictField;
        protected String dictCode;

        public DictVirtualAnnotatedMember(TypeResolutionContext typeContext, AnnotatedField field, String dictCode) {
            super(typeContext, String.class, field.getName() + "Name", TypeFactory.defaultInstance().constructType(String.class));
            this.dictField = field;
            this.dictField.fixAccess(true); //坑爹的可见性
            this.dictCode = dictCode;
        }
    }

    public class DictNamePropertyWriter extends VirtualBeanPropertyWriter {
        public DictNamePropertyWriter() {
            super();
        }

        public DictNamePropertyWriter(BeanPropertyDefinition propDef, Annotations contextAnnotations, JavaType declaredType) {
            super(propDef, contextAnnotations, declaredType);
        }

        @Override
        protected Object value(Object bean, JsonGenerator gen, SerializerProvider prov) throws Exception {
            if (this._member instanceof DictVirtualAnnotatedMember) {
                DictVirtualAnnotatedMember dvam = (DictVirtualAnnotatedMember) this._member;
                if (dvam.dictField.getType().getRawClass() == List.class) {
                    List<String> values = (List) dvam.dictField.getValue(bean);
                    return values.stream().map(x -> {
                        return DictPool.getInstance().getDictName(dvam.dictCode, x.toString());
                    }).reduce(String::join).orElseGet(() -> {
                        return null;
                    });
                }
                String key = String.valueOf(dvam.dictField.getValue(bean));
                if (key.contains(",")) {
                    return Arrays.stream(key.split(","))
                            .map(x -> {
                                return DictPool.getInstance().getDictName(dvam.dictCode, x);
                            }).reduce(String::join);
                }
                return DictPool.getInstance().getDictName(dvam.dictCode, key);
            }
            return null;
        }

        @Override
        public VirtualBeanPropertyWriter withConfig(MapperConfig<?> config, AnnotatedClass declaringClass, BeanPropertyDefinition propDef, JavaType type) {
            return new DictNamePropertyWriter(propDef, null, type);
        }
    }
}
