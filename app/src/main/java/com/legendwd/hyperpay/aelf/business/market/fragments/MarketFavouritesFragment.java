package com.legendwd.hyperpay.aelf.business.market.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseAdapterModel;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.market.MarketAdapter;
import com.legendwd.hyperpay.aelf.model.MessageEvent;
import com.legendwd.hyperpay.aelf.model.bean.MarketDataBean;
import com.legendwd.hyperpay.aelf.model.param.MarketParam;
import com.legendwd.hyperpay.aelf.presenters.IMarketPresenter;
import com.legendwd.hyperpay.aelf.presenters.impl.MarketPresenter;
import com.legendwd.hyperpay.aelf.util.FavouritesUtils;
import com.legendwd.hyperpay.aelf.views.IMarketView;
import com.legendwd.hyperpay.lib.CacheUtil;
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
    private List<MarketDataBean> mDataList = new ArrayList<>();
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
                int index = (Integer) o;
                if (index >= mDataList.size()) {
                    return;
                }
                MarketDataBean bean = mDataList.get((Integer) o);
                bundle.putSerializable("bean", bean);
                bundle.putString("name", bean.getId());
                bundle.putString("price", bean.getCurrentPrice() + "");
                bundle.putString("increase", bean.getPriceChangePercentage24h() + "");
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

    private void favouritesData(List<MarketDataBean> dataList) {
        ll_favourites.setVisibility(View.VISIBLE);
        ll_market_star.setVisibility(View.VISIBLE);
        if (dataList == null || dataList.size() == 0) {
            mDataList.clear();
            refresh.finishRefresh();
            MarketDataBean bean = new MarketDataBean();
            bean.setItemType(BaseAdapterModel.ItemType.EMPTY);
            mDataList.add(bean);
            mMarketAdapter.refreshView(mDataList);
            ll_favourites.setVisibility(View.GONE);
            ll_market_star.setVisibility(View.GONE);
        } else {
            getMyCoinList(dataList);
        }
    }

    private void getMyCoinList(List<MarketDataBean> dataList) {
        StringBuilder buffer = new StringBuilder();
        for (MarketDataBean bean : dataList) {
            buffer.append(bean.getId());
            buffer.append(",");
        }
        MarketParam param = new MarketParam();
        param.currency = CacheUtil.getInstance().getProperty(Constant.Sp.PRICING_CURRENCY_ID_DEFAULT, Constant.DEFAULT_CURRENCY);
        param.coinName = buffer.toString();
        mPresenter.getCoinList(param, "");
    }

    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);

        switch (event.getMessage()) {
            case Constant.Event.REFRSH_STAR:
                referData();
                break;
        }
    }

    @Override
    public void onCoinListSuccess(List<MarketDataBean> resultBean, String type) {
        refresh.finishRefresh();
        if (resultBean == null) {
            return;
        }
        mDataList.clear();
        mDataList.addAll(resultBean);
        mMarketAdapter.refreshView(mDataList);
    }

    @Override
    public void onAelfCoinListSuccess(List<MarketDataBean> resultBean, String type,String sort) {

    }

    @Override
    public void onCoinListError(int code, String msg, String type) {
        refresh.finishRefresh();
    }

    @Override
    public void onMyCoinListSuccess(List<MarketDataBean> resultBean) {
    }

    @Override
    public void onMyCoinListError(int i, String message) {
        refresh.finishRefresh();

    }
}
