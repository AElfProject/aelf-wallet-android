package com.legendwd.hyperpay.aelf.presenters.impl;

import com.legendwd.hyperpay.aelf.httpservices.HttpService;
import com.legendwd.hyperpay.aelf.httpservices.ResponseTransformer;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.presenters.BasePresenter;
import com.legendwd.hyperpay.aelf.presenters.INotificationsParentPresenter;
import com.legendwd.hyperpay.aelf.views.INotificationsParentView;
import com.legendwd.hyperpay.httputil.ServiceGenerator;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.functions.Consumer;

public class NotificationsParentPresenter extends BasePresenter implements INotificationsParentPresenter {

    INotificationsParentView mNotificationsParentView;

    public NotificationsParentPresenter( INotificationsParentView notificationsParentView) {
        super((LifecycleProvider<ActivityEvent>) notificationsParentView);
        mNotificationsParentView = notificationsParentView;
    }

    @Override
    public void clearNotice() {
        HttpService service = ServiceGenerator.createService(HttpService.class);
        service.clearNotice()
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        mNotificationsParentView.onClearSuccess(resultBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mNotificationsParentView.onClearError(-1, throwable.getMessage());
                    }
                });
    }
}
