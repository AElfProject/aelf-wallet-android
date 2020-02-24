package com.legendwd.hyperpay.aelf.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.assets.fragments.AssetsFragment;
import com.legendwd.hyperpay.aelf.business.discover.DiscoverFragment;
import com.legendwd.hyperpay.aelf.business.market.MarketFragment;
import com.legendwd.hyperpay.aelf.business.my.fragments.MyFragment;
import com.legendwd.hyperpay.aelf.business.wallet.fragments.MnemonicFragment;
import com.legendwd.hyperpay.aelf.dialogfragments.BackupDialog;
import com.legendwd.hyperpay.aelf.listeners.HandleCallback;
import com.legendwd.hyperpay.aelf.listeners.OnTextCorrectCallback;
import com.legendwd.hyperpay.aelf.model.bean.WalletBean;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import me.yokeyword.fragmentation.SupportFragment;

public class MainFragment extends BaseFragment {
    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOUR = 3;
    LinearLayout mTabs;
    private SupportFragment[] mFragments = new SupportFragment[4];
    private int mPrePos = 0;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void process() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTabs = getActivity().findViewById(R.id.tab_layout);
        SupportFragment firstFragment = findChildFragment(AssetsFragment.class);
        if (firstFragment == null) {
            mFragments[FIRST] = AssetsFragment.newInstance();
            mFragments[SECOND] = MarketFragment.newInstance();
            mFragments[THIRD] = DiscoverFragment.newInstance();
            mFragments[FOUR] = MyFragment.newInstance();
            loadMultipleRootFragment(R.id.fl_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOUR]);
        } else {
            mPrePos = 3;
            mFragments[FIRST] = firstFragment;
            mFragments[SECOND] = findChildFragment(MarketFragment.class);
            mFragments[THIRD] = findChildFragment(DiscoverFragment.class);
            mFragments[FOUR] = findChildFragment(MyFragment.class);
        }


        for (int i = 0; i < mTabs.getChildCount(); i++) {
            final int finalI = i;

            mTabs.getChildAt(i).setOnClickListener(v -> {
                if (finalI == mPrePos) {
                    return;
                }
                showHideFragment(mFragments[finalI], mFragments[mPrePos]);
                mPrePos = finalI;
                setBottomBarSelect(mPrePos);
            });


        }
        setBottomBarSelect(mPrePos);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setBottomBarSelect(int position) {
        for (int i = 0; i < mTabs.getChildCount(); i++) {
            ((RelativeLayout) mTabs.getChildAt(i)).getChildAt(0).setSelected(false);
        }
        ((RelativeLayout) mTabs.getChildAt(position)).getChildAt(0).setSelected(true);
        if(!CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_BACKUP, false)) {
            DialogUtils.showDialog(BackupDialog.class, getFragmentManager()).setHandleCallback(new HandleCallback() {
                @Override
                public void onHandle(Object o) {
                            showPasswordDialogForMnemonic(new OnTextCorrectCallback() {
                                @Override
                                public void onTextCorrect(Object... obj) {
                                    String key = (String) obj[1];
                                    backup(key);
                                }
                            });
                }
            });
        }
    }

    private void backup(String key) {
        WalletBean bean = new WalletBean();
        bean.mnemonic = key;
        Bundle bundle = new Bundle();
        bundle.putString("bean", new Gson().toJson(bean));
        start(MnemonicFragment.newInstance(bundle));
    }

    @Override
    protected boolean enableSwipeBack() {
        return false;
    }

    @Override
    public void startBrotherFragment(SupportFragment targetFragment) {
        start(targetFragment);
    }


}
