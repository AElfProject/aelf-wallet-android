package com.legendwd.hyperpay.aelf.business.my.fragments;

import android.app.KeyguardManager;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.wallet.CreateImportWalletActivity;
import com.legendwd.hyperpay.aelf.dialogfragments.ConfirmDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.DoubleButtonDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.FingerDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.ToastDialog;
import com.legendwd.hyperpay.aelf.listeners.HandleCallback;
import com.legendwd.hyperpay.aelf.listeners.OnTextCorrectCallback;
import com.legendwd.hyperpay.aelf.model.bean.CurrenciesBean;
import com.legendwd.hyperpay.aelf.util.AESUtil;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.aelf.util.LanguageUtil;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class SettingFragment extends BaseFragment {

    @BindView(R.id.tv_asset_display)
    TextView tv_asset_display;
    @BindView(R.id.tv_language)
    TextView tv_language;
    @BindView(R.id.tv_pricing_currency)
    TextView tv_pricing_currency;
    @BindView(R.id.switch_touch)
    SwitchCompat switch_touch;
    @BindView(R.id.switch_mode)
    SwitchCompat switch_mode;

    public static SettingFragment newInstance() {
        Bundle args = new Bundle();
        SettingFragment tabFourFragment = new SettingFragment();
        tabFourFragment.setArguments(args);
        return tabFourFragment;
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        //         return super.onCreateFragmentAnimation();
        return new FragmentAnimator();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_setting;
    }

    @Override
    public void process() {
        initToolbarNav(mToolbar, R.string.settings, true);
        refreshView();

        boolean isMode = CacheUtil.getInstance().getProperty(Constant.Sp.PRIVATE_MODE, false);
        switch_mode.setChecked(isMode);

        switch_mode.setOnCheckedChangeListener((buttonView, isChecked) -> CacheUtil.getInstance().setProperty(Constant.Sp.PRIVATE_MODE, isChecked));
    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        refreshView();
    }

    private void refreshView() {
        showAssetDisplay();
        showLanguage();
        showPricingCurrency();
        showTouchIdView();
    }

    private void showTouchIdView() {
        boolean isTouch = CacheUtil.getInstance().getProperty(Constant.Sp.TOUCH_ID, false);
        switch_touch.setChecked(isTouch ? true : false);
    }

    private void showLanguage() {
        String lang = LanguageUtil.readLanguage();

        if (TextUtils.equals(LanguageUtil.LANG_CN, lang)) {
            tv_language.setText(R.string.lang_simplified_chinese);
        } else {
            tv_language.setText(R.string.lang_english);
        }
    }

    private void showAssetDisplay() {
        boolean bAsset = CacheUtil.getInstance().getProperty(Constant.Sp.ASSETS_DISPLAY_INT, 0) == 0;
        if (bAsset) {
            tv_asset_display.setText(R.string.by_token);
        } else {
            tv_asset_display.setText(R.string.by_chain);
        }
    }
    private void showPricingCurrency() {
        String json = CacheUtil.getInstance().getProperty(Constant.Sp.PRICING_CURRENCY_DEFAULT);
        CurrenciesBean.ListBean bean = new Gson().fromJson(json, CurrenciesBean.ListBean.class);
        tv_pricing_currency.setText(null == bean ? "" : bean.id);
    }

    @OnClick({R.id.rel_language, R.id.rel_asset_display, R.id.rel_pricing_currency, R.id.rel_touch})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.rel_language:
                Bundle bundle = new Bundle();
                bundle.putString(Constant.BundleKey.LANG, Constant.BundleValue.SETTING_PAGE);
                start(MultiLanguageFragment.newInstance(bundle));
                break;

            case R.id.rel_asset_display:
                start(AssetDisplayFragment.newInstance());
                break;
            case R.id.rel_pricing_currency:
                start(PricingCurrencyFragment.newInstance());
                break;

            case R.id.rel_touch:
                showTouchId();
//                start(TouchIDFragment.newInstance());
                break;
        }
    }

    private void showTipDialog() {

        DialogUtils.showDialog(ConfirmDialog.class, getFragmentManager())
                .setDialogTitle(getString(R.string.touch_id_title_1))
                .setConfirmText(getString(R.string.confirm))
                .setHandleCallback(new HandleCallback() {
                    @Override
                    public void onHandle(Object o) {
                        startTouch();
                    }
                });
    }

    private void showTouchId() {
        boolean bCheck = switch_touch.isChecked();
        if (bCheck) {
            closeTouch();
        } else {
            showTipDialog();
        }
    }

    private void startTouch() {
        if (!checkTouch()) {
            return;
        }
        showPasswordDialogForPrivate(new OnTextCorrectCallback() {
            @Override
            public void onTextCorrect(Object... obj) {
                String key = (String) obj[0];
                fingerDialog(key);
            }
        });
//        DialogUtils.showDialog(ConfirmDialog.class, getFragmentManager())
//                .setDialogTitle(getString(R.string.touch_id_allow))
//                .setConfirmText(getString(R.string.input_password))
//                .setHandleCallback(new HandleCallback() {
//                    @Override
//                    public void onHandle(Object o) {
//                        showPasswordDialogForPrivate(new OnTextCorrectCallback() {
//                            @Override
//                            public void onTextCorrect(Object... obj) {
//                                String key = (String) obj[0];
//                                fingerDialog(key);
//                            }
//                        });
//                    }
//                });
    }

    private boolean checkTouch() {
        boolean bCheck = true;
        FingerprintManager fingerprintManager = null;
        KeyguardManager keyguardManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            fingerprintManager = getContext().getSystemService(FingerprintManager.class);
            keyguardManager = getContext().getSystemService(KeyguardManager.class);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                || fingerprintManager == null
                || keyguardManager == null
                || !fingerprintManager.isHardwareDetected()) {
            //不支持
            bCheck = false;
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                    .setToast(R.string.not_support);
        } else if (!fingerprintManager.hasEnrolledFingerprints()) {
            //请录入至少一个指纹"
            bCheck = false;
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                    .setToast(R.string.atleast_one_finger);
        }
        return bCheck;
    }

    private void closeTouch() {
        DialogUtils.showDialog(ConfirmDialog.class, getFragmentManager())
                .setDialogTitle(getString(R.string.touch_id_cancle))
                .setConfirmText(getString(R.string.confirm))
                .setHandleCallback(new HandleCallback() {
                    @Override
                    public void onHandle(Object o) {
                        switch_touch.setChecked(false);
                        CacheUtil.getInstance().setProperty(Constant.Sp.TOUCH_ID, false);
                    }
                });
    }

    private void fingerDialog(String key) {
        FingerDialog fingerDialog = DialogUtils.showDialog(FingerDialog.class, getFragmentManager());
        fingerDialog.setBoolHide(true);
        fingerDialog.setHandleCallback(o -> {
            fingerDialog.dismiss();
            if (o instanceof Boolean) {
                switch_touch.setChecked(true);
                DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.touch_success));
                CacheUtil.getInstance().setProperty(Constant.Sp.TOUCH_ID, true);
                CacheUtil.getInstance().setProperty(Constant.Sp.TOUCH_PASSWORD, AESUtil.getInstance().encrypt(key));
            } else {
                DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                        .setToast((String) o);
            }
        });
    }

}
