package com.legendwd.hyperpay.aelf.business.assets.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.model.bean.TransactionNoticeBean;

import butterknife.BindView;
import butterknife.OnClick;

public class TransactionFailFragment extends BaseFragment {
    @BindView(R.id.tv_copy_txid)
    TextView tv_copy_txid;
    @BindView(R.id.tv_copy_from)
    TextView tv_copy_from;
    @BindView(R.id.tv_copy_to)
    TextView tv_copy_to;
    @BindView(R.id.tv_title_right)
    TextView title_right;

    public static TransactionFailFragment newInstance(Bundle args) {
        TransactionFailFragment tabFourFragment = new TransactionFailFragment();
        tabFourFragment.setArguments(args);
        return tabFourFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initToolbarNav(mToolbar, R.string.transaction_record, true);
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_transaction_record_fail;
    }

    @Override
    public void process() {
        TransactionNoticeBean.NoticeBean bean = new Gson().fromJson(getArguments().getString("bean"), TransactionNoticeBean.NoticeBean.class);
        title_right.setText(getResources().getString(R.string.refresh));
    }


    @OnClick({R.id.tv_copy_from, R.id.tv_copy_to, R.id.tv_copy_txid})
    public void clickView(View view) {
        String text = "";

        switch (view.getId()) {

            case R.id.tv_copy_from:
                text = tv_copy_from.getText().toString();
                break;
            case R.id.tv_copy_to:
                text = tv_copy_to.getText().toString();
                break;
            case R.id.tv_copy_txid:
                text = tv_copy_to.getText().toString();
                break;
        }

        copyText(text);
    }
}
