package com.wsmrxd.bloglite.controller.admin;

import com.wsmrxd.bloglite.dto.CommentReviewInfo;
import com.wsmrxd.bloglite.service.CommentService;
import com.wsmrxd.bloglite.vo.CommentAdminDetail;
import com.wsmrxd.bloglite.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/comment")
public class CommentAdminAPI {

    @Autowired
    CommentService commentService;

    @GetMapping("/review")
    public RestResponse<List<CommentAdminDetail>> getCommentsToReview(
                                                    @RequestParam(value = "num", defaultValue = "10") int num) {
        commentService.syncCommentsForReview();
        return RestResponse.ok(commentService.getCommentsDisabled(num));
    }

    @PostMapping("/review")
    public RestResponse<Object> handleReviewResult(@RequestBody CommentReviewInfo reviewInfo){
        for(Integer commentID : reviewInfo.getEnableIDs())
            commentService.enableComment(commentID);

        for(Integer commentID : reviewInfo.getDeleteIDs())
            commentService.deleteComment(commentID);

        return RestResponse.ok();
    }
}