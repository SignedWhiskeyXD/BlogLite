package com.wsmrxd.bloglite.dto;

import lombok.Data;

@Data
public class CommentUploadInfo {

    private String content;

    private String nickname;

    private String email;
}
