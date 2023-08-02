package com.wsmrxd.bloglite.vo;

import com.wsmrxd.bloglite.entity.Blog;
import com.wsmrxd.bloglite.entity.BlogTag;

import java.util.ArrayList;
import java.util.List;

public class BlogView {
    private Blog blog;

    private List<String> tagNames = new ArrayList<>();

    public BlogView(Blog blog, List<BlogTag> blogTags) {
        this.blog = blog;
        for(var tag : blogTags)
            tagNames.add(tag.getTagName());
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public List<String> getTagNames() {
        return tagNames;
    }

    public void setTagNames(List<String> tagNames) {
        this.tagNames = tagNames;
    }
}
