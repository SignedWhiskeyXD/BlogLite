package com.wsmrxd.bloglite.vo;

import lombok.Data;

@Data
public class CommentAdminDetail {

    private String blogTitle;

    private int commentID;

    private String nickname;

    private String email;

    private String publishTime;

    private String content;
}
