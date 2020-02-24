package com.legendwd.hyperpay.aelf.business.assets.fragments;


import android.os.Bundle;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.widget.webview.DWebView;

import butterknife.BindView;

public class ExplorerFragment extends BaseFragment {

    @BindView(R.id.webview)
    DWebView webview;


    public static ExplorerFragment newInstance(String name, String linkUrl) {
        ExplorerFragment explorerFragment = new ExplorerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name",name);
        bundle.putString("linkUrl",linkUrl);
        explorerFragment.setArguments(bundle);
        return explorerFragment;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_user_agreement;
    }

    @Override
    public void process() {
        initToolbarNav(mToolbar, getArguments().getString("name"), true);

        webview.loadUrl(getArguments().getString("linkUrl"));
    }

}
