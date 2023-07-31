package com.wsmrxd.bloglite.mapping;

import com.wsmrxd.bloglite.entity.Blog;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BlogMapper {

    @Select("SELECT * FROM blog WHERE id = #{id}")
    Blog selectTagByID(int id);

    @Select("SELECT * FROM blog ORDER BY id")
    List<Blog> selectAllBlogs();

    @Insert("INSERT INTO blog (title, content, thumb_ups) " +
            "VALUES (#{title}, #{content}, 0)")
    @SelectKey(statement="SELECT LAST_INSERT_ID() as id",
            keyProperty="id", before=false, resultType=int.class)
    int insertBlog(Blog newBlog);

    @Delete("DELETE FROM blog WHERE id = #{id}")
    boolean deleteBlogByID(int id);

    @Update("UPDATE blog set title = #{newTitle} WHERE id = #{id}")
    boolean updateBlogTitleByID(@Param("id") int id, @Param("newTitle") String newTitle);

    @Update("UPDATE blog set content = #{content} WHERE id = #{id}")
    boolean updateBlogContentByID(@Param("id") int id, @Param("content") String content);

    @Update("UPDATE blog set thumb_ups = thumb_ups + #{likes} WHERE id = #{id}")
    boolean updateBlogLikesByID(@Param("id") int id, @Param("likes") int likes);
}