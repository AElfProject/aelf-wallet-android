package com.legendwd.hyperpay.aelf.business.market.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseAdapterModel;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.market.MarketAdapter;
import com.legendwd.hyperpay.aelf.httpservices.HttpService;
import com.legendwd.hyperpay.aelf.httpservices.ResponseTransformer;
import com.legendwd.hyperpay.aelf.model.MessageEvent;
import com.legendwd.hyperpay.aelf.model.bean.CoinDetailBean;
import com.legendwd.hyperpay.aelf.model.bean.MarketLineBean;
import com.legendwd.hyperpay.aelf.model.bean.MarketListBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.presenters.IMarketLinePresenter;
import com.legendwd.hyperpay.aelf.presenters.IMarketPresenter;
import com.legendwd.hyperpay.aelf.presenters.impl.MarketLinePresenter;
import com.legendwd.hyperpay.aelf.presenters.impl.MarketPresenter;
import com.legendwd.hyperpay.aelf.util.FavouritesUtils;
import com.legendwd.hyperpay.aelf.views.IMarketLineView;
import com.legendwd.hyperpay.aelf.views.IMarketView;
import com.legendwd.hyperpay.httputil.ServiceGenerator;
import com.legendwd.hyperpay.lib.Constant;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * 自选
 */
public class MarketFavouritesFragment extends BaseFragment implements IMarketView {

    private static final String TYPE_PULL_UP = "TYPE_PULL_UP";
    @BindView(R.id.rv_market)
    RecyclerView mRvMarket;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.tx_edit_favourites)
    TextView tx_edit_favourites;
    @BindView(R.id.ll_favourites)
    LinearLayout ll_favourites;
    @BindView(R.id.ll_market_star)
    LinearLayout ll_market_star;
    private MarketAdapter mMarketAdapter;
    private List<MarketListBean.ListBean> mDataList = new ArrayList<>();
    private IMarketPresenter mPresenter;


    public static MarketFavouritesFragment newInstance() {
        Bundle args = new Bundle();
        MarketFavouritesFragment tabTwoFragment = new MarketFavouritesFragment();
        tabTwoFragment.setArguments(args);
        return tabTwoFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new FragmentAnimator();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_favouritesmarket;
    }

    @Override
    public void process() {

        mPresenter = new MarketPresenter(this);
        tx_edit_favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseFragment) getParentFragment()).startBrotherFragment(ManageFavouritesFragment.newInstance());
            }
        });
        initAdapter();
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                referData();
            }
        });
        refresh.autoRefresh();
    }

    private void initAdapter() {
        mRvMarket.setLayoutManager(new LinearLayoutManager(_mActivity));
        mMarketAdapter = new MarketAdapter(mDataList);
        mRvMarket.setAdapter(mMarketAdapter);
        mMarketAdapter.setOnItemClickListener(o -> {
            if (o == null) {
                refresh.autoRefresh();
            } else {
                Bundle bundle = new Bundle();
                MarketListBean.ListBean bean = mDataList.get((Integer) o);
                bundle.putSerializable("bean", bean);
                bundle.putString("name", bean.getName());
                bundle.putString("price", bean.getLast_price());
                bundle.putString("increase", bean.getIncrease());
                ((BaseFragment) getParentFragment()).startBrotherFragment(HomeMarketFragment.newInstance(bundle));
            }
        });
    }


    @Override
    protected boolean enableSwipeBack() {
        return false;
    }


    /**
     * 刷新data
     */
    public void referData() {
        favouritesData(FavouritesUtils.getFavourites());
    }

    private void favouritesData(List<MarketListBean.ListBean> dataList) {
        ll_favourites.setVisibility(View.VISIBLE);
        ll_market_star.setVisibility(View.VISIBLE);
        mDataList.clear();
        if (dataList == null || dataList.size() == 0) {
            refresh.finishRefresh();
            MarketListBean.ListBean bean = new MarketListBean.ListBean();
            bean.setItemType(BaseAdapterModel.ItemType.EMPTY);
            mDataList.add(bean);
            ll_favourites.setVisibility(View.GONE);
            ll_market_star.setVisibility(View.GONE);
        }else {
            getMyCoinList(dataList);
        }
    }

    private void getMyCoinList(List<MarketListBean.ListBean> dataList) {
        StringBuilder buffer = new StringBuilder();
        for(MarketListBean.ListBean bean : dataList) {
            buffer.append(bean.getName().toLowerCase());
            buffer.append(",");
        }
        buffer.deleteCharAt(buffer.length()-1);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("customCoin", buffer.toString());
        mPresenter.getMyCoinList(jsonObject);
    }

    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);

        switch (event.getMessage()) {
            case Constant.Event.REFRSH_STAR:
//                Log.d("=====", "REFRSH_STAR:");
                referData();
                break;
        }
    }

    @Override
    public void onCoinListSuccess(ResultBean<MarketListBean> resultBean, String type) {

    }

    @Override
    public void onCoinListError(int code, String msg, String type) {

    }

    @Override
    public void onMyCoinListSuccess(ResultBean<MarketListBean> resultBean) {
        refresh.finishRefresh();
        if (resultBean == null || resultBean.getData() == null) {
            return;
        }
        if(resultBean.getStatus() == 200) {
            mDataList.clear();
            mDataList.addAll(resultBean.getData().getList());
            mMarketAdapter.refreshView(mDataList);
        }
    }

    @Override
    public void onMyCoinListError(int i, String message) {
        refresh.finishRefresh();

    }
}
