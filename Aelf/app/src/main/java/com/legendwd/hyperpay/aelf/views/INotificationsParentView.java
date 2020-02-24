package com.legendwd.hyperpay.aelf.views;

import com.legendwd.hyperpay.aelf.model.bean.ResultBean;

public interface INotificationsParentView {

    void onClearSuccess(ResultBean resultBean);

    void onClearError(int code, String msg);

}
