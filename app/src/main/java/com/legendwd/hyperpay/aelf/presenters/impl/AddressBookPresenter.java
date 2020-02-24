package com.legendwd.hyperpay.aelf.presenters.impl;

import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.httpservices.HttpService;
import com.legendwd.hyperpay.aelf.httpservices.ResponseTransformer;
import com.legendwd.hyperpay.aelf.model.bean.AddressBookBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.param.AddressBookParam;
import com.legendwd.hyperpay.aelf.presenters.BasePresenter;
import com.legendwd.hyperpay.aelf.presenters.IAddressBookPresenter;
import com.legendwd.hyperpay.aelf.views.IAddressBookView;
import com.legendwd.hyperpay.httputil.ServiceGenerator;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.functions.Consumer;

/**
 * @author lovelyzxing
 * @date 2019/6/10
 * @Description
 */
public class AddressBookPresenter extends BasePresenter implements IAddressBookPresenter {

    private IAddressBookView iView;

    public AddressBookPresenter(IAddressBookView view) {
        super((LifecycleProvider<ActivityEvent>) view);
        this.iView = view;
    }


    @Override
    public void getAddresBook(AddressBookParam param) {
        HttpService service = ServiceGenerator.createService(HttpService.class);
        service.getAddressBook(param)
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(new Consumer<ResultBean<AddressBookBean>>() {
                    @Override
                    public void accept(ResultBean<AddressBookBean> addressBeanResultListBean) throws Exception {
                        iView.onSuccess(addressBeanResultListBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        iView.onError(-1, throwable.getMessage());
                    }
                });
    }

    @Override
    public void delAddress(JsonObject param, AddressBookBean.ListBean bean, int positon) {
        ServiceGenerator.createService(HttpService.class)
                .delAddress(param)
                .compose(ResponseTransformer.handleResult(getProvider()))
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        iView.onDelSuccess(resultBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        iView.onDelError(-1, throwable.getMessage(), bean, positon);
                    }
                });
    }

}
