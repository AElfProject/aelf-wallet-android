package com.legendwd.hyperpay.aelf.views;

import com.legendwd.hyperpay.aelf.business.discover.dapp.GameListBean;
import com.legendwd.hyperpay.aelf.model.bean.ChooseChainsBean;
import com.legendwd.hyperpay.aelf.model.bean.DappListBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;

import java.util.List;

public interface IDiscoveryView {
    void onDappSuccess(DappListBean discoveryBean);

    void onDappError(int code, String msg);

    void onGameListSuccess(GameListBean gameListBean,String refreshType);

    void onGameListError(int code, String msg);

    void onApiSuccessForDapp(String id, String json);

    void onApiErrorForDapp(String id ,int code, String msg);

    void onChainsSuccess(ResultBean<List<ChooseChainsBean>> resultBean);

    void onChainsError(int code, String msg);

}
