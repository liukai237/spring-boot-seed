package com.iakuil.bf.service;

import com.google.common.collect.Lists;
import com.iakuil.bf.common.BaseService;
import com.iakuil.bf.dao.entity.Notify;
import com.iakuil.bf.dao.entity.NotifyRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 公告及站内信服务
 *
 * @author Kai
 * @date 2021/3/25 9:37
 **/
@Slf4j
@Service
public class NotifyService extends BaseService<Notify> {

    private final NotifyRecordService notifyRecordService;

    public NotifyService(NotifyRecordService notifyRecordService) {
        this.notifyRecordService = notifyRecordService;
    }

    /**
     * 群发公告
     *
     * <p>目前只支持按ID群发，后续可以按部门或者按活跃用户等分组群发
     */
    public void pushBatch(Notify notify, Long... userIds) {
        this.add(notify);
        Long notifyId = notify.getId();

        List<NotifyRecord> records = Lists.newArrayList();
        for (Long userId : userIds) {
            NotifyRecord entity = new NotifyRecord();
            entity.setNotifyId(notifyId);
            entity.setUserId(userId);
            entity.setRead(false);
        }

        notifyRecordService.addAll(records);
        //TODO sending messages by websockets
    }
}
