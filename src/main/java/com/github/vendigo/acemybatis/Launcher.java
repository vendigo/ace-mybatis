package com.github.vendigo.acemybatis;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Launcher {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext(SpringConfig.class);
        UserMapper userMapper = appContext.getBean(UserMapper.class);
        System.out.println("Before: "+userMapper.selectUsers());
        userMapper.insertUser(new User("Petya", "Pomagay", "illhelpyou@gmail.com", "25315", "Nizhyn"));
        System.out.println("After: "+userMapper.selectUsers());
    }
}
