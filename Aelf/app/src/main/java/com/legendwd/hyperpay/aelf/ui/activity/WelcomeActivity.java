package com.legendwd.hyperpay.aelf.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.legendwd.hyperpay.aelf.MainActivity;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseActivity;
import com.legendwd.hyperpay.aelf.business.discover.dapp.GameWebActivity;
import com.legendwd.hyperpay.aelf.business.wallet.CreateImportWalletActivity;
import com.legendwd.hyperpay.aelf.model.bean.CurrenciesBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.param.BaseParam;
import com.legendwd.hyperpay.aelf.model.param.UploadDataParam;
import com.legendwd.hyperpay.aelf.presenters.IWelcomePresenter;
import com.legendwd.hyperpay.aelf.presenters.impl.WelcomePresenter;
import com.legendwd.hyperpay.aelf.util.LanguageUtil;
import com.legendwd.hyperpay.aelf.views.IWelcomeView;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import java.util.List;


public class WelcomeActivity extends BaseActivity implements IWelcomeView {
    private IWelcomePresenter presenter;

    private boolean mFlagNet = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).keyboardEnable(true)
                .statusBarColorInt(Color.WHITE)
                .navigationBarColorInt(Color.WHITE)
                .autoDarkModeEnable(true, 0.2f).init();

        String defaultCurrency = CacheUtil.getInstance().getProperty(Constant.Sp.PRICING_CURRENCY_DEFAULT);

        presenter = new WelcomePresenter(this);


        uploadData();

        if (TextUtils.isEmpty(defaultCurrency)) {
            presenter.getCurrencies(new BaseParam());
        } else {
            mFlagNet = true;
            getWindow().getDecorView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    switchActivity();
                }
            }, 1500);
        }

    }

    private void uploadData() {
        UploadDataParam param = new UploadDataParam();
        param.address = CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS);
        if (TextUtils.isEmpty(param.address))
            return;
        param.androidNoticeToken = CacheUtil.getInstance().getProperty(Constant.Sp.DEVICE_TOKEN);
        if (TextUtils.isEmpty(param.androidNoticeToken))
            return;
        param.parent = "elf";
        param.deviceInfo = android.os.Build.BRAND + ":" + android.os.Build.MODEL + ":" + android.os.Build.VERSION.RELEASE;
        presenter.uploadData(param);
    }

    private void switchActivity() {
        if (mFlagNet) {
            if (CacheUtil.getInstance().getProperty(Constant.Sp.IS_WALLET_EXISTS, false)) {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            } else {
                startActivity(new Intent(WelcomeActivity.this, CreateImportWalletActivity.class));
                finish();
            }
        }
    }


    @Override
    public void onCurrencySuccess(ResultBean<CurrenciesBean> bean) {
        if (null == bean || null == bean.getData())
            return;

        List<CurrenciesBean.ListBean> beanList = bean.getData().list;

        if (null != beanList && beanList.size() > 0) {
            for (CurrenciesBean.ListBean listBean : beanList) {
                if ("USD".equals(listBean.id)) {
                    listBean.symbol = "$";
                } else if ("CNY".equals(listBean.id)) {
                    listBean.symbol = "¥";
                } else if ("AUD".equals(listBean.id)) {
                    listBean.symbol = "A$";
                } else if ("KRW".equals(listBean.id)) {
                    listBean.symbol = "₩";
                } else {
                    listBean.symbol = "$";
                }
            }
        }

        CacheUtil.getInstance().setProperty(Constant.Sp.PRICING_CURRENCY, new Gson().toJson(bean.getData()));

        setCurrencyDefault(bean.getData().list);

        mFlagNet = true;
        switchActivity();
    }

    private void setCurrencyDefault(List<CurrenciesBean.ListBean> beanList) {
        if (null == beanList || beanList.size() < 0) {
            return;
        }

        String json = CacheUtil.getInstance().getProperty(Constant.Sp.PRICING_CURRENCY_DEFAULT);
        if (TextUtils.isEmpty(json)) {
            beanList.get(0).isSelected = true;
            CacheUtil.getInstance().setProperty(Constant.Sp.PRICING_CURRENCY_DEFAULT, new Gson().toJson(beanList.get(0)));
            CacheUtil.getInstance().setProperty(Constant.Sp.PRICING_CURRENCY_SYMBOL_DEFAULT, beanList.get(0).symbol);
            CacheUtil.getInstance().setProperty(Constant.Sp.PRICING_CURRENCY_ID_DEFAULT, beanList.get(0).id);
            return;
        }

        CurrenciesBean.ListBean listBean = new Gson().fromJson(json, CurrenciesBean.ListBean.class);
        if (null != listBean) {
            for (CurrenciesBean.ListBean bean : beanList) {

                if (TextUtils.equals(listBean.id, bean.id)) {
                    bean.isSelected = true;
                    CacheUtil.getInstance().setProperty(Constant.Sp.PRICING_CURRENCY_DEFAULT, new Gson().toJson(bean));
                } else {
                    bean.isSelected = false;
                }
            }
        }


    }

    @Override
    public void onCurrencyError(int code, String msg) {
        CurrenciesBean.ListBean bean = new CurrenciesBean.ListBean();
        bean.isSelected = true;
        bean.symbol = "$";
        bean.id = "USD";

        CacheUtil.getInstance().setProperty(Constant.Sp.PRICING_CURRENCY_DEFAULT, new Gson().toJson(bean));
        CacheUtil.getInstance().setProperty(Constant.Sp.PRICING_CURRENCY_SYMBOL_DEFAULT, bean.symbol);
        CacheUtil.getInstance().setProperty(Constant.Sp.PRICING_CURRENCY_ID_DEFAULT, bean.id);

        mFlagNet = true;
        switchActivity();
    }

    @Override
    public void onUploadDataSuccess(ResultBean<Object> beanList) {
    }


    @Override
    public void onUploadDataFail(int code, String msg) {

    }
}
