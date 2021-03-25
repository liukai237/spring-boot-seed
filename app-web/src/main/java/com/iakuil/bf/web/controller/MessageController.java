package com.iakuil.bf.web.controller;

import com.iakuil.bf.common.BaseController;
import com.iakuil.bf.common.domain.Resp;
import com.iakuil.bf.common.exception.BusinessException;
import com.iakuil.bf.dao.entity.MessageRecord;
import com.iakuil.bf.dao.entity.Notify;
import com.iakuil.bf.service.MessageRecordService;
import com.iakuil.bf.service.NotifyService;
import com.iakuil.bf.service.dto.MyMessage;
import com.iakuil.toolkit.BeanUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 我的消息接口
 *
 * @author Kai
 * @date 2021/3/25 11:28
 **/
@Slf4j
@Api(value = "MessageController", tags = {"我的消息"})
@RestController
@RequestMapping("/api/msg/")
public class MessageController extends BaseController {

    private final NotifyService notifyService;

    private final MessageRecordService messageRecordService;

    public MessageController(NotifyService notifyService, MessageRecordService messageRecordService) {
        this.notifyService = notifyService;
        this.messageRecordService = messageRecordService;
    }

    @ApiOperation(value = "查询我的消息", notes = "分页查询我的消息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "read", value = "是否已读，不填则查询全部", dataType = "String", paramType = "query")
    })
    @GetMapping("/list")
    @RequiresPermissions("me:msg:list")
    public Resp<List<MyMessage>> list(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                                      @RequestParam(required = false) Boolean read) {
        Long uid = Optional.ofNullable(getCurrentUserId()).orElseThrow(BusinessException::new);
        MessageRecord condition = new MessageRecord();
        condition.setReceiver(uid);
        condition.setPageNum(pageNum);
        condition.setPageSize(pageSize);
        condition.setOrderBy("create_time desc");
        if (read != null) {
            condition.setRead(read);
        }
        List<MessageRecord> myMsgList = messageRecordService.list(condition);
        List<Notify> notifyList = notifyService.findByIds(myMsgList.stream().map(MessageRecord::getMsgId).distinct().toArray(Long[]::new));
        return ok(myMsgList.stream()
                .map(item -> {
                    MyMessage msg = null;
                    Notify tmpNotify = notifyList.stream().filter(notify -> notify.getId().equals(item.getMsgId())).findFirst().orElseGet(null);
                    if (tmpNotify != null) {
                        msg = BeanUtils.copy(tmpNotify, MyMessage.class);
                        msg.setSenderId(tmpNotify.getCreateBy());
                        msg.setRead(item.getRead());
                    }
                    return msg;
                })
                .collect(Collectors.toList()));
    }

    @ApiOperation(value = "阅读消息", notes = "阅读消息并标记为已读。")
    @PostMapping("/read")
    @RequiresPermissions("me:msg:read")
    public Resp<?> markMsgAsRead(@RequestParam Long messageId) {
        MessageRecord record = messageRecordService.findById(messageId);
        if (record == null || ObjectUtils.notEqual(record.getReceiver(), getCurrentUserId())) {
            return fail("无效的消息ID！");
        }

        MessageRecord entity = new MessageRecord();
        entity.setId(messageId);
        entity.setRead(true);
        messageRecordService.modify(entity);
        return ok();
    }

    @ApiOperation(value = "删除我的消息", notes = "删除我的消息。")
    @RequiresPermissions("me:msg:remove")
    @PostMapping("/remove")
    public Resp<?> remove(@RequestParam Long[] ids) {
        final Long uid = getCurrentUserId();
        return ok(messageRecordService.removeByIds(messageRecordService.findByIds(ids).stream()
                .filter(item -> Objects.equals(item.getReceiver(), uid))
                .map(MessageRecord::getId)
                .toArray(Long[]::new)));
    }
}
