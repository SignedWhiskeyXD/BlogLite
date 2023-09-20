package com.wsmrxd.bloglite.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CommentUploadInfo {

    private String content;

    private String nickname;

    private String email;

    private Date publishTime = new Date();
}
