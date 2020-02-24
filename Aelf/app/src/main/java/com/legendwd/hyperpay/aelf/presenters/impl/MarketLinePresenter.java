package com.legendwd.hyperpay.aelf.presenters.impl;

import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.httpservices.HttpService;
import com.legendwd.hyperpay.aelf.httpservices.ResponseTransformer;
import com.legendwd.hyperpay.aelf.model.bean.CoinDetailBean;
import com.legendwd.hyperpay.aelf.model.bean.MarketLineBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.param.MarketLineParam;
import com.legendwd.hyperpay.aelf.presenters.BasePresenter;
import com.legendwd.hyperpay.aelf.presenters.IMarketLinePresenter;
import com.legendwd.hyperpay.aelf.views.IMarketLineView;
import com.legendwd.hyperpay.httputil.ServiceGenerator;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import retrofit2.Response;

/**
 * @author lovelyzxing
 * @date 2019/6/10
 * @Description
 */
public class MarketLinePresenter extends BasePresenter implements IMarketLinePresenter {

    private IMarketLineView iView;

    public MarketLinePresenter( IMarketLineView view) {
        super((LifecycleProvider<ActivityEvent>) view);
        this.iView = view;
    }

    @Override
    public void getTradeLine(MarketLineParam param) {
        HttpService service = ServiceGenerator.createService(HttpService.class);
        Observable<Response<ResultBean<MarketLineBean>>> observable = service.getTradeLine(param);
        observable.compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(beanList -> iView.onSuccess(beanList), throwable -> iView.onError(-1, throwable.toString()));
    }

    @Override
    public void getCoinDetail(JsonObject param) {
        HttpService service = ServiceGenerator.createService(HttpService.class);
        service.getCoinDetail(param)
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(new Consumer<ResultBean<CoinDetailBean>>() {
                    @Override
                    public void accept(ResultBean<CoinDetailBean> coinDetailBeanResultBean) throws Exception {
                        iView.onCoinDetailSuccess(coinDetailBeanResultBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        iView.onCoinDetailError(-1, throwable.getMessage());
                    }
                });
    }
}
