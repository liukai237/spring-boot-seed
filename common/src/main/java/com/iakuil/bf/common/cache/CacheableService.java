package com.iakuil.bf.common.cache;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.google.common.collect.Sets;
import com.iakuil.bf.common.BaseEntity;
import com.iakuil.bf.common.BaseService;
import com.iakuil.bf.common.constant.CacheConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 带两级缓存功能的通用Service（基于JetCache）
 *
 * <p>避免联表查询一般有两种思路：
 * <ul>
 *     <li>1、 冗余字段，后台保证数据的一致性，前端只需要基于ORM的单表查询即可。</li>
 *     <li>2、 使用多个基于主键的查询来替代Join查询，利用数据库和自定义两级缓存保证效率。</li>
 * </ul>
 *
 * <p>本类就是基于第二种思路的产物。
 * <p>其中所有的方法自带默认缓存策略，即使子类覆写了本类中的方法，缓存依然可能生效。
 * 假设子类方法如下，依然可以缓存：
 * <pre>{@code
 * @Override
 * public boolean modify(Test entity) {
 *   // do something
 *   return super.modify(entity);
 * }
 * }</pre>
 * <p>如果通用的缓存策略不适合业务需求，请不要继承本类，应该借助JetCache框架另行实现。
 *
 * @param <T> 实体类型
 * @author Kai
 */
@Slf4j
public abstract class CacheableService<T extends BaseEntity> extends BaseService<T> {

    @CreateCache(name = CacheConstant.CACHE_KEY_PREFIX, expire = 100, cacheType = CacheType.BOTH, localLimit = 10000)
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
                    .filter(item -> item.getId() != null)
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
