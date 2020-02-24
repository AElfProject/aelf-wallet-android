package com.legendwd.hyperpay.aelf.presenters.impl;

import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.httpservices.HttpService;
import com.legendwd.hyperpay.aelf.httpservices.ResponseTransformer;
import com.legendwd.hyperpay.aelf.model.bean.IdentityBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.UnreadBean;
import com.legendwd.hyperpay.aelf.model.param.AddressParam;
import com.legendwd.hyperpay.aelf.presenters.BasePresenter;
import com.legendwd.hyperpay.aelf.presenters.IMyPresenter;
import com.legendwd.hyperpay.aelf.views.IMyView;
import com.legendwd.hyperpay.httputil.ServiceGenerator;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import retrofit2.Response;

public class MyPresenter extends BasePresenter implements IMyPresenter {

    IMyView iView;

    public MyPresenter( IMyView iAssetsView) {
        super((LifecycleProvider<ActivityEvent>) iAssetsView);
        iView = iAssetsView;
    }

    @Override
    public void getUnreadCount(AddressParam param) {
        HttpService service = ServiceGenerator.createService(HttpService.class);
        Observable<Response<ResultBean<UnreadBean>>> observable = service.getUnreadCount(param);
        observable.compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(listResultBean -> iView.onUnreadSuccess(listResultBean), throwable -> iView.onUnreadError(-1, throwable.toString()));
    }

    @Override
    public void getIdentity(JsonObject param) {
        ServiceGenerator.createService(HttpService.class)
                .getIdentity(param)
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(new Consumer<ResultBean<IdentityBean>>() {
                    @Override
                    public void accept(ResultBean<IdentityBean> identityBeanResultBean) throws Exception {
                        iView.onIdentitySuccess(identityBeanResultBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        iView.onIdentityError(-1, throwable.getMessage());
                    }
                });
    }
}
