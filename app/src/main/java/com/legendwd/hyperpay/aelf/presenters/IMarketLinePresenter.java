package com.legendwd.hyperpay.aelf.presenters;

import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.model.param.MarketLineParam;

/**
 * @author lovelyzxing
 * @date 2019/6/10
 * @Description
 */
public interface IMarketLinePresenter {
    void getTradeLine(MarketLineParam param);

    void getCoinDetail(JsonObject param);
}
