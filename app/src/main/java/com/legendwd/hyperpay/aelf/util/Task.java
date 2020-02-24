package com.legendwd.hyperpay.aelf.util;

/**
 * @author lovelyzxing
 * @date 2019/5/21
 * @Description
 */
public abstract class Task<T> {
    private T t;

    public abstract void doOnUIThread(T t);

    public abstract T doOnIOThread();
}
