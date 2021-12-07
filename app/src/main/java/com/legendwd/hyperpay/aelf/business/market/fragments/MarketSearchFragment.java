package com.legendwd.hyperpay.aelf.business.market.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseAdapterModel;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.market.adapters.MarketSearchAdapter;
import com.legendwd.hyperpay.aelf.db.MarketCoindb;
import com.legendwd.hyperpay.aelf.db.dao.MarketCoinDao;
import com.legendwd.hyperpay.aelf.listeners.OnStarClickListener;
import com.legendwd.hyperpay.aelf.model.MessageEvent;
import com.legendwd.hyperpay.aelf.model.bean.MarketDataBean;
import com.legendwd.hyperpay.aelf.model.param.MarketParam;
import com.legendwd.hyperpay.aelf.presenters.MarketSearchPresenter;
import com.legendwd.hyperpay.aelf.util.FavouritesUtils;
import com.legendwd.hyperpay.aelf.views.IMarketView;
import com.legendwd.hyperpay.aelf.widget.ClassicsFooter;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author Colin
 * @date 2019/8/7.
 * description： 市场搜索界面
 */
public class MarketSearchFragment extends BaseFragment implements IMarketView, OnStarClickListener {
    @BindView(R.id.market_et_search)
    EditText mEditText;
    @BindView(R.id.rv_market_search)
    RecyclerView rv_market_search;
    @BindView(R.id.refresh_market_search)
    SmartRefreshLayout refresh;
    @BindView(R.id.tx_cancel)
    TextView tx_cancel;
    private static final String TYPE_PULL_UP = "TYPE_PULL_UP";
    private static final String TYPE_PULL_DOWN = "TYPE_PULL_DOWN";
    private static int PAGE_COUNT = 10;
    private MarketSearchPresenter mMarketPresenter;
    private String mSortType = "-1";
    private int mCurrentPage = 1;
    private List<MarketDataBean> mBeanList = new ArrayList<>();
    private MarketSearchAdapter mMarketAdapter;
    protected List<MarketDataBean> mStarData;

    public static MarketSearchFragment newInstance() {
        Bundle bundle = new Bundle();
        MarketSearchFragment homeMarketFragment = new MarketSearchFragment();
        homeMarketFragment.setArguments(bundle);
        return homeMarketFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initToolbarNav(view.findViewById(R.id.toolbar), R.string.tab_market, true);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_market_search;
    }

