package com.github.vendigo.acemybatis.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserTestDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void deleteAll() {
        jdbcTemplate.execute("DELETE FROM USER");
    }

    public void insert(User user) {
        jdbcTemplate.update("INSERT INTO USER (FIRST_NAME, LAST_NAME, EMAIL, PHONE_NUMBER, CITY) " +
                "VALUES (?, ?, ?, ?, ?)", ps -> {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhoneNumber());
            ps.setString(5, user.getCity());
        });
    }
}
