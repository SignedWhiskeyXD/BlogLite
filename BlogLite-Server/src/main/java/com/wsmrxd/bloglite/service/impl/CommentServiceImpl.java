package com.wsmrxd.bloglite.service.impl;

import com.github.pagehelper.PageInfo;
import com.wsmrxd.bloglite.dto.CommentUploadInfo;
import com.wsmrxd.bloglite.entity.Comment;
import com.wsmrxd.bloglite.mapping.CacheableMapper;
import com.wsmrxd.bloglite.mapping.CommentMapper;
import com.wsmrxd.bloglite.service.CacheService;
import com.wsmrxd.bloglite.service.CommentService;
import com.wsmrxd.bloglite.vo.CommentAdminDetail;
import com.wsmrxd.bloglite.vo.CommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.wsmrxd.bloglite.enums.RedisKeyForZSet.Integer_CommentIDByBlogID;
import static com.wsmrxd.bloglite.enums.RedisKeyForList.Comment_CommentToReview;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CacheableMapper cacheableMapper;

    @Autowired
    private CacheService cacheService;

    // 我希望直接通过缓存项来分页，这样的话startPage方法就没用了，那就手动分页
    @Override
    public PageInfo<CommentVO> getCommentsByBlogID(int blogID, int pageNum, int pageSize) {
        long startIndex = (long) (pageNum - 1) * pageSize;
        long endIndex = startIndex + pageSize - 1;

        if(endIndex < startIndex)
            return new PageInfo<>();

        List<CommentVO> comments = getCommentsByBlogID(blogID, startIndex, endIndex);
        hideCommentEmail(comments);
        var ret = new PageInfo<>(comments);
        ret.setPageNum(pageNum);
        ret.setPageSize(pageSize);
        ret.setTotal(getCommentNumByBlogID(blogID));

        return ret;
    }

    @Override
    public void enqueueCommentToReview(int blogID, CommentUploadInfo newComment) {
        Comment comment = new Comment(blogID, newComment);
        cacheService.list().rPushValToList(Comment_CommentToReview.name(), comment);
    }

    @Override
    public void addNewComment(int blogID, CommentUploadInfo newComment) {
        var comment = new Comment(blogID, newComment);
        comment.setEnable(true);
        commentMapper.insertComment(comment);

        String redisZSetKey = Integer_CommentIDByBlogID.name() + "::" + blogID;
        cacheService.zSet().addValueToZSet(redisZSetKey, comment.getId(), comment.getPublishTime().getTime());
    }

    @Override
    public List<CommentAdminDetail> getCommentsDisabled(int limit) {
        return commentMapper.selectCommentsDisabled(limit);
    }

    @Override
    public void enableComment(int commentID) {
        commentMapper.updateCommentEnabled(commentID, true);

        Comment comment = cacheableMapper.getCommentByID(commentID);
        if(comment == null) return;      // TODO: 也许应该抛个异常？
        String redisListKey = Integer_CommentIDByBlogID.name() + "::" + comment.getIdentify();
        cacheService.zSet().addValueToZSet(redisListKey, commentID, comment.getPublishTime().getTime());
    }

    @Override
    public void deleteComment(int commentID) {
        commentMapper.deleteCommentByID(commentID);

        // TODO: 从缓存有序集合删除该ID
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncCommentsForReview() {
        List<Comment> comments = cacheService.list().getList(Comment_CommentToReview.name());
        if(comments == null || comments.isEmpty()) return;

        for(Comment comment : comments)
            commentMapper.insertComment(comment);

        cacheService.delete(Comment_CommentToReview.name());
    }

    private static void hideCommentEmail(List<CommentVO> commentList){
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
    }

    private List<CommentVO> getCommentsByBlogID(int blogID, long startIndex, long endIndex){
        ensureCommentIDListCached(blogID);
        String redisListKey = Integer_CommentIDByBlogID.name() + "::" + blogID;

        List<Integer> commentIDs = cacheService.zSet().getListByReversedIndexRange(redisListKey, startIndex, endIndex);
        if(commentIDs == null || commentIDs.isEmpty()) return Collections.emptyList();

        List<CommentVO> ret = new ArrayList<>(commentIDs.size());
        for(Integer commentID : commentIDs){
            Comment comment = cacheableMapper.getCommentByID(commentID);
            if (comment != null)
                ret.add(new CommentVO(comment));
        }
        return ret;
    }

    private long getCommentNumByBlogID(int blogID){
        String redisListKey = Integer_CommentIDByBlogID.name() + "::" + blogID;
        return cacheService.zSet().getZSetSize(redisListKey);
    }

    private void ensureCommentIDListCached(int blogID){
        String redisListKey = Integer_CommentIDByBlogID.name() + "::" + blogID;
        if(cacheService.hasKey(redisListKey)) return;

        List<Comment> comments = commentMapper.selectAllCommentEnabledByIdent(blogID);
        for(Comment comment : comments)
            cacheService.zSet().addValueToZSet(redisListKey, comment.getId(), comment.getPublishTime().getTime());
    }
}
