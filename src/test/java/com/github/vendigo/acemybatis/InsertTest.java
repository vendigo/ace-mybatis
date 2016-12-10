package com.github.vendigo.acemybatis;

import com.github.vendigo.acemybatis.util.AbstractTest;
import com.github.vendigo.acemybatis.util.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

public class InsertTest extends AbstractTest {

    private final User petya = new User("Petya", "Pomagay", "illhelpyou@gmail.com", "25315", "Nizhyn");
    private final User boris = new User("Boris", "Britva", "boris50@gmail.com", "344", "London");
    private final User eric = new User("Eric", "Cartman", "eric2006@gmail.com", "25315", "South Park");
    private final User galya = new User("Galya", "Ivanova", "galya_ivanova@gmail.com", "54915", "Konotop");
    private final User ostin = new User("Ostin", "Lyapunov", "ostin_lyapota@in.ua", "54915", "Brovary");
    private final List<User> users = Arrays.asList(petya, boris, eric, galya, ostin);

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
        assertThat(actualResults, hasSize(5));
        assertThat(actualResults, containsInAnyOrder(users.toArray()));
    }

    @Test
    public void insertAsync() throws Exception {
        CompletableFuture<Integer> future = userMapper.insertAsync(users);
        Integer inserted = future.get();
        List<User> actualResults = userTestDao.selectAll();
        assertThat(inserted, equalTo(5));
        assertThat(actualResults, hasSize(5));
        assertThat(actualResults, containsInAnyOrder(users.toArray()));
    }

    @Test
    public void insertAsyncVoid() throws Exception {
        CompletableFuture<Void> future = userMapper.insertAsyncVoid(users);
        future.get();
        List<User> actualResults = userTestDao.selectAll();
        assertThat(actualResults, hasSize(5));
        assertThat(actualResults, containsInAnyOrder(users.toArray()));
    }
}
