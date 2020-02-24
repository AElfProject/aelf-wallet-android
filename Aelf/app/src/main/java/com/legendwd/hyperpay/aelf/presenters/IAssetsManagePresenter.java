package com.legendwd.hyperpay.aelf.presenters;

import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.model.bean.AssetsBean;

public interface IAssetsManagePresenter {
    void getAddAssetsList();

    void bind(JsonObject param, AssetsBean aelfBean, int i);
}
