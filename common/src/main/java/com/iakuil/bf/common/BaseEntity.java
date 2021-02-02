package com.iakuil.bf.common;

import javax.persistence.Id;

/**
 * Entity基类
 *
 * <p>【强制】所有Table必须设计ID字段。
 * <p>在MyBatis框架下，所有的Entity都可以作为DTO使用(但是不应该传递给前端)。
 * <p>同时可以作为单表分页、排序和查询过滤条件参数。
 *
 * @author Kai
 */
public class BaseEntity extends Pageable {

    @Id
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