    @Override
    public void process() {

        mMarketPresenter = new MarketSearchPresenter(this);

        refresh.setEnableLoadMore(true);
        refresh.setRefreshFooter(new ClassicsFooter(_mActivity));
        getMarketList(mSortType);
        refresh.autoRefresh();
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mCurrentPage = 1;
                refresh.setEnableLoadMore(true);
                getMarketList(mSortType);
            }
        });

        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                mCurrentPage++;
                getMarketList(mSortType, TYPE_PULL_UP);
            }
        });
        tx_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getMarketList(mSortType, TYPE_PULL_DOWN);
            }
        });

    }

    private void getMarketList(String sort) {
        getMarketList(sort, TYPE_PULL_DOWN);
    }

    private void getMarketList(String sort, String type) {
        String name = mEditText.getText().toString().trim();
        String coinId = "";
        if (!TextUtils.isEmpty(name)) {
            List<MarketCoindb> list = MarketCoinDao.queryList(name);
            for (MarketCoindb coindb : list) {
                coinId += coindb.getId() + ",";
            }
        }
        getMarketData(coinId, type);
    }

    private void getMarketData(String name, String type) {
        MarketParam param = new MarketParam();
        param.currency = CacheUtil.getInstance().getProperty(Constant.Sp.PRICING_CURRENCY_ID_DEFAULT, Constant.DEFAULT_CURRENCY);
        param.time = "1";
        param.p = String.valueOf(mCurrentPage);
        //关键字搜索
        param.coinName = name;
        mMarketPresenter.getCoinList(param, type);
    }

    @Override
    public void onCoinListSuccess(List<MarketDataBean> resultBean, String type) {
        refresh.finishRefresh();
        refresh.finishLoadMore();
        if (resultBean == null && mBeanList != null) {
            MarketDataBean bean = new MarketDataBean();
            bean.setItemType(BaseAdapterModel.ItemType.EMPTY);
            mBeanList.add(bean);
        } else {
            List<MarketDataBean> list = resultBean;

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
                MarketDataBean bean = new MarketDataBean();
                bean.setItemType(BaseAdapterModel.ItemType.EMPTY);
                mBeanList.add(bean);
            } else {
                if (mBeanList.get(0).getItemType() == BaseAdapterModel.ItemType.EMPTY) {
                    mBeanList.remove(0);
                }
            }
        }

        if (mMarketAdapter == null) {
            rv_market_search.setLayoutManager(new LinearLayoutManager(_mActivity));
            mMarketAdapter = new MarketSearchAdapter(mBeanList);
            rv_market_search.setAdapter(mMarketAdapter);
            mMarketAdapter.setOnStarClickListener(this);
            mMarketAdapter.setOnItemClickListener(o -> {
                hideSoftInput();
                if (o == null) {
                    refresh.autoRefresh();
                } else {
                    Bundle bundle = new Bundle();
                    MarketDataBean bean = mBeanList.get((Integer) o);
                    bundle.putSerializable("bean", bean);
                    bundle.putString("name", bean.getId());
                    bundle.putString("price", bean.getCurrentPrice() + "");
                    bundle.putString("increase", bean.getPriceChangePercentage24h() + "");
                    ((BaseFragment) getPreFragment()).startBrotherFragment(HomeMarketFragment.newInstance(bundle));
                }
            });
        } else {
            mMarketAdapter.refreshView(mBeanList);
        }
        checkStarList(mBeanList);
    }

    @Override
    public void onAelfCoinListSuccess(List<MarketDataBean> resultBean, String type, String sort) {

    }

    @Override
    public void onCoinListError(int code, String msg, String type) {
        refresh.finishRefresh();
        refresh.finishLoadMore();
    }

    @Override
    public void onMyCoinListSuccess(List<MarketDataBean> marketListBeanResultBean) {

    }

    @Override
    public void onMyCoinListError(int i, String message) {

    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().postSticky(new MessageEvent(Constant.Event.REFRSH_STAR));
    }

    /**
     * star 回传过来的
     *
     * @param bean
     * @param position
     */
    @Override
    public void onStarPosition(MarketDataBean bean, int position, View view) {
        try {
            List<MarketDataBean> listBeans = FavouritesUtils.getFavourites();
            if (!bean.isStar()) {
                ((ImageView) view).setImageResource(R.mipmap.favor_solid);
                listBeans.add(bean);
                FavouritesUtils.setFavourites(listBeans);
                bean.setStar(true);
            } else {
                ((ImageView) view).setImageResource(R.mipmap.favour_empty_state);
                for (int c = 0; c < listBeans.size(); c++) {
                    if (bean.getName().equals(listBeans.get(c).getName())) {
                        listBeans.remove(c);
                    }
                }
                FavouritesUtils.setFavourites(listBeans);
                bean.setStar(false);
            }
        } catch (Exception e) {
            Log.d("=====>", "errOn:StarPosition");
        }
    }


    /**
     * 检索search 搜索数据集
     *
     * @param netDataList
     */
    public void checkStarList(List<MarketDataBean> netDataList) {
        List<MarketDataBean> mLocalData = FavouritesUtils.getFavourites();
        /**
         * 本地网络数据对比
         */
        if (mLocalData != null && mLocalData.size() > 0) {
            for (int a = 0; a < mLocalData.size(); a++) { //本地name仓库
                String name = mLocalData.get(a).getName();
                for (int b = 0; b < netDataList.size(); b++) {
                    MarketDataBean listBean = netDataList.get(b);
                    String netName = listBean.getName();
                    if (netName.equals(name)) { //网络数据仓库
                        listBean.setStar(true);
                        b++;
                    }
                }
            }
        }
        mMarketAdapter.refreshView(netDataList);
    }

    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);

        switch (event.getMessage()) {
            case Constant.Event.REFRSH_STAR:
                if (mMarketAdapter == null || mMarketAdapter.getData() == null) {
                    return;
                }
                checkStarList(mMarketAdapter.getData());
                break;
        }
    }
}
