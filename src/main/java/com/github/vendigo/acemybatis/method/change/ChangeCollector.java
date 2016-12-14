package com.github.vendigo.acemybatis.method.change;

import com.github.vendigo.acemybatis.config.AceConfig;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class ChangeCollector implements Collector<Object, CollectorContainer, List<Object>> {

    private final AceConfig config;
    private final SqlSessionFactory sqlSessionFactory;
    private final String statementName;
    private final ChangeFunction changeFunction;

    public ChangeCollector(AceConfig config, SqlSessionFactory sqlSessionFactory, String statementName,
                           ChangeFunction changeFunction) {
        this.config = config;
        this.sqlSessionFactory = sqlSessionFactory;
        this.statementName = statementName;
        this.changeFunction = changeFunction;
    }

    @Override
    public Supplier<CollectorContainer> supplier() {
        return () -> new CollectorContainer(config, sqlSessionFactory, statementName, changeFunction);
    }

    @Override
    public BiConsumer<CollectorContainer, Object> accumulator() {
        return CollectorContainer::accumulate;
    }

    @Override
    public BinaryOperator<CollectorContainer> combiner() {
        return CollectorContainer::combine;
    }

    @Override
    public Function<CollectorContainer, List<Object>> finisher() {
        return CollectorContainer::getAll;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.emptySet();
    }
}
