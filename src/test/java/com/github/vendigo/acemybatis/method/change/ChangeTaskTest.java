package com.github.vendigo.acemybatis.method.change;

import com.github.vendigo.acemybatis.config.AceConfig;
import com.github.vendigo.acemybatis.parser.ParamsHolder;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.github.vendigo.acemybatis.method.change.TestUtils.createParamsHolder;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ChangeTaskTest {
    @Mock
    private SqlSessionFactory sqlSessionFactory;
    @Mock
    private SqlSession sqlSession;

    @Test
    public void do3CommitsWhen7By3() throws Exception {
        testCall(3, 7 ,3);
    }

    @Test
    public void do10CommitsWhen20By2() throws Exception {
        testCall(10, 20 ,2);
    }

    @Test
    public void do1CommitWhen20By100() throws Exception {
        testCall(1, 20 ,100);
    }

    private void testCall(int expectedNumberOfCommits, int numberOfElements, int chunkSize) throws Exception {
        AceConfig aceConfig = new AceConfig(chunkSize, 2);

        ParamsHolder paramsHolder = createParamsHolder(numberOfElements);
        ChangeTask changeTask = new ChangeTask(aceConfig, (sqlSession, statementName, entity) -> {}, sqlSessionFactory,
                "insert", paramsHolder);
        when(sqlSessionFactory.openSession(ExecutorType.BATCH, false)).thenReturn(sqlSession);
        changeTask.call();
        verify(sqlSession, times(expectedNumberOfCommits)).commit();
    }
}
