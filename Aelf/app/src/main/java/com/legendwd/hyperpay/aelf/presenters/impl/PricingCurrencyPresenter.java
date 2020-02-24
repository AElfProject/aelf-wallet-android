package com.legendwd.hyperpay.aelf.presenters.impl;

import com.legendwd.hyperpay.aelf.httpservices.HttpService;
import com.legendwd.hyperpay.aelf.httpservices.ResponseTransformer;
import com.legendwd.hyperpay.aelf.model.bean.CurrenciesBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.param.BaseParam;
import com.legendwd.hyperpay.aelf.presenters.BasePresenter;
import com.legendwd.hyperpay.aelf.presenters.IPricingCurrencyPresenter;
import com.legendwd.hyperpay.aelf.views.IPricingCurrencyView;
import com.legendwd.hyperpay.httputil.ServiceGenerator;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * @author lovelyzxing
 * @date 2019/6/10
 * @Description
 */
public class PricingCurrencyPresenter extends BasePresenter implements IPricingCurrencyPresenter {

    private IPricingCurrencyView iView;

    public PricingCurrencyPresenter( IPricingCurrencyView view) {
        super((LifecycleProvider<ActivityEvent>) view);
        this.iView = view;
    }

    @Override
    public void get_currencies(BaseParam param) {

        HttpService service = ServiceGenerator.createService(HttpService.class);
        Observable<Response<ResultBean<CurrenciesBean>>> observable = service.getCurrencies(param);
        observable.compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(listResultBean -> iView.onSuccess(listResultBean), throwable -> iView.onError(-1, throwable.toString()));
    }
}
