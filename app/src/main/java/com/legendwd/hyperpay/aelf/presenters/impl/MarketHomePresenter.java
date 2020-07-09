package com.legendwd.hyperpay.aelf.presenters.impl;

import com.legendwd.hyperpay.aelf.config.ApiUrlConfig;
import com.legendwd.hyperpay.aelf.httpservices.HttpService;
import com.legendwd.hyperpay.aelf.httpservices.ResponseTransformer;
import com.legendwd.hyperpay.aelf.presenters.BasePresenter;
import com.legendwd.hyperpay.aelf.presenters.IMarketHomePresenter;
import com.legendwd.hyperpay.aelf.views.IMarketHomeView;
import com.legendwd.hyperpay.httputil.ServiceGenerator;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class MarketHomePresenter extends BasePresenter implements IMarketHomePresenter {

    IMarketHomeView mMarketView;

    public MarketHomePresenter(IMarketHomeView marketView) {
        super((LifecycleProvider<ActivityEvent>) marketView);
        mMarketView = marketView;
    }

    @Override
    public void getMarketCoinList() {
        HttpService service = ServiceGenerator.createServiceMarket(HttpService.class, ApiUrlConfig.MARKET_UTL);
        service.getMarketCoinList()
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(marketListBeanResultBean -> mMarketView.onCoinListSuccess(marketListBeanResultBean)
                        , throwable -> mMarketView.onCoinListError(-1, throwable.getMessage()));
    }
}
