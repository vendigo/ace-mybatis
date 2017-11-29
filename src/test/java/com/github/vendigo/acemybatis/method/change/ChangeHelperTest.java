package com.github.vendigo.acemybatis.method.change;

import com.github.vendigo.acemybatis.config.AceConfig;
import com.github.vendigo.acemybatis.parser.ParamsHolder;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.github.vendigo.acemybatis.method.change.TestUtils.createParamsHolder;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ChangeHelperTest {
    @Mock
    private SqlSessionFactory sqlSessionFactory;
    @Mock
    private SqlSession sqlSession;

    private Map<String, Object> otherParams;

    @Before
    public void setUp() throws Exception {
        otherParams = new HashMap<>();
        otherParams.put("param1", "value1");
        otherParams.put("param2", "value2");
    }

    @Test
    public void do4CommitsWhen10By3In2Threads() throws Exception {
        testApply(4, 10, 3, 2);
    }

    @Test
    public void do3CommitWhen50By100In3Threads() throws Exception {
        //Fix to 1
        testApply(3, 50, 100, 3);
    }

    @Test
    public void do12CommitsWhen1000By100In4Threads() throws Exception {
        //Fix to 10
        testApply(12, 1000, 100, 4);
    }

    private void testApply(int expectedCommits, int numberOfElements, int chunkSize, int threadCount) throws Exception {
        AceConfig aceConfig = new AceConfig(chunkSize, threadCount);
        ParamsHolder paramsHolder = createParamsHolder(numberOfElements);
        when(sqlSessionFactory.openSession(ExecutorType.BATCH, false)).thenReturn(sqlSession);

        ChangeHelper.applyInParallel(aceConfig, (sqlSession, statementName, entity) -> {}, sqlSessionFactory,
                "insert", paramsHolder);
        verify(sqlSession, times(expectedCommits)).commit();
    }

    @Test
    public void divideSixOnThreeParts() throws Exception {
        assertThat(ChangeHelper.divideOnParts(wrap(1, 2, 3, 4, 5, 6), 3),
                equalTo(asList(wrap(1, 2), wrap(3, 4), wrap(5, 6))));
    }

    @Test
    public void divideSevenOnThreeParts() throws Exception {
        assertThat(ChangeHelper.divideOnParts(wrap(1, 2, 3, 4, 5, 6, 7), 3),
                equalTo(asList(wrap(1, 2), wrap(3, 4), wrap(5, 6, 7))));
    }

    @Test
    public void divideOnOnePart() throws Exception {
        assertThat(ChangeHelper.divideOnParts(wrap(1, 2, 3, 4, 5, 6, 7), 1),
                equalTo(singletonList(wrap(1, 2, 3, 4, 5, 6, 7))));
    }

    @Test
    public void divideOnTwoParts() throws Exception {
        assertThat(ChangeHelper.divideOnParts(wrap(1, 2, 3, 4, 5, 6, 7), 2),
                equalTo(asList(wrap(1, 2, 3), wrap(4, 5, 6, 7))));
    }

    private ParamsHolder wrap(Object... args) {
        return new ParamsHolder(Arrays.asList(args), otherParams);
    }
}
