package com.github.vendigo.acemybatis;

import com.github.vendigo.acemybatis.test.app.SpringTestConfig;
import com.github.vendigo.acemybatis.test.app.User;
import com.github.vendigo.acemybatis.test.app.UserMapper;
import com.github.vendigo.acemybatis.test.app.UserTestDao;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringTestConfig.class})
public abstract class AbstractTest {

    User petya = new User("Petya", "Pomagay", "illhelpyou@gmail.com", "25315", "Nizhyn");
    User boris = new User("Boris", "Britva", "boris50@gmail.com", "344", "London");
    User eric = new User("Eric", "Cartman", "eric2006@gmail.com", "25315", "South Park");
    User galya = new User("Galya", "Ivanova", "galya_ivanova@gmail.com", "54915", "Konotop");
    User ostin = new User("Ostin", "Lyapunov", "ostin_lyapota@in.ua", "54915", "Brovary");
    final List<User> users = Arrays.asList(petya, boris, eric, galya, ostin);

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserTestDao userTestDao;

    void reInsertAllGuys() throws Exception {
        userTestDao.deleteAll();
        userTestDao.insert(petya);
        userTestDao.insert(boris);
        userTestDao.insert(eric);
        userTestDao.insert(galya);
        userTestDao.insert(ostin);
    }

    public <T> void assertCollections(Collection<T> actual, Collection<T> expected) {
        assertThat(actual, hasSize(expected.size()));
        assertThat(actual, containsInAnyOrder(expected.toArray()));
    }
}
