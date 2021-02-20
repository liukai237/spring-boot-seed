package com.iakuil.bf.common;

import com.iakuil.bf.common.db.CrudMapper;
import com.iakuil.bf.common.tool.ReflectUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.annotation.Version;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 通用Service基类
 *
 * <p>提供一些业务层常用的公用方法，统一处理乐观锁、逻辑删除、批量插入、saveOrUpdate模式以及按范围查询等。
 * <p>不与Entity对应的Service无需继承此类。
 * <p>默认忽略所有null值，如有需要，请另行实现。
 *
 * <p>PS.BaseService主要是为了弥补tkMapper的不足（借鉴了MyBatis Plus的一些思路），解决90%的单表CRUD场景，剩下的10%建议手写SQL，或者了解一下CQRS。
 *
 * @param <T> 实体类型
 * @author Kai
 */
@Slf4j
@Service
public abstract class BaseService<T extends BaseEntity> {

    private Class<T> entityClass;

    @Autowired
    private CrudMapper<T> mapper;

    @PostConstruct
    private void init() {
        this.entityClass = ReflectUtils.getSuperClassGenericType(getClass());
    }

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
        int count = mapper.deleteByIds(Arrays.stream(ids).map(String::valueOf).collect(Collectors.joining(",")));
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
     * 查询在指定时间之后创建的数据
     *
     * @param entity    实体类对象
     * @param startTime 开始时间
     * @return 指定时间之后创建的对象
     */
    @Transactional(readOnly = true)
    public List<T> findByCreateTimeAfter(T entity, Date startTime) {
        return this.findByCreateTimeBetween(entity, startTime, null);
    }

    /**
     * 查询在指定时间之前创建的数据
     *
     * @param entity  实体类对象
     * @param endTime 结束时间
     * @return 指定时间之前创建的对象
     */
    @Transactional(readOnly = true)
    public List<T> findByCreateTimeBefore(T entity, Date endTime) {
        return this.findByCreateTimeBetween(entity, null, endTime);
    }

    /**
     * 查询在指定时间范围之内创建的数据
     *
     * @param entity 实体类对象
     * @param since  开始时间
     * @param until  结束时间
     * @return 指定时间范围之内创建的对象
     */
    @Transactional(readOnly = true)
    public List<T> findByCreateTimeBetween(T entity, Date since, Date until) {
        String createDateField = ReflectUtils.getNameByAnnotation(entity, CreatedDate.class);
        return this.findInRange(entity, new Range(createDateField, since, until));
    }

    /**
     * 查询在指定时间之后发生修改的数据
     *
     * @param entity    实体类对象
     * @param startTime 开始时间
     * @return 指定时间之后发生修改的对象
     */
    @Transactional(readOnly = true)
    public List<T> findByUpdateTimeAfter(T entity, Date startTime) {
        return this.findByUpdateTimeBetween(entity, startTime, null);
    }

    /**
     * 查询在指定时间之前创建的数据
     *
     * @param entity  实体类对象
     * @param endTime 结束时间
     * @return 指定时间之前发生修改的对象
     */
    @Transactional(readOnly = true)
    public List<T> findByUpdateTimeBefore(T entity, Date endTime) {
        return this.findByUpdateTimeBetween(entity, null, endTime);
    }

    /**
     * 查询在指定时间范围之内发生修改的数据
     *
     * @param entity 实体类对象
     * @param since  开始时间
     * @param until  结束时间
     * @return 指定时间范围之内发生修改的对象
     */
    @Transactional(readOnly = true)
    public List<T> findByUpdateTimeBetween(T entity, Date since, Date until) {
        String createDateField = ReflectUtils.getNameByAnnotation(entity, LastModifiedDate.class);
        return this.findInRange(entity, new Range(createDateField, since, until));
    }

    /**
     * 查询在指定范围之内的数据
     *
     * <p>在原有通用查询方法基础上增加范围查询，大于或等于下限且小于或等于上限。
     *
     * @param entity 实体类对象
     * @param ranges 范围查询参数
     * @return 实体类对象列表
     */
    @Transactional(readOnly = true)
    protected List<T> findInRange(T entity, Range... ranges) {
        if (ranges == null) {
            log.debug("there is no range for query!");
            return Collections.emptyList();
        }

        return mapper.selectInRange(getCondition(entity, ranges));
    }

    /**
     * 查询在指定范围之内的数据
     *
     * @param entity    实体类对象
     * @param ranges    范围查询参数
     * @param converter Entity/DTO转换器
     * @return DTO列表
     */
    @Transactional(readOnly = true)
    protected <R> List<R> findInRange(T entity, Function<? super T, ? extends R> converter, Range... ranges) {
        if (ranges == null) {
            log.debug("there is no range for query!");
            return Collections.emptyList();
        }

        return mapper.selectInRange(getCondition(entity, ranges)).stream().map(converter).collect(Collectors.toList());
    }

    protected Condition getCondition(T entity, Range... ranges) {
        // Condition仅支持等于、大于等于和小于等于，其余条件会被忽略。
        Condition condition = new Condition(this.entityClass);
        Condition.Criteria criteria = condition.createCriteria();
        for (Range range : ranges) {
            String field = range.getField();
            Object lower = range.getLower();
            if (lower != null) {
                criteria.andGreaterThanOrEqualTo(field, lower);
            }
            Object upper = range.getUpper();
            if (upper != null) {
                criteria.andLessThanOrEqualTo(field, upper);
            }
        }

        criteria.andAllEqualTo(entity);
        return condition;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    protected static class Range {
        private String field;
        private Object lower;
        private Object upper;
    }
}
