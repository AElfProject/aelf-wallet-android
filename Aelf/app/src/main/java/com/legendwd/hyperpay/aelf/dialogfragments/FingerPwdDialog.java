package com.legendwd.hyperpay.aelf.dialogfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseDialogFragment;

import butterknife.BindView;

public class FingerPwdDialog extends BaseDialogFragment {

    @BindView(R.id.tv_create)
    TextView mtvCreate;
    @BindView(R.id.et_input)
    EditText mEtName;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    private String mTitle;
    private String mHint;
    private int mTitleId = -1;
    private int mHintId = -1;


    public FingerPwdDialog setTitle(String title) {
        mTitle = title;
        return this;
    }

    public FingerPwdDialog setTitle(int titleId) {
        mTitleId = titleId;
        return this;
    }

    public FingerPwdDialog setHint(String hint) {
        mHint = hint;
        return this;
    }

    public FingerPwdDialog setHint(int hintId) {
        mHintId = hintId;
        return this;
    }


    @Override
    public int getLayoutId() {
        return R.layout.dialog_password;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mtvCreate.setOnClickListener(v -> {

            String pwd = mEtName.getText().toString();
            if (TextUtils.isEmpty(pwd)) {
                return;
            }

            if (null != mHandleCallback) {
                mHandleCallback.onHandle(pwd);
            }
        });


        if (!TextUtils.isEmpty(mTitle)) {
            mTvTitle.setText(mTitle);
        } else if (mTitleId != -1) {
            mTvTitle.setText(mTitleId);
        }

        if (!TextUtils.isEmpty(mHint)) {
            mEtName.setHint(mHint);
        } else if (mHintId != -1) {
            mEtName.setHint(mHintId);
        }
    }

    @Override
    protected boolean cancelEnable() {
        return false;
    }
}
