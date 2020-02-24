package com.legendwd.hyperpay.aelf.views;

import com.legendwd.hyperpay.aelf.model.bean.AddressBookBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;

public interface IAddressBookView {

    void onSuccess(ResultBean<AddressBookBean> listResultBean);

    void onError(int code, String msg);

    void onDelSuccess(ResultBean resultBean);

    void onDelError(int code, String msg, AddressBookBean.ListBean bean, int index);

}
