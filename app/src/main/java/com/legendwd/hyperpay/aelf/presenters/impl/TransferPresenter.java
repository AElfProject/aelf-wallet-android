package com.legendwd.hyperpay.aelf.presenters.impl;

import com.legendwd.hyperpay.aelf.httpservices.HttpService;
import com.legendwd.hyperpay.aelf.httpservices.ResponseTransformer;
import com.legendwd.hyperpay.aelf.model.bean.ChainAddressBean;
import com.legendwd.hyperpay.aelf.model.bean.ChooseChainsBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.TransferBalanceBean;
import com.legendwd.hyperpay.aelf.model.param.BaseParam;
import com.legendwd.hyperpay.aelf.model.param.ChooseChainParam;
import com.legendwd.hyperpay.aelf.model.param.TransferBalanceParam;
import com.legendwd.hyperpay.aelf.presenters.BasePresenter;
import com.legendwd.hyperpay.aelf.presenters.ITransferPresenter;
import com.legendwd.hyperpay.aelf.views.ITransferView;
import com.legendwd.hyperpay.httputil.ServiceGenerator;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;
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
public class TransferPresenter extends BasePresenter implements ITransferPresenter {

    private ITransferView iView;

    public TransferPresenter(ITransferView view) {
        super((LifecycleProvider<ActivityEvent>) view);
        this.iView = view;
    }


    @Override
    public void getTransferBalance(TransferBalanceParam param) {
        HttpService service = ServiceGenerator.createService(HttpService.class);
        Observable<Response<ResultBean<TransferBalanceBean>>> observable = service.getTransferBalance(param);
        observable.compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(listResultBean -> iView.onTransferBalanceSuccess(listResultBean, param.chainid), throwable -> iView.onTransferBalanceError(-1, throwable.toString()));
    }

    @Override
    public void getConcurrentAddress() {
        ChooseChainParam chooseChainParam = new ChooseChainParam();
        String wallet_address = CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS);
        chooseChainParam.address = wallet_address;
        chooseChainParam.type = "0";
        HttpService service = ServiceGenerator.createService(HttpService.class);
        Observable<Response<ResultBean<List<ChainAddressBean>>>> observable = service.getConcurrent_Address(chooseChainParam);
        observable.compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(r -> iView.onConcurrentSuccess(r)
                        , e -> iView.onConcurrentError(-1, e.getMessage()));
    }

    /**
     * 获取所有链信息
     */
    @Override
    public void getCrossChains() {
        BaseParam baseParam = new BaseParam();
        HttpService service = ServiceGenerator.createService(HttpService.class);
        Observable<Response<ResultBean<List<ChooseChainsBean>>>> observable = service.getCrossChains(baseParam);
        observable.compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(r -> iView.onChainsSuccess(r)
                        , e -> iView.onChainsError(-1, e.getMessage()));
    }

    @Override
    public void getCrossChainsForDapp(String id) {

    }


    /**
     * 获取所有链信息
     */
    public void getCrossChains_forDapp(String id) {
        BaseParam baseParam = new BaseParam();
        HttpService service = ServiceGenerator.createService(HttpService.class);
        Observable<Response<ResultBean<List<ChooseChainsBean>>>> observable = service.getCrossChains(baseParam);
        observable.compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(r -> iView.onChainsSuccessForDapp(id, r)
                        , e -> iView.onChainsErrorForDapp(id, -1, e.getMessage()));
    }
}
