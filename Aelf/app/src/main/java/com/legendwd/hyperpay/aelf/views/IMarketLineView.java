package com.legendwd.hyperpay.aelf.views;

import com.legendwd.hyperpay.aelf.model.bean.CoinDetailBean;
import com.legendwd.hyperpay.aelf.model.bean.MarketLineBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;

public interface IMarketLineView {

    void onSuccess(ResultBean<MarketLineBean> bean);

    void onError(int code, String msg);

    void onCoinDetailSuccess(ResultBean<CoinDetailBean> bean);

    void onCoinDetailError(int code, String msg);

}
