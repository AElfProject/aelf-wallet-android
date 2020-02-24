package com.legendwd.hyperpay.httputil;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.legendwd.hyperpay.lib.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class BasicParamsInterceptor implements Interceptor {

    Map<String, String> queryParamsMap = new HashMap<>();
    Map<String, String> paramsMap = new HashMap<>();
    Map<String, String> headerParamsMap = new HashMap<>();
    List<String> headerLinesList = new ArrayList<>();

    private static JsonObject iterator(JsonObject jsonObject) {
        Logger.d(new Gson().toJson(jsonObject));
        JsonObject jo = new JsonObject();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            JsonElement value = entry.getValue();
            jo.addProperty(entry.getKey(), EncrptUtil.enRSA(value.getAsString()));
        }
        return jo;
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();

        Headers.Builder headerBuilder = request.headers().newBuilder();
        if (headerParamsMap.size() > 0) {
            Iterator iterator = headerParamsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                headerBuilder.add((String) entry.getKey(), (String) entry.getValue());
            }
        }
        if (headerLinesList.size() > 0) {
            for (String line : headerLinesList) {
                headerBuilder.add(line);
            }
        }
        requestBuilder.headers(headerBuilder.build());


        if (queryParamsMap.size() > 0) {
            injectParamsIntoUrl(request, requestBuilder, queryParamsMap);
        }


        if (paramsMap.size() > 0) {
            // process post body inject
            if (request.method().equals("POST")) {
                JsonObject jsonObject;
                if (!(request.body() instanceof MultipartBody)) {
                    String postBodyString = bodyToString(request.body());
                    if (!TextUtils.isEmpty(postBodyString)) {
                        jsonObject = new JsonParser().parse(postBodyString).getAsJsonObject();
                    } else {
                        jsonObject = new JsonObject();
                    }
                    Iterator iterator = paramsMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        jsonObject.addProperty((String) entry.getKey(), (String) entry.getValue());
                    }

                    jsonObject = iterator(jsonObject);

                    requestBuilder.post(RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), jsonObject.toString()));
                }
            } else {
                injectParamsIntoUrl(request, requestBuilder, paramsMap);
            }
        }

        request = requestBuilder.build();
        return chain.proceed(request);
    }

    private void injectParamsIntoUrl(Request request, Request.Builder requestBuilder, Map<String, String> paramsMap) {
        HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();
        if (paramsMap.size() > 0) {
            Iterator iterator = paramsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                httpUrlBuilder.addQueryParameter((String) entry.getKey(), (String) entry.getValue());
            }
        }

        requestBuilder.url(httpUrlBuilder.build());
    }

    public static class Builder {

        BasicParamsInterceptor interceptor;

        public Builder() {
            interceptor = new BasicParamsInterceptor();
        }

        public Builder addParam(String key, String value) {
            interceptor.paramsMap.put(key, value);
            return this;
        }

        public Builder addParamsMap(Map<String, String> paramsMap) {
            interceptor.paramsMap.putAll(paramsMap);
            return this;
        }

        public Builder addHeaderParam(String key, String value) {
            interceptor.headerParamsMap.put(key, value);
            return this;
        }

        public Builder addHeaderParamsMap(Map<String, String> headerParamsMap) {
            interceptor.headerParamsMap.putAll(headerParamsMap);
            return this;
        }

        public Builder addHeaderLine(String headerLine) {
            int index = headerLine.indexOf(":");
            if (index == -1) {
                throw new IllegalArgumentException("Unexpected header: " + headerLine);
            }
            interceptor.headerLinesList.add(headerLine);
            return this;
        }

        public Builder addHeaderLinesList(List<String> headerLinesList) {
            for (String headerLine : headerLinesList) {
                int index = headerLine.indexOf(":");
                if (index == -1) {
                    throw new IllegalArgumentException("Unexpected header: " + headerLine);
                }
                interceptor.headerLinesList.add(headerLine);
            }
            return this;
        }

        public Builder addQueryParam(String key, String value) {
            interceptor.queryParamsMap.put(key, value);
            return this;
        }

        public Builder addQueryParamsMap(Map<String, String> queryParamsMap) {
            interceptor.queryParamsMap.putAll(queryParamsMap);
            return this;
        }

        public BasicParamsInterceptor build() {
            return interceptor;
        }

    }

}
