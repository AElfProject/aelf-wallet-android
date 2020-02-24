package com.legendwd.hyperpay.aelf.presenters.impl;

import android.os.Handler;

import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.httpservices.HttpService;
import com.legendwd.hyperpay.aelf.httpservices.ResponseTransformer;
import com.legendwd.hyperpay.aelf.model.bean.AssetsBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.presenters.BasePresenter;
import com.legendwd.hyperpay.aelf.presenters.IAssetsManagePresenter;
import com.legendwd.hyperpay.aelf.views.IAssetsManageView;
import com.legendwd.hyperpay.httputil.ServiceGenerator;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;

public class AssetsManagePresenter extends BasePresenter implements IAssetsManagePresenter {
    IAssetsManageView mAddAssetsView;

    public AssetsManagePresenter(IAssetsManageView addAssetsView) {
        super((LifecycleProvider<ActivityEvent>) addAssetsView);
        mAddAssetsView = addAssetsView;
    }

    @Override
    public void getAddAssetsList() {
        HttpService service = ServiceGenerator.createService(HttpService.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("address", CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS));
        jsonObject.addProperty("chainid", ServiceGenerator.getChainId());
        service.getAssetsManageList(jsonObject)
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(resultBean -> mAddAssetsView.onAssetsSuccess(resultBean), throwable -> mAddAssetsView.onAssetsError(-1, throwable.getMessage()));
    }

    @Override
    public void bind(JsonObject param, AssetsBean aelfBean, int pos) {
        param.addProperty("chainid", aelfBean.getChainId());
        HttpService service = ServiceGenerator.createService(HttpService.class);
        mAddAssetsView.showLoading();
        service.bind(param)
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(resultBean -> {
                    mAddAssetsView.onBindSuccess(resultBean, aelfBean, pos);
                    new Handler().postDelayed(() -> mAddAssetsView.dismissLoading(),1500);
                }, throwable -> {
                    mAddAssetsView.onBindError(-1, throwable.getMessage(), aelfBean, pos);
                    new Handler().postDelayed(() -> mAddAssetsView.dismissLoading(),1500);
                });
    }
}
