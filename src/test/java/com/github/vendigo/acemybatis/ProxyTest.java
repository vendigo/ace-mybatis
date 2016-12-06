package com.github.vendigo.acemybatis;

import com.github.vendigo.acemybatis.util.SpringTestConfig;
import com.github.vendigo.acemybatis.util.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class, SpringTestConfig.class})
public class ProxyTest {
    @Autowired
    UserMapper userMapper;

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Test
    public void proxyHasObjectMethods() throws Exception {
        assertThat(userMapper.equals("string"), is(false));
        assertThat(userMapper.hashCode(), notNullValue());
        assertThat(userMapper.toString(), notNullValue());
    }
}
