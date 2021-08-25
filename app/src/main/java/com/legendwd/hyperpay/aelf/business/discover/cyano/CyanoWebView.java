package com.legendwd.hyperpay.aelf.business.discover.cyano;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import com.alibaba.fastjson.JSON;
import com.legendwd.hyperpay.aelf.business.discover.dapp.GameWebActivity;

import org.bitcoinj.core.Utils;
import org.json.JSONException;
import org.json.JSONObject;
import org.spongycastle.util.encoders.Hex;
import org.web3j.crypto.Hash;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CyanoWebView extends WebView {
    private static final String BRIDGE_NAME = "android";
    private NativeJsBridge nativeJsBridge = new NativeJsBridge(this);

    private Context context;

    public CyanoWebView(Context context) {
        super(context);
        this.context = context;
        initView(context);
    }

    public NativeJsBridge getNativeJsBridge() {
        return nativeJsBridge;
    }

    private void initView(Context context) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(params);
        WebSettings webSetting = getSettings();
        webSetting.setUseWideViewPort(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSetting.setAllowFileAccess(false);
        webSetting.setAllowFileAccessFromFileURLs(false);

        webSetting.setAllowContentAccess(false);
        webSetting.setDomStorageEnabled(true);
        addJavascriptInterface(nativeJsBridge, BRIDGE_NAME);

        setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //Android8.0以下的需要返回true 并且需要loadUrl；8.0之后效果相反
                if (Build.VERSION.SDK_INT < 26) {
                    view.loadUrl(url);
                    return true;
                }
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                linkBridge();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                加载本地失败的界面
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // Google play review proceed -> cancel
                handler.proceed();
                // AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                // alertDialog.setMessage("SSL Error");
                // alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                //     @Override
                //     public void onClick(DialogInterface dialog, int which) {
                //             handler.cancel();
                //     }
                // });
                // alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                //     @Override
                //     public void onClick(DialogInterface dialog, int which) {
                //         handler.proceed();
                //     }
                // });
                // AlertDialog dialog = alertDialog.create();
                // alertDialog.show();
                super.onReceivedSslError(view, handler, error);
            }
        });
    }

    public void linkBridge() {
        String js = "window.originalPostMessage = window.postMessage;" + "window.postMessage = function(data) {" + BRIDGE_NAME + ".postMessage(data);}";
        evaluateJavascriptWithFallback(js);

    }

    protected void evaluateJavascriptWithFallback(final String script) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            post(() -> evaluateJavascript(script, null));
            return;
        }

        try {
            loadUrl("javascript:" + URLEncoder.encode(script, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessageToWeb(String data) {

        JSONObject eventInitDict = new JSONObject();
        try {
            eventInitDict.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String script = "(function () {" + "var event;"
                + "var data = " + eventInitDict.toString() + ";"
                + "try {"
                + "event = new MessageEvent('message', data);"
                + "} catch (e) {"
                + "event = document.createEvent('MessageEvent');"
                + "event.initMessageEvent('message', true, true, data.data, data.origin, data.lastEventId, data.source);"
                + "}"
                + "window.dispatchEvent(event);" + "})();";


        evaluateJavascriptWithFallback(script);



//        Log.e("log","-----\n"+script);
        Log.e("message","已发送");


    }


    public void sendSuccessToWebNoSign(Object data,String id) {

        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        result.put("code", 0);
        result.put("msg", "success");
        result.put("data",data);
        result.put("error",new String[]{});
        com.alibaba.fastjson.JSONObject message = new com.alibaba.fastjson.JSONObject();
        message.put("id", id);
        message.put("result", result);
        Log.e("1111","---------->\n"
                +JSON.toJSONString(message));
        sendMessageToWeb(Base64.encodeToString(Uri.encode(JSON.toJSONString(message)).getBytes(), Base64.NO_WRAP));
    }

    public void sendSuccessToWeb(Object data,String id) {

        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        result.put("code", 0);
        result.put("msg", "success");
        result.put("data",data);
        result.put("error",new String[]{});
        String originalResult = Base64.encodeToString(Uri.encode(JSON.toJSONString(result)).getBytes(), Base64.NO_WRAP);
        Log.e("1111","---------->\n"
                +JSON.toJSONString(result));
        result.clear();
        result.put("originalResult",originalResult);

        DappUtils.doSign(Base64.decode(originalResult, Base64.NO_WRAP), o -> ((Activity)context).runOnUiThread(() -> {
            result.put("signature",o.toString());
            com.alibaba.fastjson.JSONObject message = new com.alibaba.fastjson.JSONObject();
            message.put("id", id);
            message.put("result", result);
            sendMessageToWeb(Base64.encodeToString(Uri.encode(JSON.toJSONString(message)).getBytes(), Base64.NO_WRAP));
        }));




    }

    public void sendFailToWeb(String id,String error,int code) {
        sendFailToWeb(error,id,error,code);
    }
    public void sendFailToWeb(String msg,String id,String error,int code) {
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        result.put("code", code);
        result.put("msg", msg);
        result.put("error",new String[]{error});
        String originalResult = Base64.encodeToString(Uri.encode(JSON.toJSONString(result)).getBytes(), Base64.NO_WRAP);
        result.clear();
        result.put("originalResult",originalResult);

        DappUtils.doSign(Base64.decode(originalResult, Base64.NO_WRAP), o -> ((Activity)context).runOnUiThread(() -> {
            result.put("signature",o.toString());
            com.alibaba.fastjson.JSONObject message = new com.alibaba.fastjson.JSONObject();
            message.put("id", id);
            message.put("result", result);
            sendMessageToWeb(Base64.encodeToString(Uri.encode(JSON.toJSONString(message)).getBytes(), Base64.NO_WRAP));
        }));
    }

    public void destorySelf() {
        loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        clearHistory();
        ((ViewGroup) getParent()).removeView(this);
    }
}
