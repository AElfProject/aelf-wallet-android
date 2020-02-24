package com.legendwd.hyperpay.aelf.presenters;

import com.legendwd.hyperpay.aelf.model.param.BaseParam;
import com.legendwd.hyperpay.aelf.model.param.UploadDataParam;

public interface IWelcomePresenter {
    void getCurrencies(BaseParam param);

    void uploadData(UploadDataParam param);
}

