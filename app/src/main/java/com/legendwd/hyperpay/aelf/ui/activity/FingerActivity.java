package com.legendwd.hyperpay.aelf.ui.activity;

import android.graphics.Color;
import android.os.Bundle;

import com.gyf.immersionbar.ImmersionBar;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseActivity;
import com.legendwd.hyperpay.aelf.business.my.fragments.FingerFragment;
import com.legendwd.hyperpay.aelf.business.my.fragments.FingerPwdFragment;
import com.legendwd.hyperpay.aelf.util.LanguageUtil;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

public class FingerActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_finger;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImmersionBar.with(this).keyboardEnable(true)
                .statusBarColorInt(Color.WHITE)
                .navigationBarColorInt(Color.WHITE)
                .autoDarkModeEnable(true, 0.2f).init();

        if (CacheUtil.getInstance().getProperty(Constant.Sp.TOUCH_ID, false)) {
            if (CacheUtil.getInstance().getProperty(Constant.Sp.TOUCH_ID_FAILED, false)) {
                loadRootFragment(R.id.fl_container, FingerPwdFragment.newInstance());
            } else {
                loadRootFragment(R.id.fl_container, FingerFragment.newInstance());
            }
        }

    }


    @Override
    public void onBackPressedSupport() {
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }


}
