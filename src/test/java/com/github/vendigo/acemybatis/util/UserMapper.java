package com.github.vendigo.acemybatis.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface UserMapper {
    List<User> selectList();

    Integer selectOne();

    List<Map<String, Object>> selectMap();

    Stream<User> selectStream();

    void insertOne(User user);

    int deleteAll();

    int deleteByEmail(String email);
}
