package com.legendwd.hyperpay.aelf.views;

import com.legendwd.hyperpay.aelf.model.bean.ChooseChainsBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.WaitTransactionBean;

import java.util.List;

public interface IWaitTransferView {

    void onRcvTxidSuccess(ResultBean resultBean);

    void onRcvTxidError(int i, String message);

    void onWaitCrossTransSuccess(ResultBean<WaitTransactionBean> resultBean);

    void onWaitCrossTransError(int i, String message);

    void onChainsSuccess(ResultBean<List<ChooseChainsBean>> r);

    void onChainsError(int i, String message);
}
