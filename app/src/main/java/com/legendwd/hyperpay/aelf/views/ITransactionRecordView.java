package com.legendwd.hyperpay.aelf.views;

import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.TransactionBean;
import com.legendwd.hyperpay.aelf.model.bean.TransferBalanceBean;

public interface ITransactionRecordView {

    void onTransactionDetailSuccess(TransactionBean.ListBean bean);

    void onTransactionDetailError(int code, String msg);

    void onCrossChainSuccess(ResultBean resultBean);

    void onCrossChainError(int code, String msg);

    void onAddIndexSuccess(ResultBean resultBean);

    void onAddIndexError(int i, String message);

    void onTransferBalanceSuccess(ResultBean<TransferBalanceBean> listResultBean);

    void onTransferBalanceError(int i, String toString);
}
