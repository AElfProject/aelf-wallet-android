package com.legendwd.hyperpay.aelf.business.wallet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.MainActivity;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import butterknife.BindView;
import butterknife.OnClick;

public class NotificationFragment extends BaseFragment {
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.img_back)
    ImageView img_back;

    public static NotificationFragment newInstance(Bundle bundle) {

        NotificationFragment fragment = new NotificationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbarNav(mToolbar, "", false);
    }

    @Override
    protected boolean enableSwipeBack() {
        Bundle bundle = getArguments();
        String from = bundle.getString(Constant.BundleKey.BACKUP);
        if (TextUtils.equals(from, Constant.BundleValue.CREATE_IMPORT_PAGE)) {
            return false;
        }
        return true;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_notification;
    }

    @Override
    public void process() {

        Bundle bundle = getArguments();
        String from = bundle.getString(Constant.BundleKey.BACKUP);
        if (TextUtils.equals(from, Constant.BundleValue.CREATE_IMPORT_PAGE)) {
            ((TextView) getView().findViewById(R.id.tv_title_right)).setText(R.string.later);
            img_back.setVisibility(View.GONE);
            CacheUtil.getInstance().setProperty(Constant.Sp.IS_WALLET_EXISTS, true);
        } else {
            tv_title_right.setVisibility(View.GONE);
            tv_title.setText(R.string.backup_wallet);
            img_back.setVisibility(View.VISIBLE);
        }

        tv_title_right.setOnClickListener(v -> {
            startActivity(new Intent(_mActivity, MainActivity.class));
            _mActivity.finish();

        });


    }


    @OnClick({R.id.tv_start, R.id.img_back})
    void onClickStart(View view) {
        switch (view.getId()) {
            case R.id.tv_start:
                start(MnemonicFragment.newInstance(getArguments()));
                break;

            case R.id.img_back:
                pop();
                break;
        }

    }


    @Override
    public boolean onBackPressedSupport() {
        Bundle bundle = getArguments();
        String from = bundle.getString(Constant.BundleKey.BACKUP);
        if (TextUtils.equals(from, Constant.BundleValue.CREATE_IMPORT_PAGE)) {
            return true;
        }
        return false;
    }
}
