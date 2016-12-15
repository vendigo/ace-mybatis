# ACE-MYBATIS

[![Build Status](https://travis-ci.org/vendigo/ace-mybatis.svg?branch=master)](https://travis-ci.org/vendigo/ace-mybatis)

Runtime generation efficient implementation for myBatis mappers

## Configuration

### Explicit mapper creation

```java
@Configuration
class SpringConfig {
@Bean
public UserMapper userMapper(SqlSessionFactory sqlSessionFactory) {
        return AceMapperFactory.<UserMapper>builder()
                .mapperInterface(UserMapper.class)
                .sqlSessionFactory(sqlSessionFactory)
                .selectChunkSize(2000)
                .updateChunkSize(1000)
                .threadCount(4)
                .build();
    }
}
```

### Auto discovering annotated interfaces

```java
@AceMapper
public interface UserMapper {
    Stream<User> selectUsers();
}

@Configuration
class SpringConfig {
@Bean
    public static AceMapperScannerConfigurer mapperScannerConfigurer() {
        return AceMapperScannerConfigurer.builder()
                .basePackage("com.github.vendigo.acemybatis.test.app")
                .selectChunkSize(2000)
                .updateChunkSize(1000)
                .threadCount(4)
                .build();
    }
}
```

## Supported methods

Apart from standard myBatis methods, efficient ace methods:

```java
@AceMapper
public interface UserMapper {
    Stream<User> selectUsers();

    int insertSync(List<User> users);

    CompletableFuture<Integer> insertAsync(List<User> users);

    ChangeCollector<User> insertCollector();

    int updateSync(List<User> users);

    CompletableFuture<Integer> updateAsync(List<User> users);

    ChangeCollector<User> updateCollector();

    int deleteSync(List<User> users);

    CompletableFuture<Integer> deleteAsync(List<User> users);

    ChangeCollector<User> deleteCollector();
}
```

## Xml mapper example

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.github.vendigo.acemybatis.test.app.UserMapper">
    <select id="selectUsersCount" resultType="int">
        SELECT count(*) FROM USER
    </select>

    <select id="selectUsers" resultType="com.github.vendigo.acemybatis.test.app.User">
        SELECT
        FIRST_NAME as firstName,
        LAST_NAME as lastName,
        EMAIL as email,
        PHONE_NUMBER as phoneNumber,
        CITY as city
        FROM USER
        ORDER BY EMAIL
    </select>

    <insert id="insertAsync" parameterType="com.github.vendigo.acemybatis.test.app.User">
        INSERT INTO USER (
        FIRST_NAME,
        LAST_NAME,
        EMAIL,
        PHONE_NUMBER,
        CITY
        )
        VALUES (
        #{firstName},
        #{lastName},
        #{email},
        #{phoneNumber},
        #{city}
        )
    </insert>
</mapper>
```

## Important notes

* Queries in xml mappers must match with the interface methods. Namespace - full class name. Query name - methodName.
* Count query must be named: "methodName"Count
