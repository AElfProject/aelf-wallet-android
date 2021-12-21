package com.legendwd.hyperpay.aelf.business.wallet.fragments;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.legendwd.hyperpay.aelf.BuildConfig;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.my.fragments.UserAgreementFragment;
import com.legendwd.hyperpay.aelf.dialogfragments.ToastDialog;
import com.legendwd.hyperpay.aelf.model.bean.WalletBean;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.aelf.util.ShowMessage;
import com.legendwd.hyperpay.lib.Util;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Colin
 * @date 2019/8/2.
 * description： 助记词找回账户
 */
public class WalletMnemonicsFragment extends BaseFragment {
    @BindView(R.id.et_mnemonic)
    EditText et_mnemonic;

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
    private int mKeyType;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_wallet_mnemonics;
    }

    public static WalletMnemonicsFragment newInstance() {
        Bundle args = new Bundle();
        WalletMnemonicsFragment fragment = new WalletMnemonicsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setKeyType(int keyType) {
        this.mKeyType = keyType;
    }

    @Override
    public void process() {
        if (BuildConfig.DEBUG){
            et_pwd.setText("Aa111111111.");
            et_pwd_confirm.setText("Aa111111111.");
            et_mnemonic.setText("f341026dd7a2512aadb197303eea203043f8fe9180d459cf19590c54a0264a31");
        }
        if(mKeyType == 0) {
            et_mnemonic.setHint(R.string.import_wallet_hint_seperate_each_mnemonic);
        }else {
            et_mnemonic.setHint(R.string.private_key_input);
        }

    }

    @OnClick(R.id.tv_agree)
    void onClickAgree() {
        ((BaseFragment) getParentFragment()).start(UserAgreementFragment.newInstance());
    }

    private boolean isEditTextValid() {
        String mnemonic = et_mnemonic.getText().toString().trim();
        String pwd = et_pwd.getText().toString();
        String pwdConfirm = et_pwd_confirm.getText().toString();

        if (TextUtils.isEmpty(mnemonic)) {
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

        if(mKeyType == 0 && !checkMnemonic(mnemonic)) {
            return false;
        }

        if (!chb_terms.isChecked()) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.terms_agreement));
            return false;
        }

        return true;
    }

    private boolean checkMnemonic(String mnemonic) {
        if (mnemonic.split("\\s").length != 12) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.mnemonic_not_correct));
            return false;
        }
        boolean flag = true;
        String[] data = mnemonic.split("\\s");
        for (String s : data) {
            if (!isWord(s)) {
                flag = false;
                break;
            }
        }

        if (!flag) {
            ShowMessage.toastErrorMsg(getActivity(), getString(R.string.mnemonic_not_correct));
            return false;
        }
        return true;
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

    private boolean isWord(String mnemonic) {
        return Pattern.compile("[a-z]+").matcher(mnemonic).matches();
    }

    @OnClick(R.id.tv_import)
    void onClickImport() {

        if (isEditTextValid()) {
            importWallet();
        }
    }

    private void importWallet() {

        hideSoftInput();
        WalletBean bean = new WalletBean();
        bean.password = et_pwd.getText().toString();
        bean.hint = et_pwd_hint.getText().toString();
        String mnem = et_mnemonic.getText().toString().trim();
        bean.setKeyType(mKeyType);
        bean.fromType="1";
        if(mKeyType == 0) {
            bean.mnemonic = mnem;
        }else {
            bean.privateKey = mnem;
        }
        Bundle bundle = new Bundle();
        bundle.putString("bean", new Gson().toJson(bean));
        ((BaseFragment) getParentFragment()).start(ImportingFragment.newInstance(bundle));

    }

    /**
     * @param qCode
     */
    protected void setEt_mnemonic(String qCode) {
        et_mnemonic.setText(qCode);
    }
}
