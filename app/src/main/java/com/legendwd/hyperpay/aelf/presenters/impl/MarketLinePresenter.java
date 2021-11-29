package com.legendwd.hyperpay.aelf.presenters.impl;

import com.legendwd.hyperpay.aelf.config.ApiUrlConfig;
import com.legendwd.hyperpay.aelf.httpservices.HttpService;
import com.legendwd.hyperpay.aelf.httpservices.ResponseTransformer;
import com.legendwd.hyperpay.aelf.model.bean.MarketDataBean;
import com.legendwd.hyperpay.aelf.model.param.MarketLineParam;
import com.legendwd.hyperpay.aelf.presenters.BasePresenter;
import com.legendwd.hyperpay.aelf.presenters.IMarketLinePresenter;
import com.legendwd.hyperpay.aelf.views.IMarketLineView;
import com.legendwd.hyperpay.httputil.ServiceGenerator;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * @author lovelyzxing
 * @date 2019/6/10
 * @Description
 */
public class MarketLinePresenter extends BasePresenter implements IMarketLinePresenter {

    private IMarketLineView iView;

    public MarketLinePresenter(IMarketLineView view) {
        super((LifecycleProvider<ActivityEvent>) view);
        this.iView = view;
    }

    @Override
    public void getTradeLine(MarketLineParam param) {
        HttpService service = ServiceGenerator.createServiceMarket(HttpService.class, ApiUrlConfig.MARKET_UTL);
        Map<String, String> map = new HashMap<>();
        map.put("Currency", param.currency);
        map.put("Days", param.time);
        map.put("Id", param.name);
        service.getTradeLine(map).compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(beanList -> iView.onSuccess(beanList), throwable -> iView.onError(-1, throwable.toString()));
    }

    @Override
    public void getCoinDetail(String id) {
        HttpService service = ServiceGenerator.createServiceMarket(HttpService.class, ApiUrlConfig.MARKET_UTL);
        String currency = CacheUtil.getInstance().getProperty(Constant.Sp.PRICING_CURRENCY_ID_DEFAULT, "USD");
        Map<String, String> map = new HashMap<>();
        map.put("Currency", currency);
        map.put("Ids", id);
        map.put("sparkline", "false");
        service.getCoinList(map)
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(new Consumer<List<MarketDataBean>>() {
                    @Override
                    public void accept(List<MarketDataBean> list) throws Exception {
                        iView.onCoinDetailSuccess(list);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        iView.onCoinDetailError(-1, throwable.getMessage());
                    }
                });
    }
}
