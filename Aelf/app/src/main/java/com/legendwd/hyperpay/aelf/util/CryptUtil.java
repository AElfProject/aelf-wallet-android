package com.legendwd.hyperpay.aelf.util;

import android.text.TextUtils;
import android.util.Log;

import com.legendwd.hyperpay.lib.CacheUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author lovelyzxing
 * @date 2019/6/17
 * @Description
 */
public class CryptUtil {

    public static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return byteToHex(md.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String byteToHex(byte[] data) {
        final StringBuilder builder = new StringBuilder();
        for (byte b : data) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    /**
     * 加密
     *
     * @param string
     * @return
     */
    public static String sha256(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("sha-256");
            byte[] bytes = md5.digest((string).getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 存储加密信息
     *
     * @param gameUrl
     * @param privateKey
     */
    public static void saveWhite(String gameUrl, String privateKey) {
        CacheUtil.getInstance().setProperty(gameUrl, String.valueOf(DateUtils.getSystemMillis()));//存时间戳
        String SystemMill = CacheUtil.getInstance().getProperty(gameUrl);//获取时间戳
        String whiteKey = SystemMill + "_" + gameUrl;//拼接whitekey；
        String sha = CryptUtil.sha256(whiteKey);
        CacheUtil.getInstance().setProperty(sha, AESUtil.getInstance().encrypt(privateKey));
    }

    /**
     * 获取白名单
     *
     * @param gameUrl
     * @return
     */
    public static String getWhite(String gameUrl) {
        String SystemMill = CacheUtil.getInstance().getProperty(gameUrl);//获取时间戳
        String whiteKey = SystemMill + "_" + gameUrl;//拼接whitekey；
        String sha = CryptUtil.sha256(whiteKey);
        String privateKey = CacheUtil.getInstance().getProperty(sha);
        return AESUtil.getInstance().decrypt(privateKey);
    }

}
