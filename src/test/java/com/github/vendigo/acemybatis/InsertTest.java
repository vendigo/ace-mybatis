package com.github.vendigo.acemybatis;

import com.github.vendigo.acemybatis.util.SpringTestConfig;
import com.github.vendigo.acemybatis.util.User;
import com.github.vendigo.acemybatis.util.UserMapper;
import com.github.vendigo.acemybatis.util.UserTestDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class, SpringTestConfig.class})
public class InsertTest {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserTestDao userTestDao;

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
