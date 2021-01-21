package com.iakuil.bf.common;

import javax.persistence.Id;

/**
 * Entity基类
 *
 * <p>【强制】所有Table必须设计ID字段。
 * <p>所有的Entity都可以作为分页排序的参数对象。
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
