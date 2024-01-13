package com.wsmrxd.bloglite.controller.publics;

import com.wsmrxd.bloglite.service.SiteMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class SiteMapAPI {

    @Autowired
    private SiteMapService siteMapService;

    @GetMapping("/site-map.txt")
    public ResponseEntity<String> getSiteMap() {
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(6, TimeUnit.HOURS))
                .contentType(MediaType.TEXT_PLAIN)
                .body(siteMapService.getSiteMapText());
    }
}
