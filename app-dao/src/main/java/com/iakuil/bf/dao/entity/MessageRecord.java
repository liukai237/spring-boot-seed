package com.iakuil.bf.dao.entity;

import com.iakuil.bf.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * 消息记录
 *
 * @author Kai
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "t_msg_record")
public class MessageRecord extends BaseEntity {
    /**
     * 消息ID，可以是公告通知ID，也可以是私信ID
     */
    @Column(name = "msg_id")
    private Long msgId;

    /**
     * 接收人ID
     */
    @Column(name = "receiver_id")
    private Long receiver;

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