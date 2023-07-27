package com.wsmrxd.bloglite.mapping;

import com.wsmrxd.bloglite.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE id = #{id}")
    User selectUserByID(int id);

    @Select("SELECT * FROM user WHERE email = #{email}")
    User selectUserByEmail(String email);

    @Delete("Delete FROM user WHERE id = #{id}")
    boolean deleteUserByID(int id);

    @Delete("Delete FROM user WHERE email = #{email}")
    boolean deleteUserByEmail(String email);

    @Insert("INSERT INTO user (email, username, password, role)" +
            "VALUES (#{email}, #{username}, #{password}, #{role})")
    @SelectKey(statement="SELECT LAST_INSERT_ID() as id", keyProperty="id", before=false, resultType=int.class)
    int insertUser(User user);

    @Update("Update user SET password = #{newPassword} WHERE email = #{email}")
    boolean updateUserPassword(@Param("email") String email, @Param("newPassword") String newPassword);

    @Update("Update user SET password = #{newUsername} WHERE email = #{email}")
    boolean updateUserUsername(@Param("email") String email, @Param("newUsername") String newUsername);
}
