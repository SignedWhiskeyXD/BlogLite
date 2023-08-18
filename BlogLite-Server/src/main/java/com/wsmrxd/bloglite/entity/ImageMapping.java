package com.wsmrxd.bloglite.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImageMapping {

    public ImageMapping(String md5, String folder, String originalName, String typeSuffix) {
        this.md5 = md5;
        this.folder = folder;
        this.originalName = originalName;
        this.typeSuffix = typeSuffix;
    }

    private Integer id;

    private String source;

    private String md5;

    private String folder;

    private String originalName;

    private String typeSuffix;
}
