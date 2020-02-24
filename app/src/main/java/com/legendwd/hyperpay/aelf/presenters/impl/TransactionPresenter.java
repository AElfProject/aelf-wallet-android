package com.legendwd.hyperpay.aelf.presenters.impl;

import com.legendwd.hyperpay.aelf.httpservices.HttpService;
import com.legendwd.hyperpay.aelf.httpservices.ResponseTransformer;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.TransactionBean;
import com.legendwd.hyperpay.aelf.model.param.TransactionParam;
import com.legendwd.hyperpay.aelf.presenters.BasePresenter;
import com.legendwd.hyperpay.aelf.presenters.ITransactionPresenter;
import com.legendwd.hyperpay.aelf.views.ITransactionView;
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
public class TransactionPresenter extends BasePresenter implements ITransactionPresenter {

    private ITransactionView iView;

    public TransactionPresenter( ITransactionView view) {
        super((LifecycleProvider<ActivityEvent>) view);
        this.iView = view;
    }

    @Override
    public void getTransaction(TransactionParam param, String page,String refreshType) {
        HttpService service = ServiceGenerator.createService(HttpService.class);
        Observable<Response<ResultBean<TransactionBean>>> observable = service.getTransaction(param);
        observable.compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(resultBean -> iView.onSuccess(resultBean, page,refreshType),
                        throwable -> iView.onError(-1, throwable.toString(), page,refreshType));
    }
}
