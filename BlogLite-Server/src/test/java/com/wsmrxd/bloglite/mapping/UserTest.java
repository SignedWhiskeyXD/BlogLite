package com.wsmrxd.bloglite.mapping;

import com.wsmrxd.bloglite.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTest {
    @Autowired
    UserMapper mapper;

    @Test
    public void testSelect(){
        var result = mapper.selectUserByID(1);
        System.out.println(result);
        result = mapper.selectUserByEmail("wsmrxd@gmail.com");
        System.out.println(result);
    }

    @Test
    public void testInsertAndDelete(){
        var newBlogger = new User("123@123.com", "wsmrxd",
                "114514", "visitor");
        var assignedID = mapper.insertUser(newBlogger);
        System.out.println(newBlogger.getId());
        var result = mapper.deleteUserByEmail(newBlogger.getEmail());
        System.out.println(result);
    }

}
