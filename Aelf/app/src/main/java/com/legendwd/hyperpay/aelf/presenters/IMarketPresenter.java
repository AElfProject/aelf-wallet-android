package com.legendwd.hyperpay.aelf.presenters;

import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.model.param.MarketParam;

public interface IMarketPresenter {
    void getCoinList(MarketParam param, String type);

    void getMyCoinList(JsonObject jsonObject);
}
