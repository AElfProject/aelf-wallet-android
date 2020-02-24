package com.legendwd.hyperpay.aelf.views;

import com.legendwd.hyperpay.aelf.model.bean.ResultBean;

public interface IAddContactView {

    void onSuccess(ResultBean bean);

    void onError(int code, String msg);

}
