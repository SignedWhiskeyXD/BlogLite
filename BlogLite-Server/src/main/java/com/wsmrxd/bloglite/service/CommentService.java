package com.wsmrxd.bloglite.service;


import com.github.pagehelper.PageInfo;
import com.wsmrxd.bloglite.vo.CommentVO;

public interface CommentService {

    PageInfo<CommentVO> getCommentsByBlogID(int blogID, int pageNum, int pageSize);
}
