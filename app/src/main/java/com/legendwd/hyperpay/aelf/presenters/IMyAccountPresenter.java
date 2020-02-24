package com.legendwd.hyperpay.aelf.presenters;

import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.model.param.UploadDataParam;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * created by joseph at 2019/6/18
 */

public interface IMyAccountPresenter {
    void getChainInfo();

    void updateIdentityName(JsonObject param);

    void updateIdentityCover(String path, MultipartBody.Part image, RequestBody address, RequestBody device, RequestBody udid, RequestBody version, RequestBody fileName, RequestBody test);

    void uploadData(UploadDataParam param);

    void bindCoin(JsonObject param);
}
