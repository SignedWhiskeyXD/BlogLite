package com.wsmrxd.bloglite.service;

import com.wsmrxd.bloglite.dto.BlogCollectionCreateInfo;
import com.wsmrxd.bloglite.entity.BlogCollection;
import com.wsmrxd.bloglite.vo.BlogCollectionVO;

import java.util.List;

public interface BlogCollectionService {

    List<BlogCollectionVO> getBlogCollectionVOList(List<BlogCollection> collections);

    List<BlogCollection> getAllBlogCollection();

    void modifyCollectionInfo(BlogCollection modifyInfo);

    void createNewCollection(BlogCollectionCreateInfo newCollection);

    void removeBlogCollection(int collectionID);
}
