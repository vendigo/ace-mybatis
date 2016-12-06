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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class, SpringTestConfig.class})
public class SelectTest {

    @Autowired
    UserMapper userMapper;
    @Autowired
    UserTestDao userTestDao;

    @Before
    public void setUp() throws Exception {
        userTestDao.deleteAll();
    }

    @Test
    public void selectList() throws Exception {
        userTestDao.insert(new User("Petya", "Pomagay", "illhelpyou@gmail.com", "25315", "Nizhyn"));
        List<User> users = userMapper.selectList();
        assertThat(users, hasSize(1));
        assertThat(users, hasItem(
                allOf(
                   hasProperty("email", equalTo("illhelpyou@gmail.com")),
                   hasProperty("firstName", equalTo("Petya"))
                )
        ));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void selectMap() throws Exception {
        User petya = new User("Petya", "Pomagay", "illhelpyou@gmail.com", "25315", "Nizhyn");
        User boris = new User("Boris", "Britva", "boris50@gmail.com", "344", "London");
        User eric = new User("Eric", "Cartman", "eric2006@gmail.com", "25315", "South Park");
        userTestDao.insert(petya);
        userTestDao.insert(boris);
        userTestDao.insert(eric);
        List<Map<String, Object>> actualResult = userMapper.selectMap();

        assertThat(actualResult, hasSize(3));
        assertThat(actualResult, containsInAnyOrder(userToMap(petya), userToMap(boris), userToMap(eric)));
    }

    @Test
    public void selectOne() throws Exception {
        userTestDao.insert(new User("Petya", "Pomagay", "illhelpyou@gmail.com", "25315", "Nizhyn"));
        userTestDao.insert(new User("Boris", "Britva", "boris50@gmail.com", "344", "London"));
        userTestDao.insert(new User("Eric", "Cartman", "eric2006@gmail.com", "25315", "South Park"));
        int count = userMapper.selectOne();
        assertThat(count, equalTo(3));
    }

    private Map<String, Object> userToMap(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("EMAIL", user.getEmail());
        map.put("FIRST_NAME", user.getFirstName());
        map.put("LAST_NAME", user.getLastName());
        map.put("CITY", user.getCity());
        map.put("PHONE_NUMBER", user.getPhoneNumber());
        return map;
    }
}
