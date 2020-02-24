package com.legendwd.hyperpay.aelf.presenters;

import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.model.param.AddressParam;

public interface IMyPresenter {

    void getUnreadCount(AddressParam param);

    void getIdentity(JsonObject param);
}
