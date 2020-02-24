package com.legendwd.hyperpay.aelf.business.my.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.my.adapters.AssetDisplayAdapter;
import com.legendwd.hyperpay.aelf.listeners.OnItemClickListener;
import com.legendwd.hyperpay.aelf.model.AssetDisplayBean;
import com.legendwd.hyperpay.aelf.model.MessageEvent;
import com.legendwd.hyperpay.aelf.widget.LinearSpacingItemDecoration;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AssetDisplayFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    private AssetDisplayAdapter mAdapter;

    private List<AssetDisplayBean> mDataList = new ArrayList<>();

    public static AssetDisplayFragment newInstance() {
        Bundle args = new Bundle();
        AssetDisplayFragment tabFourFragment = new AssetDisplayFragment();
        tabFourFragment.setArguments(args);
        return tabFourFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initToolbarNav((Toolbar) view.findViewById(R.id.toolbar), R.string.asset_display, true);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_asset_display;
    }

    @Override
    public void process() {

        boolean bAsset = CacheUtil.getInstance().getProperty(Constant.Sp.ASSETS_DISPLAY_INT, 0) == 0;

        AssetDisplayBean bean = getAssetDisplayBean(0, getString(R.string.by_token), bAsset);
        AssetDisplayBean bean2 = getAssetDisplayBean(1, getString(R.string.by_chain), !bAsset);

        mDataList.add(bean);
        mDataList.add(bean2);

        mAdapter = new AssetDisplayAdapter(_mActivity, mDataList, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o) {
                if (o instanceof AssetDisplayBean) {
                    if (((AssetDisplayBean) o).isSelected) {
                        return;
                    }

                    CacheUtil.getInstance().setProperty(Constant.Sp.ASSETS_DISPLAY_INT, ((AssetDisplayBean) o).position);
                    refreshView(((AssetDisplayBean) o).position);
                    EventBus.getDefault().post(new MessageEvent(Constant.Event.ASSETS_DISPLAY));
                }
            }
        });

        recyclerView.addItemDecoration(new LinearSpacingItemDecoration(_mActivity,1, Color.parseColor("#E0E0E0")));
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        recyclerView.setAdapter(mAdapter);
    }

    private void refreshView(int postion) {
        if (postion == 0) {
            mDataList.get(0).isSelected = true;
            mDataList.get(1).isSelected = false;
        } else {
            mDataList.get(0).isSelected = false;
            mDataList.get(1).isSelected = true;
        }

        mAdapter.notifyDataSetChanged();
    }

    private AssetDisplayBean getAssetDisplayBean(int position, String s, boolean isSelected) {
        AssetDisplayBean bean = new AssetDisplayBean();
        bean.title = s;
        bean.isSelected = isSelected;
        bean.position = position;
        return bean;
    }


}
