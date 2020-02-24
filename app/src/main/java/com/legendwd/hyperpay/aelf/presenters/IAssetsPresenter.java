package com.legendwd.hyperpay.aelf.presenters;

import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.model.param.AddressParam;
import com.legendwd.hyperpay.aelf.model.param.BaseParam;

public interface IAssetsPresenter {

    void getAssetsList(String address);

    void getCurrencies(BaseParam param);

    void getUnreadCount(AddressParam param);

    void getPublicMessage(JsonObject param);

    void getConcurrentAddress(boolean bAsset);
}
