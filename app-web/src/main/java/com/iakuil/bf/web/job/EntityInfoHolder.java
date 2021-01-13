package com.iakuil.bf.web.job;

import com.google.common.collect.Maps;
import com.iakuil.bf.common.tool.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.reflections.Reflections;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.annotation.LogicDelete;

import javax.persistence.Column;
import javax.persistence.Table;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 启动完成后扫描所有Entity并记录属性名和字段名
 *
 * @author Kai
 */
@Slf4j
@Component
public class EntityInfoHolder implements InitializingBean {

    private static Map<Class<?>, Map<String, String>> FIELD_AND_COLUMNS = Maps.newHashMap();

    private static Map<Class<?>, String> ANNOTATED_LOGINC_DELETE_COLUMN = Maps.newHashMap();

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    public static String getLogicDeleteColumn(Class<?> type, String defaultColumn) {
        String logicDelete = ANNOTATED_LOGINC_DELETE_COLUMN.get(type);
        if (logicDelete == null) {
            Map<String, String> map = FIELD_AND_COLUMNS.get(type);
            //TODO 默认字段应该可配置
            return map.get(defaultColumn);
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Configuration configuration = sqlSessionFactory.getConfiguration();
        boolean mapUnderscoreToCamelCase = configuration.isMapUnderscoreToCamelCase();

        // 记录被@Table注解注释的Entity类
        Reflections reflections = new Reflections("com.iakuil.bf.dao.entity");
        Set<Class<?>> entityTypes = reflections.getTypesAnnotatedWith(Table.class);
        for (Class<?> clazz : entityTypes) {
            Map<String, String> fieldAndColumns = ObjectUtils.defaultIfNull(FIELD_AND_COLUMNS.get(clazz), Maps.newHashMap());
            BeanInfo beanInfo;
            try {
                beanInfo = Introspector.getBeanInfo(clazz);
            } catch (IntrospectionException e) {
                throw new IllegalStateException(e); // it can't happen here
            }

            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String fieldName = property.getName();

                // 过滤class属性
                if ("class".equals(fieldName)) {
                    continue;
                }

                Field field;
                try {
                    field = clazz.getDeclaredField(fieldName);
                } catch (NoSuchFieldException e) {
                    throw new IllegalStateException(e); // it can't happen here
                }

                String columnName = mapUnderscoreToCamelCase ? Strings.toUnderlineCase(fieldName) : fieldName;
                Column columnAnnotation = AnnotationUtils.findAnnotation(field, Column.class);
                if (columnAnnotation != null && StringUtils.isBlank(columnAnnotation.name())) {
                    columnName = columnAnnotation.name();
                }
                fieldAndColumns.put(fieldName, columnName);

                LogicDelete logicDelete = AnnotationUtils.findAnnotation(clazz, LogicDelete.class);
                if (logicDelete != null) {
                    ANNOTATED_LOGINC_DELETE_COLUMN.put(clazz, columnName);
                }
            }

            FIELD_AND_COLUMNS.put(clazz, fieldAndColumns);
        }

        // workaround，记录在XML定义的ResultMap
        Collection<ResultMap> resultMaps = configuration.getResultMaps();
        for (Object obj : resultMaps) {
            if (obj instanceof ResultMap) {
                ResultMap rm = (ResultMap) obj;
                Class<?> type = rm.getType();
                List<ResultMapping> mappings = rm.getResultMappings();
                if (mappings.size() == 0) {
                    continue;
                }
                Map<String, String> entityInfo = ObjectUtils.defaultIfNull(FIELD_AND_COLUMNS.get(type), Maps.newHashMap());
                for (ResultMapping mapping : mappings) {
                    entityInfo.putIfAbsent(mapping.getProperty(), mapping.getColumn());
                }

                FIELD_AND_COLUMNS.put(type, entityInfo);
            }
        }

        log.info("Mapping size: [{}]", FIELD_AND_COLUMNS.size());
    }
}
