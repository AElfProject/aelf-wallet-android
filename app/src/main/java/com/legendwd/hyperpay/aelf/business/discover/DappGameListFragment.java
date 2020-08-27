package com.legendwd.hyperpay.aelf.business.discover;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.wallet.adapters.TransferReceivePagerAdapter;
import com.legendwd.hyperpay.lib.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author Colin
 * @date 2019/10/10.
 * description：
 */
public class DappGameListFragment extends BaseFragment {
    @BindView(R.id.img_dapp_bell)
    ImageView img_dapp_bell;
    @BindView(R.id.tv_dapp_title)
    TextView tv_dapp_title;
    @BindView(R.id.iv_dapp_search)
    ImageView iv_dapp_search;
    @BindView(R.id.tb_dapp_layout)
    TabLayout tb_dapp_layout;
    @BindView(R.id.view_dapp_pager)
    ViewPager view_dapp_pager;
    private String[] mTabTitles;


    public static DappGameListFragment newInstance(Bundle bundle) {
        DappGameListFragment chooseChainFragment = new DappGameListFragment();
        chooseChainFragment.setArguments(bundle);
        return chooseChainFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_dapp_gamelist;
    }

    @Override
    public void process() {
        String title = getArguments().getString(Constant.BundleKey.DAPP_GROUP_NAME);
        String dappCat = getArguments().getString(Constant.BundleKey.DAPP_GROUP_CAT);
        mTabTitles = new String[]{getString(R.string.all), title};
        tb_dapp_layout.addTab(tb_dapp_layout.newTab());
        tb_dapp_layout.addTab(tb_dapp_layout.newTab());
        List<Fragment> list = new ArrayList<>();
        list.add(DappGamesFragment.newInstance());
        list.add(DappGamesFragment.newInstance());
        TransferReceivePagerAdapter transferReceivePagerAdapter = new TransferReceivePagerAdapter(getChildFragmentManager(), list, mTabTitles);
        view_dapp_pager.setAdapter(transferReceivePagerAdapter);
        tb_dapp_layout.setupWithViewPager(view_dapp_pager);
        tb_dapp_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                DappGamesFragment dappGamesFragment = (DappGamesFragment) list.get(tab.getPosition());
                if (dappGamesFragment != null) {
                    String cat = dappCat;
                    if (tab.getPosition() == 0) {
                        cat = "0";
                    }
                    dappGamesFragment.refreshData(cat, "TYPE_PULL_DOWN");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        iv_dapp_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseFragment) getPreFragment()).startBrotherFragment(DappSearchFragment.newInstance());
            }
        });
        img_dapp_bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
    }


}
