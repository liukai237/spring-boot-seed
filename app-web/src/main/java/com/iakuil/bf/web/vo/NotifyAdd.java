package com.iakuil.bf.web.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * NotifyAdd
 *
 * @author Kai
 * @date 2021/3/25 11:17
 **/
@Getter
@Setter
@ApiModel(value = "NotifyAdd", description = "新增公告通知参数")
public class NotifyAdd implements Serializable {
    /**
     * 类型
     */
    @NotNull(message = "类型不能为空！")
    private String type;

    /**
     * 标题
     */
    @Size(max = 20, min = 4, message = "标题必须小于20个字符！")
    private String title;

    /**
     * 内容
     */
    @Size(max = 200, min = 1, message = "内容必须小于200个字符！")
    private String content;

    /**
     * 附件
     */
    @Size(max = 100, min = 1, message = "内容必须小于100个字符！")
    private String attachment;
}
