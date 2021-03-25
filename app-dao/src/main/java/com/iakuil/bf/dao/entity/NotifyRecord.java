package com.iakuil.bf.dao.entity;

import com.iakuil.bf.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "t_notify_record")
public class NotifyRecord extends BaseEntity {
    /**
     * 通知通告ID
     */
    @Column(name = "notify_id")
    private Long notifyId;

    /**
     * 接收人
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 阅读标记
     */
    @Column(name = "is_read")
    private Boolean read;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @LastModifiedDate
    @Column(name = "update_time")
    private Date updateTime;
}