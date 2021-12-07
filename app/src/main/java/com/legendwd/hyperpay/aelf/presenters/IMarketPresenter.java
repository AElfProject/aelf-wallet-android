package com.legendwd.hyperpay.aelf.presenters;

import com.legendwd.hyperpay.aelf.model.param.MarketParam;

public interface IMarketPresenter {
    void getCoinList(MarketParam param, String type);
    void getAelfCoinList(MarketParam param, String type,String sort);
    void getMyCoinList(MarketParam param);
}
