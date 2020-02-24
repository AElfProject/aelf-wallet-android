package com.legendwd.hyperpay.aelf.presenters;

import com.legendwd.hyperpay.aelf.model.param.TransferBalanceParam;

/**
 * @author lovelyzxing
 * @date 2019/6/10
 * @Description
 */
public interface ITransferPresenter {
    void getTransferBalance(TransferBalanceParam param);

    void getConcurrentAddress();

    void getCrossChains();
    void getCrossChainsForDapp(String id);

}
