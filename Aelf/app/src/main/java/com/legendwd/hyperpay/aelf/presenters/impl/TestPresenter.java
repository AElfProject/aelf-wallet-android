package com.legendwd.hyperpay.aelf.presenters.impl;

import com.legendwd.hyperpay.aelf.httpservices.HttpService;
import com.legendwd.hyperpay.aelf.httpservices.ResponseTransformer;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.TransactionBean;
import com.legendwd.hyperpay.aelf.model.param.TestParam;
import com.legendwd.hyperpay.aelf.presenters.BasePresenter;
import com.legendwd.hyperpay.aelf.presenters.ITestPresenter;
import com.legendwd.hyperpay.aelf.views.ITestView;
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
public class TestPresenter extends BasePresenter implements ITestPresenter {

    private ITestView iView;

    public TestPresenter( ITestView view) {
        super((LifecycleProvider<ActivityEvent>) view);
        this.iView = view;
    }


    @Override
    public void testPush(TestParam param) {
        HttpService service = ServiceGenerator.createService(HttpService.class);
        Observable<Response<ResultBean<TransactionBean.ListBean>>> observable = service.testPush(param);
        observable.compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(listResultBean -> iView.onTestSuccess(listResultBean), throwable -> iView.onTestError(-1, throwable.toString()));

    }
}
