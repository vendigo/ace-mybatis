package com.github.vendigo.acemybatis.parser;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ParamsHolder {
    private final List<Object> entities;
    private final Map<String, Object> otherParams;

    public ParamsHolder(List<Object> entities, Map<String, Object> otherParams) {
        this.entities = entities;
        this.otherParams = otherParams;
    }

    public List<Object> getEntities() {
        return entities;
    }

    public Map<String, Object> getOtherParams() {
        return otherParams;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParamsHolder that = (ParamsHolder) o;
        return Objects.equals(entities, that.entities) &&
                Objects.equals(otherParams, that.otherParams);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entities, otherParams);
    }

    @Override
    public String toString() {
        return "ParamsHolder{" + "entities=" + entities +
                ", otherParams=" + otherParams +
                '}';
    }
}
