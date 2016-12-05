package com.github.vendigo.acemybatis;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class, SpringTestConfig.class})
public class SimpleTest {

    @Autowired
    UserMapper userMapper;

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Test
    public void proxyHasObjectMethods() throws Exception {
        assertThat(userMapper.equals("string"), is(false));
        assertThat(userMapper.hashCode(), notNullValue());
        assertThat(userMapper.toString(), notNullValue());
    }

    @Test
    public void simpleCall() throws Exception {
        userMapper.selectUsers();
    }

    @Test
    @Ignore
    public void insertAndSelect() throws Exception {
        userMapper.insertUser(new User("Petya", "Pomagay", "illhelpyou@gmail.com", "25315", "Nizhyn"));
        List<User> users = userMapper.selectUsers();
        assertThat(users, hasSize(1));
        assertThat(users, hasItem(
                allOf(
                   hasProperty("email", equalTo("illhelpyou@gmail.com")),
                   hasProperty("firstName", equalTo("Petya"))
                )
        ));
    }
}
