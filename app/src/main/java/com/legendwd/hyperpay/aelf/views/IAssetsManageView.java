package com.legendwd.hyperpay.aelf.views;

import com.legendwd.hyperpay.aelf.model.bean.AssetsBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;

import java.util.List;
import java.util.Map;

public interface IAssetsManageView {

    void onAssetsSuccess(ResultBean<Map<String, List<AssetsBean>>> resultBean);

    void onAssetsError(int code, String msg);

    void onBindSuccess(ResultBean resultBean, AssetsBean aelfBean, int i);

    void onBindError(int code, String msg, AssetsBean aelfBean, int i);

    void showLoading();
    void dismissLoading();

}
