package com.legendwd.hyperpay.aelf.views;

import com.legendwd.hyperpay.aelf.model.bean.ChainBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;

/**
 * created by joseph at 2019/6/18
 */

public interface IMyAccountView {
    void onUpdateIdentitySuccess(String path, ResultBean resultBean);

    void onUpdateIdentityError(int code, String msg);

    void onUpdateIdentityNameSuccess(String name, ResultBean resultBean);

    void onUpdateIdentityNameError(int code, String msg);

    void onChaininfoSuccess(ResultBean<ChainBean> resultBean);

    void onChaininfoError(int code, String msg);

    void onUploadDataSuccess(ResultBean<Object> beanList);

    void onUploadDataFail(int code, String msg);

    void onBindCoinSuccess(ResultBean resultBean);

    void onBindCoinFail(int code, String msg);

}
