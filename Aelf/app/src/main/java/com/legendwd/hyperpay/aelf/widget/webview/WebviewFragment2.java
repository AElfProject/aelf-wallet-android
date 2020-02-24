package com.legendwd.hyperpay.aelf.widget.webview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.google.gson.Gson;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.model.bean.WalletBean;
import com.legendwd.hyperpay.aelf.util.WalletUtil;
import com.legendwd.hyperpay.lib.Constant;
import com.legendwd.hyperpay.lib.Logger;

import butterknife.BindView;
import butterknife.OnClick;

public class WebviewFragment2 extends BaseFragment {

    @BindView(R.id.webview)
    DWebView mWebView;
    @BindView(R.id.progress_Bar)
    ProgressBar mProgressBar;
    private String mUrl;
    private String mTitle;

    public static WebviewFragment2 newInstance(Bundle bundle) {
        WebviewFragment2 webviewFragment = new WebviewFragment2();
        webviewFragment.setArguments(bundle);
        return webviewFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_web2;
    }

    @Override
    public void process() {
        initWeb();
        Bundle bundle = getArguments();
        mUrl = bundle.getString(Constant.BundleKey.KEY_WEBVIEW_URl);
        mTitle = bundle.getString(Constant.BundleKey.KEY_WEBVIEW_TITLE);

        initToolbarNav(mToolbar, mTitle, true);
        mWebView.loadUrl(mUrl);
        setHandler();
    }

    @SuppressLint("JavascriptInterface")
    private void initWeb() {
//        mProgressBar.setMax(100);
//        WebSettings webSetting = mWebView.getSettings();
//        webSetting.setJavaScriptEnabled(true);
//        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSetting.setAllowFileAccess(true);
//        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//        webSetting.setSupportZoom(true);
//        webSetting.setBuiltInZoomControls(true);
//        webSetting.setUseWideViewPort(true);
//        webSetting.setSupportMultipleWindows(true);
//        webSetting.setLoadWithOverviewMode(true);
//        webSetting.setAppCacheEnabled(true);
//        // webSetting.setDatabaseEnabled(true);
//        webSetting.setDomStorageEnabled(true);
//        webSetting.setGeolocationEnabled(true);
//        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
//        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
//        webSetting.setAppCachePath(getContext().getDir("appcache", 0).getPath());
//        webSetting.setDatabasePath(getContext().getDir("databases", 0).getPath());
//        webSetting.setGeolocationDatabasePath(getContext().getDir("geolocation", 0).getPath());
//        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
////         webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
//        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);

        //增加
//        webSetting.setTextSize(WebSettings.TextSize.NORMAL);
        //支持混合模式
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            webSetting.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }
        //接口禁止(直接或反射)调用，避免视频画面无法显示：
//        setLayerType(View.LAYER_TYPE_SOFTWARE,null);
//        setDrawingCacheEnabled(true);
        // this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
        // settings 的设计
//        CookieSyncManager.createInstance(getContext());
//        CookieSyncManager.getInstance().sync();

//        mWebView.addJavascriptInterface(new WebViewJavaScriptFunction() {
//            @Override
//            public void onJsFunctionCalled(String tag) {
//
//            }
//        }, "AElf");
//        mWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
//                webView.loadUrl(s);
//                return true;
//            }
//
//            @Override
//            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
//                super.onPageStarted(webView, s, bitmap);
//                mProgressBar.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onPageFinished(WebView webView, String s) {
//                super.onPageFinished(webView, s);
//                mProgressBar.setVisibility(View.GONE);
//            }
//        });
//        mWebView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView webView, int i) {
//                super.onProgressChanged(webView, i);
//                mProgressBar.setProgress(i);
//            }
//
//        });
    }

    //data是JavaScript返回的数据
    private void setHandler() {
        mWebView.setDefaultHandler(new DefaultHandler());

        mWebView.registerHandler("functionInJs", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                Logger.e("handler = submitFromWeb, data from web = " + data);
                function.onCallBack("submitFromWeb exe, response data 中文 from Java");
            }

        });
    }

    @OnClick(R.id.tv_title)
    void onClick() {
        Gson gson = new Gson();
        WalletBean walletBean = new WalletBean();
        walletBean.mnemonic = WalletUtil.generateMnemonic();
        mWebView.callHandler("getWalletByMnemonicJS", gson.toJson(walletBean), new CallBackFunction() {

            @Override
            public void onCallBack(String data) {
                // TODO Auto-generated method stub
                Logger.i("getWalletByMnemonicJS reponse data from js " + data);
            }

        });
    }
}
