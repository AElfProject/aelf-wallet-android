package com.legendwd.hyperpay.aelf.views;

import com.legendwd.hyperpay.aelf.model.bean.CurrenciesBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;

public interface IWelcomeView {
    void onCurrencySuccess(ResultBean<CurrenciesBean> listResultBean);

    void onCurrencyError(int code, String msg);

    void onUploadDataSuccess(ResultBean<Object> beanList);

    void onUploadDataFail(int code, String msg);
}
