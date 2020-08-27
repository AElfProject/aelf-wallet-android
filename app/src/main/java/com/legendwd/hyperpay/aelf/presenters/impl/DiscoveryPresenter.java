package com.legendwd.hyperpay.aelf.presenters.impl;

import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.BuildConfig;
import com.legendwd.hyperpay.aelf.business.discover.dapp.GameListBean;
import com.legendwd.hyperpay.aelf.httpservices.HttpService;
import com.legendwd.hyperpay.aelf.httpservices.ResponseTransformer;
import com.legendwd.hyperpay.aelf.model.bean.ChooseChainsBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.param.BaseParam;
import com.legendwd.hyperpay.aelf.presenters.BasePresenter;
import com.legendwd.hyperpay.aelf.presenters.IDiscoveryPresenter;
import com.legendwd.hyperpay.aelf.views.IDiscoveryView;
import com.legendwd.hyperpay.httputil.ServiceGenerator;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscoveryPresenter extends BasePresenter implements IDiscoveryPresenter {

    IDiscoveryView mDiscoveryView;

    public DiscoveryPresenter(IDiscoveryView discoveryView) {
        super((LifecycleProvider<ActivityEvent>) discoveryView);
        mDiscoveryView = discoveryView;
    }

    @Override
    public void getDapp() {
        HttpService service = ServiceGenerator.createService(HttpService.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("device", "Android");
        jsonObject.addProperty("udid", CacheUtil.getInstance().getProperty(Constant.Sp.UDID));
        jsonObject.addProperty("version", BuildConfig.VERSION_NAME);
        service.getDapp(jsonObject)
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(discoveryBeanResultBean -> mDiscoveryView.onDappSuccess(discoveryBeanResultBean.getData()), new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        mDiscoveryView.onDappError(-1, throwable.getMessage());
                    }
                });
    }

    /**
     * 获取游戏列表
     */
    @Override
    public void getGameList(JsonObject jsonObject,String refreshType) {
        HttpService service = ServiceGenerator.createService(HttpService.class);
        Observable<Response<ResultBean<GameListBean>>> observable = service.getGams(jsonObject);
        observable.compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(listResultBean -> mDiscoveryView.onGameListSuccess(listResultBean.getData(),refreshType),
                        throwable -> mDiscoveryView.onGameListError(-1, throwable.toString()));
    }

    /**
     */
    @Override
    public void getBridgeApi(String id, String baseUrl,String method,String param) {
        HttpService service = ServiceGenerator.createServiceByBase(HttpService.class,baseUrl);
        Call<String> call = null;
        if("transactionResult".equalsIgnoreCase(method)){
            call = service.get_trans_game(param);
        }
        if(call!=null){
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    mDiscoveryView.onApiSuccessForDapp(id, response.body());
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    mDiscoveryView.onApiErrorForDapp(id,-1, t.toString());
                }
            });
        }

    }

    /**
     * 获取所有链信息,查询nodeurl
     */
    @Override
    public void getCrossChains() {
        BaseParam baseParam = new BaseParam();
        HttpService service = ServiceGenerator.createService(HttpService.class);
        Observable<Response<ResultBean<List<ChooseChainsBean>>>> observable = service.getCrossChains(baseParam);
        observable.compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(r -> mDiscoveryView.onChainsSuccess(r)
                        , e -> mDiscoveryView.onChainsError(-1, e.getMessage()));
    }
}
