package com.wsmrxd.bloglite.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Blog {

    private int id;

    private String title;

    private String contentAbstract = "博主很懒，连简介也没有写！";

    private String content;

    private int views;

    private Date publishTime;

    private String previewImage;
}
