package com.iakuil.bf.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 我的消息
 *
 * <p>我的收件箱钟所有公告通知和私信
 *
 * @author Kai
 * @date 2021/3/25 11:17
 **/
@Getter
@Setter
@ApiModel(value = "MyMessage", description = "我的消息")
public class MyMessage implements Serializable {
    /**
     * ID
     */
    @ApiModelProperty(name = "id", value = "用户ID")
    private Long id;

    /**
     * 类型
     */
    @ApiModelProperty(name = "id", value = "公告通知/私信")
    private String type;

    /**
     * 发送者
     */
    @ApiModelProperty(name = "senderId", value = "发送者", notes = "0代表系统发送。")
    private Long senderId;

    /**
     * 标题
     */
    @ApiModelProperty(name = "title", value = "标题")
    private String title;

    /**
     * 内容
     */
    @ApiModelProperty(name = "content", value = "内容")
    private String content;

    /**
     * 附件
     */
    @ApiModelProperty(name = "attachment", value = "附件")
    private String attachment;

    /**
     * 阅读标记
     */
    @ApiModelProperty(name = "read", value = "是否已读")
    private Boolean read;

    /**
     * 创建（接收）时间
     */
    private Date createTime;

    /**
     * 更新（阅读）时间
     */
    private Date updateTime;
}
