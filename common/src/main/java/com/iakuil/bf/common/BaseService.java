package com.iakuil.bf.common;

import com.iakuil.bf.common.db.CrudMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 泛型化的Service基类
 *
 * <p>提供一些业务层常用的公用方法，主要针对单表操作。
 * <p>不与Entity对应的Service无需继承此类。
 *
 * @param <T> 实体类型
 *
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
     * @return 查询结果
     */
    @Transactional(readOnly = true)
    public List<T> query(T entity) {
        return mapper.select(entity);
    }

    /**
     * 将实体类作为查询条件进行查询
     *
     * <p>可分页，同时返回分页信息
     *
     * @param entity 实体类对象
     * @return 带分页信息的实体类集合
     */
    @Transactional(readOnly = true)
    public PageData<T> queryPage(T entity) {
        return new PageData<>(mapper.select(entity));
    }

    /**
     * 根据ID查询实体类
     *
     * @param id 实体ID
     * @return 实体类对象
     */
    @Transactional(readOnly = true)
    public T queryById(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 将实体类作为查询条件进行查询
     *
     * @param entity 实体类对象
     * @return 唯一的实体类对象
     */
    @Transactional(readOnly = true)
    public T queryOne(T entity) {
        return mapper.selectOne(entity);
    }

    /**
     * 将实体类作为条件进行删除
     *
     * @param entity 实体类对象
     * @return 删除结果
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(T entity) {
        return mapper.delete(entity) > 0;
    }

    /**
     * 根据ID进行删除
     *
     * @param id 实体类ID
     * @return 删除结果
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Long id) {
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
    public boolean deleteByIds(String... ids) {
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
    public boolean save(T entity) {
        return mapper.insertSelective(entity) > 0;
    }

    /**
     * 批量保存
     *
     * <p>null字段将会被忽略
     *
     * @param entities 实体类对象列表
     * @return 保存结果
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatch(List<T> entities) {
        return mapper.insertList(entities) == entities.size();
    }

    /**
     * 修改实体类
     *
     * <p>null字段将会被忽略
     *
     * @param entity 实体类对象
     * @return 修改结果
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean modify(T entity) {
        if (entity.getId() == null) {
            log.error("Id should not be empty!");
            return false;
        }
        return mapper.updateByPrimaryKeySelective(entity) > 0;
    }

    /**
     * 新增或修改实体类
     *
     * <p>null字段将会被忽略
     *
     * @param entity 实体类对象
     * @return 新增/修改结果
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
                succ = mapper.updateByPrimaryKeySelective(entity) > 0;
            }
        }

        return succ;
    }

    /**
     * 批量新增或者修改Entity
     * <p>一次操作失败则全部回滚。
     *
     * @param entityList 实体类对象列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateBatch(Collection<T> entityList) {
        for (T obj : entityList) {
            if (!saveOrUpdate(obj)) {
                throw new IllegalStateException("saveOrUpdatePatch fail!");
            }
        }
    }
}
