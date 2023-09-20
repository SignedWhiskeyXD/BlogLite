package com.wsmrxd.bloglite.service.impl;

import com.wsmrxd.bloglite.mapping.BlogMapper;
import com.wsmrxd.bloglite.service.BlogSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty(name = "myConfig.blog.searchEngine", havingValue = "SQL", matchIfMissing = true)
public class BlogSearchServiceSQLImpl implements BlogSearchService {

    @Autowired
    private BlogMapper blogMapper;

    @Override
    public List<Integer> searchBlogByKeyword(String keyword) {
        String pattern = "%" + keyword + "%";
        return blogMapper.selectBlogIDByContentPattern(pattern);
    }
}
