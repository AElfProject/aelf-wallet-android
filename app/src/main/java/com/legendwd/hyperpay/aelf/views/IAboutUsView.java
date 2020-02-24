package com.legendwd.hyperpay.aelf.views;

import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.UpdateBean;

public interface IAboutUsView {
    void onUpdateSuccess(ResultBean<UpdateBean> bean);

    void onUpdateError(int code, String msg);

}
