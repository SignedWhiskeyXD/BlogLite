package com.wsmrxd.bloglite.controller.publics;

import com.github.pagehelper.PageInfo;
import com.wsmrxd.bloglite.dto.CommentUploadInfo;
import com.wsmrxd.bloglite.service.CommentService;
import com.wsmrxd.bloglite.vo.CommentVO;
import com.wsmrxd.bloglite.vo.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/comment")
public class CommentAPI {

    public static final int PAGE_SIZE = 10;

    @Autowired
    private CommentService commentService;

    @GetMapping
    public RestResponse<PageInfo<CommentVO>> serveCommentPageByID(@RequestParam("id") int id,
                                                                  @RequestParam("pageNum") int pageNum){

        return RestResponse.ok(commentService.getCommentsByBlogID(id, pageNum, PAGE_SIZE));
    }

    @PostMapping("/{id}")
    public RestResponse<Object> uploadCommentByID(@PathVariable Integer id,
                                                  @RequestBody CommentUploadInfo newComment){
        log.trace("Comment Uploaded: \n{}", newComment.toString());
        return RestResponse.build(201, "Comment needs to be Reviewed");
    }
}
