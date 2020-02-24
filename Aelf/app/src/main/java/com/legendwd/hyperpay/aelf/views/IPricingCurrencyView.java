package com.legendwd.hyperpay.aelf.views;

import com.legendwd.hyperpay.aelf.model.bean.CurrenciesBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;

public interface IPricingCurrencyView {

    void onSuccess(ResultBean<CurrenciesBean> listResultBean);

    void onError(int code, String msg);

}
