package com.wsmrxd.bloglite.enums;

public enum RedisKeyForHash {

    // 该Hash包含了所有文章的实时阅读量，键为文章ID，值为阅读量
    Integer_BlogViewsByID,

    // 该Hash包含了所有需要增加阅读量的文章信息，键为文章ID，值为与数据库同步时新增的阅读量
    Integer_AddBlogViewsByID,

    // 该Hash包含下面两个键
    Integer_SiteInfo,

    // 对应总阅读量
    SiteInfo_TotalViews,

    // 对应总文章数
    SiteInfo_TotalBlogs
}
