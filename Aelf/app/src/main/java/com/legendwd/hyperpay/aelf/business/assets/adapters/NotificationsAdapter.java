package com.legendwd.hyperpay.aelf.business.assets.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


/**
 * Created by YoKeyword on 16/6/5.
 */
public class NotificationsAdapter extends FragmentPagerAdapter {
    private List<Fragment> mDataList;

    public NotificationsAdapter(FragmentManager fm, List<Fragment> fragmentList) {
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
