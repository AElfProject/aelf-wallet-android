package com.legendwd.hyperpay.lib;

import com.securepreferences.SecurePreferences;
import com.legendwd.hyperpay.lib.Constant;

public class CacheUtil {

    private SecurePreferences mSecurePrefs;

    private CacheUtil() {
        mSecurePrefs = new SecurePreferences(AppConfig.appContext, "", "my_prefs.xml");
        SecurePreferences.setLoggingEnabled(true);
    }

    public static CacheUtil getInstance() {
        return CacheUtil.SingleHolder.INSTANCE;
    }

    public void setProperty(String key, String value) {
        if (null == value)
            value = "";

        mSecurePrefs.edit().putString(key, value).commit();
    }

    public void setProperty(String key, int value) {
        mSecurePrefs.edit().putInt(key, value).commit();
    }

    public void setProperty(String key, boolean value) {
        mSecurePrefs.edit().putBoolean(key, value).commit();
    }

    public boolean getProperty(String key, boolean value) {
        return mSecurePrefs.getBoolean(key, value);
    }

    public String getProperty(String key, String value) {
        return mSecurePrefs.getString(key, value);
    }

    public int getProperty(String key, int value) {
        try {
            return mSecurePrefs.getInt(key, value);
        }catch (Exception e) {
            return value;
        }
    }

    public String getProperty(String key) {
        return getProperty(key, "");
    }

    /**
     * 清空所有数据
     */
    public void clearAllData() {
        String deviceToken = getProperty(Constant.Sp.DEVICE_TOKEN,"");
        mSecurePrefs.edit().clear().commit();
        // fix exit bug
        CacheUtil.SingleHolder.INSTANCE = new CacheUtil();
        setProperty(Constant.Sp.DEVICE_TOKEN, deviceToken);
    }

    private static class SingleHolder {
        private static CacheUtil INSTANCE = new CacheUtil();
    }

}
