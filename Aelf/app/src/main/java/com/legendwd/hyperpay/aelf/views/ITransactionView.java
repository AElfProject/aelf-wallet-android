package com.legendwd.hyperpay.aelf.views;

import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.TransactionBean;

public interface ITransactionView {

    void onSuccess(ResultBean<TransactionBean> resultBean, String page,String refreshType);

    void onError(int code, String msg, String page,String refreshType);

}
