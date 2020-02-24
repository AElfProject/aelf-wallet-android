package com.legendwd.hyperpay.aelf.widget.webview;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.lib.Constant;

import butterknife.BindView;

public class WebviewFragment extends BaseFragment {

    @BindView(R.id.webview)
    DWebView mWebView;
    @BindView(R.id.progress_Bar)
    ProgressBar mProgressBar;
    private String mUrl;
    private String mTitle;
    private String mData;

    public static WebviewFragment newInstance(Bundle bundle) {
        WebviewFragment webviewFragment = new WebviewFragment();
        webviewFragment.setArguments(bundle);
        return webviewFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_web;
    }

    @Override
    public void process() {

        Bundle bundle = getArguments();
        mUrl = bundle.getString(Constant.BundleKey.KEY_WEBVIEW_URl);
        mTitle = bundle.getString(Constant.BundleKey.KEY_WEBVIEW_TITLE);
        mData = bundle.getString(Constant.BundleKey.KEY_WEBVIEW_DATA);

        initToolbarNav(mToolbar, mTitle, true);

        if (!TextUtils.isEmpty(mData)) {

            String htmlhead = "<html lang=\"zh-cn\"><head><meta charset=\"utf-8\"><meta name=\"viewport\" content=\"width=device-width, nickName-scalable=no\"></meta><style>img{max-width: 100%; width:auto; height:auto;}</style></head><body>";

            String htmlEnd = "</body></html>";

            String content = htmlhead + mData + htmlEnd;

            mWebView.loadData(content, "text/html", "UTF-8");
        } else {
            mWebView.loadUrl(mUrl);
        }


        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                if(!TextUtils.isEmpty(title)){
//                    initToolbarNav(mToolbar, title, true);
//                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }

            }
        });
    }

}
