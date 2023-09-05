package com.wsmrxd.bloglite.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wsmrxd.bloglite.dto.CommentUploadInfo;
import com.wsmrxd.bloglite.entity.Comment;
import com.wsmrxd.bloglite.mapping.CommentMapper;
import com.wsmrxd.bloglite.service.CacheService;
import com.wsmrxd.bloglite.service.CommentService;
import com.wsmrxd.bloglite.vo.CommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CacheService cacheService;

    @Override
    public PageInfo<CommentVO> getCommentsByBlogID(int blogID, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(commentMapper.selectAllCommentByIdent(blogID));
    }

    @Override
    public void enqueueCommentToReview(int blogID, CommentUploadInfo newComment) {

    }

    @Override
    public void addNewComment(int blogID, CommentUploadInfo newComment) {
        var comment = new Comment(blogID, newComment);
        comment.setEnable(true);
        commentMapper.insertComment(comment);
    }
}
