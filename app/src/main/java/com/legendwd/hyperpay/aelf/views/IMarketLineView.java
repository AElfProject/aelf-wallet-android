package com.legendwd.hyperpay.aelf.views;

import com.legendwd.hyperpay.aelf.model.bean.MarketDataBean;
import com.legendwd.hyperpay.aelf.model.bean.MarketLineBean;

import java.util.List;

public interface IMarketLineView {

    void onSuccess(MarketLineBean bean);

    void onError(int code, String msg);

    void onCoinDetailSuccess(List<MarketDataBean> bean);

    void onCoinDetailError(int code, String msg);

}
