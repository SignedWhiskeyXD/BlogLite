package com.wsmrxd.bloglite.mapping;

import com.wsmrxd.bloglite.entity.Comment;
import com.wsmrxd.bloglite.vo.CommentVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Select("SELECT * FROM comment WHERE id = #{id} ORDER BY id DESC")
    Comment selectCommentByID(int id);

    @Select("SELECT id, nickname, email, content FROM comment WHERE identify = #{ident} ORDER BY id DESC")
    List<CommentVO> selectAllCommentByIdent(int ident);

    @Insert("INSERT INTO comment (identify, nickname, email, enable, publish_time, content) " +
            "VALUES (#{identify}, #{nickname}, #{email}, #{enable}, #{publish_time}, #{content})")
    void insertComment(Comment comment);

    @Update("UPDATE comment SET enable = #{enable} WHERE id = #{id}")
    void updateCommentEnabled(@Param("id") int id, @Param("enable") boolean enable);
}
