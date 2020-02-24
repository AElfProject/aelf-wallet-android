package com.legendwd.hyperpay.aelf.business.wallet.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.google.gson.Gson;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.model.bean.WalletBean;

import butterknife.BindView;
import butterknife.OnClick;

public class ExportPrivateKeyFragment extends BaseFragment {

    @BindView(R.id.et_input)
    EditText et_input;


    public static ExportPrivateKeyFragment newInstance(Bundle bundle) {
        ExportPrivateKeyFragment fragment = new ExportPrivateKeyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_export_private_key;
    }

    @Override
    public void process() {
        initToolbarNav(mToolbar, R.string.export_private_key, true);

        WalletBean bean = new Gson().fromJson(getArguments().getString("bean"), WalletBean.class);
        if (null != bean) {
            et_input.setText(bean.privateKey);
        }

    }

    @OnClick(R.id.tv_create)
    public void clickView() {
        String text = et_input.getText().toString();
        if (TextUtils.isEmpty(text))
            return;

        copyText(et_input.getText().toString());
    }
}
