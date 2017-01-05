package com.github.vendigo.acemybatis.method.change;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

class ChangeHelper {
    static CompletableFuture<Integer> applyAsync(ChangeFunction changeFunction, SqlSessionFactory sqlSessionFactory, String statementName,
                                                        List<Object> entities, int chunkSize, int threadCount) {
        return CompletableFuture.supplyAsync(() -> {
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            List<List<Object>> parts = divideOnParts(entities, threadCount);

            int result = IntStream.range(0, threadCount)
                    .mapToObj((n) -> executorService.submit(new ChangeTask(changeFunction, sqlSessionFactory, statementName, parts.get(n),
                            chunkSize)))
                    .map(ChangeHelper::getFromFuture)
                    .mapToInt(Integer::valueOf)
                    .sum();
            executorService.shutdown();
            return result;
        });
    }

    static void changeChunk(SqlSessionFactory sqlSessionFactory, List<Object> chunk, String statementName,
                                   ChangeFunction changeFunction) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            for (Object entity : chunk) {
                changeFunction.apply(sqlSession, statementName, entity);
            }
            sqlSession.commit();
        }
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
