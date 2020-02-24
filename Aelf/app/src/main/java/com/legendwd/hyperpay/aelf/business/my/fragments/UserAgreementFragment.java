package com.legendwd.hyperpay.aelf.business.my.fragments;

import android.text.TextUtils;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.ApiUrl;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.util.LanguageUtil;
import com.legendwd.hyperpay.aelf.widget.webview.DWebView;

import butterknife.BindView;

public class UserAgreementFragment extends BaseFragment {

    @BindView(R.id.webview)
    DWebView webview;


    public static UserAgreementFragment newInstance() {
        return new UserAgreementFragment();
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_user_agreement;
    }

    @Override
    public void process() {
        initToolbarNav(mToolbar, R.string.user_agreement, true);

        String lang = LanguageUtil.readLanguage();

        if (TextUtils.equals(LanguageUtil.LANG_CN, lang)) {
            webview.loadUrl(ApiUrl.UserAgreeUrl_ZH);
        } else {
            webview.loadUrl(ApiUrl.UserAgreeUrl_EN);
        }
    }

}
