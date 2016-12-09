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
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItems;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class, SpringTestConfig.class})
public class SelectTest {

    private final User petya = new User("Petya", "Pomagay", "illhelpyou@gmail.com", "25315", "Nizhyn");
    private final User boris = new User("Boris", "Britva", "boris50@gmail.com", "344", "London");
    private final User eric = new User("Eric", "Cartman", "eric2006@gmail.com", "25315", "South Park");
    private final User galya = new User("Galya", "Ivanova", "galya_ivanova@gmail.com", "54915", "Konotop");
    private final User ostin = new User("Ostin", "Lyapunov", "ostin_lyapota@in.ua", "54915", "Brovary");

    @Autowired
    UserMapper userMapper;
    @Autowired
    UserTestDao userTestDao;

    @Before
    public void setUp() throws Exception {
        userTestDao.deleteAll();
        userTestDao.insert(petya);
        userTestDao.insert(boris);
        userTestDao.insert(eric);
    }

    @Test
    public void selectList() throws Exception {
        List<User> users = userMapper.selectList();
        assertAllGuys(users);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void selectMap() throws Exception {
        List<Map<String, Object>> actualResult = userMapper.selectMap();
        assertThat(actualResult, hasSize(3));
        assertThat(actualResult, containsInAnyOrder(userToMap(petya), userToMap(boris), userToMap(eric)));
    }

    @Test
    public void selectOne() throws Exception {
        int count = userMapper.count();
        assertThat(count, equalTo(3));
    }

    @Test
    public void selectReactiveStream() throws Exception {
        List<User> users = userMapper.selectReactiveStream().collect(Collectors.toList());
        assertAllGuys(users);
    }

    @Test
    public void selectSimpleStreamWithParams() throws Exception {
        userTestDao.insert(galya);
        userTestDao.insert(ostin);
        List<User> users = userMapper.selectSimpleStreamWithParams("Konotop", "Ostin").collect(Collectors.toList());
        assertAllGuys(users);
    }

    @Test
    public void selectSimpleStream() throws Exception {
        List<User> users = userMapper.selectSimpleStream().collect(Collectors.toList());
        assertAllGuys(users);
    }

    @SuppressWarnings("unchecked")
    private void assertAllGuys(List<User> users) {
        assertThat(users, hasSize(3));
        assertThat(users, hasItems(
                hasProperty("email", equalTo("illhelpyou@gmail.com")),
                hasProperty("email", equalTo("boris50@gmail.com")),
                hasProperty("email", equalTo("eric2006@gmail.com"))
        ));
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
