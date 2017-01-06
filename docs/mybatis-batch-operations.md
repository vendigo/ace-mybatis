Lets imagine that our goal is to insert big amount of data with myBatis.

We could use the following mapper:

```java
public interface UserMapper {
    void insertUser(User user);
}
```

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.github.vendigo.acemybatis.test.app.UserMapper">
    <insert id="insertUser" parameterType="com.github.vendigo.acemybatis.test.app.User">
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

And perform inserts:

```java
for (User user: users) {
    userMapper.insert(user);
}
```

But it turns out that this approach is extremely slow. It performs sqlSession.commit after each call.
As possible solution we could try the next option:

```java
public interface UserMapper {
    void insertUsers(List<User> user);
}
```

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.github.vendigo.acemybatis.test.app.UserMapper">
    <insert id="insertUsers" parameterType="java.util.List">
      INSERT INTO USER (
      FIRST_NAME,
      LAST_NAME,
      EMAIL,
      PHONE_NUMBER,
      CITY
      )
      VALUES (
      <foreach collection="list" item="element" separator="),(">
       #{element.firstName},
       #{element.lastName},
       #{element.email},
       #{element.phoneNumber},
       #{element.city}
      </foreach>
             )
    </insert>
</mapper>
```

In this case myBatis inserts list of Users in one commit, by creating huge prepared statement.
It works faster than previous method, but there is very important shortcoming.
Prepared statement cannot have more than 2100 parameters.
In our example it will fail with 420 users (2100/5 parameters per user).

It is impossible to solve our problem using just generated mapper implementation.
We have to descend to the lower level and use sqlSession directly.

```java
int i = 0;
try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false)) {
            for (User user : users) {
                sqlSession.insertOne(STATEMENT_NAME, user);
                i++;
                if (i % CHUNK_SIZE == 0) {
                    sqlSession.commit();
                }
            }
            sqlSession.commit();
        }
```

In basic variant it might look like that. To be even more efficient we can parallelize this logic and perform it
asynchronously, immediately returning Future/CompletableFuture.

The main goal of the project ace-mybatis is to cover all this logic under the hood and generate efficient implementations
out of the box.