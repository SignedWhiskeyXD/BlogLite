package com.wsmrxd.bloglite.service;

import com.wsmrxd.bloglite.vo.BlogCollectionVO;

import java.util.List;

public interface BlogCollectionService {

    List<BlogCollectionVO> getAllBlogCollectionWithStatistic();
}
