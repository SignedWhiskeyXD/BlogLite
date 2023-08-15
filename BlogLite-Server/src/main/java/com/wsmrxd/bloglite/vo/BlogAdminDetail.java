package com.wsmrxd.bloglite.vo;

import com.wsmrxd.bloglite.entity.Blog;
import com.wsmrxd.bloglite.entity.BlogTag;
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

    public BlogAdminDetail(Blog blog, List<BlogTag> blogTags) {
        this.blog = blog;
        for(var tag : blogTags)
            tagNames.add(tag.getTagName());
    }
}
