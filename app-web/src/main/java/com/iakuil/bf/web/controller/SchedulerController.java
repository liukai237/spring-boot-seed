package com.iakuil.bf.web.controller;

import com.iakuil.bf.common.BaseController;
import com.iakuil.bf.common.domain.Resp;
import com.iakuil.bf.qrtz.JobDto;
import com.iakuil.bf.qrtz.QuartzJobService;
import com.iakuil.bf.web.vo.JobAdd;
import com.iakuil.bf.web.vo.JobEdit;
import com.iakuil.toolkit.BeanUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(value = "SchedulerController", tags = {"任务调度"})
@RequiresRoles("admin")
@RestController
@RequestMapping("/api/scheduler/")
public class SchedulerController extends BaseController {

    private final QuartzJobService quartzJobService;

    public SchedulerController(QuartzJobService quartzJobService) {
        this.quartzJobService = quartzJobService;
    }

    @ApiOperation(value = "查询定时任务", notes = "查询所有定时任务。")
    @GetMapping(value = "/list")
    public Resp<List<JobDto>> list() throws SchedulerException {
        return ok(quartzJobService.getAllJob());
    }

    @ApiOperation(value = "新建定时任务", notes = "新建定时任务。")
    @PostMapping(value = "/add")
    public Resp<?> add(@RequestBody JobAdd job) throws SchedulerException {
        String className = job.getClassName();
        Class<?> clazz;
        try {
            clazz = ClassUtils.getClass(className);
        } catch (ClassNotFoundException e) {
            return fail("无效类名：" + className);
        }
        JobDto dto = BeanUtils.copy(job, JobDto.class);
        dto.setClazz(clazz);
        quartzJobService.addJob(dto);
        return ok();
    }

    @ApiOperation(value = "修改定时任务", notes = "修改定时任务。")
    @PostMapping(value = "/edit")
    public Resp<?> edit(@RequestBody JobEdit job) throws SchedulerException {
        JobDto dto = BeanUtils.copy(job, JobDto.class);
        quartzJobService.updateJob(dto);
        return ok();
    }

    @ApiOperation(value = "删除定时任务", notes = "删除定时任务。")
    @PostMapping(value = "/remove")
    public Resp<?> remove(@RequestParam String jobName, @RequestParam String jobGroupName) throws SchedulerException {
        quartzJobService.deleteJob(jobName, jobGroupName);
        return ok();
    }

    @ApiOperation(value = "暂停定时任务", notes = "暂停定时任务。")
    @PostMapping(value = "/pause")
    public Resp<?> pause(@RequestParam String jobName, @RequestParam String jobGroupName) throws SchedulerException {
        quartzJobService.pauseJob(jobName, jobGroupName);
        return ok();
    }

    @ApiOperation(value = "恢复定时任务", notes = "恢复定时任务（暂停以后）。")
    @PostMapping(value = "/resume")
    public Resp<?> resume(@RequestParam String jobName, @RequestParam String jobGroupName) throws SchedulerException {
        quartzJobService.resumeJob(jobName, jobGroupName);
        return ok();
    }

    @ApiOperation(value = "启动所有定时任务", notes = "启动所有定时任务。")
    @PostMapping(value = "/startAll")
    public Resp<?> startAll() throws SchedulerException {
        quartzJobService.startAllJobs();
        return ok();
    }

    @ApiOperation(value = "停止所有定时任务", notes = "停止所有定时任务。")
    @PostMapping(value = "/shutdownAll")
    public Resp<?> shutdownAll() throws SchedulerException {
        quartzJobService.shutdownAllJobs();
        return ok();
    }
}
