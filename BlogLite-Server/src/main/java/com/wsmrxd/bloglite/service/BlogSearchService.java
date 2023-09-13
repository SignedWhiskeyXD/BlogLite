package com.wsmrxd.bloglite.service;

import java.util.List;

public interface BlogSearchService {

    List<Integer> searchBlogContentFromDB(String keyword);
}
