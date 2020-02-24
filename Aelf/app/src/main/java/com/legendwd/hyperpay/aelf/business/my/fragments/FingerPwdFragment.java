package com.legendwd.hyperpay.aelf.business.my.fragments;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.dialogfragments.ToastDialog;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import butterknife.BindView;

public class FingerPwdFragment extends BaseFragment {

    @BindView(R.id.tv_create)
    TextView mtvCreate;
    @BindView(R.id.et_name)
    EditText mEtName;

    public static FingerPwdFragment newInstance() {
        return new FingerPwdFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_password;
    }

    @Override
    public void process() {
        mtvCreate.setOnClickListener(v -> {

            String pwd = mEtName.getText().toString();

            if (TextUtils.isEmpty(pwd)) {
//                DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.pwd_not_correct));
                return;
            }

            String data = CacheUtil.getInstance().getProperty(pwd + "private");
            if (TextUtils.isEmpty(data)) {
                DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.pwd_not_correct));
            } else {
                CacheUtil.getInstance().setProperty(Constant.Sp.TOUCH_ID_FAILED, false);
                _mActivity.finish();
            }

//            String crypt = RSAUtil.fileDecrypt(_mActivity.getFilesDir() + Constant.PRIVATE_KEYSTORE, data);
//            if (!TextUtils.equals(pwd, crypt)) {
//                DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.pwd_not_correct));
//            }else {
//                CacheUtil.getInstance().setProperty(Constant.Sp.TOUCH_ID_FAILED, false);
//                _mActivity.finish();
//            }

        });


    }


    @Override
    protected boolean enableSwipeBack() {
        return false;
    }

    @Override
    public boolean onBackPressedSupport() {
        return true;
    }
}
