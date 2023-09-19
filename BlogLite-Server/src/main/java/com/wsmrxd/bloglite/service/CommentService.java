package com.wsmrxd.bloglite.service;


import com.github.pagehelper.PageInfo;
import com.wsmrxd.bloglite.dto.CommentUploadInfo;
import com.wsmrxd.bloglite.vo.CommentAdminDetail;
import com.wsmrxd.bloglite.vo.CommentVO;

import java.util.List;

public interface CommentService {

    PageInfo<CommentVO> getCommentsByBlogID(int blogID, int pageNum, int pageSize);

    void enqueueCommentToReview(int blogID, CommentUploadInfo newComment);

    void addNewComment(int blogID, CommentUploadInfo newComment);

    List<CommentAdminDetail> getCommentsDisabled(int limit);

    void enableComment(int commentID);

    void deleteComment(int commentID);

    void deleteComment(int commentID, int blogID);

    void syncCommentsForReview();
}
