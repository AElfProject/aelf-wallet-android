package com.legendwd.hyperpay.aelf.httpservices;

import android.content.Context;

import com.legendwd.hyperpay.aelf.AelfApplication;
import com.legendwd.hyperpay.aelf.util.LanguageUtil;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;
import com.legendwd.hyperpay.router.IMainService;

/**
 * Created by joseph on 2018/6/2.
 */
public class MainService implements IMainService {


    @Override
    public String getVersion() {
        return null;
    }

    @Override
    public Context getContext() {
        return AelfApplication.getSContext();
    }

    @Override
    public String getAddress() {
        return CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS);
    }

    @Override
    public String getCurrency() {
        return CacheUtil.getInstance().getProperty(Constant.Sp.PRICING_CURRENCY_ID_DEFAULT);
    }

    @Override
    public String getLang() {
        return LanguageUtil.readLanguage();
    }
}