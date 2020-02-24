package com.legendwd.hyperpay.aelf.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;

import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import java.util.Locale;

public class LanguageUtil {
    public static final String LANG_CN = "zh-cn";
    public static final String LANG_EN = "en";

    /**
     * 设置语言
     */
    public static void setLanguage(Context context) {
        Locale targetLocale = readLocale();
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(targetLocale);
        } else {
            configuration.locale = targetLocale;
        }
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        Locale.setDefault(targetLocale);
    }

    private static Locale readLocale() {
        String lang = readLanguage();
        if (LANG_EN.equals(lang)) {
            return Locale.ENGLISH;
        }
        return Locale.SIMPLIFIED_CHINESE;
    }

    public static void saveLanguage(String language) {
        CacheUtil.getInstance().setProperty(Constant.Sp.SET_LANGUAGE, language);
        Constant.lang = language;
    }

    public static String readLanguage() {
        if (TextUtils.isEmpty(CacheUtil.getInstance().getProperty(Constant.Sp.SET_LANGUAGE))) {
            CacheUtil.getInstance().setProperty(Constant.Sp.SET_LANGUAGE, LANG_EN);
        }
        return CacheUtil.getInstance().getProperty(Constant.Sp.SET_LANGUAGE, LANG_EN);
    }

    //7.0以上获取方式需要特殊处理一下
    private static Locale getSysLocale(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return context.getResources().getConfiguration().locale;
        } else {
            return context.getResources().getConfiguration().getLocales().get(0);
        }
    }

}
