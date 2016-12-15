package com.github.vendigo.acemybatis.method.change;

import java.util.List;
import java.util.stream.Collector;

/**
 * Collector for inserting/updating/deleting stream entries on the fly in chunks. Returns collected values.
 * @param <T> - type of stream entry.
 */
public interface ChangeCollector<T> extends Collector<T, CollectorContainer, List<T>> {
}
