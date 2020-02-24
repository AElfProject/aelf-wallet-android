package com.legendwd.hyperpay.aelf.dialogfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseBottomSheetDialogFragment;
import com.legendwd.hyperpay.aelf.model.SortModel;

import butterknife.BindView;
import butterknife.OnClick;

public class BottomSortDialog extends BaseBottomSheetDialogFragment {
    @BindView(R.id.ll_items)
    LinearLayout llItems;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.dialog_sort;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        for (int i = 1; i < llItems.getChildCount(); i++) {
            SortModel model = new SortModel();
            model.position = i;
            model.text = ((TextView) llItems.getChildAt(i)).getText().toString();
            llItems.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mHandleCallback != null) {
                        mHandleCallback.onHandle(model);
                    }
                    dismiss();
                }
            });
        }
    }


    @OnClick(R.id.tv_cancel)
    void onClickCancel() {
        dismiss();
    }

}
