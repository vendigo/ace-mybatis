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

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class, SpringTestConfig.class})
public class DeleteTest {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserTestDao userTestDao;

    @Before
    public void setUp() throws Exception {
        userTestDao.deleteAll();
    }

    @Test
    public void deleteAll() throws Exception {
        User petya = new User("Petya", "Pomagay", "illhelpyou@gmail.com", "25315", "Nizhyn");
        User boris = new User("Boris", "Britva", "boris50@gmail.com", "344", "London");
        User eric = new User("Eric", "Cartman", "eric2006@gmail.com", "25315", "South Park");
        userTestDao.insert(petya);
        userTestDao.insert(boris);
        userTestDao.insert(eric);

        int rowCount = userMapper.deleteAll();
        assertThat(rowCount, equalTo(3));

        List<User> actualResults = userTestDao.selectAll();
        assertThat(actualResults, hasSize(0));
    }

    @Test
    public void deleteByEmail() throws Exception {
        User petya = new User("Petya", "Pomagay", "illhelpyou@gmail.com", "25315", "Nizhyn");
        User boris = new User("Boris", "Britva", "boris50@gmail.com", "344", "London");
        User eric = new User("Eric", "Cartman", "eric2006@gmail.com", "25315", "South Park");
        userTestDao.insert(petya);
        userTestDao.insert(boris);
        userTestDao.insert(eric);

        int rowCount = userMapper.deleteByEmail("boris50@gmail.com");
        assertThat(rowCount, equalTo(1));

        List<User> actualResults = userTestDao.selectAll();
        assertThat(actualResults, hasSize(2));
        assertThat(actualResults, containsInAnyOrder(petya, eric));
    }
}
