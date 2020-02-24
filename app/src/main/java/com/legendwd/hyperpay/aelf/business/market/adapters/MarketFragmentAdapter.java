package com.legendwd.hyperpay.aelf.business.market.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author Colin
 * @date 2019/8/2.
 * description：market 市场adapter
 */
public class MarketFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mDataList;

    public MarketFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
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
