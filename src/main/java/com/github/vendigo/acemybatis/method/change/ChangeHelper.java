package com.github.vendigo.acemybatis.method.change;

import com.github.vendigo.acemybatis.config.AceConfig;
import com.github.vendigo.acemybatis.parser.ParamsHolder;
import com.github.vendigo.acemybatis.proxy.RuntimeExecutionException;
import org.apache.ibatis.session.ExecutorType;
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
    static final boolean AUTO_COMMIT_FALSE = false;

    static CompletableFuture<Integer> applyAsync(AceConfig config, ChangeFunction changeFunction,
                                                 SqlSessionFactory sqlSessionFactory, String statementName,
                                                 ParamsHolder params) {
        return CompletableFuture.supplyAsync(() -> {
            ExecutorService executorService = Executors.newFixedThreadPool(config.getThreadCount());
            List<ParamsHolder> parts = divideOnParts(params, config.getThreadCount());
            int result;

            try {
                result = IntStream.range(0, config.getThreadCount())
                        .mapToObj((n) -> executorService.submit(new ChangeTask(config, changeFunction, sqlSessionFactory,
                                statementName, parts.get(n))))
                        .map(ChangeHelper::getFromFuture)
                        .mapToInt(Integer::valueOf)
                        .sum();
            } finally {
                executorService.shutdown();
            }
            return result;
        });
    }

    static void changeChunk(SqlSessionFactory sqlSessionFactory, List<Object> chunk, String statementName,
                            ChangeFunction changeFunction) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, AUTO_COMMIT_FALSE)) {
            for (Object entity : chunk) {
                changeFunction.apply(sqlSession, statementName, entity);
            }
            sqlSession.commit();
        }
    }

    static List<ParamsHolder> divideOnParts(ParamsHolder params, int partsCount) {
        List<Object> entities = params.getEntities();

        List<ParamsHolder> parts = new ArrayList<>();
        int partSize = entities.size() / partsCount;
        int left = 0;
        int right = partSize;
        for (int i = 0; i < partsCount; i++) {
            List<Object> partOfEntities = entities.subList(left, right);
            parts.add(new ParamsHolder(partOfEntities, params.getOtherParams()));
            left = right;
            right += partSize;
            if (i == partsCount - 2) {
                right = entities.size();
            }
        }
        return parts;
    }

    private static Integer getFromFuture(Future<Integer> future) {
        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeExecutionException(e);
        }
    }
}
