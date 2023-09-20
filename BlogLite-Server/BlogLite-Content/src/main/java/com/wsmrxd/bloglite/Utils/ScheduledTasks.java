package com.wsmrxd.bloglite.Utils;

import com.wsmrxd.bloglite.service.CommentService;
import com.wsmrxd.bloglite.service.SiteInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    SiteInfoService siteInfoService;

    @Autowired
    CommentService commentService;

    @Scheduled(fixedRate = 300000)      // 每5分钟向数据库更新一次浏览次数
    public void syncSiteInfo(){
        siteInfoService.UpdateSiteInfo();
    }

    @Scheduled(fixedRate = 1800000)      // 每30分钟向数据库更新一次缓存评论
    public void syncCommentsInCache(){
        commentService.syncCommentsForReview();
    }
}
