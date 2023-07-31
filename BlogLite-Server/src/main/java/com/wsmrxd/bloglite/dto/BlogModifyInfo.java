package com.wsmrxd.bloglite.dto;

public class BlogModifyInfo {
    private int id;

    private String newTitle;

    private String newContent;

    private int addLikes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNewTitle() {
        return newTitle;
    }

    public void setNewTitle(String newTitle) {
        this.newTitle = newTitle;
    }

    public String getNewContent() {
        return newContent;
    }

    public void setNewContent(String newContent) {
        this.newContent = newContent;
    }

    public int getAddLikes() {
        return addLikes;
    }

    public void setAddLikes(int addLikes) {
        this.addLikes = addLikes;
    }
}
