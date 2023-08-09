package com.wsmrxd.bloglite.mapping;

import com.wsmrxd.bloglite.entity.Blog;
import com.wsmrxd.bloglite.entity.BlogTag;
import com.wsmrxd.bloglite.entity.BlogTagMapping;
import com.wsmrxd.bloglite.vo.BlogPageView;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BlogMapper {

    @Select("SELECT * FROM blog WHERE id = #{id}")
    Blog selectBlogByID(int id);

    @Select("SELECT id, title FROM blog ORDER BY id DESC")
    List<BlogPageView> selectAllBlogs();

    @Select("SELECT id FROM blog WHERE id < #{latestID} ORDER BY id DESC LIMIT #{num}")
    List<Integer> selectLatestBlogIDs(@Param("latestID") int latestID, @Param("num") int num);

    @Select("SELECT bt.* FROM blog_tag_mapping btm\n" +
            "INNER JOIN blog_tag bt ON btm.tag_id = bt.id\n" +
            "WHERE btm.blog_id = #{id}")
    List<BlogTag> selectTagsByBlogID(int id);

    @Select("SELECT bt.tag_name FROM blog_tag_mapping btm\n" +
            "INNER JOIN blog_tag bt ON btm.tag_id = bt.id\n" +
            "WHERE btm.blog_id = #{id}")
    List<String> selectTagNamesByBlogID(int id);

    @Insert("INSERT INTO blog_tag_mapping VALUES (#{blog_id}, #{tag_id})")
    void insertBlogTagMapping(BlogTagMapping mapping);

    @Insert("INSERT INTO blog (title, content, thumb_ups) " +
            "VALUES (#{title}, #{content}, 0)")
    @SelectKey(statement="SELECT LAST_INSERT_ID()",
            keyProperty="id", before=false, resultType=int.class)
    void insertBlog(Blog newBlog);

    @Delete("DELETE FROM blog WHERE id = #{id}")
    boolean deleteBlogByID(int id);

    @Delete("DELETE FROM blog_tag_mapping WHERE blog_id = #{blogID}")
    boolean deleteTagMappingByBlogID(int blogID);

    @Update("UPDATE blog set title = #{newTitle} WHERE id = #{id}")
    boolean updateBlogTitleByID(@Param("id") int id, @Param("newTitle") String newTitle);

    @Update("UPDATE blog set content = #{content} WHERE id = #{id}")
    boolean updateBlogContentByID(@Param("id") int id, @Param("content") String content);

    @Update("UPDATE blog set thumb_ups = thumb_ups + #{likes} WHERE id = #{id}")
    boolean updateBlogLikesByID(@Param("id") int id, @Param("likes") int likes);
}