package com.github.vendigo.acemybatis.util;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    List<User> selectList();

    Integer selectOne();

    List<Map<String, Object>> selectMap();

    void insertUser(User user);
}
