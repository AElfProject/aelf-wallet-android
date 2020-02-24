package com.legendwd.hyperpay.aelf.dialogfragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseBottomSheetDialogFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class ConfirmDialog extends BaseBottomSheetDialogFragment {

    @BindView(R.id.tv_cancel)
    TextView mTvCancel;
    @BindView(R.id.tv_confirm)
    TextView mTvConfirm;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    private String mTitle;
    private String mMessage;

    public static ConfirmDialog newInstance() {
        return new ConfirmDialog();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!TextUtils.isEmpty(mTitle)) {
            mTvTitle.setText(mTitle);
        }

        if (!TextUtils.isEmpty(mMessage)) {
            mTvConfirm.setText(mMessage);
        }
    }

    @OnClick({R.id.tv_cancel, R.id.iv_close})
    void onClickCancel() {
        dismiss();
    }

    @OnClick(R.id.tv_confirm)
    void onClickConfirm() {
        if (mHandleCallback != null) {
            mHandleCallback.onHandle(true);
        }
        dismiss();
    }


    @Override
    public int getLayoutId() {
        return R.layout.dialog_confirm;
    }

    public ConfirmDialog setDialogTitle(String title) {
        mTitle = title;
        return this;
    }

    public ConfirmDialog setConfirmText(String confirmText) {
        mMessage = confirmText;
        return this;
    }
}
