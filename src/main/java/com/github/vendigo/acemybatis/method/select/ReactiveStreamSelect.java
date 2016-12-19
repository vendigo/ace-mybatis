package com.github.vendigo.acemybatis.method.select;

import com.aol.cyclops.data.async.Queue;
import com.aol.cyclops.data.async.QueueFactories;
import com.github.vendigo.acemybatis.config.AceConfig;
import com.github.vendigo.acemybatis.method.AceMethod;
import com.github.vendigo.acemybatis.method.CommonUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.github.vendigo.acemybatis.method.CommonUtils.getStatementName;
import static java.util.stream.Collectors.toList;

/**
 * Batch async select method implementation. Require explicitly defined countQuery.
 */
public class ReactiveStreamSelect implements AceMethod {

    private final Method method;
    private final MapperMethod.MethodSignature methodSignature;
    private final AceConfig config;

    public ReactiveStreamSelect(Method method, MapperMethod.MethodSignature methodSignature, AceConfig config) {
        this.method = method;
        this.methodSignature = methodSignature;
        this.config = config;
    }

    @Override
    public Stream<Object> execute(SqlSessionFactory sqlSessionFactory, Object[] args) throws Exception {
        String statementName = getStatementName(method);
        Object parameter = methodSignature.convertArgsToSqlCommandParam(args);
        int count = getCount(sqlSessionFactory, parameter);

        Queue<Object> queue = QueueFactories.boundedQueue(count).build();
        int nThreads = CommonUtils.computeThreadPullSize(config.getThreadCount(), count, config.getSelectChunkSize());
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        AtomicInteger offset = new AtomicInteger(0);
        List<Future<?>> futures = IntStream.range(0, nThreads)
                .mapToObj((n) -> executorService.submit(new SelectTask(statementName, parameter,
                        sqlSessionFactory, queue, offset, count, config.getSelectChunkSize())))
                .collect(toList());
        closeQueue(queue, futures);
        return queue.jdkStream().limit(count);
    }

    private void closeQueue(Queue<Object> queue, List<Future<?>> futures) {
        CompletableFuture.runAsync(() -> {
            futures.forEach(this::waitForFuture);
            queue.close();
        });
    }

    private void waitForFuture(Future<?> future) {
        try {
            future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int getCount(SqlSessionFactory sqlSessionFactory, Object parameter) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return sqlSession.selectOne(CommonUtils.getCountStatementName(method),
                    parameter);
        }
    }

}
