package com.github.vendigo.acemybatis.method.change;

import com.github.vendigo.acemybatis.parser.ParamsHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestUtils {
    static ParamsHolder createParamsHolder(int numberOfElements) {
        List<Object> list = new ArrayList<>(numberOfElements);
        for (int i = 0; i < numberOfElements; i++) {
            list.add(i);
        }
        return new ParamsHolder(list, Collections.emptyMap());
    }
}
