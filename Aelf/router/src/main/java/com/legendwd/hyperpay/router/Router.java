package com.legendwd.hyperpay.router;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by joseph on 2018/5/26.
 */
public class Router {

    private static Map<Class, Object> sServices = new HashMap<>();

    public static <T> void register(Class<T> clazz, T impl) {
        sServices.put(clazz, impl);
    }

    public static <T> T getService(Class<T> clazz) {
        return (T) sServices.get(clazz);
    }

}

