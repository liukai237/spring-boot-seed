package com.iakuil.bf.common;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 带缓存功能的通用Service（试验功能）
 *
 * @param <T> 实体类型
 * @author Kai
 */
@Slf4j
public abstract class CacheableService<T extends BaseEntity> extends BaseService<T> {

    @CreateCache(name = "com.iakuil.entity:", expire = 100, cacheType = CacheType.BOTH, localLimit = 10000)
    protected Cache<Long, T> entityCache;

    @Transactional(readOnly = true)
    @Override
    public T findById(Long id) {
        T target = entityCache.get(id);
        if (target == null) {
            target = super.findById(id);
            if (target != null) {
                entityCache.put(id, target);
            }
        }

        return target;
    }

    @Transactional(readOnly = true)
    @Override
    public T findOne(T entity) {
        Long id = entity.getId();
        if (id == null) {
            return super.findOne(entity);
        }

        T target = entityCache.get(id);
        if (target == null) {
            target = super.findOne(entity);
            if (target != null) {
                entityCache.put(id, target);
            }
        }

        return target;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean add(T entity) {
        boolean added = super.add(entity);
        if (added) {
            entityCache.put(entity.getId(), entity);
        }
        return added;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addAll(List<T> entities) {
        boolean success = super.addAll(entities);
        if (success) {
            entityCache.putAll(entities.stream()
                    .filter(item -> item.getId() == null)
                    .collect(Collectors.toMap(BaseEntity::getId, v -> v)));
        }
        return success;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean modify(T entity) {
        boolean modified = super.modify(entity);
        if (modified) {
            entityCache.put(entity.getId(), entity);
        }
        return modified;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean modifyWithVersion(T entity) {
        boolean modified = super.modifyWithVersion(entity);
        if (modified) {
            entityCache.put(entity.getId(), entity);
        }
        return modified;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeById(Long id) {
        entityCache.remove(id);
        return super.removeById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeByIds(Long... ids) {
        boolean removed = super.removeByIds(ids);
        if (removed) {
            entityCache.removeAll(Sets.newHashSet(ids));
        }
        return removed;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean remove(T entity) {
        Long id = entity.getId();
        if (id != null) {
            entityCache.remove(id);
        }
        return super.remove(entity);
    }
}
