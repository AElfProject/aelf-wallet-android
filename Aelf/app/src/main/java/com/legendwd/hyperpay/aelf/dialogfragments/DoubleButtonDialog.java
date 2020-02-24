package com.legendwd.hyperpay.aelf.dialogfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseDialogFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class DoubleButtonDialog extends BaseDialogFragment {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_message)
    TextView tv_message;
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_right)
    TextView tv_right;


    private String mTitle;
    private String mMessage;
    private String mLeftText;



    @Override
    public int getLayoutId() {
        return R.layout.dialog_double_button;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        if(TextUtils.isEmpty(mTitle)){
            tv_title.setText("");
        }else{
            tv_title.setText(mTitle);
        }

        if(TextUtils.isEmpty(mMessage)){
            tv_message.setText("");
        }else{
            tv_message.setText(mMessage);
        }

        if(TextUtils.isEmpty(mLeftText)){
            tv_left.setText(getString(R.string.cancel));
        }else{
            tv_left.setText(mLeftText);
        }



    }




    @OnClick({R.id.tv_left, R.id.tv_right})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                if (null != mHandleCallback) {
                    mHandleCallback.onHandle(false);
                }
                dismiss();
                break;

            case R.id.tv_right:
                if (null != mHandleCallback) {
                    mHandleCallback.onHandle(true);
                }
                dismiss();
                break;
        }
    }

    public DoubleButtonDialog setTitle(String title) {
        mTitle = title;
        return this;
    }

    public DoubleButtonDialog setLeftText(String leftText) {
        mLeftText = leftText;
        return this;
    }

    public DoubleButtonDialog setMessage(String message) {
        mMessage = message;
        return this;
    }
}
