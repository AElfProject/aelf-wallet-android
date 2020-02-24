package com.legendwd.hyperpay.httputil;


import android.text.TextUtils;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;
import com.legendwd.hyperpay.lib.Logger;
import com.legendwd.hyperpay.router.IMainService;
import com.legendwd.hyperpay.router.Router;

import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ServiceGenerator {
    //    public static final String API_BASE_URL = "http://aelf.phpdl.com/app/";   //线上环境
    public static String API_BASE_URL = "https://hp-pre-wallet.aelf.io/app/";//预发布环境

    public static final String publicKey = "-----";

    private static OkHttpClient.Builder sHttpClient;

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        if (sHttpClient == null) {

            synchronized (ServiceGenerator.class) {
                if (sHttpClient == null) {
                    sHttpClient = new OkHttpClient.Builder();
                    BasicParamsInterceptor basicParamsInterceptor = new BasicParamsInterceptor.Builder()
                            .addParam("device", "Android") //公共参数
                            .addParam("version", "3.9.7")
//                            .addParam("chainid", getChainId())
                            .addParam("Content-Type", "application/x-www-form-urlencoded")
                            .build();
                    Router.getService(IMainService.class);
                    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                        @Override
                        public void log(String message) {
                            Logger.d(message);
                        }
                    });
                    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    sHttpClient.addInterceptor(loggingInterceptor);
                    sHttpClient.addInterceptor(basicParamsInterceptor);
                }
            }
        }

        List<Interceptor> interceptors = sHttpClient.interceptors();
        for (Interceptor interceptor : interceptors) {
            if (interceptor instanceof BasicParamsInterceptor) {
                BasicParamsInterceptor basicParamsInterceptor = (BasicParamsInterceptor) interceptor;

                IMainService mainService = Router.getService(IMainService.class);
                if (mainService != null) {
                    basicParamsInterceptor.queryParamsMap.put("lang", TextUtils.isEmpty(mainService.getLang()) ? "en" : mainService.getLang());
                    if (!TextUtils.isEmpty(mainService.getCurrency())) {
                        basicParamsInterceptor.paramsMap.put("currency", mainService.getCurrency());
                    }

                    if (!TextUtils.isEmpty(mainService.getAddress())) {
                        String address = Router.getService(IMainService.class).getAddress();
                        basicParamsInterceptor.paramsMap.put("address", address);
                    }
//                    basicParamsInterceptor.paramsMap.put("chainid", getChainId());
                    basicParamsInterceptor.paramsMap.put("udid", CacheUtil.getInstance().getProperty(Constant.Sp.UDID));
                }
            }
        }

        Retrofit retrofit = builder.client(sHttpClient.build()).build();
        return retrofit.create(serviceClass);
    }

    /**
     * 更换baseUrl请求数据
     *
     * @param serviceClass
     * @param url
     */
    public static <S> S createServiceByBase(Class<S> serviceClass, String url) {
        if (sHttpClient == null) {

            synchronized (ServiceGenerator.class) {
                if (sHttpClient == null) {
                    sHttpClient = new OkHttpClient.Builder();
                    BasicParamsInterceptor basicParamsInterceptor = new BasicParamsInterceptor.Builder()
                            .addParam("device", "Android") //公共参数
                            .addParam("version", "3.9.7")
//                            .addParam("chainid", getChainId())
                            .addParam("Content-Type", "application/x-www-form-urlencoded")
                            .build();
                    Router.getService(IMainService.class);
                    sHttpClient.addInterceptor(basicParamsInterceptor);
                    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                        @Override
                        public void log(String message) {
                            Logger.d(message);
                        }
                    });
                    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    sHttpClient.addInterceptor(loggingInterceptor);
                }
            }
        }

        List<Interceptor> interceptors = sHttpClient.interceptors();
        for (Interceptor interceptor : interceptors) {
            if (interceptor instanceof BasicParamsInterceptor) {
                BasicParamsInterceptor basicParamsInterceptor = (BasicParamsInterceptor) interceptor;

                IMainService mainService = Router.getService(IMainService.class);
                if (mainService != null) {
                    basicParamsInterceptor.queryParamsMap.put("lang", TextUtils.isEmpty(mainService.getLang()) ? "en" : mainService.getLang());
                    if (!TextUtils.isEmpty(mainService.getCurrency())) {
                        basicParamsInterceptor.paramsMap.put("currency", mainService.getCurrency());
                    }

                    if (!TextUtils.isEmpty(mainService.getAddress())) {
                        basicParamsInterceptor.paramsMap.put("address", Router.getService(IMainService.class).getAddress());
                    }
//                    basicParamsInterceptor.paramsMap.put("chainid", getChainId());
                    String uuid = CacheUtil.getInstance().getProperty(Constant.Sp.UDID);
                    basicParamsInterceptor.paramsMap.put("udid", uuid);
                }
            }
        }
        Retrofit.Builder builder2 =
                new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        Retrofit retrofit = builder2.client(sHttpClient.build()).build();
        return retrofit.create(serviceClass);
    }

    /**
     * 获取chainid
     *
     * @return
     */
    public static String getChainId() {
        String chainId = CacheUtil.getInstance().getProperty(Constant.Sp.CURRENT_CHAIN_ID);
        return TextUtils.isEmpty(chainId) ? Constant.DEFAULT_CHAIN_ID : chainId;
    }
}
