package com.wsmrxd.bloglite.vo;

import com.wsmrxd.bloglite.entity.Blog;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BlogAdminDetail {
    private Blog blog;

    private List<String> tagNames = new ArrayList<>();

    private List<String> collections = new ArrayList<>();

    public BlogAdminDetail(Blog blog, List<String> blogTagNames, List<String> blogCollectionNames) {
        this.blog = blog;
        tagNames = blogTagNames;
        this.collections = blogCollectionNames;
    }
}
