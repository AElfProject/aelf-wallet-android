package com.legendwd.hyperpay.aelf.business.my.fragments;

import android.app.KeyguardManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.dialogfragments.ConfirmDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.FingerDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.ToastDialog;
import com.legendwd.hyperpay.aelf.listeners.HandleCallback;
import com.legendwd.hyperpay.aelf.listeners.OnTextCorrectCallback;
import com.legendwd.hyperpay.aelf.util.AESUtil;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class TouchIDFragment extends BaseFragment {

    @BindView(R.id.switch_touch)
    SwitchCompat switch_touch;

    public static TouchIDFragment newInstance() {
        Bundle args = new Bundle();
        TouchIDFragment tabFourFragment = new TouchIDFragment();
        tabFourFragment.setArguments(args);
        return tabFourFragment;
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        //         return super.onCreateFragmentAnimation();
        return new FragmentAnimator();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_touch_id;
    }

    @Override
    public void process() {
        initToolbarNav(mToolbar, R.string.touch_id, true);
        boolean isTouch = CacheUtil.getInstance().getProperty(Constant.Sp.TOUCH_ID, false);
        switch_touch.setChecked(isTouch ? true : false);
    }


    @OnClick(R.id.rel_touch)
    public void clickView() {

        if (switch_touch.isChecked()) {
            cancelToichId();
            return;
        }
        FingerprintManager fingerprintManager = null;
        KeyguardManager keyguardManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            fingerprintManager = getContext().getSystemService(FingerprintManager.class);
            keyguardManager = getContext().getSystemService(KeyguardManager.class);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                || fingerprintManager == null
                || keyguardManager == null
                || !fingerprintManager.isHardwareDetected()) {
            //不支持
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                    .setToast(R.string.not_support);
        } else if (!fingerprintManager.hasEnrolledFingerprints()) {
            //请录入至少一个指纹"
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                    .setToast(R.string.atleast_one_finger);
        } else {
            showPasswordDialogForPrivate(new OnTextCorrectCallback() {
                @Override
                public void onTextCorrect(Object... obj) {
                    String key = (String) obj[0];
                    fingerDialog(key);
                }
            });
        }

    }

    private void cancelToichId() {
        DialogUtils.showDialog(ConfirmDialog.class, getFragmentManager())
                .setDialogTitle(getString(R.string.touch_id_cancle))
                .setConfirmText(getString(R.string.confirm))
                .setHandleCallback(new HandleCallback() {
            @Override
            public void onHandle(Object o) {
                switch_touch.setChecked(false);
                CacheUtil.getInstance().setProperty(Constant.Sp.TOUCH_ID, false);
            }
        });
    }

    private void fingerDialog(String key) {
        FingerDialog fingerDialog = DialogUtils.showDialog(FingerDialog.class, getFragmentManager());
        fingerDialog.setBoolHide(true);
        fingerDialog.setHandleCallback(o -> {
            fingerDialog.dismiss();
            if (o instanceof Boolean) {
                switch_touch.setChecked(true);
                DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.touch_success));
                CacheUtil.getInstance().setProperty(Constant.Sp.TOUCH_ID, true);
                CacheUtil.getInstance().setProperty(Constant.Sp.TOUCH_PASSWORD, AESUtil.getInstance().encrypt(key));
            } else {
                DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                        .setToast((String) o);
            }
        });
    }
}
