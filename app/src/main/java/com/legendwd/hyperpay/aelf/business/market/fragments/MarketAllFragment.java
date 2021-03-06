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

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseAdapterModel;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.market.adapters.MarketAllAdapter;
import com.legendwd.hyperpay.aelf.model.MessageEvent;
import com.legendwd.hyperpay.aelf.model.bean.MarketListBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.param.MarketParam;
import com.legendwd.hyperpay.aelf.presenters.impl.MarketPresenter;
import com.legendwd.hyperpay.aelf.views.IMarketView;
import com.legendwd.hyperpay.aelf.widget.ClassicsFooter;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * @author Colin
 * @date 2019/8/6.
 * description： 全部的市场展示
 */
public class MarketAllFragment extends BaseFragment implements IMarketView {
    private static final String TYPE_PULL_UP = "TYPE_PULL_UP";
    private static final String TYPE_PULL_DOWN = "TYPE_PULL_DOWN";
    private static int PAGE_COUNT = 10;
    @BindView(R.id.rv_all_market)
    RecyclerView mRvMarket;
    @BindView(R.id.all_refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.ll_sort_price)
    LinearLayout ll_sort_price;
    @BindView(R.id.ll_sort_change)
    LinearLayout ll_sort_change;
    @BindView(R.id.tv_sort_price)
    TextView tv_sort_price;
    @BindView(R.id.tv_sort_change)
    TextView tv_sort_change;
    private MarketPresenter mMarketPresenter;
    private List<MarketListBean.ListBean> mBeanList = new ArrayList<>();
    private MarketAllAdapter mMarketAdapter;
    private String mSortType = "-1";
    private int mPriceClickTimes;
    private int mChangeClickTimes;
    private int mCurrentPage = 1;

    public static MarketAllFragment newInstance() {
        Bundle args = new Bundle();
        MarketAllFragment tabTwoFragment = new MarketAllFragment();
        tabTwoFragment.setArguments(args);
        return tabTwoFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        //         return super.onCreateFragmentAnimation();
        return new FragmentAnimator();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_allmarket;
    }

