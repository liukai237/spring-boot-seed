package com.iakuil.bf.qrtz.impl;

import com.iakuil.bf.qrtz.JobDto;
import com.iakuil.bf.qrtz.QuartzJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Quartz定时任务服务实现
 *
 * @author Kai
 */
@Slf4j
@Service
public class QuartzJobServiceImpl implements QuartzJobService {

    @Autowired
    private Scheduler scheduler;

    /**
     * 创建定时任务
     *
     * @param jobDto 任务参数传输对象
     */
    @Override
    public void addJob(JobDto jobDto) throws SchedulerException {
        addJob(jobDto, null);
    }

    /**
     * 创建定时任务，可传参
     *
     * @param jobDto 任务参数传输对象
     * @param argMap map形式参数
     */
    @Override
    public void addJob(JobDto jobDto, Map<String, Object> argMap) throws SchedulerException {
        Class<?> clazz = jobDto.getClazz();
        String jobName = jobDto.getJobName();
        String groupName = jobDto.getGroupName();
        String cronExpression = jobDto.getCronExpression();
        // 启动调度器
        scheduler.start();
        // 构建job信息
        Object obj;
        try {
            obj = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new SchedulerException(e); // should not been happened
        }
        JobDetail jobDetail = JobBuilder.newJob(((Job) obj).getClass()).withIdentity(jobName, groupName).withDescription(jobDto.getDesc()).build();

        // 表达式调度构建器(即任务执行的时间)
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        // 按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, groupName).withSchedule(scheduleBuilder).build();
        // 获得JobDataMap，写入数据
        if (argMap != null) {
            trigger.getJobDataMap().putAll(argMap);
        }
        if (!StringUtils.isEmpty(jobDto.getTime())) {
            trigger.getJobDataMap().put("time", jobDto.getTime());
        }
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 暂停定时任务
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务所在组名称
     */
    @Override
    public void pauseJob(String jobName, String jobGroupName) throws SchedulerException {
        scheduler.pauseJob(JobKey.jobKey(jobName, jobGroupName));
    }

    /**
     * 恢复定时任务
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务所在组名称
     */
    @Override
    public void resumeJob(String jobName, String jobGroupName) throws SchedulerException {
        scheduler.resumeJob(JobKey.jobKey(jobName, jobGroupName));
    }

    /**
     * 定时任务更新,只更新频率
     */
    @Override
    public void updateJob(JobDto jobDto) throws SchedulerException {
        updateJob(jobDto, null);
    }

    /**
     * 定时任务更新,更新频率和参数
     *
     * @param argMap 参数
     */
    @Override
    public void updateJob(JobDto jobDto, Map<String, Object> argMap) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobDto.getJobName(), jobDto.getGroupName());
        // 表达式调度构建器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobDto.getCronExpression());
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        // 按新的cronExpression表达式重新构建trigger
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
        // 更新Data
        if (argMap != null) {
            trigger.getJobDataMap().putAll(argMap);
        }
        trigger.getJobDataMap().put("time", jobDto.getTime());
        // 按新的trigger重新设置job执行
        scheduler.rescheduleJob(triggerKey, trigger);
    }

    /**
     * 定时任务更新,只更新更新参数
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务所在组名称
     * @param argMap       参数
     */
    @Override
    public void updateJob(String jobName, String jobGroupName, Map<String, Object> argMap) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        // 更新Data
        trigger.getJobDataMap().putAll(argMap);
        // 按新的trigger重新设置job执行
        scheduler.rescheduleJob(triggerKey, trigger);
    }

    /**
     * 定时任务删除
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务所在组名称
     */
    @Override
    public void deleteJob(String jobName, String jobGroupName) throws SchedulerException {
        scheduler.pauseTrigger(TriggerKey.triggerKey(jobName, jobGroupName));
        scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, jobGroupName));
        scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));
    }

    /**
     * 启动所有定时任务
     */
    @Override
    public void startAllJobs() throws SchedulerException {
        scheduler.start();
    }

    /**
     * 关闭所有定时任务
     */
    @Override
    public void shutdownAllJobs() throws SchedulerException {
        if (!scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }

    /**
     * 获取所有任务列表
     *
     * @return 所有定时任务列表
     */
    @Override
    public List<JobDto> getAllJob() throws SchedulerException {
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        List<JobDto> jobList = new ArrayList<>();
        Set<JobKey> jobKeys;
        jobKeys = scheduler.getJobKeys(matcher);
        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggers) {
                JobDto jobDto = new JobDto();

                jobDto.setJobName(jobKey.getName());
                jobDto.setGroupName(jobKey.getGroup());
                JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                jobDto.setClassName(jobDetail.getJobClass().getName());
                jobDto.setClazz(jobDetail.getJobClass());
                jobDto.setTriggerName(trigger.getCalendarName());
                jobDto.setDesc(jobDetail.getDescription());
                jobDto.setTime(trigger.getJobDataMap().getString("time"));

                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                jobDto.setStatus(triggerState.name());

                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    jobDto.setCronExpression(cronExpression);
                }
                jobList.add(jobDto);
            }
        }

        return jobList;
    }


    /**
     * 通过jobname查询定时任务
     *
     * @param jobName 定时任务名称
     * @return 定时任务状态
     */
    @Override
    public Trigger.TriggerState getStateByJobName(String jobName) throws SchedulerException {
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys;
        jobKeys = scheduler.getJobKeys(matcher);
        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggers) {
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                if (jobKey.getName().equals(jobName)) {
                    return triggerState;
                }
            }
        }
        return null;
    }
}
