package com.legendwd.hyperpay.aelf.presenters.impl;

import com.legendwd.hyperpay.aelf.httpservices.HttpService;
import com.legendwd.hyperpay.aelf.httpservices.ResponseTransformer;
import com.legendwd.hyperpay.aelf.model.bean.CurrenciesBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.param.BaseParam;
import com.legendwd.hyperpay.aelf.model.param.UploadDataParam;
import com.legendwd.hyperpay.aelf.presenters.BasePresenter;
import com.legendwd.hyperpay.aelf.presenters.IWelcomePresenter;
import com.legendwd.hyperpay.aelf.views.IWelcomeView;
import com.legendwd.hyperpay.httputil.ServiceGenerator;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import retrofit2.Response;

public class WelcomePresenter extends BasePresenter implements IWelcomePresenter {

    IWelcomeView iView;

    public WelcomePresenter( IWelcomeView iAssetsView) {
        super((LifecycleProvider<ActivityEvent>) iAssetsView);
        iView = iAssetsView;
    }

    @Override
    public void getCurrencies(BaseParam param) {
        HttpService service = ServiceGenerator.createService(HttpService.class);
        Observable<Response<ResultBean<CurrenciesBean>>> observable = service.getCurrencies(param);
        observable.compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(listResultBean -> iView.onCurrencySuccess(listResultBean), new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                iView.onCurrencyError(-1, throwable.toString());

                            }
                        }
                );
    }

    @Override
    public void uploadData(UploadDataParam param) {
        HttpService service = ServiceGenerator.createService(HttpService.class);
        Observable<Response<ResultBean<Object>>> observable = service.uploadData(param);
        observable.compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(listResultBean -> iView.onUploadDataSuccess(listResultBean), throwable -> iView.onUploadDataFail(-1, throwable.toString()));

    }

}
