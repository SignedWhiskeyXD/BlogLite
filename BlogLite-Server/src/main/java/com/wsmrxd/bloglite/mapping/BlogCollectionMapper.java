package com.wsmrxd.bloglite.mapping;

import com.wsmrxd.bloglite.entity.BlogCollection;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BlogCollectionMapper {

    @Select("SELECT * FROM blog_collection WHERE id = #{collectionID}")
    BlogCollection selectBlogCollectionByID(int collectionID);

    @Select("SELECT * FROM blog_collection WHERE collection_name = #{collectionName}")
    BlogCollection selectBlogCollectionByName(String collectionName);

    @Select("SELECT * FROM blog_collection ORDER BY id DESC")
    List<BlogCollection> selectAllBlogCollection();

    @Select("SELECT blog_id FROM blog_collection_mapping WHERE collection_id = #{collectionID}")
    List<Integer> selectBlogIDsByCollectionID(int collectionID);

    @Insert("INSERT INTO blog_collection (image_link, collection_name, description) " +
            "VALUES (#{imageLink}, #{collectionName}, #{description})")
    @SelectKey(statement="SELECT LAST_INSERT_ID()",
            keyProperty="id", before=false, resultType=int.class)
    void insertBlogCollection(BlogCollection blogCollection);

    @Delete("DELETE FROM blog_collection WHERE id = #{collectionID}")
    void deleteBlogCollectionByID(int collectionID);

    @Update("UPDATE blog_collection SET " +
            "image_link = #{imageLink}, collection_name = #{collectionName}, description = {description}" +
            "WHERE id = #{id}")
    void updateBlogCollection(BlogCollection modifyInfo);
}
