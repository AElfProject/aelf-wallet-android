package com.legendwd.hyperpay.aelf.presenters;

import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.httpservices.HttpService;
import com.legendwd.hyperpay.aelf.httpservices.ResponseTransformer;
import com.legendwd.hyperpay.aelf.model.param.MarketParam;
import com.legendwd.hyperpay.aelf.views.IMarketView;
import com.legendwd.hyperpay.httputil.ServiceGenerator;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

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
        HttpService service = ServiceGenerator.createService(HttpService.class);

        service.getCoinList(param)
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(marketListBeanResultBean -> mMarketView.onCoinListSuccess(marketListBeanResultBean, type)
                        , throwable -> mMarketView.onCoinListError(-1, throwable.getMessage(), type));

    }

    @Override
    public void getMyCoinList(JsonObject jsonObject) {

    }
}
