package com.iakuil.bf.web.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel(value = "JobAdd", description = "新增定时任务参数")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobAdd implements Serializable {
    @ApiModelProperty(name = "className", value = "Job类名。", example = "com.iakuil.bf.web.job.TestJob", required = true)
    private String className;
    @ApiModelProperty(name = "jobName", value = "任务名称。", example = "refreshCacheDaily", required = true)
    private String jobName;
    @ApiModelProperty(name = "groupName", value = "任务分组。", example = "WechatService", required = true)
    private String groupName;
    @ApiModelProperty(name = "cronExpression", value = "CRON表达式。", example = "0/5 * * * * ?", required = true)
    private String cronExpression;
    @ApiModelProperty(name = "desc", value = "任务描述。", example = "每天凌晨更新用户信息缓存。", required = true)
    private String desc;
}
