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

/**
 * created by joseph at 2019/6/18
 */

public class OpenCompInfoDialog extends BaseBottomSheetDialogFragment {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_message)
    TextView tv_message;

    private String mTitle;
    private String mMessage;

    public OpenCompInfoDialog setTitle(String title) {
        this.mTitle = title;
        return this;
    }

    public OpenCompInfoDialog setMessage(String message) {
        this.mMessage = message;
        return this;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_open_comp_info;
    }


    @OnClick({R.id.tv_cancel, R.id.tv_message})
    void clickButton(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_message:
                dismiss();
                mHandleCallback.onHandle(tv_message.getText().toString());
                break;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!TextUtils.isEmpty(mTitle)) {
            tv_title.setText(mTitle);
        }

        if (!TextUtils.isEmpty(mMessage)) {
            tv_message.setText(mMessage);
        }

    }


}
