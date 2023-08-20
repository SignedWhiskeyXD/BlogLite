package com.wsmrxd.bloglite.vo;

import lombok.Data;

import java.util.List;

@Data
public class BlogStream {

    private int blogNum;

    private Integer nextRequestParam;

    private List<BlogCard> blogList;
}
