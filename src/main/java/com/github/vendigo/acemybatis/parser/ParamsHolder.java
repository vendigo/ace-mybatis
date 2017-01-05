package com.github.vendigo.acemybatis.parser;

import java.util.Collection;
import java.util.Map;

public class ParamsHolder {
    private final Collection<Object> entities;
    private final Map<String, Object> otherParams;

    public ParamsHolder(Collection<Object> entities, Map<String, Object> otherParams) {
        this.entities = entities;
        this.otherParams = otherParams;
    }

    public Collection<Object> getEntities() {
        return entities;
    }

    public Map<String, Object> getOtherParams() {
        return otherParams;
    }
}
