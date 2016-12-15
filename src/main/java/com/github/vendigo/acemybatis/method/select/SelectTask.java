package com.github.vendigo.acemybatis.method.select;

import com.aol.cyclops.data.async.Queue;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Task for selecting bunch of entities in chunks into the queue.
 */
public class SelectTask implements Runnable {

    private String statementName;
    private Object param;
    private SqlSessionFactory sqlSessionFactory;
    private Queue<Object> queue;
    private AtomicInteger offset;
    private int count;
    private int chunkSize;

    public SelectTask(String statementName, Object param, SqlSessionFactory sqlSessionFactory, Queue<Object> queue,
                      AtomicInteger offset, int count, int chunkSize) {
        this.statementName = statementName;
        this.param = param;
        this.sqlSessionFactory = sqlSessionFactory;
        this.queue = queue;
        this.offset = offset;
        this.count = count;
        this.chunkSize = chunkSize;
    }

    @Override
    public void run() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            int currentOffset = 0;
            int newOffset;

            while (currentOffset < count) {
                do {
                    currentOffset = offset.get();
                    newOffset = currentOffset + chunkSize;
                } while (!offset.compareAndSet(currentOffset, newOffset));

                Cursor<Object> cursor = sqlSession.selectCursor(statementName, param,
                        new RowBounds(currentOffset, chunkSize));
                for (Object obj : cursor) {
                    queue.add(obj);
                }
            }
        }
    }
}
