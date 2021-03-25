package com.iakuil.bf.service;

import com.iakuil.bf.common.BaseService;
import com.iakuil.bf.dao.entity.MessageRecord;
import com.iakuil.bf.dao.entity.Notify;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 公告服务
 *
 * @author Kai
 * @date 2021/3/25 9:37
 **/
@Slf4j
@Service
public class NotifyService extends BaseService<Notify> {

    private final MessageRecordService messageRecordService;

    public NotifyService(MessageRecordService messageRecordService) {
        this.messageRecordService = messageRecordService;
    }

    /**
     * 群发公告
     *
     * <p>目前只支持按ID群发，后续可以按部门或者按活跃用户等分组群发
     */
    public void pushBatch(Notify notify, Long... userIds) {
        Long notifyId = notify.getId();

        for (Long userId : userIds) {
            MessageRecord entity = new MessageRecord();
            entity.setMsgId(notifyId);
            entity.setReceiver(userId);
            entity.setRead(false);
            messageRecordService.add(entity);
            //TODO sending messages by websockets
        }

        notify.setStatus("1");
        this.modify(notify);
    }
}
