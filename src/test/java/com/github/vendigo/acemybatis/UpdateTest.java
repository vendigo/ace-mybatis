package com.github.vendigo.acemybatis;

import com.github.vendigo.acemybatis.test.app.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class UpdateTest extends AbstractTest {

    private final User newPetya = new User("Petya", "Pomagay", "illhelpyou@gmail.com", "0931112112", "Nizhyn");
    private final User newBoris = new User("Boris", "Britva", "boris50@gmail.com", "309", "Dublin");
    private final User newEric = new User("Eric", "Cartman", "eric2006@gmail.com", "54915", "South Park");
    private final User newGalya = new User("Galya", "Ivanova", "galya_ivanova@gmail.com", "54915", "Yagotin");
    private final User newOstin = new User("Ostin", "Lyapunov", "ostin_lyapota@in.ua", "54915", "Kyiv");
    private final List<User> newUsers = Arrays.asList(newPetya, newBoris, newEric, newGalya, newOstin);

    @Before
    public void setUp() throws Exception {
        reInsertAllGuys();
    }

    @Test
    public void updateOne() throws Exception {
        userMapper.updateOne(newPetya);
        List<User> actualResults = userTestDao.selectAll();
        assertCollections(actualResults, Arrays.asList(newPetya, boris, eric, galya, ostin));
    }

    @Test
    public void updateWithParams() throws Exception {
        int updated = userMapper.updateWithParams("%@in.ua", "Kyiv");
        List<User> actualResults = userTestDao.selectAll();
        assertThat(updated, equalTo(1));
        assertCollections(actualResults, Arrays.asList(petya, boris, eric, galya, newOstin));
    }

    @Test
    public void updateSync() throws Exception {
        int updated = userMapper.updateSync(newUsers);
        List<User> actualResults = userTestDao.selectAll();
        assertThat(updated, equalTo(5));
        assertCollections(actualResults, newUsers);
    }
}
