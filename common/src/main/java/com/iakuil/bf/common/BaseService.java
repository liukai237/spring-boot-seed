package com.iakuil.bf.common;

import com.iakuil.bf.common.db.CrudMapper;
import com.iakuil.bf.common.tool.ApplicationContextHolder;
import com.iakuil.bf.common.tool.Strings;
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
import java.util.Collection;

/**
 * 非泛型化的BaseService
 * <p>提供一些业务层常用的公用方法。</p>
 *
 * @author Kai
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
                    throw new IllegalStateException("无效的ID！");
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

    /**
     * 批量新增或者修改Entity
     * <p>一次操作失败则全部回滚。</p>
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateBatch(Collection<Object> entityList) {
        for (Object obj : entityList) {
            if (!saveOrUpdate(obj)) {
                throw new IllegalStateException("saveOrUpdatePatch fail!");
            }
        }
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
                    throw new IllegalStateException(e);
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
                    throw new IllegalStateException(e);
                }
                field.setAccessible(false);
            }
        }
    }
}
