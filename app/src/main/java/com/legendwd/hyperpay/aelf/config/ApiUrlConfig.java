package com.legendwd.hyperpay.aelf.config;

/**
 * @author Colin
 * @date 2019/11/19.
 * description： 存放特定的url地址
 */
public class ApiUrlConfig {
    public static String AssetsUrl = "file:///android_asset/minaelfandroid.html";  // 本地js web 资源
    public static String UserAgreeUrl_ZH = "file:////android_asset/h5/UserProtocol_zh.html";//同意协议链接中文
    public static String UserAgreeUrl_EN = "file:////android_asset/h5/UserProtocol_en.html";//同意协议链接英文

    public static String DappTransUrl = "http://54.249.197.246:9876/transfer.html";//转账Demo的地址
    public static String DappResourceUrl = "http://54.249.197.246:9876/transfer.html";//资源交易Demo的地址
    public static String DappVoteUrl = "http://54.249.197.246:9876/transfer.html";//投票Demo的地址


    /**
     * 第三方行情api
     */
    public static final String MARKET_UTL = "https://api.coingecko.com/api/v3/";
}
