package com.legendwd.hyperpay.lib;

import android.util.Log;

/**
 * @author lovelyzxing
 * @date 2018/8/31
 */
public class Logger {
    private static String TAG = "zxing";

    public static void i(String msg) {
        if (BuildConfig.DEBUG)
            Log.i(TAG, collectMessage() + msg);
    }

    public static void e(String msg) {
        if (BuildConfig.DEBUG)
            Log.e(TAG, collectMessage() + msg);
    }


    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, collectMessage() + msg);
        }
    }

    public static void d(String msg) {
        d(TAG, msg);
    }


    private static String collectMessage() {
        String tag = "%s.%s(Line:%d)";
        StackTraceElement traceElement = Thread.currentThread().getStackTrace()[4];

        tag = String.format(tag, traceElement.getClassName(), traceElement.getMethodName(), traceElement.getLineNumber());

        return tag;
    }
}
