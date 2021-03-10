package com.iakuil.bf.web.job;

import com.iakuil.bf.common.DictCache;
import com.iakuil.bf.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据字典刷新任务
 */
@Slf4j
@Component
public class DictRefreshingJob implements InitializingBean {

    private final DictService dictService;

    public DictRefreshingJob(DictService dictService) {
        this.dictService = dictService;
    }

    @Override
    public void afterPropertiesSet() {
        this.collectDictFromDb();
    }

    @Scheduled(fixedRate = 1000 * 60)
    public void refreshDictItems() {
        this.collectDictFromDb();
    }

    private void collectDictFromDb() {
        List<DictCache.DictItem> items = dictService.findAll().stream()
                .map(item -> new DictCache.DictItem(item.getType(), item.getDescription(), item.getValue(), item.getName(), item.getSort().intValue()))
                .collect(Collectors.toList());
        log.debug("Current dict size in DB: {}", items.size());
        DictCache.getInstance().pushAll(items);
    }
}