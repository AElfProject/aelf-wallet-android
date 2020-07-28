package com.legendwd.hyperpay.aelf.views;

import com.legendwd.hyperpay.aelf.model.bean.AssetsListBean;
import com.legendwd.hyperpay.aelf.model.bean.ChainAddressBean;
import com.legendwd.hyperpay.aelf.model.bean.CurrenciesBean;
import com.legendwd.hyperpay.aelf.model.bean.MarketDataBean;
import com.legendwd.hyperpay.aelf.model.bean.PublicMessageBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.UnreadBean;
import com.legendwd.hyperpay.aelf.model.bean.WaitTransactionBean;

import java.util.List;

public interface IAssetsView {

    void onAssetsSuccess(ResultBean<AssetsListBean> resultBean);

    void onAssetsError(int code, String msg);


    void onCurrencySuccess(ResultBean<CurrenciesBean> listResultBean);

    void onCurrencyError(int code, String msg);

    void onUnreadSuccess(ResultBean<UnreadBean> listResultBean);

    void onUnreadError(int code, String msg);

    void onPublicMessageSuccess(ResultBean<PublicMessageBean> resultBean);

    void onPublicMessageError(int code, String msg);

    void onConcurrentSuccess(ResultBean<List<ChainAddressBean>> resultBean);

    void onConcurrentError(int code, String msg);

    void onWaitCrossTransSuccess(ResultBean<WaitTransactionBean> resultBean);

    void onWaitCrossTransError(int i, String message);

    void onCoinListSuccess(List<MarketDataBean> marketListBeanResultBean, String type);

    void onCoinListError(int i, String message, String type);
}
