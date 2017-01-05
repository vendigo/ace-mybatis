package com.github.vendigo.acemybatis.test.app;

import com.github.vendigo.acemybatis.config.AceMapper;
import com.github.vendigo.acemybatis.config.NonBatchMethod;
import com.github.vendigo.acemybatis.method.change.ChangeCollector;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@AceMapper
public interface UserMapper {
    Integer count();

    List<User> selectList();

    List<Map<String, Object>> selectMap();

    Stream<User> selectReactiveStream();

    Stream<User> selectSimpleStreamWithParams(@Param("excludeCity") String excludeCity,
                                              @Param("excludeName") String excludeName);

    Stream<User> selectSimpleStream();

    void insertOne(User user);

    void insertSync(List<User> users);

    @NonBatchMethod
    void insertListAsOne(List<User> users);

    void insertWithAdditionalParameters(@Param("list")List<User> users, @Param("customCity")String customCity);

    CompletableFuture<Integer> insertAsync(List<User> users);

    CompletableFuture<Void> insertAsyncVoid(List<User> users);

    ChangeCollector<User> insertCollector();

    void updateOne(User user);

    int updateWithParams(@Param("emailLike") String emailLike, @Param("city") String city);

    int updateSync(List<User> users);

    CompletableFuture<Integer> updateAsync(List<User> users);

    ChangeCollector<User> updateCollector();

    int deleteAll();

    int deleteByEmail(String email);

    int syncDelete(List<User> users);

    CompletableFuture<Integer> asyncDelete(List<User> users);

    ChangeCollector<User> deleteCollector();
}
