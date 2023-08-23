package com.wsmrxd.bloglite.mapping;

import com.wsmrxd.bloglite.entity.*;
import com.wsmrxd.bloglite.vo.BlogPreview;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BlogMapper {

    @Select("SELECT * FROM blog WHERE id = #{id}")
    Blog selectBlogByID(int id);

    @Select("SELECT id, title FROM blog ORDER BY id DESC")
    List<BlogPreview> selectAllBlogs();

    @Select("SELECT id FROM blog ORDER BY id DESC")
    List<Integer> selectAllBlogID();

    @Select("SELECT bt.* FROM blog_tag_mapping btm\n" +
            "INNER JOIN blog_tag bt ON btm.tag_id = bt.id\n" +
            "WHERE btm.blog_id = #{id}")
    List<BlogTag> selectTagsByBlogID(int id);

    @Select("SELECT bt.tag_name FROM blog_tag_mapping btm\n" +
            "INNER JOIN blog_tag bt ON btm.tag_id = bt.id\n" +
            "WHERE btm.blog_id = #{id}")
    List<String> selectTagNamesByBlogID(int id);

    @Select("SELECT bc.* FROM blog_collection_mapping bcm\n" +
            "INNER JOIN blog_collection bc ON bcm.collection_id = bc.id\n" +
            "WHERE bcm.blog_id = #{id}")
    List<BlogCollection> selectBlogCollectionByBlogID(int id);
    @Select("SELECT bc.collection_name FROM blog_collection_mapping bcm\n" +
            "INNER JOIN blog_collection bc ON bcm.collection_id = bc.id\n" +
            "WHERE bcm.blog_id = #{id}")
    List<String> selectCollectionNamesByBlogID(int id);

    @Select("SELECT views FROM blog WHERE id = #{id}")
    int selectViewsByBlogID(int id);

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

    @Update("UPDATE blog set title = #{newTitle} WHERE id = #{id}")
    boolean updateBlogTitleByID(@Param("id") int id, @Param("newTitle") String newTitle);

    @Update("UPDATE blog set content_abstract = #{newAbstract} WHERE id = #{id}")
    boolean updateBlogAbstractByID(@Param("id") int id, @Param("newAbstract") String newAbstract);

    @Update("UPDATE blog set content = #{content} WHERE id = #{id}")
    boolean updateBlogContentByID(@Param("id") int id, @Param("content") String content);

    @Update("UPDATE blog set views = views + #{views} WHERE id = #{id}")
    boolean updateBlogViewsByID(@Param("id") int id, @Param("views") int views);
}