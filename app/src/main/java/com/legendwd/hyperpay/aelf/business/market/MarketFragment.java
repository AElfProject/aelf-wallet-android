package com.legendwd.hyperpay.aelf.business.market;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.market.adapters.MarketFragmentAdapter;
import com.legendwd.hyperpay.aelf.business.market.fragments.MarketAllFragment;
import com.legendwd.hyperpay.aelf.business.market.fragments.MarketFavouritesFragment;
import com.legendwd.hyperpay.aelf.business.market.fragments.MarketSearchFragment;
import com.legendwd.hyperpay.aelf.db.MarketCoindb;
import com.legendwd.hyperpay.aelf.db.dao.MarketCoinDao;
import com.legendwd.hyperpay.aelf.model.MessageEvent;
import com.legendwd.hyperpay.aelf.presenters.IMarketHomePresenter;
import com.legendwd.hyperpay.aelf.presenters.impl.MarketHomePresenter;
import com.legendwd.hyperpay.aelf.views.IMarketHomeView;
import com.legendwd.hyperpay.lib.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * 市场Fragment
 */
public class MarketFragment extends BaseFragment implements IMarketHomeView {

    @BindView(R.id.tv_title_right)
    TextView titleRight;
    @BindView(R.id.market_viewPager)
    ViewPager mViewPager;
    @BindView(R.id.market_tabLayout)
    TabLayout mTabLayout;
    private MarketFavouritesFragment mMarketFavouritesFragment;
    private MarketAllFragment mMarketAllFragment;
    private MarketFragmentAdapter mMarketFragmentAdapter;


    public static MarketFragment newInstance() {
        Bundle args = new Bundle();
        MarketFragment tabTwoFragment = new MarketFragment();
        tabTwoFragment.setArguments(args);
        return tabTwoFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbarNav(view.findViewById(R.id.toolbar), R.string.tab_market, false);
        String[] titles = new String[]{getString(R.string.favourites), getString(R.string.all)};
        List<Fragment> fragmentList = new ArrayList<>();
        mMarketFavouritesFragment = MarketFavouritesFragment.newInstance();
        mMarketAllFragment = MarketAllFragment.newInstance();
        fragmentList.add(mMarketFavouritesFragment);
        fragmentList.add(mMarketAllFragment);
        mViewPager.setOffscreenPageLimit(2);
        mMarketFragmentAdapter = new MarketFragmentAdapter(getChildFragmentManager(), fragmentList);
        mViewPager.setAdapter(mMarketFragmentAdapter);
        for (int x = 0; x < 2; x++) {
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
                mViewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    mMarketFavouritesFragment.referData();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setScrollPosition(position, 0f, true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        //         return super.onCreateFragmentAnimation();
        return new FragmentAnimator();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_market;
    }

    @Override
    public void process() {
        titleRight.setText("");
        titleRight.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.search, 0);
        titleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBrotherFragment(MarketSearchFragment.newInstance());
            }
        });
        IMarketHomePresenter presenter = new MarketHomePresenter(this);
        presenter.getMarketCoinList();
    }


    @Override
    protected boolean enableSwipeBack() {
        return false;
    }

    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);

        switch (event.getMessage()) {
            case Constant.Event.HOME_SEARCH:
//                Log.d("=====", "REFRSH_STAR:");
                startBrotherFragment(MarketSearchFragment.newInstance());
                break;
        }
    }

    @Override
    public void onCoinListSuccess(List<MarketCoindb> list) {
        MarketCoinDao.deleteAll();
        MarketCoinDao.save(list);
    }

    @Override
    public void onCoinListError(int code, String msg) {

    }
}
