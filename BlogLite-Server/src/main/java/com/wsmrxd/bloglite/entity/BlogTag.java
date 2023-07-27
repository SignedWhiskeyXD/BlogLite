package com.wsmrxd.bloglite.entity;

public class BlogTag {
    private Integer id;

    private String tagName;

    @Override
    public String toString() {
        return "BlogTag{" +
                "id=" + id +
                ", tagName='" + tagName + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
