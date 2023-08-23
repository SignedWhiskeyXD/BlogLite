package com.wsmrxd.bloglite.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogTag {

    public BlogTag(String name){
        this.tagName = name;
    }

    private Integer id;

    private String tagName;
}
