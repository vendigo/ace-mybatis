package com.github.vendigo.acemybatis.method.insert;

import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class InsertHelper {
    static CompletableFuture<Integer> insertAsync(SqlSessionFactory sqlSessionFactory, Method method,
                                                  List<Object> entities, int chunkSize, int threadCount) {
        return CompletableFuture.supplyAsync(() -> {
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            List<List<Object>> parts = divideOnParts(entities, threadCount);

            return IntStream.range(0, threadCount)
                    .mapToObj((n) -> executorService.submit(new InsertTask(sqlSessionFactory, method, parts.get(n),
                            chunkSize)))
                    .map(InsertHelper::getFromFuture)
                    .mapToInt(Integer::valueOf)
                    .sum();
        });
    }

    static List<List<Object>> divideOnParts(List<Object> list, int parts) {
        return new ArrayList<>();
    }

    static Integer getFromFuture(Future<Integer> future) {
        try {
            return future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
