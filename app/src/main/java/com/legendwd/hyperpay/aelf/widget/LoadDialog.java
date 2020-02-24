package com.legendwd.hyperpay.aelf.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.legendwd.hyperpay.aelf.R;

public class LoadDialog extends Dialog {
    private Button cancel;
    private Context context;
    private boolean canCancel = true;

    public LoadDialog(Context context) {
        super(context, ResourcesId.getResourcesId(context, "style",
                "load_dialog"));
        this.context = context;
        setCanceledOnTouchOutside(false);
    }

    public LoadDialog(Context context, boolean canCancel) {
        super(context, ResourcesId.getResourcesId(context, "style",
                "load_dialog"));
        this.context = context;
        this.canCancel = canCancel;
        setCanceledOnTouchOutside(false);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(ResourcesId.getResourcesId(getContext(), "layout",
                "layout_dialog_load"));
        cancel = findViewById(R.id.cancel);
        cancel.setText(canCancel ? context.getResources().getString(R.string.loading_cancel) : "");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canCancel) {
                    dismiss();
                }
            }
        });


    }

    @Override
    public void show() {

        setCancelable(canCancel);
        try {
            super.show();
        } catch (Exception e) {

        }

    }

    @Override
    public void dismiss() {

        try {
            super.dismiss();
        } catch (Exception e) {

        }
    }

    public interface OnCancelListener {
        public void onCancel();
    }
}
