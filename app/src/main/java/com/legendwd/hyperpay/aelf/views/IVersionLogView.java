package com.legendwd.hyperpay.aelf.views;

import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.VersionLogBean;

public interface IVersionLogView {

    void onVersionLogSuccess(ResultBean<VersionLogBean> bean);

    void onVersionLogError(int code, String msg);

}
