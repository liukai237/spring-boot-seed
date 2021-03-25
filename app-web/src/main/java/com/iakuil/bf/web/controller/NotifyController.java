package com.iakuil.bf.web.controller;

import com.iakuil.bf.common.BaseController;
import com.iakuil.bf.common.domain.PageData;
import com.iakuil.bf.common.domain.Resp;
import com.iakuil.bf.dao.entity.Notify;
import com.iakuil.bf.service.NotifyService;
import com.iakuil.bf.service.converter.NotifyConverter;
import com.iakuil.bf.service.dto.NotifyDto;
import com.iakuil.bf.web.vo.NotifyAdd;
import com.iakuil.bf.web.vo.NotifyEdit;
import com.iakuil.toolkit.BeanUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * NotifyController
 *
 * @author Kai
 * @date 2021/3/25 11:09
 **/
@Slf4j
@Api(value = "NotifyController", tags = {"公告通知"})
@RestController
@RequestMapping("/api/notify/")
public class NotifyController extends BaseController {
    private final NotifyService notifyService;

    public NotifyController(NotifyService notifyService) {
        this.notifyService = notifyService;
    }

    @ApiOperation(value = "查询公告通知", notes = "分页查询公告通知。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", dataType = "Integer", paramType = "query")
    })
    @GetMapping("/list")
    @RequiresPermissions("oa:notify:list")
    public Resp<PageData<NotifyDto>> list(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        Notify query = new Notify();
        query.setPageNum(pageNum);
        query.setPageSize(pageSize);
        query.setOrderBy("create_time desc");
        return ok(notifyService.page(query, NotifyConverter.INSTANCE::toDto));
    }

    @ApiOperation(value = "新增公告通知", notes = "新增公告通知。")
    @PostMapping("/add")
    @RequiresPermissions("oa:notify:add")
    public Resp<?> add(@Valid @RequestBody NotifyAdd notify) {
        return ok(notifyService.add(BeanUtils.copy(notify, Notify.class)));
    }

    @ApiOperation(value = "编辑公告通知", notes = "编辑公告通知。")
    @PostMapping("/edit")
    @RequiresPermissions("oa:notify:edit")
    public Resp<?> edit(@Valid @RequestBody NotifyEdit notify) {
        return ok(notifyService.modify(BeanUtils.copy(notify, Notify.class)));
    }

    @ApiOperation(value = "推送公告通知", notes = "推送公告通知。")
    @PostMapping("/push")
    @RequiresPermissions("oa:notify:push")
    public Resp<?> edit(@RequestParam Long notifyId, @RequestParam Long[] userIds) {
        notifyService.pushBatch(notifyService.findById(notifyId), userIds);
        return ok();
    }

    @ApiOperation(value = "删除公告通知", notes = "删除公告通知。")
    @RequiresPermissions("oa:notify:remove")
    @PostMapping("/remove")
    public Resp<?> remove(@RequestParam Long[] ids) {
        return ok(notifyService.removeByIds(ids));
    }
}
