package com.github.vendigo.acemybatis.method.change;

import com.github.vendigo.acemybatis.method.change.ChangeHelper;
import org.junit.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ChangeHelperTest {
    @Test
    public void divideSixOnThreeParts() throws Exception {
        assertThat(ChangeHelper.divideOnParts(asList(1, 2, 3, 4, 5, 6), 3),
                equalTo(asList(asList(1, 2), asList(3, 4), asList(5, 6))));
    }

    @Test
    public void divideSevenOnThreeParts() throws Exception {
        assertThat(ChangeHelper.divideOnParts(asList(1, 2, 3, 4, 5, 6, 7), 3),
                equalTo(asList(asList(1, 2), asList(3, 4), asList(5, 6, 7))));
    }

    @Test
    public void divideOnOnePart() throws Exception {
        assertThat(ChangeHelper.divideOnParts(asList(1, 2, 3, 4, 5, 6, 7), 1),
                equalTo(singletonList(asList(1, 2, 3, 4, 5, 6, 7))));
    }

    @Test
    public void divideOnTwoParts() throws Exception {
        assertThat(ChangeHelper.divideOnParts(asList(1, 2, 3, 4, 5, 6, 7), 2),
                equalTo(asList(asList(1, 2, 3), asList(4, 5, 6, 7))));
    }
}
