package com.wsmrxd.bloglite.vo;

import com.wsmrxd.bloglite.entity.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class CommentVO {

    public CommentVO(Comment entity) {
        this.id = entity.getId();
        this.nickname = entity.getNickname();
        this.email = entity.getEmail();
        this.content = entity.getContent();
        this.publish_time = entity.getPublishTime();
    }

    private int id;

    private String nickname;

    private String email;

    private String content;

    private Date publish_time;

}
