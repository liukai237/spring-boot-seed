package com.iakuil.bf.dao.entity;


import com.iakuil.bf.common.BaseDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import tk.mybatis.mapper.annotation.LogicDelete;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "sys_dict")
public class Dict extends BaseDomain {

    /**
     * 编号
     */
    @Id
    private Long id;
    /**
     * 标签名
     */
    private String name;
    /**
     * 数据值
     */
    private String value;
    /**
     * 类型
     */
    private String type;
    /**
     * 描述
     */
    private String description;
    /**
     * 排序（升序）
     */
    private BigDecimal sort;
    /**
     * 父级编号
     */
    private Long parentId;
    /**
     * 创建者
     */
    private Integer createBy;
    /**
     * 创建时间
     */
    @CreatedDate
    private Date createTime;
    /**
     * 更新者
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    @LastModifiedBy
    private Date updateTime;
    /**
     * 备注信息
     */
    private String remark;
    /**
     * 删除标记
     */
    @LogicDelete
    private String delFlag;
}
