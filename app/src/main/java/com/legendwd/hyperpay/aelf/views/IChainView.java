package com.legendwd.hyperpay.aelf.views;


import com.legendwd.hyperpay.aelf.model.bean.ChainAddressBean;
import com.legendwd.hyperpay.aelf.model.bean.ChooseChainsBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;

import java.util.List;

public interface IChainView {

    void onChainsSuccess(ResultBean<List<ChainAddressBean>> resultBean);

    void onChainsError(int code, String msg);

}
