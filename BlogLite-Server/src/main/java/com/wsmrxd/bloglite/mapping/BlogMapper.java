package com.wsmrxd.bloglite.mapping;

import com.wsmrxd.bloglite.annotation.MapperCache;
import com.wsmrxd.bloglite.entity.Blog;
import com.wsmrxd.bloglite.entity.BlogCollection;
import com.wsmrxd.bloglite.entity.BlogCollectionMapping;
import com.wsmrxd.bloglite.entity.BlogTagMapping;
import com.wsmrxd.bloglite.vo.BlogPreview;
import org.apache.ibatis.annotations.*;

import javax.annotation.Nullable;
import java.util.List;

@Mapper
public interface BlogMapper {

    @Select("SELECT * FROM blog WHERE id = #{id}")
    @MapperCache(value = "Blog", key = "#id")
    Blog selectBlogByID(int id);

    @Select("SELECT id, title, views FROM blog ORDER BY id DESC")
    List<BlogPreview> selectAllBlogs();

    @Select("SELECT id, title, views FROM blog ORDER BY views DESC LIMIT #{limit}")
    List<BlogPreview> selectBlogsOrderByViews(int limit);

    @Select("SELECT id FROM blog ORDER BY id DESC")
    List<Integer> selectAllBlogID();

    @Select("SELECT id FROM blog " +
            "WHERE title LIKE #{pattern} OR content_abstract LIKE #{pattern} OR content LIKE #{pattern} " +
            "ORDER BY id DESC")
    List<Integer> selectBlogIDByContentPattern(String pattern);

    @Select("SELECT bt.tag_name FROM blog_tag_mapping btm\n" +
            "INNER JOIN blog_tag bt ON btm.tag_id = bt.id\n" +
            "WHERE btm.blog_id = #{id}")
    @MapperCache(value = "TagNamesOfBlog", key = "#id")
    List<String> selectTagNamesByBlogID(int id);

    @Select("SELECT bc.* FROM blog_collection_mapping bcm\n" +
            "INNER JOIN blog_collection bc ON bcm.collection_id = bc.id\n" +
            "WHERE bcm.blog_id = #{id}")
    List<BlogCollection> selectBlogCollectionByBlogID(int id);

    @Select("SELECT bc.collection_name FROM blog_collection_mapping bcm\n" +
            "INNER JOIN blog_collection bc ON bcm.collection_id = bc.id\n" +
            "WHERE bcm.blog_id = #{id}")
    List<String> selectCollectionNamesByBlogID(int id);

    @Nullable
    @Select("SELECT views FROM blog WHERE id = #{id}")
    Integer selectViewsByBlogID(int id);

    @Insert("INSERT INTO blog_tag_mapping VALUES (#{blog_id}, #{tag_id})")
    void insertBlogTagMapping(BlogTagMapping mapping);

    @Insert("INSERT INTO blog_collection_mapping VALUES (#{blogID}, #{collectionID})")
    void insertBlogCollectionMapping(BlogCollectionMapping mapping);

    @Insert("INSERT INTO blog (title, content_abstract, content, preview_image) " +
            "VALUES (#{title}, #{contentAbstract}, #{content}, #{previewImage})")
    @SelectKey(statement="SELECT LAST_INSERT_ID()",
            keyProperty="id", before=false, resultType=int.class)
    void insertBlog(Blog newBlog);

    @Select("SELECT COUNT(*) FROM blog")
    Integer selectBlogCount();

    @Select("SELECT SUM(views) FROM blog")
    Integer selectViewsCount();

    @Delete("DELETE FROM blog WHERE id = #{id}")
    boolean deleteBlogByID(int id);

    @Delete("DELETE FROM blog_tag_mapping WHERE blog_id = #{blogID}")
    void deleteTagMappingByBlogID(int blogID);

    @Delete("DELETE FROM blog_collection_mapping WHERE blog_id = #{blogID}")
    void deleteCollectionMappingByBlogID(int blogID);

    @Update("UPDATE blog set views = #{viewsUpdate} WHERE id = #{id}")
    void updateBlogViewsByID(@Param("id") int id, @Param("viewsUpdate") int viewsUpdate);

    @Update("UPDATE blog SET " +
            "title = #{modified.title}, " +
            "content = #{modified.content}, " +
            "content_abstract = #{modified.contentAbstract}, " +
            "preview_image = #{modified.previewImage} " +
            "WHERE id = #{id}")
    void updateBlogByModifyInfo(@Param("id") int id, @Param("modified") Blog modified);
}