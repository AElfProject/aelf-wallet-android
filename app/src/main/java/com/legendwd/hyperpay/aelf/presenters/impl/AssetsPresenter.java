package com.legendwd.hyperpay.aelf.presenters.impl;

import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.httpservices.HttpService;
import com.legendwd.hyperpay.aelf.httpservices.ResponseTransformer;
import com.legendwd.hyperpay.aelf.model.bean.AssetsListBean;
import com.legendwd.hyperpay.aelf.model.bean.ChainAddressBean;
import com.legendwd.hyperpay.aelf.model.bean.CurrenciesBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.UnreadBean;
import com.legendwd.hyperpay.aelf.model.param.AddressParam;
import com.legendwd.hyperpay.aelf.model.param.BaseParam;
import com.legendwd.hyperpay.aelf.model.param.ChooseChainParam;
import com.legendwd.hyperpay.aelf.model.params.AssetsListParams;
import com.legendwd.hyperpay.aelf.presenters.BasePresenter;
import com.legendwd.hyperpay.aelf.presenters.IAssetsPresenter;
import com.legendwd.hyperpay.aelf.views.IAssetsView;
import com.legendwd.hyperpay.httputil.ServiceGenerator;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;

public class AssetsPresenter extends BasePresenter implements IAssetsPresenter {

    IAssetsView mIAssetsView;

    public AssetsPresenter(IAssetsView iAssetsView) {
        super((LifecycleProvider<ActivityEvent>) iAssetsView);
        mIAssetsView = iAssetsView;
    }

    @Override
    public void getAssetsList(String address) {
        AssetsListParams assetsListParams = new AssetsListParams();
        assetsListParams.setAddress(address);
        assetsListParams.setChainid(ServiceGenerator.getChainId());
        HttpService service = ServiceGenerator.createService(HttpService.class);
        Observable<Response<ResultBean<AssetsListBean>>> observable = service.getAssetsList(assetsListParams);
        observable.compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(r -> mIAssetsView.onAssetsSuccess(r)
                        , e -> mIAssetsView.onAssetsError(-1, e.getMessage()));
    }

    @Override
    public void getCurrencies(BaseParam param) {
        HttpService service = ServiceGenerator.createService(HttpService.class);
        Observable<Response<ResultBean<CurrenciesBean>>> observable = service.getCurrencies(param);
        observable.compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(listResultBean -> mIAssetsView.onCurrencySuccess(listResultBean), throwable -> mIAssetsView.onCurrencyError(-1, throwable.toString()));
    }

    @Override
    public void getUnreadCount(AddressParam param) {
        HttpService service = ServiceGenerator.createService(HttpService.class);
        Observable<Response<ResultBean<UnreadBean>>> observable = service.getUnreadCount(param);
        observable.compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(listResultBean -> mIAssetsView.onUnreadSuccess(listResultBean), throwable -> mIAssetsView.onUnreadError(-1, throwable.toString()));
    }

    @Override
    public void getPublicMessage(JsonObject param) {
        ServiceGenerator.createService(HttpService.class)
                .getPublicMessage(param)
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(resultBean -> mIAssetsView.onPublicMessageSuccess(resultBean)
                        , throwable -> mIAssetsView.onPublicMessageError(-1, throwable.getMessage()));
    }

    /**
     * 获取所有链列表
     * bAsset true表示按资产/ false按链
     */
    @Override
    public void getConcurrentAddress(boolean bAsset) {
        ChooseChainParam chooseChainParam = new ChooseChainParam();
        String address = CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS);
        chooseChainParam.address = address;
        chooseChainParam.type = bAsset ? "1": "0";
        HttpService service = ServiceGenerator.createService(HttpService.class);
        Observable<Response<ResultBean<List<ChainAddressBean>>>> observable = service.getConcurrent_Address(chooseChainParam);
        observable.compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(r -> mIAssetsView.onConcurrentSuccess(r)
                        , e -> mIAssetsView.onConcurrentError(-1, e.getMessage()));
    }

    public void getWaitTransList(String address) {
        JsonObject param = new JsonObject();
        param.addProperty("address", address);
        ServiceGenerator.createService(HttpService.class)
                .waitCrossTrans(param)
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(resultBean -> mIAssetsView.onWaitCrossTransSuccess(resultBean)
                        , throwable -> mIAssetsView.onWaitCrossTransError(-1, throwable.getMessage()));
    }
}
