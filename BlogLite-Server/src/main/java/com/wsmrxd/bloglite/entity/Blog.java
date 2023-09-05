package com.wsmrxd.bloglite.entity;

import com.wsmrxd.bloglite.dto.BlogUploadInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
public class Blog {

    public Blog(BlogUploadInfo uploadInfo){
        this.title = uploadInfo.getTitle();
        var uploadedAbstract = uploadInfo.getContentAbstract();
        if(uploadedAbstract != null && !uploadedAbstract.isEmpty())
            this.contentAbstract = uploadedAbstract;
        this.content = uploadInfo.getContent();
        this.previewImage = Objects.requireNonNullElse(uploadInfo.getPreviewImage(), "");
    }

    public boolean isModified(BlogUploadInfo modifyInfo){
        return  !this.title.equals(modifyInfo.getTitle()) ||
                !this.content.equals(modifyInfo.getContent()) ||
                !this.contentAbstract.equals(modifyInfo.getContentAbstract()) ||
                !this.previewImage.equals(modifyInfo.getPreviewImage());
    }

    private int id;

    private String title;

    private String contentAbstract = "博主很懒，连简介也没有写！";

    private String content;

    private int views;

    private Date publishTime;

    private String previewImage;
}
