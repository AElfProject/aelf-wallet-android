package com.legendwd.hyperpay.aelf.views;

import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.TransactionBean;

public interface ITestView {
    void onTestSuccess(ResultBean<TransactionBean.ListBean> bean);

    void onTestError(int code, String msg);

}
