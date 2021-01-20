package com.iakuil.bf.web.job;

import com.iakuil.bf.common.DictItem;
import com.iakuil.bf.common.DictPool;
import com.iakuil.bf.dao.entity.Dict;
import com.iakuil.bf.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DictRefreshingJob implements InitializingBean {

    private final DictService dictService;

    public DictRefreshingJob(DictService dictService) {
        this.dictService = dictService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.collectDictItems();
    }

    @Scheduled(fixedRate = 1000 * 60)
    public void refreshDictItems() {
        this.collectDictItems();
    }

    private void collectDictItems() {
        Dict dict = new Dict();
        dict.setDelFlag("0");
        List<Dict> dicts = dictService.query(dict);
        List<DictItem> items = dicts.stream()
                .map(item -> new DictItem(item.getType(), item.getDescription(), item.getValue(), item.getName(), item.getSort().intValue()))
                .collect(Collectors.toList());
        log.debug("Current dict size in DB: {}", items.size());
        DictPool.getInstance().pushDictItems(items);
    }
}