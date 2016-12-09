package com.github.vendigo.acemybatis.util;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringTestConfig.class})
public abstract class AbstractTest {
    @Autowired
    protected UserMapper userMapper;
    @Autowired
    protected UserTestDao userTestDao;
}
