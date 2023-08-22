package com.wsmrxd.bloglite.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BlogTagMapping {

    private int blog_id;

    private int tag_id;

    public BlogTagMapping(int blog_id, int tag_id) {
        this.blog_id = blog_id;
        this.tag_id = tag_id;
    }
}
