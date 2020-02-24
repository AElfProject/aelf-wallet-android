package com.github.ont.cyano;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.github.ont.aelfmoudle.ConnectModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.util.HashMap;
import java.util.Map;

public class CyanoWebView extends WebView {
    private static final String BRIDGE_NAME = "android";
    private NativeJsBridge nativeJsBridge = new NativeJsBridge(this);
    private PublicKey dappPublicKey;
    private PrivateKey dappPrivateKey;


    public CyanoWebView(Context context) {
        super(context);
        initView(context);
    }

    public NativeJsBridge getNativeJsBridge() {
        return nativeJsBridge;
    }

    @SuppressLint("NewApi")
    private void initView(Context context) {
        setWebContentsDebuggingEnabled(true);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(params);
        WebSettings webSetting = getSettings();//

        this.setInitialScale(1);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setUseWideViewPort(true);

        webSetting.setLoadWithOverviewMode(true);
//        webSetting.setDisplayZoomControls(false);


        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
//• Ensure that all WebViews call:
        webSetting.setAllowFileAccess(false);
//        • Consider calling:
        webSetting.setAllowFileAccessFromFileURLs(false);

        webSetting.setAllowContentAccess(false);
        webSetting.setDomStorageEnabled(true);
//        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSetting.setLoadsImagesAutomatically(true);
        webSetting.setJavaScriptEnabled(true);

        addJavascriptInterface(nativeJsBridge, BRIDGE_NAME);

        setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
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
//                error.getErrorCode()
//                加载本地失败的界面
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //https
                handler.proceed();
//                super.onReceivedSslError(view, handler, error);
            }
        });
    }

    private void linkBridge() {
        String js = "window.originalPostMessage = window.postMessage;" + "window.postMessage = function(data) {" + BRIDGE_NAME + ".postMessage(data);}";
        evaluateJavascriptWithFallback(js);
    }

    protected void evaluateJavascriptWithFallback(final String script) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            post(new Runnable() {
                @Override
                public void run() {
                    evaluateJavascript(script, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            Log.d("value", value);
                        }

                    });
                }
            });
            return;
        }

        try {
            loadUrl("javascript:" + URLEncoder.encode(script, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // UTF-8 should always be supported
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
        evaluateJavascriptWithFallback("(function () {" + "var event;" + "var data = " + eventInitDict.toString() + ";" + "try {" + "event = new MessageEvent('message', data);" + "} catch (e) {" + "event = document.createEvent('MessageEvent');" + "event.initMessageEvent('message', true, true, data.data, data.origin, data.lastEventId, data.source);" + "}" + "document.dispatchEvent(event);" + "})();");
    }

    public void sendMessageToWeb(String data, boolean isUrlSchema) {
        JSONObject eventInitDict = new JSONObject();
        try {
            eventInitDict.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String js = "window.originalPostMessage = window.postMessage;" + "window.postMessage = function(data) {" + eventInitDict.toString() + ".postMessage(data);}";
        evaluateJavascriptWithFallback(js);

    }

    public void sendSuccessToWeb(String action, String version, String id, Object result) {
        Map map = new HashMap<>();
        map.put("action", action);
        map.put("error", 0);
        map.put("version", version);
        map.put("id", id);
        map.put("desc", "SUCCESS");
        map.put("result", result);
        Log.i("haha", "sendSuccessToWeb: " + JSON.toJSONString(map));
        sendMessageToWeb(Base64.encodeToString(Uri.encode(JSON.toJSONString(map)).getBytes(), Base64.NO_WRAP));
    }

    /**
     * AELF 单独的处理
     *
     * @param connectModule
     */
    public void sendAelfToWeb(ConnectModule connectModule) {
        Map map = new HashMap<>();
        map.put("id", connectModule.getId());
        map.put("appId", connectModule.getAppId());
        map.put("action", connectModule.getAction());
        ConnectModule.ParamsBean params = connectModule.getParams();
        String publicKey = params.getPublicKey();
        String random = params.getRandom();
        String encryptAlgorithm = params.getEncryptAlgorithm();
        String signature = params.getSignature();
        getEncrypt(encryptAlgorithm);
        Map mapParams = new HashMap();
        mapParams.put("publicKey", publicKey);
        mapParams.put("random", random);
        mapParams.put("encryptAlgorithm", encryptAlgorithm);
        mapParams.put("params", signature);
        map.put("params", mapParams);
        Log.i("haha", "sendSuccessToWeb: " + JSON.toJSONString(map));
        sendMessageToWeb(Base64.encodeToString(Uri.encode(JSON.toJSONString(map)).getBytes(), Base64.NO_WRAP), true);
    }

    public void sendFailToWeb(String action, int errorCode, String version, String id, Object result) {
        Map map = new HashMap<>();
        map.put("action", action);
        map.put("error", errorCode);
        map.put("version", version);
        map.put("id", id);
        map.put("desc", "SUCCESS");
        map.put("result", result);
        sendMessageToWeb(Base64.encodeToString(Uri.encode(JSON.toJSONString(map)).getBytes(), Base64.NO_WRAP));
    }

    public void destorySelf() {
        loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        clearHistory();
        ((ViewGroup) getParent()).removeView(this);
    }

    /**
     * 公私钥生成的椭圆曲线函数算法类型，客户端根据此值生成对应类型的公私钥
     *
     * @param curveName
     */
    public void getEncrypt(String curveName) {
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("EC");
            // curveName这里取值：secp256k1
            ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec(curveName);
            keyPairGenerator.initialize(ecGenParameterSpec, new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            // 获取公钥
            dappPublicKey = keyPair.getPublic();
            Log.d("dappPublicKey", dappPublicKey.getEncoded().toString());
            // 获取私钥
            dappPrivateKey = keyPair.getPrivate();
            Log.d("dappPrivateKey", dappPrivateKey.getEncoded().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
