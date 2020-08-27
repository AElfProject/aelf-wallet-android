package com.legendwd.hyperpay.aelf.business.discover;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.discover.adapter.GamesChildAdapter;
import com.legendwd.hyperpay.aelf.business.discover.dapp.Dapp;
import com.legendwd.hyperpay.aelf.business.discover.dapp.GameListBean;
import com.legendwd.hyperpay.aelf.listeners.OnItemClickListener;
import com.legendwd.hyperpay.aelf.model.MessageEvent;
import com.legendwd.hyperpay.aelf.model.bean.ChooseChainsBean;
import com.legendwd.hyperpay.aelf.model.bean.DappListBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.presenters.impl.DiscoveryPresenter;
import com.legendwd.hyperpay.aelf.views.IDiscoveryView;
import com.legendwd.hyperpay.aelf.widget.LinearSpacingItemDecoration;
import com.legendwd.hyperpay.lib.Constant;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DappGamesFragment extends BaseFragment implements IDiscoveryView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private GamesChildAdapter mGamesChildAdapter;
    private DiscoveryPresenter mDiscoveryPresenter;

    private ArrayList<Dapp> mDataList = new ArrayList<>();
    private static int PAGE_COUNT = 10;
    public int mCurrentPage = 1;
    public String mCat = "0";
    private static final String TYPE_PULL_UP = "TYPE_PULL_UP";
    private static final String TYPE_PULL_DOWN = "TYPE_PULL_DOWN";

    public static DappGamesFragment newInstance() {
        return new DappGamesFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_child_transfer;
    }

    @Override
    public void process() {

        mDiscoveryPresenter = new DiscoveryPresenter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        recyclerView.addItemDecoration(new LinearSpacingItemDecoration(_mActivity, 1, Color.parseColor("#E0E0E0")));

        refresh.setOnRefreshListener(refreshLayout -> {

            refresh.setEnableLoadMore(true);
            refreshData(TYPE_PULL_DOWN);
        });
        refresh.setOnLoadMoreListener(refreshLayout -> {
            refreshData(TYPE_PULL_UP);
        });
    }

    /**
     * 刷新数据集
     *
     * @param cat 分类1=>"游戏",2=>"交易",3=>“工具”，4=>"其他"
     */
    public void refreshData(String cat, String refreshType) {
        String index = "0";
        if ("-1".equals(cat)) {
            mCat = "0";
            index = "1";
        } else {
            mCat = cat;
        }
        if (refreshType.equalsIgnoreCase(TYPE_PULL_DOWN)) {
            mCurrentPage = 1;
        } else if (refreshType.equalsIgnoreCase(TYPE_PULL_UP)) {
            mCurrentPage++;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("p", mCurrentPage + "");
        jsonObject.addProperty("cat", mCat);
        jsonObject.addProperty("isindex", index);
        mDiscoveryPresenter.getGameList(jsonObject, refreshType);
    }

    /**
     * 刷新数据集
     */
    public void refreshData(String refreshType) {
        refreshData(mCat, refreshType);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        refresh.autoRefresh();
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    @Override
    protected boolean enableSwipeBack() {
        return false;
    }

    private void refreshView(ArrayList<Dapp> dapps, String refreshType) {

        if (dapps == null) {
            return;
        }
        if (TYPE_PULL_UP.equals(refreshType)) {
            if (dapps.size() < PAGE_COUNT) {
                refresh.setEnableLoadMore(false);
            }
            mDataList.addAll(dapps);
        } else {
            if (mCurrentPage == 1) {
                PAGE_COUNT = dapps.size();
            }
            mDataList.clear();
            mDataList.addAll(dapps);
        }


        if (null == mGamesChildAdapter) {
            mGamesChildAdapter = new GamesChildAdapter(this, dapps, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o) {
                    refresh.autoRefresh();
                    refreshData(mCat, refreshType);
                }
            });
            recyclerView.setAdapter(mGamesChildAdapter);
        } else {
            mGamesChildAdapter.refreshView(mDataList);
        }

    }


    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        if (event.getMessage() == Constant.Event.REFRSH_TRANSATION) {
            refresh.postDelayed(() -> refresh.autoRefresh(), 1500);

        }
    }


    @Override
    public void onGameListSuccess(GameListBean gameListBean, String refreshType) {
        if (null != refresh) {
            refresh.finishRefresh();
            refresh.finishLoadMore();
        }
        refreshView(gameListBean.dapps, refreshType);
    }

    @Override
    public void onGameListError(int code, String msg) {
        if (null != refresh) {
            refresh.finishRefresh();
            refresh.finishLoadMore();
        }
//        DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
//                .setToast(msg);
    }

    @Override
    public void onApiSuccessForDapp(String id, String json) {

    }

    @Override
    public void onApiErrorForDapp(String id, int code, String msg) {

    }


    @Override
    public void onChainsSuccess(ResultBean<List<ChooseChainsBean>> resultBean) {

    }

    @Override
    public void onChainsError(int code, String msg) {

    }

    @Override
    public void onDappSuccess(DappListBean discoveryBean) {

    }

    @Override
    public void onDappError(int code, String msg) {

    }
}
