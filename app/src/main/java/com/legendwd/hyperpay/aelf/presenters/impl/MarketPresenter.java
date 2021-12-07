package com.legendwd.hyperpay.aelf.presenters.impl;

import android.text.TextUtils;

import com.legendwd.hyperpay.aelf.config.ApiUrlConfig;
import com.legendwd.hyperpay.aelf.httpservices.HttpService;
import com.legendwd.hyperpay.aelf.httpservices.ResponseTransformer;
import com.legendwd.hyperpay.aelf.model.param.MarketParam;
import com.legendwd.hyperpay.aelf.presenters.BasePresenter;
import com.legendwd.hyperpay.aelf.presenters.IMarketPresenter;
import com.legendwd.hyperpay.aelf.views.IMarketView;
import com.legendwd.hyperpay.httputil.ServiceGenerator;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.HashMap;
import java.util.Map;

public class MarketPresenter extends BasePresenter implements IMarketPresenter {

    IMarketView mMarketView;

    public MarketPresenter(IMarketView marketView) {
        super((LifecycleProvider<ActivityEvent>) marketView);
        mMarketView = marketView;
    }

    @Override
    public void getCoinList(MarketParam param, String type) {
        HttpService service = ServiceGenerator.createServiceMarket(HttpService.class, ApiUrlConfig.MARKET_UTL);
        Map<String, String> map = new HashMap<>();
        map.put("Currency", param.currency);
        if (!TextUtils.isEmpty(param.coinName)) {
            map.put("Ids", param.coinName);
        }
        map.put("Order", "market_cap_desc");
        if (!TextUtils.isEmpty(param.p)) {
            map.put("PerPage", "100");
            map.put("Page", param.p);
        }
        map.put("sparkline", "false");
        service.getCoinList(map)
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(marketListBeanResultBean -> mMarketView.onCoinListSuccess(marketListBeanResultBean, type)
                        , throwable -> mMarketView.onCoinListError(-1, throwable.getMessage(), type));

    }

    @Override
    public void getAelfCoinList(MarketParam param, String type,String sort) {

        HttpService service = ServiceGenerator.createServiceMarket(HttpService.class, ApiUrlConfig.MARKET_UTL);
        Map<String, String> map = new HashMap<>();
        map.put("Currency", param.currency);
        if (!TextUtils.isEmpty(param.coinName)) {
            map.put("Ids", param.coinName);
        }
        map.put("Order", "market_cap_desc");
        if (!TextUtils.isEmpty(param.p)) {
            map.put("PerPage", "100");
            map.put("Page", param.p);
        }
        map.put("sparkline", "false");
        service.getCoinList(map)
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(marketListBeanResultBean -> mMarketView.onAelfCoinListSuccess(marketListBeanResultBean, type,sort)
                        , throwable -> mMarketView.onCoinListError(-1, throwable.getMessage(), type));

    }

    @Override
    public void getMyCoinList(MarketParam param) {

//        HttpService service = ServiceGenerator.createServiceMarket(HttpService.class, ApiUrlConfig.MARKET_UTL);
//        service.getCoinList(param)
//                .compose(ResponseTransformer.handleResult(getProvider()))
//                .subscribe(marketListBeanResultBean -> mMarketView.onMyCoinListSuccess(marketListBeanResultBean)
//                        , throwable -> mMarketView.onMyCoinListError(-1, throwable.getMessage()));
    }
}
