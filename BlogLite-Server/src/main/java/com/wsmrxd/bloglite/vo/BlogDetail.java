package com.wsmrxd.bloglite.vo;

import com.wsmrxd.bloglite.entity.Blog;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.List;

@Data
@NoArgsConstructor
public class BlogDetail {

    public BlogDetail(Blog blog){
        this.id = blog.getId();
        this.title = blog.getTitle();
        this.views = blog.getViews();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        this.publishTime = formatter.format(blog.getPublishTime());
    }

    private int id;

    private String title;

    private String publishTime;

    private int views;

    private String contentHTML;

    private List<String> tagNames;
}
