package com.legendwd.hyperpay.aelf.presenters.impl;

import com.legendwd.hyperpay.aelf.httpservices.HttpService;
import com.legendwd.hyperpay.aelf.httpservices.ResponseTransformer;
import com.legendwd.hyperpay.aelf.model.bean.ChainAddressBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.ChooseChainsBean;
import com.legendwd.hyperpay.aelf.model.param.BaseParam;
import com.legendwd.hyperpay.aelf.model.param.ChooseChainParam;
import com.legendwd.hyperpay.aelf.presenters.BasePresenter;
import com.legendwd.hyperpay.aelf.presenters.IChainPresenter;
import com.legendwd.hyperpay.aelf.views.IChainView;
import com.legendwd.hyperpay.httputil.ServiceGenerator;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;

public class ChooseChainPresenter extends BasePresenter implements IChainPresenter {

    IChainView mIChainView;

    public ChooseChainPresenter(IChainView iChainView) {
        super((LifecycleProvider<ActivityEvent>) iChainView);
        mIChainView = iChainView;
    }

    @Override
    public void getCrossChains(ChooseChainParam chooseChainParam) {
        HttpService service = ServiceGenerator.createService(HttpService.class);
        Observable<Response<ResultBean<List<ChainAddressBean>>>> observable = service.getConcurrent_Address(chooseChainParam);
        observable.compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(r -> mIChainView.onChainsSuccess(r)
                        , e -> mIChainView.onChainsError(-1, e.getMessage()));
    }

}
