package com.wsmrxd.bloglite.mapping;

import com.wsmrxd.bloglite.entity.Comment;
import com.wsmrxd.bloglite.vo.CommentVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Select("SELECT * FROM comment WHERE id = #{id}")
    Comment selectCommentByID(int id);

    @Select("SELECT * FROM comment WHERE identify = #{ident} ORDER BY id DESC")
    List<Comment> selectAllCommentByIdent(int ident);

    @Insert("INSERT INTO comment (identify, nickname, email, enable, publish_time, content) " +
            "VALUES (#{identify}, #{nickname}, #{email}, #{enable}, #{publishTime}, #{content})")
    @SelectKey(statement="SELECT LAST_INSERT_ID()",
            keyProperty="id", before=false, resultType=int.class)
    void insertComment(Comment comment);

    @Update("UPDATE comment SET enable = #{enable} WHERE id = #{id}")
    void updateCommentEnabled(@Param("id") int id, @Param("enable") boolean enable);
}
