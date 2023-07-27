package com.wsmrxd.bloglite.mapping;

import com.wsmrxd.bloglite.entity.BlogTag;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BlogTagMapper {
    @Select("SELECT * FROM blog_tag WHERE id = #{id}")
    BlogTag selectTagByID(int id);

    @Select("SELECT * FROM blog_tag WHERE tag_name = #{name}")
    BlogTag selectTagByName(String name);

    @Select("SELECT * FROM blog_tag")
    List<BlogTag> selectAllTags();

    @Insert("INSERT INTO blog_tag (tag_name) VALUES (#{name})")
    boolean insertTagFromName(String name);

    @Delete("DELETE FROM blog_tag WHERE id = #{id}")
    boolean deleteTagByID(int id);

    @Delete("DELETE FROM blog_tag WHERE tag_name = #{name}")
    boolean deleteTagByName(String name);

    @Update("UPDATE blog_tag SET tag_name = #{newName} WHERE id = #{id}")
    boolean updateTagNameByID(@Param("id") int id, @Param("newName") String newName);

    @Update("UPDATE blog_tag SET tag_name = #{newName} WHERE tag_name = #{oldName}")
    boolean updateTagNameByName(@Param("oldName") String oldName, @Param("newName") String newName);
}
