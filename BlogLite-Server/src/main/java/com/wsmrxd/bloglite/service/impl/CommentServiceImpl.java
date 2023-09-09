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
        var ret = new PageInfo<>(commentMapper.selectAllCommentByIdent(blogID));

        var commentList = ret.getList();
        for(var comment : commentList){
            String originalEmail = comment.getEmail();
            var split = originalEmail.split("@");
            if(split.length == 2){
                if(split[0].length() <= 3)
                    comment.setEmail("***@" + split[1]);
                else
                    comment.setEmail("***" + split[0].substring(3) + "@" + split[1]);
            }
        }

        ret.setList(commentList);
        return ret;
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
