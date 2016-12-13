package com.github.vendigo.acemybatis.utils;

public class Validator {
    private Validator() {}

    public static int isPositive(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Number must be positive");
        }
        return n;
    }

    public static <V> V notNull(V o) {
        if (o == null) {
            throw new IllegalArgumentException("Argument must be not null");
        }
        return o;
    }
}
