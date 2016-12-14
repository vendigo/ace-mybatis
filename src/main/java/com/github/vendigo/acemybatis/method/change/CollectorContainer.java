package com.github.vendigo.acemybatis.method.change;

import com.github.vendigo.acemybatis.config.AceConfig;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.ArrayList;
import java.util.List;

public class CollectorContainer {
    private List<Object> all;
    private List<Object> onInsert;
    private final AceConfig config;
    private final SqlSessionFactory sqlSessionFactory;
    private final String statementName;
    private final ChangeFunction changeFunction;

    public CollectorContainer(AceConfig config, SqlSessionFactory sqlSessionFactory, String statementName,
                              ChangeFunction changeFunction) {
        this.config = config;
        this.sqlSessionFactory = sqlSessionFactory;
        this.statementName = statementName;
        this.changeFunction = changeFunction;
        all = new ArrayList<>();
        onInsert = new ArrayList<>();
    }

    public void accumulate(Object o) {
        all.add(o);
        onInsert.add(o);

        if (onInsert.size() >= config.getUpdateChunkSize()) {
            ChangeHelper.changeChunk(sqlSessionFactory, onInsert, statementName, changeFunction);
            onInsert.clear();
        }
    }

    public List finish() {
        if (!onInsert.isEmpty()) {
            ChangeHelper.changeChunk(sqlSessionFactory, onInsert, statementName, changeFunction);
            onInsert.clear();
        }
        return all;
    }

    public CollectorContainer combine(CollectorContainer other) {
        this.all.addAll(other.all);
        this.onInsert.addAll(other.onInsert);
        return this;
    }
}
