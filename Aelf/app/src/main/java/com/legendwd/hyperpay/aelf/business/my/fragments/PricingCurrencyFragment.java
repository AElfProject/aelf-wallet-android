package com.legendwd.hyperpay.aelf.business.my.fragments;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.my.adapters.PricingCurrencyAdapter;
import com.legendwd.hyperpay.aelf.model.MessageEvent;
import com.legendwd.hyperpay.aelf.model.bean.CurrenciesBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.param.BaseParam;
import com.legendwd.hyperpay.aelf.presenters.IPricingCurrencyPresenter;
import com.legendwd.hyperpay.aelf.presenters.impl.PricingCurrencyPresenter;
import com.legendwd.hyperpay.aelf.util.ScreenUtils;
import com.legendwd.hyperpay.aelf.views.IPricingCurrencyView;
import com.legendwd.hyperpay.aelf.widget.LinearSpacingItemDecoration;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.yokeyword.fragmentation.SupportFragment;

public class PricingCurrencyFragment extends BaseFragment implements IPricingCurrencyView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    List<CurrenciesBean.ListBean> mDataList = new ArrayList<>();
    private PricingCurrencyAdapter mAdapter;
    private IPricingCurrencyPresenter presenter;

    public static SupportFragment newInstance() {
        return new PricingCurrencyFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_pricing_currency;
    }

    @Override
    public void process() {
        presenter = new PricingCurrencyPresenter(this);

        initToolbarNav(mToolbar, R.string.pricing_currency, true);

        presenter.get_currencies(new BaseParam());

        refreshAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        recyclerView.addItemDecoration(new LinearSpacingItemDecoration(_mActivity, ScreenUtils.dip2px(_mActivity, 0.5f), Color.parseColor("#E0E0E0")));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        String json = CacheUtil.getInstance().getProperty(Constant.Sp.PRICING_CURRENCY);
        if (!TextUtils.isEmpty(json)) {
            CurrenciesBean bean = new Gson().fromJson(json, CurrenciesBean.class);

            setCurrencyDefault(bean.list);
            refreshAdapter();
            mAdapter.refreshView(bean.list);
        }
    }

    private void refreshAdapter() {
        if (null == mAdapter) {
            mAdapter = new PricingCurrencyAdapter(mDataList, o -> {

                if (o instanceof CurrenciesBean.ListBean) {
                    if (((CurrenciesBean.ListBean) o).isSelected) {
                        return;
                    }

                    CacheUtil.getInstance().setProperty(Constant.Sp.PRICING_CURRENCY_DEFAULT, new Gson().toJson(o));
                    CacheUtil.getInstance().setProperty(Constant.Sp.PRICING_CURRENCY_SYMBOL_DEFAULT, ((CurrenciesBean.ListBean) o).symbol);
                    CacheUtil.getInstance().setProperty(Constant.Sp.PRICING_CURRENCY_ID_DEFAULT, ((CurrenciesBean.ListBean) o).id);
                    refreshView(((CurrenciesBean.ListBean) o).id);
                    EventBus.getDefault().post(new MessageEvent(Constant.Event.PRICING_CURRENCY));
                }
            });
        }
    }


    private void refreshView(String id) {
        for (int i = 0; i < mDataList.size(); i++) {
            mDataList.get(i).isSelected = false;

            if (TextUtils.equals(id, mDataList.get(i).id)) {
                mDataList.get(i).isSelected = true;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private void setCurrencyDefault(List<CurrenciesBean.ListBean> beanList) {
        if (null == beanList || beanList.size() < 0) {
            return;
        }

        String json = CacheUtil.getInstance().getProperty(Constant.Sp.PRICING_CURRENCY_DEFAULT);
        if (TextUtils.isEmpty(json)) {
            beanList.get(0).isSelected = true;
            CacheUtil.getInstance().setProperty(Constant.Sp.PRICING_CURRENCY_DEFAULT, new Gson().toJson(beanList.get(0)));
            CacheUtil.getInstance().setProperty(Constant.Sp.PRICING_CURRENCY_SYMBOL_DEFAULT, beanList.get(0).symbol);
            CacheUtil.getInstance().setProperty(Constant.Sp.PRICING_CURRENCY_ID_DEFAULT, beanList.get(0).id);
            return;
        }

        CurrenciesBean.ListBean listBean = new Gson().fromJson(json, CurrenciesBean.ListBean.class);
        if (null != listBean) {
            for (CurrenciesBean.ListBean bean : beanList) {

                if (TextUtils.equals(listBean.id, bean.id)) {
                    bean.isSelected = true;
                    CacheUtil.getInstance().setProperty(Constant.Sp.PRICING_CURRENCY_DEFAULT, new Gson().toJson(bean));
                } else {
                    bean.isSelected = false;
                }
            }
        }

    }

    @Override
    public void onSuccess(ResultBean<CurrenciesBean> bean) {
        if (null == bean || null == bean.getData())
            return;

        if (200 == bean.getStatus()) {
            List<CurrenciesBean.ListBean> beanList = bean.getData().list;

            if (null != beanList && beanList.size() > 0) {
                for (CurrenciesBean.ListBean listBean : beanList) {
                    if ("USD".equals(listBean.id)) {
                        listBean.symbol = "$";
                    } else if ("CNY".equals(listBean.id)) {
                        listBean.symbol = "¥";
                    } else if ("AUD".equals(listBean.id)) {
                        listBean.symbol = "A$";
                    } else if ("KRW".equals(listBean.id)) {
                        listBean.symbol = "₩";
                    } else {
                        listBean.symbol = "$";
                    }
                }
            }

            CacheUtil.getInstance().setProperty(Constant.Sp.PRICING_CURRENCY, new Gson().toJson(bean.getData()));

            setCurrencyDefault(bean.getData().list);
            mDataList = bean.getData().list;
            mAdapter.refreshView(bean.getData().list);
        }
    }

    @Override
    public void onError(int code, String msg) {

    }
}
