package com.legendwd.hyperpay.aelf.views;

import com.legendwd.hyperpay.aelf.model.bean.MarketListBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;

public interface IMarketView {
    void onCoinListSuccess(ResultBean<MarketListBean> resultBean, String type);

    void onCoinListError(int code, String msg, String type);

    void onMyCoinListSuccess(ResultBean<MarketListBean> marketListBeanResultBean);

    void onMyCoinListError(int i, String message);
}
