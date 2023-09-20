package com.wsmrxd.bloglite.service;

import com.wsmrxd.bloglite.vo.BlogCard;
import com.wsmrxd.bloglite.vo.BlogStream;

import java.util.List;

public interface BlogStreamService {

    BlogStream getInitStream(int num);

    BlogStream getBlogStream(int startID, int num);

    List<BlogCard> getBlogCardList(List<Integer> blogIDs);
}
