package com.legendwd.hyperpay.aelf.presenters.impl;

import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.httpservices.HttpService;
import com.legendwd.hyperpay.aelf.httpservices.ResponseTransformer;
import com.legendwd.hyperpay.aelf.model.bean.ChainBean;
import com.legendwd.hyperpay.aelf.model.bean.ChooseChainsBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.param.UploadDataParam;
import com.legendwd.hyperpay.aelf.presenters.BasePresenter;
import com.legendwd.hyperpay.aelf.presenters.IMyAccountPresenter;
import com.legendwd.hyperpay.aelf.views.IMyAccountView;
import com.legendwd.hyperpay.httputil.ServiceGenerator;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

/**
 * created by joseph at 2019/6/18
 */

public class MyAccountPresenter extends BasePresenter implements IMyAccountPresenter {
    IMyAccountView mAccountView;
    private int mIndex = 0;
    private List<ChooseChainsBean> mBeans;

    public MyAccountPresenter( IMyAccountView accountView) {
        super((LifecycleProvider<ActivityEvent>) accountView);
        mAccountView = accountView;
    }

    @Override
    public void getChainInfo() {
        ServiceGenerator.createService(HttpService.class)
                .getChainInfo()
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(new Consumer<ResultBean<ChainBean>>() {
                    @Override
                    public void accept(ResultBean<ChainBean> resultBean) throws Exception {
                        mAccountView.onChaininfoSuccess(resultBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mAccountView.onChaininfoError(-1, throwable.getMessage());
                    }
                });

    }

    @Override
    public void updateIdentityName(JsonObject param) {
        ServiceGenerator.createService(HttpService.class)
                .updateIdentityName(param)
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        mAccountView.onUpdateIdentityNameSuccess(param.get("name").getAsString(), resultBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mAccountView.onUpdateIdentityNameError(-1, throwable.getMessage());
                    }
                });
    }

    @Override
    public void updateIdentityCover(String path, MultipartBody.Part image, RequestBody address, RequestBody device, RequestBody udid, RequestBody version, RequestBody fileName, RequestBody test) {
        ServiceGenerator.createService(HttpService.class)
                .updateIdentityCover(image, address, device, udid, version, fileName)
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        mAccountView.onUpdateIdentitySuccess(path, resultBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mAccountView.onUpdateIdentityError(-1, throwable.getMessage());
                    }
                });
    }

    @Override
    public void uploadData(UploadDataParam param) {
        HttpService service = ServiceGenerator.createService(HttpService.class);
        Observable<Response<ResultBean<Object>>> observable = service.uploadData(param);
        observable.compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(listResultBean -> mAccountView.onUploadDataSuccess(listResultBean), throwable -> mAccountView.onUploadDataFail(-1, throwable.toString()));

    }

    @Override
    public void bindCoin(JsonObject param) {

        param.addProperty("init", "1");
        ServiceGenerator.createService(HttpService.class)
            .bind(param)
            .compose(ResponseTransformer.handleResult(getProvider()))
            .subscribe(new Consumer<ResultBean>() {
                @Override
                public void accept(ResultBean resultBean) throws Exception {
                    mAccountView.onBindCoinSuccess(resultBean);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    mAccountView.onBindCoinFail(-1, throwable.getMessage());
                }
            });
    }

//    public void getCrossChains() {
//        BaseParam baseParam = new BaseParam();
//        HttpService service = ServiceGenerator.createService(HttpService.class);
//        Observable<Response<ResultBean<List<ChooseChainsBean>>>> observable = service.getCrossChains(baseParam);
//        observable.compose(ResponseTransformer.handleResult(getProvider()))
//                .subscribe(r -> onChainsSuccess(r)
//                        , e -> mAccountView.onBindCoinFail(-1, e.getMessage()));
//    }

    private void onChainsSuccess(ResultBean<List<ChooseChainsBean>> bean) {
        mBeans = bean.getData();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("address", CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS));
        jsonObject.addProperty("flag", "1");
        jsonObject.addProperty("signed_address", CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_SIGNED_ADDRESS));
        jsonObject.addProperty("public_key", CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_PUBLIC_KEY));
        bindAllCoin(jsonObject,0);
    }

    private void bindAllCoin(JsonObject param, int index) {
        mIndex = index;
        param.addProperty("symbol", mBeans.get(mIndex).getSymbol());
        param.addProperty("chainid", mBeans.get(mIndex).getName());
        param.addProperty("contract_address", mBeans.get(mIndex).getContract_address());
        ServiceGenerator.createService(HttpService.class)
                .bind(param)
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        if (mIndex >= mBeans.size() - 1) {
                            mAccountView.onBindCoinSuccess(resultBean);
                        } else {
                            bindAllCoin(param, mIndex + 1);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mAccountView.onBindCoinFail(-1, throwable.getMessage());
                        if (mIndex < mBeans.size() - 1) {
                            bindAllCoin(param, mIndex + 1);
                        }
                    }
                });
    }

}
