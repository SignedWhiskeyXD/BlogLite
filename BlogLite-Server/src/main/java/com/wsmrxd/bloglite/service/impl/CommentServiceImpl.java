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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.wsmrxd.bloglite.enums.RedisKeyForList.Comment_CommentToReview;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CacheService cacheService;

    @Override
    // TODO: 启用缓存
    public PageInfo<CommentVO> getCommentsByBlogID(int blogID, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        var ret = new PageInfo<>(commentMapper.selectAllCommentByIdent(blogID));

        hideCommentEmail(ret);

        return ret;
    }

    @Override
    public void enqueueCommentToReview(int blogID, CommentUploadInfo newComment) {
        Comment comment = new Comment(blogID, newComment);
        cacheService.rPushValToList(Comment_CommentToReview.name(), comment);
    }

    @Override
    public void addNewComment(int blogID, CommentUploadInfo newComment) {
        var comment = new Comment(blogID, newComment);
        comment.setEnable(true);
        commentMapper.insertComment(comment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncCommentsForReview() {
        List<Comment> comments = cacheService.getList(Comment_CommentToReview.name());
        if(comments == null || comments.isEmpty()) return;

        for(Comment comment : comments)
            commentMapper.insertComment(comment);

        cacheService.delete(Comment_CommentToReview.name());
    }

    private static void hideCommentEmail(PageInfo<CommentVO> commentPage){
        var commentList = commentPage.getList();
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
        commentPage.setList(commentList);
    }
}
