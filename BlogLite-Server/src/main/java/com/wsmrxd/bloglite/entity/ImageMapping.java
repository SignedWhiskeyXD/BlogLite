package com.wsmrxd.bloglite.entity;

import lombok.Data;

@Data
public class ImageMapping {

    private Integer id;

    private String source;

    private String md5;

    private String folder;

    private String originalName;

    private String typeSuffix;
}
