package com.github.vendigo.acemybatis;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ProxyTest extends AbstractTest {

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Test
    public void proxyHasObjectMethods() throws Exception {
        assertThat(userMapper.equals("string"), is(false));
        assertThat(userMapper.hashCode(), notNullValue());
        assertThat(userMapper.toString(), notNullValue());
    }
}
