package com.wsmrxd.bloglite.vo;

import com.wsmrxd.bloglite.entity.Blog;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BlogCard {

    public BlogCard(Blog blog){
        this.id = blog.getId();
        this.title = blog.getTitle();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        this.publishTime = formatter.format(blog.getPublishTime());
    }

    private int id;

    private String title;

    private String publishTime;

    private int views;

    private String contentAbstract;

    private String previewImage;

    private List<String> tagNames;
}
