package com.iakuil.bf.common;

import com.iakuil.bf.common.db.Condition;
import com.iakuil.bf.common.db.CrudMapper;
import com.iakuil.bf.common.domain.BaseEntity;
import com.iakuil.bf.common.domain.PageData;
import com.iakuil.bf.common.tool.ReflectUtils;
import com.iakuil.bf.common.tool.Validate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
 * <p>BTW. 如果出现<strong>'com.iakuil.bf.common.db.CrudMapper' that could not be found.</strong>错误，说明子类忘记添加泛型了。
 *
 * @param <T> 实体类型
 * @author Kai
 */
@Slf4j
public abstract class BaseService<T extends BaseEntity> {

    @Autowired
    protected CrudMapper<T> mapper;

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
        Validate.notNull(entity, "Entity should not be empty!");
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
        Validate.notNull(converter, "Converter should not be empty!");
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
        Validate.notNull(entity, "Entity should not be empty!");
        Validate.notNull(entity.getPageNum(), "PageNum should not be empty!");
        Validate.notNull(entity.getPageSize(), "PageSize should not be empty!");
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
        Validate.notNull(entity, "Entity should not be empty!");
        Validate.notNull(entity.getPageNum(), "PageNum should not be empty!");
        Validate.notNull(entity.getPageSize(), "PageSize should not be empty!");
        return new PageData<>(mapper.select(entity)).map(converter);
    }

    /**
     * Condition条件查询
     *
     * <p>同时返回分页信息
     *
     * @param condition Condition查询条件
     * @return 带分页信息的实体类集合
     */
    @Transactional(readOnly = true)
    public PageData<T> page(Condition condition) {
        Validate.notNull(condition.getPageNum(), "PageNum should not be empty!");
        Validate.notNull(condition.getPageSize(), "PageSize should not be empty!");
        return new PageData<>(this.findByCondition(condition));
    }

    /**
     * Condition条件查询
     *
     * <p>同时返回分页信息
     *
     * @param condition Condition查询条件
     * @param converter Entity/DTO转换器
     * @return 带分页信息的DTO集合
     */
    @Transactional(readOnly = true)
    public <R> PageData<R> page(Condition condition, Function<? super T, ? extends R> converter) {
        Validate.notNull(converter, "Converter should not be empty!");
        return new PageData<>(this.findByCondition(condition)).map(converter);
    }

    /**
     * 根据ID查询实体类
     *
     * @param id 实体ID
     * @return 实体类对象
     */
    @Transactional(readOnly = true)
    public T findById(Long id) {
        Validate.notNull(id, "PageNum should not be empty!");
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
        Validate.notEmpty(ids, "The params should not be empty!");
        return mapper.selectByIds(Arrays.stream(ids)
                .filter(Objects::nonNull)
                .distinct()
                .map(String::valueOf)
                .collect(Collectors.joining(",")));
    }

    /**
     * 将实体类作为查询条件进行查询
     *
     * @param entity 实体类对象
     * @return 唯一的实体类对象
     */
    @Transactional(readOnly = true)
    public T findOne(T entity) {
        Validate.notNull(entity, "Entity should not be empty!");
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
        Validate.notNull(id, "Id should not be empty!");
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
        Validate.notEmpty(ids, "Id should not be empty!");
        int count = mapper.deleteByIds(Arrays.stream(ids)
                .filter(Objects::nonNull)
                .distinct()
                .map(String::valueOf)
                .collect(Collectors.joining(",")));
        log.info("{} records were deleted!", count);
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
        Validate.notNull(entity, "Entity should not be empty!");
        return mapper.insertSelective(entity) > 0;
    }

    /**
     * 批量保存（MySQL专用）
     *
     * <p>null字段将<strong>不会</strong>被忽略，特别是乐观锁字段，请记得赋值！
     * <p>注意避免长事务，建议每次批量插入数量不要超过100。
     *
     * @param entities 实体类对象列表
     * @return 保存结果
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addAll(List<T> entities) {
        Validate.containsNoNulls(entities, "Entity should not be empty!");
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
        Validate.notNull(entity, "Entity should not be empty!");
        Long id = entity.getId();
        Validate.notNull(id, "Id should not be empty!");

        // 处理乐观锁
        if (hasVersion) {
            if (ReflectUtils.getValueByAnnotation(entity, Version.class) == null) {
                T before = mapper.selectByPrimaryKey(id);
                Validate.notNull(before, "Invalid id " + id + "!");
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
     * Condition条件查询
     *
     * <p>查询条件使用{@link Condition}，而不是原生的{@link tk.mybatis.mapper.entity.Example}。
     *
     * @param condition Condition查询条件
     * @return 实体类对象列表
     */
    @Transactional(readOnly = true)
    public List<T> findByCondition(Condition condition) {
        Validate.notNull(condition, "Condition should not be empty!");
        return mapper.selectByExample(condition);
    }

    /**
     * Condition条件查询
     *
     * <p>查询条件使用{@link Condition}，而不是原生的{@link tk.mybatis.mapper.entity.Example}。
     *
     * @param condition Condition查询条件
     * @param converter Entity/DTO转换器
     * @return DTO列表
     */
    @Transactional(readOnly = true)
    public <R> List<R> findByCondition(Condition condition, Function<? super T, ? extends R> converter) {
        Validate.notNull(condition, "Condition should not be empty!");
        Validate.notNull(converter, "Condition should not be empty!");
        List<T> results = mapper.selectByExample(condition);
        return results.stream().map(converter).collect(Collectors.toList());
    }

    /**
     * 滚动查询（试验功能）
     *
     * <p>类似Elasticsearch Scroll查询，参考了微信next_openid的实现。
     * <p>用于代替pageSize超过100的深分页，一般配合{@link BaseService#findByIds(Long...)}使用。
     * <p>注意：每次最多返回一万个ID。
     *
     * @param entity   实体类对象，pageNum会被忽略，永远为1
     * @param scrollId 游标ID，为空则从第一个ID开始
     * @return 实体类对象
     */
    @Transactional(readOnly = true)
    public Long[] findByScroll(T entity, Long scrollId) {
        Validate.notNull(entity, "Entity should not be empty!");
        return mapper.selectByScrollId(entity, scrollId).stream()
                .map(BaseEntity::getId)
                .toArray(Long[]::new);
    }
}
