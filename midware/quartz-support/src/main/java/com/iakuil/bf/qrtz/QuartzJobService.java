package com.iakuil.bf.qrtz;

import org.quartz.SchedulerException;
import org.quartz.Trigger;

import java.util.List;
import java.util.Map;

/**
 * Quartz定时任务服务
 *
 * @author Kai
 */
public interface QuartzJobService {
    /**
     * 创建job
     *
     * @param jobDto 任务类
     */
    void addJob(JobDto jobDto) throws SchedulerException;

    /**
     * 创建job，可传参
     *
     * @param jobDto 任务类
     * @param argMap map形式参数
     */
    void addJob(JobDto jobDto, Map<String, Object> argMap) throws SchedulerException;

    /**
     * 暂停job
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务所在组名称
     */
    void pauseJob(String jobName, String jobGroupName) throws SchedulerException;

    /**
     * 恢复job
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务所在组名称
     */
    void resumeJob(String jobName, String jobGroupName) throws SchedulerException;


    /**
     * job 更新,只更新频率
     */
    void updateJob(JobDto jobDto) throws SchedulerException;


    /**
     * job 更新,更新频率和参数
     *
     * @param argMap 参数
     */
    void updateJob(JobDto jobDto, Map<String, Object> argMap) throws SchedulerException;

    /**
     * job 更新,只更新更新参数
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务所在组名称
     * @param argMap       参数
     */
    void updateJob(String jobName, String jobGroupName, Map<String, Object> argMap) throws SchedulerException;


    /**
     * job 删除
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务所在组名称
     */
    void deleteJob(String jobName, String jobGroupName) throws SchedulerException;


    /**
     * 启动所有定时任务
     */
    void startAllJobs() throws SchedulerException;

    /**
     * 关闭所有定时任务
     */
    void shutdownAllJobs() throws SchedulerException;

    /**
     * 获取所有任务列表
     *
     * @return
     */
    List<JobDto> getAllJob() throws SchedulerException;


    /**
     * 通过jobname查询定时任务状态
     *
     * @param jobName
     * @return job的状态
     */
    Trigger.TriggerState getStateByJobName(String jobName) throws SchedulerException;
}
