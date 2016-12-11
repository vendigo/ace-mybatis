package com.github.vendigo.acemybatis.util;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

public class AssertHelper {
    public static <T> void assertCollections(Collection<T> actual, Collection<T> expected) {
        assertThat(actual, hasSize(expected.size()));
        assertThat(actual, containsInAnyOrder(expected.toArray()));
    }
}
