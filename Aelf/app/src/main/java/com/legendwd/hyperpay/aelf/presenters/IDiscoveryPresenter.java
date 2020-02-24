package com.legendwd.hyperpay.aelf.presenters;

import com.google.gson.JsonObject;

public interface IDiscoveryPresenter {
    void getDapp();

    void getGameList(JsonObject jsonObject,String refreshType);

    void getBridgeApi(String id, String baseUrl, String method,String param);

    void getCrossChains();
}
