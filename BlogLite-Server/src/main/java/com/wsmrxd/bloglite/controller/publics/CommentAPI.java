package com.wsmrxd.bloglite.controller.publics;

import com.github.pagehelper.PageInfo;
import com.wsmrxd.bloglite.Utils.HttpUtil;
import com.wsmrxd.bloglite.dto.CommentUploadInfo;
import com.wsmrxd.bloglite.service.CacheService;
import com.wsmrxd.bloglite.service.CommentService;
import com.wsmrxd.bloglite.vo.CommentVO;
import com.wsmrxd.bloglite.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Objects;

@RestController
@RequestMapping("/api/comment")
public class CommentAPI {

    private static final int PAGE_SIZE = 10;

    @Value("${myConfig.comment.needReview}")
    private boolean needReview;

    @Value("${myConfig.comment.coolDownMinutes}")
    private int coolDownMinutes;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CacheService cacheService;

    @GetMapping
    public RestResponse<PageInfo<CommentVO>> serveCommentPageByID(@RequestParam("id") int id,
                                                                  @RequestParam("pageNum") int pageNum){

        return RestResponse.ok(commentService.getCommentsByBlogID(id, pageNum, PAGE_SIZE));
    }

    @PostMapping("/{id}")
    public RestResponse<Object> uploadCommentByID(@PathVariable Integer id,
                                                  @RequestBody CommentUploadInfo newComment,
                                                  HttpServletRequest request){
        String sourceIP = HttpUtil.getIP(request);
        String ipKey = "CommentIPV4::" + Objects.requireNonNullElse(sourceIP, "UnknownIP");
        if(coolDownMinutes > 0 && !cacheService.setKeyValueIfAbsent(ipKey, " ", Duration.ofMinutes(coolDownMinutes)))
            return RestResponse.build(50400, Integer.toString(coolDownMinutes));

        if(needReview) {
            commentService.enqueueCommentToReview(id, newComment);
            return RestResponse.build(201, "Comment needs to be Reviewed");
        }else{
            commentService.addNewComment(id, newComment);
            return RestResponse.ok();
        }
    }
}
