package com.wsmrxd.bloglite.entity;

import com.wsmrxd.bloglite.dto.CommentUploadInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Comment {

    public Comment(int blogID, CommentUploadInfo newComment){
        this.identify = blogID;
        this.nickname = newComment.getNickname();
        this.email = newComment.getEmail();
        this.publish_time = newComment.getPublishTime();
        this.content = newComment.getContent();
    }

    private int id;

    private int identify;

    private String nickname;

    private String email;

    private boolean enable = false;

    private Date publish_time;

    private String content;
}
