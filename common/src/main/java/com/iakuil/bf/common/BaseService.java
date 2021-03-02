package com.iakuil.bf.common;

import com.iakuil.bf.common.db.Condition;
import com.iakuil.bf.common.db.CrudMapper;
import com.iakuil.bf.common.tool.ReflectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.annotation.Version;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 通用Service基类
 *
 * <p>提供一些业务层常用的公用方法，统一处理乐观锁、逻辑删除、批量插入以及saveOrUpdate等。
 * <p>不与Entity对应的Service无需继承此类。
 * <p>默认忽略所有null值。
 *
 * <p>PS.BaseService主要是为了弥补tkMapper的不足（借鉴了MyBatis Plus的一些思路），解决80%的单表CRUD场景，剩下的20%建议手写SQL，或者尝试CQRS。
 * <p>考虑到按时间范围查询的场景比较多，引入了{@code Example}对象查询。通用查询与Example查询都可以使用的情况下，优先使用通用查询方法。
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
        return this.list(entity).stream().map(converter).collect(Collectors.toList());
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
        return new PageData<>(this.list(entity));
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
        return new PageData<>(this.list(entity, converter));
    }

    /**
     * Condition条件查询（试验功能）
     *
     * <p>同时返回分页信息
     *
     * @param condition Condition查询条件
     * @return 带分页信息的实体类集合
     */
    @Transactional(readOnly = true)
    public PageData<T> page(Condition condition) {
        return new PageData<>(this.findByCondition(condition));
    }

    /**
     * Condition条件查询（试验功能）
     *
     * <p>同时返回分页信息
     *
     * @param condition Condition查询条件
     * @param converter Entity/DTO转换器
     * @return 带分页信息的DTO集合
     */
    @Transactional(readOnly = true)
    public <R> PageData<R> page(Condition condition, Function<? super T, ? extends R> converter) {
        return new PageData<>(this.findByCondition(condition), converter);
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
     * 根据ID查询实体类
     *
     * @param ids 实体ID列表
     * @return 实体类对象
     */
    @Transactional(readOnly = true)
    public List<T> findByIds(Long... ids) {
        return mapper.selectByIds(Arrays.stream(ids).map(String::valueOf).collect(Collectors.joining(",")));
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
     * 返回全部
     *
     * <p>出于性能考虑，请谨慎使用此方法。
     *
     * @return 全部实体类对象
     */
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return mapper.selectAll();
    }

    /**
     * 将实体类作为查询条件进行统计
     *
     * @param entity 实体类对象
     * @return 查询统计结果
     */
    @Transactional(readOnly = true)
    public int count(T entity) {
        return mapper.selectCount(entity);
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
    public boolean removeByIds(Long... ids) {
        int count = mapper.deleteByIds(Arrays.stream(ids)
                .filter(Objects::nonNull)
                .distinct()
                .map(String::valueOf)
                .collect(Collectors.joining(",")));
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
     * @param entity 实体类对象
     * @return 修改结果
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean modify(T entity) {
        return this.modify(entity, false);
    }

    /**
     * 修改实体
     *
     * <p>同时处理乐观锁
     *
     * @param entity 实体类对象
     * @return 修改结果
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean modifyWithVersion(T entity) {
        return this.modify(entity, true);
    }

    /**
     * 修改实体
     *
     * <p>null字段将会被忽略
     *
     * @param entity     实体类对象
     * @param hasVersion 是否有乐观锁
     * @return 修改结果
     */
    private boolean modify(T entity, boolean hasVersion) {
        Long id = entity.getId();
        if (id == null) {
            throw new IllegalArgumentException("Id should not be empty!");
        }

        // 处理乐观锁
        if (hasVersion) {
            if (ReflectUtils.getValueByAnnotation(entity, Version.class) == null) {
                T before = this.findById(id);
                if (before == null) {
                    throw new IllegalArgumentException("Invalid id " + id + "!");
                }

                Object version = ObjectUtils.defaultIfNull(ReflectUtils.getValueByAnnotation(before, Version.class), 1);
                ReflectUtils.setValueByAnnotation(entity, version, Version.class);
            }
        }

        return mapper.updateByPrimaryKeySelective(entity) > 0;
    }

    /**
     * 新增或修改Entity
     *
     * @param entity 实体类对象
     * @return 新增/修改结果
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addOrModify(T entity) {
        return entity.getId() == null ? add(entity) : modify(entity, false);
    }

    /**
     * 新增或修改Entity
     *
     * <p>同时处理乐观锁
     *
     * @param entity 实体类对象
     * @return 新增/修改结果
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addOrModifyWithVersion(T entity) {
        return entity.getId() == null ? add(entity) : modify(entity, true);
    }

    /**
     * 新增或修改Entity
     *
     * <p>null字段将会被忽略
     *
     * @param entity     实体类对象
     * @param hasVersion 是否有乐观锁
     * @return 新增/修改结果
     */
    private boolean addOrModify(T entity, boolean hasVersion) {
        return entity.getId() == null ? add(entity) : modify(entity, hasVersion);
    }

    /**
     * Condition条件查询（试验功能）
     *
     * <p>查询条件应该通过{@link Condition.Builder}生成。
     *
     * @param condition Condition查询条件
     * @return 实体类对象列表
     */
    @Transactional(readOnly = true)
    public List<T> findByCondition(Condition condition) {
        return mapper.selectByExample(condition);
    }

    /**
     * Condition条件查询（试验功能）
     *
     * <p>查询条件应该通过{@link Condition.Builder}生成。
     *
     * @param condition Condition查询条件
     * @param converter Entity/DTO转换器
     * @return DTO列表
     */
    @Transactional(readOnly = true)
    public <R> List<R> findByCondition(Condition condition, Function<? super T, ? extends R> converter) {
        List<T> results = mapper.selectByExample(condition);
        return results.stream().map(converter).collect(Collectors.toList());
    }
}
