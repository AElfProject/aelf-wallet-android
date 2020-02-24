package com.legendwd.hyperpay.aelf.presenters.impl;

import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.httpservices.HttpService;
import com.legendwd.hyperpay.aelf.httpservices.ResponseTransformer;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.TransactionBean;
import com.legendwd.hyperpay.aelf.model.bean.TransferBalanceBean;
import com.legendwd.hyperpay.aelf.model.param.TransferBalanceParam;
import com.legendwd.hyperpay.aelf.model.param.TransferCrossChainParam;
import com.legendwd.hyperpay.aelf.presenters.BasePresenter;
import com.legendwd.hyperpay.aelf.presenters.ITransactionRecordPresenter;
import com.legendwd.hyperpay.aelf.views.ITransactionRecordView;
import com.legendwd.hyperpay.httputil.ServiceGenerator;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import retrofit2.Response;

public class TransactionRecordPresenter extends BasePresenter implements ITransactionRecordPresenter {

    private ITransactionRecordView mITransactionRecordView;

    public TransactionRecordPresenter(ITransactionRecordView transactionRecordView) {
        super((LifecycleProvider<ActivityEvent>) transactionRecordView);
        mITransactionRecordView = transactionRecordView;
    }

    @Override
    public void getTransactionDetail(JsonObject param) {
        ServiceGenerator.createService(HttpService.class)
                .getTransactionDetail(param)
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(new Consumer<ResultBean<TransactionBean.ListBean>>() {
                    @Override
                    public void accept(ResultBean<TransactionBean.ListBean> listBeanResultBean) throws Exception {
                        mITransactionRecordView.onTransactionDetailSuccess(listBeanResultBean.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mITransactionRecordView.onTransactionDetailError(-1, throwable.getMessage());
                    }
                });
    }

    @Override
    public void getCrossChainDetail(TransferCrossChainParam transferCrossChainParam) {
        ServiceGenerator.createService(HttpService.class)
                .getCrossChainDetail(transferCrossChainParam)
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        mITransactionRecordView.onCrossChainSuccess(resultBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mITransactionRecordView.onCrossChainError(-1, throwable.getMessage());
                    }
                });
    }

    @Override
    public void addIndex(TransferCrossChainParam transferCrossChainParam) {
        ServiceGenerator.createService(HttpService.class)
                .addIndex(transferCrossChainParam)
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        mITransactionRecordView.onAddIndexSuccess(resultBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mITransactionRecordView.onAddIndexError(-1, throwable.getMessage());
                    }
                });
    }

    @Override
    public void getTransferBalance(TransferBalanceParam param) {
        HttpService service = ServiceGenerator.createService(HttpService.class);
        Observable<Response<ResultBean<TransferBalanceBean>>> observable = service.getTransferBalance(param);
        observable.compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(listResultBean -> mITransactionRecordView.onTransferBalanceSuccess(listResultBean),
                        throwable -> mITransactionRecordView.onTransferBalanceError(-1, throwable.toString()));
    }
}
