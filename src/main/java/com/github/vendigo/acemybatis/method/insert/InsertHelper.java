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
    static CompletableFuture<Integer> insertAsync(SqlSessionFactory sqlSessionFactory, String statementName,
                                                  List<Object> entities, int chunkSize, int threadCount) {
        return CompletableFuture.supplyAsync(() -> {
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            List<List<Object>> parts = divideOnParts(entities, threadCount);

            return IntStream.range(0, threadCount)
                    .mapToObj((n) -> executorService.submit(new InsertTask(sqlSessionFactory, statementName, parts.get(n),
                            chunkSize)))
                    .map(InsertHelper::getFromFuture)
                    .mapToInt(Integer::valueOf)
                    .sum();
        });
    }

    static List<List<Object>> divideOnParts(List<Object> list, int partsCount) {
        List<List<Object>> parts = new ArrayList<>();
        int partSize = list.size() / partsCount;
        int left = 0;
        int right = partSize;
        for (int i = 0; i < partsCount; i++) {
            parts.add(list.subList(left, right));
            left = right;
            right += partSize;
            if (i == partsCount - 2) {
                right = list.size();
            }
        }
        return parts;
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
