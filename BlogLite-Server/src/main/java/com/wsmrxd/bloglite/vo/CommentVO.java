package com.wsmrxd.bloglite.vo;

import lombok.Data;

@Data
public class CommentVO {

    private int id;

    private String nickname;

    private String email;

    private String content;
}