    @Override
    public void process() {
        tv_sort_price.setTag(0);
        tv_sort_change.setTag(0);

        mMarketPresenter = new MarketPresenter(this);

        refresh.setEnableLoadMore(true);
        refresh.setRefreshFooter(new ClassicsFooter(_mActivity));

        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mCurrentPage = 1;
                refresh.setEnableLoadMore(true);
                getMarketList(mSortType);
            }
        });

        refresh.autoRefresh();

        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {

                mCurrentPage++;
                getMarketList(mSortType, TYPE_PULL_UP);
            }
        });
    }

    private void refreshView(int resId) {
        switch (resId) {
            case R.id.ll_sort_price:

                if (mChangeClickTimes != 0) {
                    mChangeClickTimes = 0;
                }

                mPriceClickTimes %= 3;

                break;

            case R.id.ll_sort_change:

                if (mPriceClickTimes != 0) {
                    mPriceClickTimes = 0;
                }

                mChangeClickTimes %= 3;
                break;
        }

        if (mPriceClickTimes == 1) {
            tv_sort_price.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.sort_down, 0);
        } else if (mPriceClickTimes == 2) {
            tv_sort_price.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.sort_up, 0);
        } else {
            tv_sort_price.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.sort_default, 0);
        }

        if (mChangeClickTimes == 1) {
            tv_sort_change.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.sort_down, 0);
        } else if (mChangeClickTimes == 2) {
            tv_sort_change.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.sort_up, 0);
        } else {
            tv_sort_change.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.sort_default, 0);
        }

        setSortType();
    }

    private void setSortType() {
        if (mPriceClickTimes == 0) {
            if (mChangeClickTimes == 0) {
                mSortType = "-1";
            } else if (mChangeClickTimes == 1) {
                mSortType = "3";
            } else {
                mSortType = "2";
            }
        }

        if (mChangeClickTimes == 0) {

            if (mPriceClickTimes == 0) {
                mSortType = "-1";
            } else if (mPriceClickTimes == 1) {
                mSortType = "1";
            } else {
                mSortType = "0";
            }
        }
        Log.d("mSortType===>", mSortType);
        getMarketList(mSortType);
    }

    @OnClick({R.id.ll_sort_price, R.id.ll_sort_change})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.ll_sort_price:
                mPriceClickTimes++;
                refreshView(view.getId());
                break;
            case R.id.ll_sort_change:
                mChangeClickTimes++;
                refreshView(view.getId());
                break;
        }
    }

    private void getMarketList(String sort) {
        getMarketList(sort, TYPE_PULL_DOWN);
    }

    private void getMarketList(String sort, String type) {
        MarketParam param = new MarketParam();
        param.currency = CacheUtil.getInstance().getProperty(Constant.Sp.PRICING_CURRENCY_ID_DEFAULT, Constant.DEFAULT_CURRENCY);
        param.sort = sort;
        param.time = "1";
        param.p = String.valueOf(mCurrentPage);
        mMarketPresenter.getCoinList(param, type);
    }

    @Override
    protected boolean enableSwipeBack() {
        return false;
    }

    @Override
    public void onCoinListSuccess(ResultBean<MarketListBean> resultBean, String type) {
        refresh.finishRefresh();
        refresh.finishLoadMore();

        if (resultBean == null || resultBean.getData() == null) {
            return;
        }

        List<MarketListBean.ListBean> list = resultBean.getData().getList();

        if (TYPE_PULL_UP.equals(type)) {
            if (list.size() < PAGE_COUNT) {
                refresh.setEnableLoadMore(false);
            }
            mBeanList.addAll(list);
        } else {

            if (mCurrentPage == 1) {
                PAGE_COUNT = list.size();
            }
            mBeanList.clear();
            mBeanList.addAll(list);
        }

        if (mBeanList.size() == 0) {
            MarketListBean.ListBean bean = new MarketListBean.ListBean();
            bean.setItemType(BaseAdapterModel.ItemType.EMPTY);
            mBeanList.add(bean);
        } else {
            if (mBeanList.get(0).getItemType() == BaseAdapterModel.ItemType.EMPTY) {
                mBeanList.remove(0);
            }
        }

        if (mMarketAdapter == null) {
            mRvMarket.setLayoutManager(new LinearLayoutManager(_mActivity));
            mMarketAdapter = new MarketAllAdapter(mBeanList);
            mRvMarket.setAdapter(mMarketAdapter);
            mMarketAdapter.setOnItemClickListener(o -> {
                if (o == null) {
                    refresh.autoRefresh();
                } else {
                    Bundle bundle = new Bundle();
                    MarketListBean.ListBean bean = mBeanList.get((Integer) o);
                    bundle.putSerializable("bean", bean);
                    bundle.putString("name", bean.getName());
                    bundle.putString("price", bean.getLast_price());
                    bundle.putString("increase", bean.getIncrease());
                    ((BaseFragment) getParentFragment()).startBrotherFragment(HomeMarketFragment.newInstance(bundle));
                }
            });
        } else {
            mMarketAdapter.refreshView(mBeanList);
        }
    }

    @Override
    public void onCoinListError(int code, String msg, String type) {
        refresh.finishRefresh();
        refresh.finishLoadMore();
    }

    @Override
    public void onMyCoinListSuccess(ResultBean<MarketListBean> marketListBeanResultBean) {

    }

    @Override
    public void onMyCoinListError(int i, String message) {

    }


    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);

        switch (event.getMessage()) {
            case Constant.Event.SET_LANGUAGE:
                if (null != refresh) {
                    refresh.autoRefresh();
                }
                break;

            case Constant.Event.PRICING_CURRENCY:
                if (null != refresh) {
                    refresh.autoRefresh();
                }

                break;
        }
    }
}
