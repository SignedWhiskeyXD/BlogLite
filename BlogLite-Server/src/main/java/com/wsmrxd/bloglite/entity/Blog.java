package com.wsmrxd.bloglite.entity;

import com.wsmrxd.bloglite.dto.BlogUploadInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Blog {

    public Blog(BlogUploadInfo uploadInfo){
        this.title = uploadInfo.getTitle();
        this.contentAbstract = uploadInfo.getContentAbstract();
        this.content = uploadInfo.getContent();
        this.previewImage = uploadInfo.getPreviewImage();
    }

    private int id;

    private String title;

    private String contentAbstract = "博主很懒，连简介也没有写！";

    private String content;

    private int views;

    private Date publishTime;

    private String previewImage;
}
