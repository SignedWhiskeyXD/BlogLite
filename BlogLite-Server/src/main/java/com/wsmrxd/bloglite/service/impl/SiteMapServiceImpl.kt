package com.wsmrxd.bloglite.service.impl;

import com.wsmrxd.bloglite.mapping.BlogMapper;
import com.wsmrxd.bloglite.service.SiteMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SiteMapServiceImpl implements SiteMapService {

    private String siteMapText;

    @Autowired
    private BlogMapper blogMapper;

    @Value("${myConfig.domain}")
    private String domain;

    @Override
    public String getSiteMapText() {
        return Optional
                .ofNullable(siteMapText)
                .orElse(loadSiteMapText());
    }

    @Scheduled(cron = "0 0 0,12 * * *")
    public String loadSiteMapText() {
        List<Integer> blogIDs = blogMapper.selectAllBlogID();
        StringBuilder stringBuilder = new StringBuilder();

        for(Integer id: blogIDs) {
            stringBuilder.append("https://").append(domain)
                    .append("/blog/").append(id).append("\n");
        }

        siteMapText = stringBuilder.toString();
        return siteMapText;
    }
}
