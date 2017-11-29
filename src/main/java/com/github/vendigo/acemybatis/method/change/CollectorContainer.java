package com.github.vendigo.acemybatis.method.change;

import com.github.vendigo.acemybatis.config.AceConfig;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Inner mutable container for {@link SimpleChangeCollector}.
 */
class CollectorContainer {
    private final List<Object> all;
    private final List<Object> onInsert;
    private final AceConfig config;
    private final String statementName;
    private final ChangeFunction changeFunction;
    private final SqlSession sqlSession;

    CollectorContainer(AceConfig config, SqlSession sqlSession, String statementName,
                       ChangeFunction changeFunction) {
        this.config = config;
        this.statementName = statementName;
        this.changeFunction = changeFunction;
        this.all = new ArrayList<>();
        this.onInsert = new ArrayList<>();
        this.sqlSession = sqlSession;
    }

    void accumulate(Object o) {
        all.add(o);
        onInsert.add(o);

        if (onInsert.size() >= config.getUpdateChunkSize()) {
            ChangeHelper.changeChunk(sqlSession, onInsert, statementName, changeFunction);
            onInsert.clear();
        }
    }

    List finish() {
        if (!onInsert.isEmpty()) {
            ChangeHelper.changeChunk(sqlSession, onInsert, statementName, changeFunction);
            onInsert.clear();
        }
        sqlSession.close();
        return all;
    }

    CollectorContainer combine(CollectorContainer other) {
        this.all.addAll(other.all);
        this.onInsert.addAll(other.onInsert);
        return this;
    }
}
