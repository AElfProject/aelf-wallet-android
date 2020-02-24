package com.legendwd.hyperpay.aelf.presenters.impl;

import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.httpservices.HttpService;
import com.legendwd.hyperpay.aelf.httpservices.ResponseTransformer;
import com.legendwd.hyperpay.aelf.model.bean.ChooseChainsBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.param.BaseParam;
import com.legendwd.hyperpay.aelf.presenters.BasePresenter;
import com.legendwd.hyperpay.aelf.presenters.IWaitTransferPresenter;
import com.legendwd.hyperpay.aelf.views.IWaitTransferView;
import com.legendwd.hyperpay.httputil.ServiceGenerator;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * @author lovelyzxing
 * @date 2019/6/10
 * @Description
 */
public class WaitTransferPresenter extends BasePresenter implements IWaitTransferPresenter {

    private IWaitTransferView iView;

    public WaitTransferPresenter(IWaitTransferView view) {
        super((LifecycleProvider<ActivityEvent>) view);
        this.iView = view;
    }

    @Override
    public void rcvTxid(String fromTxid, String toTxid) {
        JsonObject param = new JsonObject();
        param.addProperty("txid_from", fromTxid);
        param.addProperty("txid_to", toTxid);
        ServiceGenerator.createService(HttpService.class)
                .rcvTxid(param)
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(resultBean -> iView.onRcvTxidSuccess(resultBean)
                        , throwable -> iView.onRcvTxidError(-1, throwable.getMessage()));
    }

    @Override
    public void getWaitTransList(String address) {
        JsonObject param = new JsonObject();
        param.addProperty("address", address);
        ServiceGenerator.createService(HttpService.class)
                .waitCrossTrans(param)
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(resultBean -> iView.onWaitCrossTransSuccess(resultBean)
                        , throwable -> iView.onWaitCrossTransError(-1, throwable.getMessage()));
    }

    @Override
    public void getCrossChains() {
        BaseParam baseParam = new BaseParam();
        HttpService service = ServiceGenerator.createService(HttpService.class);
        Observable<Response<ResultBean<List<ChooseChainsBean>>>> observable = service.getCrossChains(baseParam);
        observable.compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(r -> iView.onChainsSuccess(r)
                        , e -> iView.onChainsError(-1, e.getMessage()));
    }
}
