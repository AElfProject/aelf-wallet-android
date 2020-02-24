package com.legendwd.hyperpay.aelf.business.wallet.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author Colin
 * @date 2019/8/2.
 * description：导入钱包 （恢复身份adapter）
 */
public class ImportWalletAdapter extends FragmentPagerAdapter {
    private List<Fragment> mDataList;

    public ImportWalletAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        mDataList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }
}
