package com.legendwd.hyperpay.aelf.dialogfragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.AelfApplication;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseDialogFragment;
import com.legendwd.hyperpay.lib.AppConfig;

import butterknife.BindView;

public class ToastDialog extends BaseDialogFragment {
    @BindView(R.id.tv_toast)
    TextView mTvToast;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    private boolean mCancelAble = true;

    private String mToastStr;
    private Toast mToastTime = Toast.SHORT;
    private boolean isLoading = false;
    private boolean isDismiss = false;

    @Override
    public int getLayoutId() {
        return R.layout.dialog_toast;
    }

    public ToastDialog setToast(String toast) {
        mToastStr = toast;
        if (mTvToast != null) {
            mTvToast.setText(mToastStr);
        }
        return this;
    }

    public ToastDialog setToast(int toastId) {
        mToastStr = AelfApplication.getTopActivity().getString(toastId);
        if (mTvToast != null) {
            mTvToast.setText(mToastStr);
        }
        return this;
    }

    public ToastDialog setLoading(boolean isLoading) {
        this.isLoading = isLoading;
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        return this;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCloseable = false;
        if (!TextUtils.isEmpty(mToastStr)) {
            mTvToast.setText(mToastStr);
        } else {
            mTvToast.setText(getString(R.string.preocessing));
        }

        if (!isLoading) {
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (getContext() == null || isDismiss) {
                        return;
                    }
                    dismiss();
                    if (null != mHandleCallback) {
                        mHandleCallback.onHandle(null);
                    }

                }
            }, mToastTime == Toast.SHORT ? 1500 : (mToastTime == Toast.LONG ? 3000 : Long.MAX_VALUE));
        }
        if (isLoading) {
            mProgressBar.setVisibility(View.VISIBLE);
            getDialog().setOnKeyListener((anInterface, keyCode, event) -> {
                if (mCancelAble && keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    dismiss();
                    return true;
                }
                return false;
            });
        }

    }

    public ToastDialog setToastCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
        mCancelAble = cancelable;
        if (mIvClose != null && !cancelable) {
            mIvClose.setVisibility(View.VISIBLE);
            mIvClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        } else if (mIvClose != null) {
            mIvClose.setVisibility(View.GONE);
        }
        return ToastDialog.this;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        isDismiss = true;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        isDismiss = true;
    }

    public ToastDialog setToastTime(Toast toastTime) {
        mToastTime = toastTime;
        return this;
    }

    @Override
    protected boolean cancelEnable() {
        return mCancelAble;
    }

    public enum Toast {
        SHORT,
        LONG,
        FORVER
    }
}
