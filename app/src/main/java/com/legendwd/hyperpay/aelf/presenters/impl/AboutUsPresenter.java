package com.legendwd.hyperpay.aelf.presenters.impl;

import com.legendwd.hyperpay.aelf.httpservices.HttpService;
import com.legendwd.hyperpay.aelf.httpservices.ResponseTransformer;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.UpdateBean;
import com.legendwd.hyperpay.aelf.model.param.BaseParam;
import com.legendwd.hyperpay.aelf.presenters.BasePresenter;
import com.legendwd.hyperpay.aelf.presenters.IAboutUsPresenter;
import com.legendwd.hyperpay.aelf.views.IAboutUsView;
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
public class AboutUsPresenter extends BasePresenter implements IAboutUsPresenter {

    private IAboutUsView iView;

    public AboutUsPresenter( IAboutUsView view) {
        super((LifecycleProvider<ActivityEvent>) view);
        this.iView = view;
    }

    @Override
    public void update(BaseParam param) {
        HttpService service = ServiceGenerator.createService(HttpService.class);
        Observable<Response<ResultBean<UpdateBean>>> observable = service.update(param);
        observable.compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(listResultBean -> iView.onUpdateSuccess(listResultBean), throwable -> iView.onUpdateError(-1, throwable.toString()));
    }
}
