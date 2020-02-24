package com.legendwd.hyperpay.aelf.presenters;

import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.model.bean.AddressBookBean;
import com.legendwd.hyperpay.aelf.model.param.AddressBookParam;

/**
 * @author lovelyzxing
 * @date 2019/6/10
 * @Description
 */
public interface IAddressBookPresenter {
    void getAddresBook(AddressBookParam param);

    void delAddress(JsonObject jsonObject, AddressBookBean.ListBean bean, int positon);
}
