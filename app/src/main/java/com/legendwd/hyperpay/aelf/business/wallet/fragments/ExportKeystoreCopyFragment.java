package com.legendwd.hyperpay.aelf.business.wallet.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.util.StringUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class ExportKeystoreCopyFragment extends BaseFragment {

    @BindView(R.id.et_input)
    TextView mEtInput;


    public static ExportKeystoreCopyFragment newInstance(Bundle bundle) {
        ExportKeystoreCopyFragment exportKeystoreCopyFragment = new ExportKeystoreCopyFragment();
        exportKeystoreCopyFragment.setArguments(bundle);
        return exportKeystoreCopyFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_export_keystore_copy;
    }

    @Override
    public void process() {
        initToolbarNav(mToolbar, R.string.export_keystore, true);

        String data = getArguments().getString("data");
        mEtInput.setText(StringUtil.stringToJSON(data));
    }

    @OnClick(R.id.tv_create)
    public void clickView() {
        String text = mEtInput.getText().toString();
        if (TextUtils.isEmpty(text))
            return;

        copyText(mEtInput.getText().toString());
    }

}
