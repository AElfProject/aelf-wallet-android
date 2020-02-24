package com.legendwd.hyperpay.aelf.presenters.impl;

import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.httpservices.HttpService;
import com.legendwd.hyperpay.aelf.httpservices.ResponseTransformer;
import com.legendwd.hyperpay.aelf.model.bean.MessageBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.TransactionNoticeBean;
import com.legendwd.hyperpay.aelf.presenters.BasePresenter;
import com.legendwd.hyperpay.aelf.presenters.INotificationsPresenter;
import com.legendwd.hyperpay.aelf.views.INotificationsView;
import com.legendwd.hyperpay.httputil.ServiceGenerator;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

import io.reactivex.functions.Consumer;

public class NotificationPresenter extends BasePresenter implements INotificationsPresenter {

    INotificationsView mNotificationsView;

    public NotificationPresenter( INotificationsView notificationsView) {
        super((LifecycleProvider<ActivityEvent>) notificationsView);
        mNotificationsView = notificationsView;
    }

    @Override
    public void getSystemMessage(JsonObject jsonObject, String refreshType) {
        HttpService service = ServiceGenerator.createService(HttpService.class);

        service.getSystemMessage(jsonObject)
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(messageBeanResultBean ->
                                mNotificationsView.onMessageSuccess(messageBeanResultBean, refreshType)
                        , throwable ->
                                mNotificationsView.onMessageError(-1, throwable.getMessage(), refreshType));

    }

    @Override
    public void getTransactionMessage(JsonObject jsonObject, String refreshType) {

        HttpService service = ServiceGenerator.createService(HttpService.class);
        service.getTransactionMessage(jsonObject)
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(new Consumer<ResultBean<TransactionNoticeBean>>() {
                    @Override
                    public void accept(ResultBean<TransactionNoticeBean> resultListBean) throws Exception {
                        mNotificationsView.onNotificationsSuccess(resultListBean, refreshType);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mNotificationsView.onNotificationsError(-1, throwable.getMessage(), refreshType);
                    }
                });

    }


    @Override
    public void clearNotice(List<TransactionNoticeBean.NoticeBean> list, int index, int unReadCount) {
        HttpService service = ServiceGenerator.createService(HttpService.class);
        service.clearNotice()
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        mNotificationsView.onClearNoticeSuccess(resultBean, index, unReadCount);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mNotificationsView.onClearNoticeError(-1, throwable.getMessage(), list);
                    }
                });
    }

    @Override
    public void clearMessage(List<MessageBean.SystemMessageBean> list, int index, int unReadCount) {
        HttpService service = ServiceGenerator.createService(HttpService.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "1");
        service.clearMessage(jsonObject)
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        mNotificationsView.onClearMessageSuccess(resultBean, index, unReadCount);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mNotificationsView.onClearMessageError(-1, throwable.getMessage(), list);
                    }
                });
    }

    @Override
    public void setReadNotice(TransactionNoticeBean.NoticeBean notice) {
        HttpService service = ServiceGenerator.createService(HttpService.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", notice.getId());
        service.setNoticeRead(jsonObject)
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        mNotificationsView.onNoticeReadSuccess(resultBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mNotificationsView.onNoticeReadError(-1, throwable.getMessage(), notice);
                    }
                });
    }

    @Override
    public void setReadMessage(MessageBean.SystemMessageBean systemMessageBean) {
        HttpService service = ServiceGenerator.createService(HttpService.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "1");
        jsonObject.addProperty("mid", systemMessageBean.getId());
        service.setMessageRead(jsonObject)
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        mNotificationsView.onMessageReadSuccess(resultBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mNotificationsView.onMessageReadError(-1, throwable.getMessage(), systemMessageBean);
                    }
                });
    }
}
