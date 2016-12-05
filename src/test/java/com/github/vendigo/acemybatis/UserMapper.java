package com.github.vendigo.acemybatis;

import java.util.List;

public interface UserMapper {
    List<User> selectUsers();

    void insertUser(User user);
}
