package com.legendwd.hyperpay.aelf.views;

import com.legendwd.hyperpay.aelf.db.MarketCoindb;

import java.util.List;

public interface IMarketHomeView {
    void onCoinListSuccess(List<MarketCoindb> list);

    void onCoinListError(int code, String msg);

}
