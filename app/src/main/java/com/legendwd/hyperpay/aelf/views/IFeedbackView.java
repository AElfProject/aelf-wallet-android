package com.legendwd.hyperpay.aelf.views;

import com.legendwd.hyperpay.aelf.model.bean.ResultBean;

public interface IFeedbackView {
    void onFeedbackSuccess(ResultBean<Object> bean);

    void onFeedbackFail(int code, String msg);

}
