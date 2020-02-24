package com.legendwd.hyperpay.aelf.business.my.fragments;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.listeners.OnTextCorrectCallback;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;

public class PwdHindFragment extends BaseFragment {

    @BindView(R.id.tv_title_right)
    TextView mTitleRight;
    @BindView(R.id.et_hint)
    EditText mEtHint;

    public static SupportFragment newInstance() {
        return new PwdHindFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_pwd_hint;
    }

    @Override
    public void process() {

        initToolbarNav(mToolbar, R.string.password_hint, true);
        mTitleRight.setText(R.string.done);

        mEtHint.setText(CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_HINT));
    }

    @OnClick(R.id.tv_title_right)
    void onClickRight() {

        String property = CacheUtil.getInstance().getProperty(Constant.Sp.is_KeyStore);
        if (TextUtils.isEmpty(property)) {
            showPasswordDialogForMnemonic(obj -> {
                CacheUtil.getInstance().setProperty(Constant.Sp.WALLET_HINT, mEtHint.getText().toString().trim());
                pop();
            });
        } else {
            showPasswordDialogForPrivate(obj -> {
                CacheUtil.getInstance().setProperty(Constant.Sp.WALLET_HINT, mEtHint.getText().toString().trim());
                pop();
            });
        }


    }

}
