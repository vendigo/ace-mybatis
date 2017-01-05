package com.github.vendigo.acemybatis.method.change;

import com.github.vendigo.acemybatis.parser.ParamsHolder;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ChangeHelperTest {
    Map<String, Object> otherParams;

    @Before
    public void setUp() throws Exception {
        otherParams = new HashMap<>();
        otherParams.put("param1", "value1");
        otherParams.put("param2", "value2");
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
