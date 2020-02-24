package com.legendwd.hyperpay.aelf.business.wallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.my.fragments.UserAgreementFragment;
import com.legendwd.hyperpay.aelf.dialogfragments.ToastDialog;
import com.legendwd.hyperpay.aelf.model.bean.WalletBean;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.lib.Util;

import butterknife.BindView;
import butterknife.OnClick;

public class CreateAccountFragment extends BaseFragment {

    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_pwd)
    EditText et_pwd;
    @BindView(R.id.et_pwd_confirm)
    EditText et_pwd_confirm;
    @BindView(R.id.et_pwd_hint)
    EditText et_pwd_hint;
    @BindView(R.id.iv_eyes_confirm)
    ImageView iv_eyes_confirm;
    @BindView(R.id.iv_eyes)
    ImageView iv_eyes;
    @BindView(R.id.chb_terms)
    CheckBox chb_terms;
    @BindView(R.id.tv_agree)
    TextView tv_agree;
    private boolean isOpenConfirm = false;
    private boolean isOpen = false;

    public static CreateAccountFragment newInstance() {
        return new CreateAccountFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbarNav(mToolbar, R.string.create_account, true);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_create_account;
    }

    @OnClick(R.id.tv_create)
    void onClickCreate() {

        if (isEditTextValid()) {
            createWallet();
        }
    }

    @OnClick(R.id.tv_agree)
    void onClickAgree() {
        start(UserAgreementFragment.newInstance());
    }

    private void createWallet() {
        hideSoftInput();
        WalletBean bean = new WalletBean();
        bean.password = et_pwd.getText().toString();
        bean.hint = et_pwd_hint.getText().toString();
        bean.name = et_name.getText().toString();
        bean.fromType="0";

        Bundle bundle = new Bundle();
        bundle.putString("bean", new Gson().toJson(bean));
        start(ImportingFragment.newInstance(bundle));

    }

    private boolean isEditTextValid() {
        String name = et_name.getText().toString();
        String pwd = et_pwd.getText().toString();
        String pwdConfirm = et_pwd_confirm.getText().toString();

        if (TextUtils.isEmpty(name)) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.please_enter_wallet_name));
            return false;
        }
        if (TextUtils.isEmpty(pwd)) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.e_password));
            return false;
        }

        if (pwd.length() < 12) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.pwd_len_least_12));
            return false;
        }

        if (!Util.isValidPwd(pwd)) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.pwd_rule));
            return false;
        }


        if (!TextUtils.equals(pwd, pwdConfirm)) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.password_not_match));
            return false;
        }

        if (!chb_terms.isChecked()) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.terms_agreement));
            return false;
        }

//        if (strengthView.getStrength() == PasswordStrengthView.Strength.WEEK
//                || strengthView.getStrength() == PasswordStrengthView.Strength.NORMAL ){
//            DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.password_not_match));
//            return false;
//        }

        return true;
    }

    @Override
    public void process() {
    }

    @OnClick({R.id.iv_eyes_confirm, R.id.iv_eyes})
    void onClickEyes(View view) {
        switch (view.getId()) {
            case R.id.iv_eyes_confirm:
                isOpenConfirm = !isOpenConfirm;
                iv_eyes_confirm.setImageResource(isOpenConfirm ? R.mipmap.eye_open : R.mipmap.eye_close);
                et_pwd_confirm.setInputType(isOpenConfirm ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        : (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD));
                break;

            case R.id.iv_eyes:
                isOpen = !isOpen;
                iv_eyes.setImageResource(isOpen ? R.mipmap.eye_open : R.mipmap.eye_close);
                et_pwd.setInputType(isOpen ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        : (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD));
                break;
        }
    }
}
