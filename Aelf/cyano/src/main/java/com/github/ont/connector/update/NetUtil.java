package com.github.ont.connector.update;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetUtil {
    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static CyanoNetResponse cyanoNetResponse;

    //GET
    public static void get(String url) {
        final Request request = new Request.Builder().url(url).get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Log.d(TAG, "onFailure: ");
                if (cyanoNetResponse != null) {
                    cyanoNetResponse.handleFailResponse(e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.d(TAG, "onResponse: " + response.body().string());
                if (cyanoNetResponse != null) {
                    cyanoNetResponse.handleSuccessResponse(response.body().string());
                }
            }
        });
    }

    //POST
    public static void post(String url, String json) {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        Request request = new Request.Builder().url(url).post(RequestBody.create(mediaType, json)).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Log.d(TAG, "onFailure: " + e.getMessage());
                if (cyanoNetResponse != null) {
                    cyanoNetResponse.handleFailResponse(e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.d(TAG, response.protocol() + " " +response.code() + " " + response.message());
//                Headers headers = response.headers();
//                for (int i = 0; i < headers.size(); i++) {
//                    Log.d(TAG, headers.name(i) + ":" + headers.value(i));
//                }
//                Log.d(TAG, "onResponse: " + response.body().string());
                if (cyanoNetResponse != null) {
                    cyanoNetResponse.handleSuccessResponse(response.body().string());
                }
            }
        });
    }

    public static void setCyanoNetResponse(CyanoNetResponse cyanoNetResponse) {
        NetUtil.cyanoNetResponse = cyanoNetResponse;
    }

    public static void destory() {
        okHttpClient.dispatcher().cancelAll();
    }

    /**
     * 成功失败的回调
     */
    public interface CyanoNetResponse {
        void handleSuccessResponse(String data);

        void handleFailResponse(String data);
    }
}
