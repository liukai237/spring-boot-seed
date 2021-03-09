package com.iakuil.bf.qrtz;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Job传输对象
 *
 * @author Kai
 */
@Getter
@Setter
public class JobDto implements Serializable {
    /**
     * Job实现类
     */
    private Class<?> clazz;

    /**
     * Job实现类全路径
     */
    private String className;

    /**
     * Job名称
     */
    private String jobName;

    /**
     * Group名称
     */
    private String groupName;

    /**
     * CRON表达式
     */
    private String cronExpression;

    /**
     * 触发器名称（日历）
     */
    private String triggerName;

    /**
     * 描述
     */
    private String desc;

    /**
     * 任务状态
     */
    private String status;

    /**
     * 生效时间
     */
    private String time;
}
