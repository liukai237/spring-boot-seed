package com.iakuil.seed.common;

import com.iakuil.seed.common.tool.ApplicationContextHolder;
import com.iakuil.seed.common.tool.Strings;
import com.iakuil.seed.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;
import tk.mybatis.mapper.annotation.Version;

import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;

/**
 * 非泛型化的BaseService
 */
@Slf4j
@Service
public abstract class BaseService {

    /**
     * 新增或者修改Entity
     * <p>Entity必须创建对应的xxxMapper。</p>
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdate(Object entity) {
        boolean succ = false;
        if (hasAnnotation(entity, Table.class)) {
            String mapperName = Strings.toLowerCaseFirstLetter(ClassUtils.getShortName(entity.getClass())) + "Mapper";
            log.debug("mapper name: [{}]", mapperName);
            final CrudMapper mapper = ApplicationContextHolder.getBean(mapperName);
            Object id = getValueByAnnotation(entity, Id.class);

            if (id == null) {
                succ = mapper.insert(entity) > 0;
            } else {
                Object before = mapper.selectByPrimaryKey(id);
                if (before == null) {
                    throw new BusinessException("无效的ID！");
                } else {
                    if (hasAnnotation(entity, Version.class)) { // 处理乐观锁
                        setValueByAnnotation(entity, ObjectUtils.defaultIfNull(getValueByAnnotation(before, Version.class), 1), Version.class);
                    }
                    succ = mapper.updateByPrimaryKeySelective(entity) > 0;
                }
            }
        }

        return succ;
    }

    private boolean hasAnnotation(Object entity, Class annotation) {
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (AnnotationUtils.getAnnotation(field, annotation) != null) {
                return true;
            }
        }

        return false;
    }

    private Object getValueByAnnotation(Object entity, Class annotation) {
        Object value = null;
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (AnnotationUtils.getAnnotation(field, annotation) != null) {
                field.setAccessible(true);
                try {
                    value = field.get(entity);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                field.setAccessible(false);
            }
        }

        return value;
    }

    private void setValueByAnnotation(Object entity, Object value, Class annotation) {
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (AnnotationUtils.getAnnotation(field, annotation) != null) {
                field.setAccessible(true);
                try {
                    field.set(entity, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                field.setAccessible(false);
            }
        }
    }
}
