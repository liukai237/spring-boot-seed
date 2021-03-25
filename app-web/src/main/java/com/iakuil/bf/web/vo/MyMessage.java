package com.iakuil.bf.web.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * MyMessage
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
     * 阅读标记
     */
    private Boolean read;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
