package com.wsmrxd.bloglite.dto;

import lombok.Data;

import java.util.List;

@Data
public class BlogUploadInfo {

    private String title;

    private String contentAbstract;

    private String content;

    private List<String> tagNames;
}
