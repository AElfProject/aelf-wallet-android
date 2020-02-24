package com.legendwd.hyperpay.aelf.widget.webview;

import android.webkit.JavascriptInterface;

import com.legendwd.hyperpay.aelf.listeners.HandleCallback;

/**
 * Created by du on 16/12/31.
 */

public class JsApi {
    private HandleCallback mHandleCallback;

    public JsApi(HandleCallback handleCallback) {
        mHandleCallback = handleCallback;
    }

    @JavascriptInterface
    public String onResult(Object msg) {
        if (mHandleCallback != null) {
            mHandleCallback.onHandle(msg);
        }
        return msg + "［syn call］";
    }

    @JavascriptInterface
    public String getWalletKeyStoreJS(Object msg) {
        if (mHandleCallback != null) {
            mHandleCallback.onHandle(msg);
        }
        return msg + "［syn call］";
    }

    @JavascriptInterface
    public String getWalletByMnemonicJS(Object msg) {
        if (mHandleCallback != null) {
            mHandleCallback.onHandle(msg);
        }
        return msg + "［syn call］";
    }


    @JavascriptInterface
    public String importWalletKeyStoreJS(Object msg) {
        if (mHandleCallback != null) {
            mHandleCallback.onHandle(msg);
        }
        return msg + "［syn call］";
    }


}