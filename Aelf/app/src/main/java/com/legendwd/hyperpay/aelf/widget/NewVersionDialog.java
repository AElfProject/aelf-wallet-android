package com.legendwd.hyperpay.aelf.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;

/**
 * Created by LS on 2018/12/17.
 */

public class NewVersionDialog {
    public Dialog dialog;
    public EditText etInfo;
    public String is_force = "";
    Context context;
    DialogPositiveListener positiveListener;
    DialogNegativeListener negativeListener;
    DialogClickListener clickListener;

    public NewVersionDialog(Context context) {
        super();
        this.context = context;
    }

    public NewVersionDialog(Context context, String isForce) {
        super();
        this.context = context;
        this.is_force = isForce;
    }

    public void setPositiveListener(DialogPositiveListener positiveListener) {
        this.positiveListener = positiveListener;
    }

    public void setNegativeListener(DialogNegativeListener negativeListener) {
        this.negativeListener = negativeListener;
    }

    public void setClickListener(DialogClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /**
     *
     * */
    public Dialog initDialog(String content) {
        return initDialog(content, "");
    }

    /**
     * content = 更新内容,verNo = 版本号
     */
    public Dialog initDialog(String content, String verNo) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.layout_new_update_dialog, null);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog = ResultDialog.creatAlertDialog(context, view);
        dialog.setCancelable(false);

        TextView tvInfo = view.findViewById(R.id.tv_info);
        tvInfo.setText(content);
        TextView tvVerNo = view.findViewById(R.id.tv_version);
        if (verNo != null && !verNo.isEmpty()) {
            tvVerNo.setText(verNo);
        }


        LinearLayout btnCancle = (LinearLayout) view.findViewById(R.id.cancel);
        LinearLayout btnSure = (LinearLayout) view.findViewById(R.id.sure);

        if (is_force != null) {
            if (!is_force.isEmpty()) {
                if (is_force.equals("1")) {
                    btnCancle.setVisibility(View.GONE);
                    btnSure.setVisibility(View.VISIBLE);
                    btnSure.setBackground(context.getResources().getDrawable(R.drawable.new_version_right_1));
                } else {
                    btnCancle.setVisibility(View.VISIBLE);
                    btnSure.setVisibility(View.VISIBLE);
                    btnSure.setBackground(context.getResources().getDrawable(R.drawable.new_version_right));
                }
            }
        }

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (negativeListener != null) {
                    negativeListener.onClick();
                }

                dialog.dismiss();
            }
        });
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (positiveListener != null) {
                    positiveListener.onClick();
                }
            }
        });
        return dialog;
    }

    public interface DialogPositiveListener {
        void onClick();
    }

    public interface DialogNegativeListener {
        void onClick();
    }

    public interface DialogClickListener {
        void onClick();
    }
}
