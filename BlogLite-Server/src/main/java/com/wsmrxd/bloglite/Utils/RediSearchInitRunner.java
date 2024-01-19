package com.wsmrxd.bloglite.Utils;

import com.wsmrxd.bloglite.service.impl.BlogSearchServiceRedisImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RediSearchInitRunner implements CommandLineRunner {

    @Autowired(required = false)
    private BlogSearchServiceRedisImpl searchService;

    @Override
    public void run(String... args) {
        if(searchService == null || searchService.checkIndexExists()) return;

        log.warn("Full-text index not found, reindexing all blogs for RediSearch");
        searchService.initBlogRediSearch();
        log.info("Full-text index completed");
    }
}
