package com.legendwd.hyperpay.aelf.business.wallet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.wallet.adapters.ImportWalletAdapter;
import com.legendwd.hyperpay.aelf.dialogfragments.ToastDialog;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.lib.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 账号导入
 */
public class ImportWalletFragment extends BaseFragment {

    @BindView(R.id.tv_title_right)
    TextView titleRight;
    @BindView(R.id.wallet_tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.wallet_viewPager)
    ViewPager viewPager;
    private ImportWalletAdapter mAdapter;
    private TextView tv_title_right;
    private WalletKeystoreFragment walletKeystoreFragment;
    private WalletMnemonicsFragment walletMnemonicsFragment;
    private WalletMnemonicsFragment walletKeyFragment;
    private boolean iskeyStore = true;

    public static ImportWalletFragment newInstance() {
        return new ImportWalletFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbarNav(mToolbar, R.string.createimport_import_wallet_advanced, true);
        String[] titles = new String[]{getString(R.string.keystore), getString(R.string.mnemonics), getString(R.string.private_keys)};
        List<Fragment> fragmentList = new ArrayList<>();
        walletKeystoreFragment = WalletKeystoreFragment.newInstance();
        walletMnemonicsFragment = WalletMnemonicsFragment.newInstance();
        walletKeyFragment = WalletMnemonicsFragment.newInstance();
        fragmentList.add(walletKeystoreFragment);
        fragmentList.add(walletMnemonicsFragment);
        fragmentList.add(walletKeyFragment);
        walletKeyFragment.setKeyType(1);
        viewPager.setOffscreenPageLimit(2);
        mAdapter = new ImportWalletAdapter(getChildFragmentManager(), fragmentList);
        viewPager.setAdapter(mAdapter);
        for (int x = 0; x < 3; x++) {
            TabLayout.Tab tab = mTabLayout.newTab();
            View inflate = View.inflate(_mActivity, R.layout.item_tab_notification, null);
            TextView textView = inflate.findViewById(R.id.tv_title);
            textView.setText(titles[x]);
            tab.setCustomView(inflate);
            mTabLayout.addTab(tab);
        }
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switchTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switchTab(tab.getPosition());
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setScrollPosition(position, 0f, true);
                switchTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        tv_title_right = view.findViewById(R.id.tv_title_right);
        tv_title_right.setVisibility(View.GONE);

    }

    /**
     * tab tag
     *
     * @param position
     */
    private void switchTab(int position) {
        if (position == 0) {
            iskeyStore = true;
        } else {
            iskeyStore = false;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_import_wallet;
    }

    @Override
    public void process() {

    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.RequestCode.CODE_SCAN_ZXING) {
                String scan = data.getStringExtra(Constant.IntentKey.Scan_Zxing);
                if (iskeyStore) {
                    if (TextUtils.isEmpty(scan)){
                        DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.scan_code_null));
                    }
                    walletKeystoreFragment.setEt_keystore(TextUtils.isEmpty(scan) ? "" : scan);
                } else {
                    walletMnemonicsFragment.setEt_mnemonic(TextUtils.isEmpty(scan) ? "" : scan);
                }

            }
        }
    }
}
