package com.iakuil.bf.service.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * NotifyDto
 *
 * @author Kai
 * @date 2021/3/25 11:17
 **/
@Getter
@Setter
@ApiModel(value = "NotifyDto", description = "公告通知")
public class NotifyDto implements Serializable {
    /**
     * ID
     */
    private Long id;

    /**
     * 类型
     */
    private String type;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 附件
     */
    private String attachment;

    /**
     * 状态
     */
    private String status;

    /**
     * 删除标记
     */
    private Byte deleted;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
