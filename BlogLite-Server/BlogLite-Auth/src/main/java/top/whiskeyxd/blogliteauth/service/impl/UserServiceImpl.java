package top.whiskeyxd.blogliteauth.service.impl;

import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import top.whiskeyxd.blogliteauth.dao.UserRepository;
import top.whiskeyxd.blogliteauth.entity.User;
import top.whiskeyxd.blogliteauth.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Nullable
    public User findUserByUsername(String username) {
        User queryExample = new User();
        queryExample.setUsername(username);
        var result = userRepository.findOne(Example.of(queryExample));

        return result.orElse(null);
    }
}
