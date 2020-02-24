package com.legendwd.hyperpay.aelf.presenters;

import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.model.param.TransferBalanceParam;
import com.legendwd.hyperpay.aelf.model.param.TransferCrossChainParam;

public interface ITransactionRecordPresenter {

    void getTransactionDetail(JsonObject param);

    void getCrossChainDetail(TransferCrossChainParam transferCrossChainParam);

    void addIndex(TransferCrossChainParam transferCrossChainParam);

    void getTransferBalance(TransferBalanceParam param);
}
