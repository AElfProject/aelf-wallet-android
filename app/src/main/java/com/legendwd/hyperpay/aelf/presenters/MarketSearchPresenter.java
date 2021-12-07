package com.legendwd.hyperpay.aelf.presenters;

import android.text.TextUtils;

import com.legendwd.hyperpay.aelf.config.ApiUrlConfig;
import com.legendwd.hyperpay.aelf.httpservices.HttpService;
import com.legendwd.hyperpay.aelf.httpservices.ResponseTransformer;
import com.legendwd.hyperpay.aelf.model.param.MarketParam;
import com.legendwd.hyperpay.aelf.views.IMarketView;
import com.legendwd.hyperpay.httputil.ServiceGenerator;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class MarketSearchPresenter extends BasePresenter implements IMarketPresenter {

    IMarketView mMarketView;

    public MarketSearchPresenter(IMarketView marketView) {
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
        if (!TextUtils.isEmpty(param.sort)) {
            map.put("Order", param.sort);
        }
        if (!TextUtils.isEmpty(param.p)) {
            map.put("PerPage", "50");
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

    }

    @Override
    public void getMyCoinList(MarketParam jsonObject) {

    }
}
