package com.wsmrxd.bloglite.dto;

import lombok.Data;

@Data
public class BlogModifyInfo {
    private int id;

    private String newTitle;

    private String newContentAbstract;

    private String newContent;

    private int addLikes;
}
