package com.wsmrxd.bloglite.service.impl;

import com.wsmrxd.bloglite.mapping.BlogMapper;
import com.wsmrxd.bloglite.service.BlogSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogSearchServiceImpl implements BlogSearchService {

    @Autowired
    private BlogMapper blogMapper;

    @Override
    public List<Integer> searchBlogContentFromDB(String keyword) {
        String pattern = "%" + keyword + "%";
        return blogMapper.selectBlogIDByContentPattern(pattern);
    }
}
