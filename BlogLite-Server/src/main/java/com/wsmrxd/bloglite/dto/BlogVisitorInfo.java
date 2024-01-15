package com.wsmrxd.bloglite.dto;

import lombok.Data;

import javax.annotation.Nullable;

@Data
public class BlogVisitorInfo {

    private String ipAddr;

    @Nullable
    private String userAgent;

    private int blogID;
}
