package com.wsmrxd.bloglite.vo;

import com.wsmrxd.bloglite.entity.BlogCollection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BlogCollectionVO {

    private int id;

    private String imageLink;

    private String collectionName;

    private String description;

    private int blogNum = 0;

    private int totalViews = 0;

    public BlogCollectionVO(BlogCollection blogCollection){
        this.id = blogCollection.getId();
        this.imageLink = blogCollection.getImageLink();
        this.collectionName = blogCollection.getCollectionName();
        this.description = blogCollection.getDescription();
    }
}
