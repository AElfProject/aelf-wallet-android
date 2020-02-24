package com.legendwd.hyperpay.aelf.business.wallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.my.fragments.HelpFragment;
import com.legendwd.hyperpay.aelf.business.my.fragments.MultiLanguageFragment;
import com.legendwd.hyperpay.aelf.util.LanguageUtil;
import com.legendwd.hyperpay.lib.Constant;

import butterknife.BindView;
import butterknife.OnClick;

public class CreateImportWalletFragment extends BaseFragment {

    @BindView(R.id.tv_create)
    TextView tv_create;
    @BindView(R.id.tv_language)
    TextView tv_language;

    public static CreateImportWalletFragment newInstance() {
        return new CreateImportWalletFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  super.onCreateView(inflater, container, savedInstanceState);
        String lang = LanguageUtil.readLanguage();
        if (TextUtils.equals(LanguageUtil.LANG_CN, lang)) {
            tv_language.setText(R.string.lang_simplified_chinese_show);
        } else {
            tv_language.setText(R.string.lang_english_show);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.tv_create)
    void onClickCreate() {
        start(CreateAccountFragment.newInstance());
    }

    @OnClick(R.id.tv_import)
    void onClickImport() {
        start(ImportScanWalletFragment.newInstance());
    }

    @OnClick({R.id.tv_language, R.id.iv_arrow})
    void onClickLanguage() {
        Bundle args = new Bundle();
        args.putString(Constant.BundleKey.LANG, Constant.BundleValue.WALLET_PAGE);
        start(MultiLanguageFragment.newInstance(args));
    }

    @OnClick(R.id.tv_use)
    void onClickUse() {
        start(HelpFragment.newInstance());
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_createimport_wallet;
    }

    @Override
    public void process() {

    }

    @Override
    protected boolean enableSwipeBack() {
        return false;
    }
}
