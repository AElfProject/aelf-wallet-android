package com.legendwd.hyperpay.aelf.business.assets.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class AssetsViewPagerAdapter extends FragmentPagerAdapter {
    String[] mTitles;
    private List<Fragment> mDatas;

    public AssetsViewPagerAdapter(FragmentManager fm, List<Fragment> datas, String[] titles) {
        super(fm);
        mDatas = datas;
        mTitles = titles;
    }


    @Override
    public Fragment getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
