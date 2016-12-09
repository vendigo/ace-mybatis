package com.github.vendigo.acemybatis;

import com.github.vendigo.acemybatis.util.AbstractTest;
import com.github.vendigo.acemybatis.util.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class InsertTest extends AbstractTest {

    @Before
    public void setUp() throws Exception {
        userTestDao.deleteAll();
    }

    @Test
    public void insertOne() throws Exception {
        User petya = new User("Petya", "Pomagay", "illhelpyou@gmail.com", "25315", "Nizhyn");
        userMapper.insertOne(petya);
        List<User> actualResults = userTestDao.selectAll();
        assertThat(actualResults, equalTo(Collections.singletonList(petya)));
    }
}
