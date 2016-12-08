package com.github.vendigo.acemybatis.method.select;

import com.aol.cyclops.data.async.Queue;
import com.aol.cyclops.data.async.QueueFactories;
import com.github.vendigo.acemybatis.method.AceMethod;
import com.github.vendigo.acemybatis.method.MethodUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.github.vendigo.acemybatis.method.MethodUtils.getStatementName;

public class StreamSelectMethodImpl implements AceMethod {

    private final Method method;
    private final MapperMethod.MethodSignature methodSignature;
    private final int chunkSize = 1000;

    public StreamSelectMethodImpl(Method method, MapperMethod.MethodSignature methodSignature) {
        this.method = method;
        this.methodSignature = methodSignature;
    }

    @Override
    public Stream<Object> execute(SqlSessionFactory sqlSessionFactory, Object[] args) {
        String statementName = getStatementName(method);
        Object parameter = methodSignature.convertArgsToSqlCommandParam(args);
        int count = getCount(sqlSessionFactory, parameter);
        System.out.println("Count: "+ count);

        Queue<Object> queue = QueueFactories.boundedQueue(count).build();
        int nThreads = computeThreadPullSize(count);
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        AtomicInteger offset = new AtomicInteger(0);
        IntStream.range(0, nThreads).forEach((n) -> executorService.execute(new SelectTask(statementName, parameter,
                sqlSessionFactory, queue, offset, count,
                chunkSize)));
        return queue.jdkStream().limit(count);
    }

    private int getCount(SqlSessionFactory sqlSessionFactory, Object parameter) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return sqlSession.selectOne(MethodUtils.getCountStatementName(method),
                    parameter);
        }
    }

    private int computeThreadPullSize(int count) {
        int maxCores = Runtime.getRuntime().availableProcessors();
        int chunksCount = count / chunkSize;
        return Math.min(Math.max(chunksCount, 1), maxCores);
    }
}
