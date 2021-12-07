package com.legendwd.hyperpay.aelf.views;

import com.legendwd.hyperpay.aelf.model.bean.MarketDataBean;

import java.util.List;

public interface IMarketView {
    void onCoinListSuccess(List<MarketDataBean> resultBean, String type);

    void onAelfCoinListSuccess(List<MarketDataBean> resultBean, String type,String sort);

    void onCoinListError(int code, String msg, String type);

    void onMyCoinListSuccess(List<MarketDataBean> dataBean);

    void onMyCoinListError(int i, String message);
}
