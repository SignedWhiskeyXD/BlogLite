package com.wsmrxd.bloglite.mapping;

import com.wsmrxd.bloglite.annotation.MapperCache;
import com.wsmrxd.bloglite.entity.Comment;
import com.wsmrxd.bloglite.vo.CommentAdminDetail;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Select("SELECT * FROM comment WHERE id = #{id}")
    @MapperCache(value = "Comment", key = "#id")
    Comment selectCommentByID(int id);

    @Select("SELECT * FROM comment WHERE identify = #{ident} AND enable = true ORDER BY id DESC")
    List<Comment> selectAllCommentEnabledByIdent(int ident);

    @Select("SELECT b.title AS blogTitle, c.id as commentID, c.nickname, c.email, c.publish_time, c.content\n" +
            "FROM comment as c LEFT JOIN blog as b on c.identify = b.id\n" +
            "WHERE c.enable = false ORDER BY c.publish_time DESC LIMIT #{num}")
    List<CommentAdminDetail> selectCommentsDisabled(int num);

    @Insert("INSERT INTO comment (identify, nickname, email, enable, publish_time, content) " +
            "VALUES (#{identify}, #{nickname}, #{email}, #{enable}, #{publishTime}, #{content})")
    @SelectKey(statement="SELECT LAST_INSERT_ID()",
            keyProperty="id", before=false, resultType=int.class)
    void insertComment(Comment comment);

    @Update("UPDATE comment SET enable = #{enable} WHERE id = #{id}")
    void updateCommentEnabled(@Param("id") int id, @Param("enable") boolean enable);

    @Delete("DELETE FROM comment WHERE id = #{id}")
    void deleteCommentByID(int id);
}
