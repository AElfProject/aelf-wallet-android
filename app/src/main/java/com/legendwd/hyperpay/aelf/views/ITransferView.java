package com.legendwd.hyperpay.aelf.views;

import com.legendwd.hyperpay.aelf.model.bean.ChainAddressBean;
import com.legendwd.hyperpay.aelf.model.bean.ChooseChainsBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.TransferBalanceBean;

import java.util.List;

public interface ITransferView {

    void onTransferBalanceSuccess(ResultBean<TransferBalanceBean> bean, String chainid);

    void onTransferBalanceError(int code, String msg);

    void onConcurrentSuccess(ResultBean<List<ChainAddressBean>> resultBean);

    void onConcurrentError(int code, String msg);


    void onChainsSuccess(ResultBean<List<ChooseChainsBean>> resultBean);

    void onChainsError(int code, String msg);


    void onChainsSuccessForDapp(String id, ResultBean<List<ChooseChainsBean>> resultBean);

    void onChainsErrorForDapp(String id, int code, String msg);
}
