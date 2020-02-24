package com.legendwd.hyperpay.aelf.business.wallet.fragments;

import android.os.Bundle;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;

import butterknife.OnClick;

public class ExportMnemonicPhraseFragment extends BaseFragment {


    public static ExportMnemonicPhraseFragment newInstance(Bundle bundle) {
        ExportMnemonicPhraseFragment fragment = new ExportMnemonicPhraseFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_export_mnemonic;
    }

    @Override
    public void process() {
        initToolbarNav(mToolbar, R.string.backup_notes, true);

    }

    @OnClick(R.id.tv_next)
    public void clickView() {
        start(NotificationFragment.newInstance(getArguments()));
    }
}
