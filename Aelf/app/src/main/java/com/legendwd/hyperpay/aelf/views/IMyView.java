package com.legendwd.hyperpay.aelf.views;

import com.legendwd.hyperpay.aelf.model.bean.IdentityBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.UnreadBean;

public interface IMyView {
    void onUnreadSuccess(ResultBean<UnreadBean> listResultBean);

    void onUnreadError(int code, String msg);

    void onIdentitySuccess(ResultBean<IdentityBean> resultBean);

    void onIdentityError(int code, String msg);
}

