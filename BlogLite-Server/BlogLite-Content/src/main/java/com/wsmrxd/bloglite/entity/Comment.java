package com.wsmrxd.bloglite.entity;

import com.wsmrxd.bloglite.dto.CommentUploadInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class Comment {

    public Comment(int blogID, CommentUploadInfo newComment){
        this.identify = blogID;
        this.nickname = newComment.getNickname();
        this.email = newComment.getEmail();
        this.content = newComment.getContent();
        this.publishTime = new Timestamp(newComment.getPublishTime().getTime());
    }

    private int id;

    private int identify;

    private String nickname;

    private String email;

    private boolean enable = false;

    private Timestamp publishTime;

    private String content;
}
