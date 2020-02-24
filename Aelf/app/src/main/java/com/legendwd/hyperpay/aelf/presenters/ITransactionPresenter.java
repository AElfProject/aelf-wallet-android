package com.legendwd.hyperpay.aelf.presenters;

import com.legendwd.hyperpay.aelf.model.param.TransactionParam;

/**
 * @author lovelyzxing
 * @date 2019/6/10
 * @Description
 */
public interface ITransactionPresenter {
    void getTransaction(TransactionParam param, String page,String refreshType);
}
