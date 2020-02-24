package com.legendwd.hyperpay.aelf.business.wallet.fragments;

import android.os.Bundle;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class ExportPrivateFragment extends BaseFragment {

    @BindView(R.id.tv_next)
    TextView tv_next;

    public static ExportPrivateFragment newInstance(Bundle bundle) {

        ExportPrivateFragment fragment = new ExportPrivateFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_export_private;
    }

    @Override
    public void process() {
        initToolbarNav(mToolbar, R.string.backup_notes, true);

    }

    @OnClick(R.id.tv_next)
    public void clickView() {
        start(ExportPrivateKeyFragment.newInstance(getArguments()));
    }

}
