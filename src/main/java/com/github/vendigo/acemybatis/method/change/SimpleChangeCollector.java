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

/**
 * Simple implementation for ChangeCollector. Not concurrent.
 * Consider using async operations instead of change collector for big amount of data.
 *
 * @param <T> - type of stream entry.
 */
class SimpleChangeCollector<T> implements ChangeCollector<T> {

    private final AceConfig config;
    private final SqlSessionFactory sqlSessionFactory;
    private final String statementName;
    private final ChangeFunction changeFunction;

    SimpleChangeCollector(AceConfig config, SqlSessionFactory sqlSessionFactory, String statementName,
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
    public BiConsumer<CollectorContainer, T> accumulator() {
        return CollectorContainer::accumulate;
    }

    @Override
    public BinaryOperator<CollectorContainer> combiner() {
        return CollectorContainer::combine;
    }

    @Override
    public Function<CollectorContainer, List<T>> finisher() {
        return CollectorContainer::finish;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.emptySet();
    }
}
