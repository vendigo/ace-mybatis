package com.github.vendigo.acemybatis;

import com.github.vendigo.acemybatis.test.app.User;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class SelectTest extends AbstractTest {

    @Before
    public void setUp() throws Exception {
        reInsertAllGuys();
    }

    @Test
    public void selectList() throws Exception {
        List<User> actualUsers = userMapper.selectList();
        assertCollections(actualUsers, users);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void selectMap() throws Exception {
        List<Map<String, Object>> actualResult = userMapper.selectMap();
        assertCollections(actualResult, asList(userToMap(petya), userToMap(boris), userToMap(eric),
                userToMap(galya), userToMap(ostin)));
    }

    @Test
    public void selectOne() throws Exception {
        int count = userMapper.count();
        assertThat(count, equalTo(5));
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
