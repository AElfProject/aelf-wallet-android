/*
 * **************************************************************************************
 *  Copyright © 2014-2018 Ontology Foundation Ltd.
 *  All rights reserved.
 *
 *  This software is supplied only under the terms of a license agreement,
 *  nondisclosure agreement or other written agreement with Ontology Foundation Ltd.
 *  Use, redistribution or other disclosure of any parts of this
 *  software is prohibited except in accordance with the terms of such written
 *  agreement with Ontology Foundation Ltd. This software is confidential
 *  and proprietary information of Ontology Foundation Ltd.
 *
 * **************************************************************************************
 */

package com.github.ont.connector.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ont.connector.R;


/**
 * Created by liutao17 on 2018/3/26.
 */

public class PasswordDialog extends Dialog implements View.OnClickListener {
    private TextView title;
    private TextView to;
    private TextView from;
    private TextView fees;
    private TextView tv_ont;
    private TextView confirm;
    private ImageView cancel;
    private boolean isEnterPassword = false;
    private ConfirmListener listener;
    private View addressConfirm;
    private View pwdConfirm;
    private TextView tvPwdConfirm;
    private TextView tvDes;

    //    private PasswordEditText editText;
    private EditText editText;
    private Context context;

    public PasswordDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    public PasswordDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        init();
    }

    protected PasswordDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public PasswordDialog setTo(String toAddress) {
        if (null != to && !TextUtils.isEmpty(toAddress)) {
            to.setText(toAddress);
        }
        return this;
    }

    public PasswordDialog setFrom(String fromAddress) {
        if (null != from && !TextUtils.isEmpty(fromAddress)) {
            from.setText(fromAddress);
        }
        return this;
    }

    public PasswordDialog setFees(String feesAcount) {
        if (null != fees && !TextUtils.isEmpty(feesAcount)) {
            fees.setText(feesAcount);
        }
        return this;
    }

    public PasswordDialog setAmount(String Acount) {
        if (null != tv_ont && !TextUtils.isEmpty(Acount)) {
            tv_ont.setText(Acount);
        }
        return this;
    }

    public PasswordDialog setConfirmListener(ConfirmListener listener) {
        if (null != listener) {
            this.listener = listener;
        }
        return this;
    }

    public PasswordDialog setDes(String des) {
        if (null != tvDes && !TextUtils.isEmpty(des)) {
            tvDes.setText(des);
        }
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_cyano_password, null);
        setContentView(view);
        title = (TextView) view.findViewById(R.id.title);
        to = (TextView) view.findViewById(R.id.to_address);
        from = (TextView) view.findViewById(R.id.from_address);
        fees = (TextView) view.findViewById(R.id.account_fees);
        tv_ont = (TextView) view.findViewById(R.id.tv_ont);
        confirm = (TextView) view.findViewById(R.id.confirm);
        tvDes = (TextView) view.findViewById(R.id.tv_des);

        tvPwdConfirm = (TextView) view.findViewById(R.id.confirm_pwd);

        addressConfirm = view.findViewById(R.id.address_confirm);
        pwdConfirm = view.findViewById(R.id.password_confirm);

        editText = view.findViewById(R.id.et_password);

        addressConfirm.setVisibility(View.GONE);
        pwdConfirm.setVisibility(View.VISIBLE);

        tvPwdConfirm.setOnClickListener(this);
        confirm.setOnClickListener(this);

        Window window = getWindow();
        window.setWindowAnimations(R.style.AnimationBottomFade);
//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        attributes.gravity = Gravity.BOTTOM;
        // 一定要重新设置, 才能生效
        window.setAttributes(attributes);
        window.setBackgroundDrawableResource(R.color.colorWhite);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.confirm) {
            confirm();

        } else if (id == R.id.confirm_pwd) {
            if (null != listener) {
                listener.passwordConfirm(editText.getText().toString());
            }

        } else {
        }
    }

    private void confirm() {
        isEnterPassword = true;
        addressConfirm.setVisibility(View.GONE);
        pwdConfirm.setVisibility(View.VISIBLE);
    }


    public interface ConfirmListener {
        void passwordConfirm(String password);
    }

}
