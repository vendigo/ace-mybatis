package com.github.vendigo.acemybatis;

import com.github.vendigo.acemybatis.util.AbstractTest;
import com.github.vendigo.acemybatis.util.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class InsertTest extends AbstractTest {

    @Before
    public void setUp() throws Exception {
        userTestDao.deleteAll();
    }

    @Test
    public void insertOne() throws Exception {
        userMapper.insertOne(petya);
        List<User> actualResults = userTestDao.selectAll();
        assertThat(actualResults, equalTo(Collections.singletonList(petya)));
    }

    @Test
    public void insertSync() throws Exception {
        userMapper.insertSync(users);
        List<User> actualResults = userTestDao.selectAll();
        assertCollections(actualResults, users);
    }

    @Test
    public void insertAsync() throws Exception {
        CompletableFuture<Integer> future = userMapper.insertAsync(users);
        Integer inserted = future.get();
        List<User> actualResults = userTestDao.selectAll();
        assertThat(inserted, equalTo(5));
        assertCollections(actualResults, users);
    }

    @Test
    public void insertAsyncVoid() throws Exception {
        CompletableFuture<Void> future = userMapper.insertAsyncVoid(users);
        future.get();
        List<User> actualResults = userTestDao.selectAll();
        assertCollections(actualResults, users);
    }
}
