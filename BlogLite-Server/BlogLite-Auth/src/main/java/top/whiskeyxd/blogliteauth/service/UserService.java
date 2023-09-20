package top.whiskeyxd.blogliteauth.service;

import jakarta.annotation.Nullable;
import top.whiskeyxd.blogliteauth.entity.User;

public interface UserService {

    @Nullable
    User findUserByUsername(String username);

}
