package com.legendwd.hyperpay.lib;

import android.content.Context;

/**
 * @author lovelyzxing
 * @date 2018/8/31
 */
public class AppConfig {
    public static Context appContext;
    public static boolean DEBUG = true;

    public static void init(Context context) {
        appContext = context;
    }


}
