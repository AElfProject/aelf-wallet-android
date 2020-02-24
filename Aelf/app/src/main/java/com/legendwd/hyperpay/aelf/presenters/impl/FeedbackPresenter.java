package com.legendwd.hyperpay.aelf.presenters.impl;

import com.legendwd.hyperpay.aelf.httpservices.HttpService;
import com.legendwd.hyperpay.aelf.httpservices.ResponseTransformer;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.param.FeedbackParam;
import com.legendwd.hyperpay.aelf.presenters.BasePresenter;
import com.legendwd.hyperpay.aelf.presenters.IFeedbackPresenter;
import com.legendwd.hyperpay.aelf.views.IFeedbackView;
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
public class FeedbackPresenter extends BasePresenter implements IFeedbackPresenter {

    private IFeedbackView iView;

    public FeedbackPresenter( IFeedbackView view) {
        super((LifecycleProvider<ActivityEvent>) view);
        this.iView = view;
    }

    @Override
    public void feedback(FeedbackParam param) {
        HttpService service = ServiceGenerator.createService(HttpService.class);
        Observable<Response<ResultBean<Object>>> observable = service.feedback(param);
        observable.compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(obj -> iView.onFeedbackSuccess(obj), throwable -> iView.onFeedbackFail(-1, throwable.toString()));
    }
}
