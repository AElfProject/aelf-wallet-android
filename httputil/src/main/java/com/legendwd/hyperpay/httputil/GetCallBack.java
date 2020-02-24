package com.legendwd.hyperpay.httputil;

/**
 * @author Colin
 * @date 2019/10/18.
 * description：
 */
interface GetCallBack {
    void success(String json);

    void onError(String err);
}
