package com.wsmrxd.bloglite.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {

    private int id;

    private int identify;

    private String nickname;

    private String email;

    private String ipv4;

    private boolean enable;

    private Date publish_time;

    private String content;
}
