package com.legendwd.hyperpay.aelf.presenters.impl;

import com.legendwd.hyperpay.aelf.httpservices.HttpService;
import com.legendwd.hyperpay.aelf.httpservices.ResponseTransformer;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.param.AddContactParam;
import com.legendwd.hyperpay.aelf.presenters.BasePresenter;
import com.legendwd.hyperpay.aelf.presenters.IAddContactPresenter;
import com.legendwd.hyperpay.aelf.views.IAddContactView;
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
public class AddContactPresenter extends BasePresenter implements IAddContactPresenter {

    private IAddContactView iView;

    public AddContactPresenter(IAddContactView view) {
        super((LifecycleProvider<ActivityEvent>) view);
        this.iView = view;
    }

    @Override
    public void address_book(AddContactParam param) {

        HttpService service = ServiceGenerator.createService(HttpService.class);
        Observable<Response<ResultBean>> observable = service.add_contact(param);
        observable.compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(listResultBean -> iView.onSuccess(listResultBean), throwable -> {

                    if (throwable instanceof ResponseTransformer.ResponseThrowable) {
                        iView.onError(-1, throwable.getMessage());
                    } else {
                        iView.onError(-1, throwable.toString());
                    }
                });
    }
}
