package com.legendwd.hyperpay.aelf.business.wallet.fragments;

import android.content.Intent;

import com.legendwd.hyperpay.aelf.MainActivity;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;

import butterknife.OnClick;

public class WelcomeFragment extends BaseFragment {

    public static WelcomeFragment newInstance() {
        return new WelcomeFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_welcome;
    }

    @Override
    public void process() {
    }

    @Override
    protected boolean enableSwipeBack() {
        return false;
    }

    @OnClick(R.id.tv_learn_tutorial)
    void onClickLearn() {

        Intent intent = new Intent(_mActivity, MainActivity.class);
        startActivity(intent);
        _mActivity.finish();
    }
}
