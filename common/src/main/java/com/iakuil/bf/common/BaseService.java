package com.iakuil.bf.common;

import com.iakuil.bf.common.db.CrudMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.annotation.Version;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

/**
 * 泛型化的BaseService
 * <p>提供一些业务层常用的公用方法。
 * <p>不与Entity对应的Service无需继承此类。
 *
 * @author Kai
 */
@Slf4j
@Service
public abstract class BaseService<T extends BaseEntity> {

    @Autowired
    private CrudMapper<T> mapper;

    @Transactional(readOnly = true)
    public List<T> query(T entity) {
        return mapper.select(entity);
    }

    @Transactional(readOnly = true)
    public PageData<T> queryPage(T entity) {
        return new PageData<>(mapper.select(entity));
    }

    @Transactional(readOnly = true)
    public T queryById(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Transactional(readOnly = true)
    public T queryOne(T entity) {
        return mapper.selectOne(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean delete(T entity) {
        return mapper.delete(entity) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Long id) {
        log.info("users {} were deleted!", id);
        return mapper.deleteByPrimaryKey(id) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByIds(String... ids) {
        int count = mapper.deleteByIds(String.join(",", ids));
        log.info("{} users were deleted!", count);
        return count > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean save(T entity) {
        return mapper.insertSelective(entity) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatch(List<T> entities) {
        return mapper.insertList(entities) == entities.size();
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean update(T entity) {
        //TODO 判空
        return mapper.updateByPrimaryKeySelective(entity) > 0;
    }

    /**
     * 新增或者修改Entity
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdate(T entity) {
        boolean succ = false;
        Long id = entity.getId();
        if (id != null) {
            succ = mapper.insert(entity) > 0;
        } else {
            T before = mapper.selectByPrimaryKey(id);
            if (before == null) {
                throw new IllegalStateException("无效的ID！");
            } else {
                //TODO 测试下注解是否生效，有用就删掉这几行
                if (hasAnnotation(entity, Version.class)) { // 处理乐观锁
                    setValueByAnnotation(entity, ObjectUtils.defaultIfNull(getValueByAnnotation(before, Version.class), 1), Version.class);
                }
                succ = mapper.updateByPrimaryKeySelective(entity) > 0;
            }
        }

        return succ;
    }

    /**
     * 批量新增或者修改Entity
     * <p>一次操作失败则全部回滚。
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateBatch(Collection<T> entityList) {
        for (T obj : entityList) {
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
