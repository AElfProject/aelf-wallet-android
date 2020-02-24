package com.legendwd.hyperpay.aelf.business.assets.fragments;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.assets.adapters.AssetsViewPagerAdapter;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AssetsManagementFragment extends BaseFragment {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tb_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private String[] mTabTitles;

    public static AssetsManagementFragment newInstance() {
        return new AssetsManagementFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_assets_magagement;
    }

    @Override
    public void process() {
        initToolbarNav(mToolbar, "", true);

        tv_title.setText(getString(R.string.asset));
        mTabTitles = new String[]{getString(R.string.add_assets), getString(R.string.edit_assets)};
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        List<Fragment> list = new ArrayList<>();
        list.add(AddAssetsChildFragment.newInstance());
        list.add(EditAssetsChildFragment.newInstance());
        AssetsViewPagerAdapter assetsViewPagerAdapter = new AssetsViewPagerAdapter(getChildFragmentManager(), list, mTabTitles);
        viewPager.setAdapter(assetsViewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                ((BaseFragment)list.get(i)).process();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);
    }

}
