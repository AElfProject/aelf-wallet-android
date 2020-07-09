package com.legendwd.hyperpay.aelf.business.market.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.model.MessageEvent;
import com.legendwd.hyperpay.aelf.model.bean.MarketDataBean;
import com.legendwd.hyperpay.aelf.util.FavouritesUtils;
import com.legendwd.hyperpay.aelf.util.JsonUtils;
import com.legendwd.hyperpay.aelf.widget.draglist.ManageMarketAdapter;
import com.legendwd.hyperpay.aelf.widget.draglist.MyItemTouchHelperCallback;
import com.legendwd.hyperpay.lib.Constant;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

/**
 * @author Colin
 * @date 2019/8/6.
 * description：收藏管理
 */
public class ManageFavouritesFragment extends BaseFragment{
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private ManageMarketAdapter mManageMarketAdapter;
    private ItemTouchHelper itemTouchHelper;

    public static ManageFavouritesFragment newInstance() {
        Bundle args = new Bundle();
        ManageFavouritesFragment manageFavouritesFragment = new ManageFavouritesFragment();
        manageFavouritesFragment.setArguments(args);
        return manageFavouritesFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbarNav(view.findViewById(R.id.toolbar), R.string.manage_favourite, true);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_managefavourites;
    }

    @Override
    public void process() {
        List<MarketDataBean> listBeans = FavouritesUtils.getFavourites();
        Log.d("====>", JsonUtils.objToJson(listBeans));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mManageMarketAdapter = new ManageMarketAdapter(listBeans);
        if (listBeans != null) {
            mRecyclerView.setAdapter(mManageMarketAdapter);
        }
        //条目触目帮助类
        ItemTouchHelper.Callback callback = new MyItemTouchHelperCallback(mManageMarketAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mManageMarketAdapter != null) {
            List<MarketDataBean> mManageMarketAdapterListBeans = mManageMarketAdapter.getListBeans();
            FavouritesUtils.setFavourites(mManageMarketAdapterListBeans);
            EventBus.getDefault().postSticky(new MessageEvent(Constant.Event.REFRSH_STAR));
        }
    }
}
