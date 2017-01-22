package com.github.vendigo.acemybatis.utils;

import com.github.vendigo.acemybatis.proxy.RuntimeExecutionException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.ExecutionException;

public class ExceptionUtils {
    private ExceptionUtils() {
    }

    public static Throwable unwrapThrowable(Throwable wrapped) {
        Throwable unwrapped = wrapped;
        while (true) {
            if (unwrapped instanceof InvocationTargetException) {
                unwrapped = ((InvocationTargetException) unwrapped).getTargetException();
            } else if (unwrapped instanceof UndeclaredThrowableException) {
                unwrapped = ((UndeclaredThrowableException) unwrapped).getUndeclaredThrowable();
            } else if (unwrapped instanceof RuntimeExecutionException || unwrapped instanceof ExecutionException) {
                unwrapped = unwrapped.getCause();
            } else {
                return unwrapped;
            }
        }
    }
}
