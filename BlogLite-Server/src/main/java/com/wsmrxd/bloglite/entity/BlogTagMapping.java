package com.wsmrxd.bloglite.entity;

public class BlogTagMapping {

    private int blog_id;

    private int tag_id;

    public BlogTagMapping(int blog_id, int tag_id) {
        this.blog_id = blog_id;
        this.tag_id = tag_id;
    }

    public int getBlog_id() {
        return blog_id;
    }

    public void setBlog_id(int blog_id) {
        this.blog_id = blog_id;
    }

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }
}
