package com.github.vendigo.acemybatis.util;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface UserMapper {
    List<User> selectList();

    Integer count();

    List<Map<String, Object>> selectMap();

    Stream<User> selectReactiveStream();

    Stream<User> selectSimpleStreamWithParams(@Param("excludeCity")String excludeCity,
                                              @Param("excludeName")String excludeName);

    Stream<User> selectSimpleStream();

    void insertOne(User user);

    int deleteAll();

    int deleteByEmail(String email);
}
