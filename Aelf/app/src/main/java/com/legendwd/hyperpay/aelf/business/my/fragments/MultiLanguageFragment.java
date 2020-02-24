package com.legendwd.hyperpay.aelf.business.my.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.legendwd.hyperpay.aelf.MainActivity;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.wallet.CreateImportWalletActivity;
import com.legendwd.hyperpay.aelf.util.LanguageUtil;
import com.legendwd.hyperpay.lib.Constant;

import butterknife.BindView;

public class MultiLanguageFragment extends BaseFragment {
    @BindView(R.id.lv)
    ListView lv;
    private boolean mAlphaOut = false;
    private ArrayAdapter<String> mAdapter;

    public static MultiLanguageFragment newInstance(Bundle bundle) {
        MultiLanguageFragment tabFourFragment = new MultiLanguageFragment();
        tabFourFragment.setArguments(bundle);
        return tabFourFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initToolbarNav(mToolbar, R.string.select_language, true);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_lv;
    }

    @Override
    public void process() {
        mAdapter = new ArrayAdapter<String>(_mActivity, R.layout.item_language) {{
            add("简体中文");
            add("English");
        }};


        lv.setAdapter(mAdapter);

        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    LanguageUtil.saveLanguage(LanguageUtil.LANG_CN);
                } else {
                    LanguageUtil.saveLanguage(LanguageUtil.LANG_EN);
                }

                Bundle bundle = getArguments();
                if (null != bundle) {
                    String from = bundle.getString(Constant.BundleKey.LANG);
                    mAlphaOut = true;
                    if (Constant.BundleValue.WALLET_PAGE.equals(from)) {
                        Intent intent = new Intent(_mActivity, CreateImportWalletActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        _mActivity.overridePendingTransition(0, 0);
                    } else {
                        Intent intent = new Intent(_mActivity, MainActivity.class);
                        intent.putExtra(Constant.BundleKey.SET_LANGUAGE,true);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        _mActivity.overridePendingTransition(0, 0);
                    }
                    _mActivity.finish();
                }
            }
        });

        String lang = LanguageUtil.readLanguage();
        if (TextUtils.equals(LanguageUtil.LANG_CN, lang)) {
            lv.setItemChecked(0, true);
        } else {
            lv.setItemChecked(1, true);
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (mAlphaOut && !enter) {
            transit = R.anim.alpha_out;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }
}
