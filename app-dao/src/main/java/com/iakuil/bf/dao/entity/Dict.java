package com.iakuil.bf.dao.entity;


import com.iakuil.bf.common.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import tk.mybatis.mapper.annotation.LogicDelete;

import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 用户字典
 *
 * @author Kai
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_dict")
public class Dict extends BaseEntity {

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
    private Long createBy;
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
