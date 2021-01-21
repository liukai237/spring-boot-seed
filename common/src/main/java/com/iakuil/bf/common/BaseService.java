package com.iakuil.bf.common;

import com.iakuil.bf.common.db.CrudMapper;
import com.iakuil.bf.common.tool.ReflectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.annotation.Version;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 泛型化的Service基类
 *
 * <p>提供一些业务层常用的公用方法，主要针对单表操作。
 * <p>不与Entity对应的Service无需继承此类。
 *
 * @param <T> 实体类型
 * @author Kai
 */
@Slf4j
@Service
public abstract class BaseService<T extends BaseEntity> {

    @Autowired
    private CrudMapper<T> mapper;

    /**
     * 将实体类作为查询条件进行查询
     *
     * <p>可分页，不返回分页信息
     *
     * @param entity 实体类对象
     * @return 实体列表
     */
    @Transactional(readOnly = true)
    public List<T> list(T entity) {
        return mapper.select(entity);
    }

    /**
     * 将实体类作为查询条件进行查询
     *
     * <p>可分页，不返回分页信息
     *
     * @param entity    实体类对象
     * @param converter Entity/DTO转换器
     * @return DTO列表
     */
    @Transactional(readOnly = true)
    public <R> List<R> list(T entity, Function<? super T, ? extends R> converter) {
        return mapper.select(entity).stream().map(converter).collect(Collectors.toList());
    }

    /**
     * 将实体类作为查询条件进行分页查询
     *
     * <p>同时返回分页信息
     *
     * @param entity 实体类对象
     * @return 带分页信息的实体类集合
     */
    @Transactional(readOnly = true)
    public PageData<T> page(T entity) {
        return new PageData<>(mapper.select(entity));
    }

    /**
     * 将实体类作为查询条件进行分页查询
     *
     * <p>同时返回分页信息
     *
     * @param entity    实体类对象
     * @param converter Entity/DTO转换器
     * @return 带分页信息的DTO集合
     */
    @Transactional(readOnly = true)
    public <R> PageData<R> page(T entity, Function<? super T, ? extends R> converter) {
        return new PageData<>(mapper.select(entity), converter);
    }

    /**
     * 根据ID查询实体类
     *
     * @param id 实体ID
     * @return 实体类对象
     */
    @Transactional(readOnly = true)
    public T findById(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 将实体类作为查询条件进行查询
     *
     * @param entity 实体类对象
     * @return 唯一的实体类对象
     */
    @Transactional(readOnly = true)
    public T findOne(T entity) {
        return mapper.selectOne(entity);
    }

    /**
     * 将实体类作为条件进行删除
     *
     * @param entity 实体类对象
     * @return 删除结果
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean remove(T entity) {
        return mapper.delete(entity) > 0;
    }

    /**
     * 根据ID进行删除
     *
     * @param id 实体类ID
     * @return 删除结果
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Long id) {
        log.info("users {} were deleted!", id);
        return mapper.deleteByPrimaryKey(id) > 0;
    }

    /**
     * 根据ID列表进行批量删除
     *
     * @param ids 实体类ID列表
     * @return 删除结果
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(String... ids) {
        int count = mapper.deleteByIds(String.join(",", ids));
        log.info("{} users were deleted!", count);
        return count > 0;
    }

    /**
     * 持久化实体类
     *
     * <p>null字段将会被忽略
     *
     * @param entity 实体类对象
     * @return 保存结果
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean add(T entity) {
        return mapper.insertSelective(entity) > 0;
    }

    /**
     * 批量保存
     *
     * <p>null字段将会被忽略
     * <p>注意避免长事务
     *
     * @param entities 实体类对象列表
     * @return 保存结果
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addAll(List<T> entities) {
        return mapper.insertList(entities) == entities.size();
    }

    /**
     * 修改实体
     *
     * <p>null字段将会被忽略
     *
     * @param entity 实体类对象
     * @return 修改结果
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean modify(T entity) {
        Long id = entity.getId();
        if (id == null) {
            throw new IllegalArgumentException("Id should not be empty!");
        }

        T before = this.findById(id);
        if (before == null) {
            throw new IllegalArgumentException("Invalid id " + id + "!");
        }

        // 处理乐观锁
        if (ReflectUtils.hasAnnotation(entity, Version.class)
                && ReflectUtils.getValueByAnnotation(entity, Version.class) == null) {
            Object version = ReflectUtils.getValueByAnnotation(before, Version.class);
            ReflectUtils.setValueByAnnotation(entity, version, Version.class);
        }

        return mapper.updateByPrimaryKeySelective(entity) > 0;
    }

    /**
     * 新增或修改Entity
     *
     * <p>null字段将会被忽略
     *
     * @param entity 实体类对象
     * @return 新增/修改结果
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addOrModify(T entity) {
        return entity.getId() == null ? add(entity) : modify(entity);
    }
}
