package com.iakuil.bf.common;

import com.github.pagehelper.IPage;

/**
 * 分页排序参数基类
 *
 * <p>pageSize、pageNum和orderBy三个属性配合PageHelper实现分页排序。
 *
 * @author Kai
 */
public interface Pageable extends IPage {

    void setPageSize(Integer pageSize);

    void setPageNum(Integer pageNum);

    void setOrderBy(String orderBy);
}
