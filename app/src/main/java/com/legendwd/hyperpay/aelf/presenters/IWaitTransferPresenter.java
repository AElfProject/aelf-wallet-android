package com.legendwd.hyperpay.aelf.presenters;

/**
 * @author lovelyzxing
 * @date 2019/6/10
 * @Description
 */
public interface IWaitTransferPresenter {

    void rcvTxid(String fromTxid, String toTxid);

    void getWaitTransList(String property);

    void getCrossChains();
}
