package com.wsmrxd.bloglite.utils;

import com.wsmrxd.bloglite.service.CommentService;
import com.wsmrxd.bloglite.service.SiteInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduledTasks {

    @Autowired
    SiteInfoService siteInfoService;

    @Autowired
    CommentService commentService;

    @Scheduled(fixedRate = 300000)      // 每5分钟向数据库更新一次浏览次数
    @CacheEvict("BlogRanking")
    public void syncSiteInfo(){
        boolean isFlushed = siteInfoService.flushSiteInfo();
        if (isFlushed) {
            log.info("Site info flushed.");
        }
    }

    @Scheduled(fixedRate = 1800000)      // 每30分钟向数据库更新一次缓存评论
    public void syncCommentsInCache(){
        commentService.syncCommentsForReview();
    }
}
