package com.legendwd.hyperpay.aelf.dialogfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseBottomSheetDialogFragment;

import butterknife.OnClick;

public class BackupDialog extends BaseBottomSheetDialogFragment {

    @Override
    public int getLayoutId() {
        return R.layout.dialog_backup;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @OnClick({R.id.iv_close, R.id.tv_commit})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;

            case R.id.tv_commit:
                dismiss();
                if (mHandleCallback != null) {
                    mHandleCallback.onHandle(null);
                }
                break;
        }
    }


    @Override
    protected boolean hideEnable() {
        return false;
    }


    @Override
    protected boolean cancelEnable() {
        return false;
    }
}
