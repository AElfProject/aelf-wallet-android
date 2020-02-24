package com.legendwd.hyperpay.aelf.business.wallet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.BuildConfig;
import com.legendwd.hyperpay.aelf.MainActivity;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.ApiUrl;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.my.fragments.UserAgreementFragment;
import com.legendwd.hyperpay.aelf.dialogfragments.ToastDialog;
import com.legendwd.hyperpay.aelf.listeners.HandleCallback;
import com.legendwd.hyperpay.aelf.model.bean.ChainBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.WalletBean;
import com.legendwd.hyperpay.aelf.model.param.UploadDataParam;
import com.legendwd.hyperpay.aelf.presenters.IMyAccountPresenter;
import com.legendwd.hyperpay.aelf.presenters.impl.MyAccountPresenter;
import com.legendwd.hyperpay.aelf.util.BitmapUtil;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.aelf.util.JsonUtils;
import com.legendwd.hyperpay.aelf.util.LanguageUtil;
import com.legendwd.hyperpay.aelf.util.StringUtil;
import com.legendwd.hyperpay.aelf.util.zxing.CaptureActivity;
import com.legendwd.hyperpay.aelf.views.IMyAccountView;
import com.legendwd.hyperpay.aelf.widget.webview.DWebView;
import com.legendwd.hyperpay.aelf.widget.webview.JsApi;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;
import com.legendwd.hyperpay.lib.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 账号导入
 */
public class ImportScanWalletFragment extends BaseFragment implements IMyAccountView {

    @BindView(R.id.tv_title_right)
    TextView titleRight;
    @BindView(R.id.iv_qr)
    ImageView ivQr;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.et_key_pwd)
    EditText etKeyPwd;
    @BindView(R.id.iv_key_eyes)
    ImageView ivKeyEyes;
    @BindView(R.id.chb_key_terms)
    CheckBox cbKeyTerms;
    @BindView(R.id.tv_key_import)
    TextView tvKeyImport;
    @BindView(R.id.wb_keystore_bridge)
    DWebView mDWebView;

    @BindView(R.id.tv_note)
    TextView tvNote;
    @BindView(R.id.tv_html)
    TextView tvHtml;
    @BindView(R.id.tv_key_agree)
    TextView tvAgree;


    private boolean pwdIsOpen;
    private ToastDialog loadDialog;
    private String mChainId;
    private IMyAccountPresenter presenter;
    private Bundle mTransferBundle;
    private String mScanData;

    public static ImportScanWalletFragment newInstance() {
        return new ImportScanWalletFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LanguageUtil.setLanguage(getContext());
        super.onViewCreated(view, savedInstanceState);
        initToolbarNav(mToolbar, R.string.createimport_import_wallet, true);
        titleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(ImportWalletFragment.newInstance());
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_wallet_scan_keystore;
    }

    @Override
    public void process() {
        titleRight.setText(R.string.advanced_btn_tv);
        tvNote.setText(R.string.import_scan_keystore_tip);
        etKeyPwd.setHint(R.string.input_keystore_password);
        tvHtml.setText(R.string.import_agree_tip);
        tvAgree.setText(R.string.terms_of_service);
        tvKeyImport.setText(R.string.createimport_import_wallet);

        String language = CacheUtil.getInstance().getProperty(Constant.Sp.SET_LANGUAGE);
        if("en".equals(language)) {
            ivQr.setImageResource(R.mipmap.click_en);
        }else {
            ivQr.setImageResource(R.mipmap.click_cn);
        }
        mDWebView.loadUrl(ApiUrl.AssetsUrl);
        presenter = new MyAccountPresenter(this);
        //获取当前链信息
        presenter.getChainInfo();
        if (BuildConfig.DEBUG) {
            etKeyPwd.setText("Aa111111111.");
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }

    @OnClick(R.id.iv_qr)
    void onQrClick() {
        Intent intent = new Intent(_mActivity, CaptureActivity.class);
        intent.putExtra(Constant.IntentKey.Scan_Zxing, Constant.IntentValue.SCAN_KEY_MNEMONIC);
        startActivityForResult(intent, Constant.RequestCode.CODE_SCAN_ZXING);
    }


    @OnClick({R.id.iv_key_eyes, R.id.tv_key_agree})
    void onClickEyes(View view) {
        switch (view.getId()) {
            case R.id.iv_key_eyes:
                pwdIsOpen = !pwdIsOpen;
                ivKeyEyes.setImageResource(pwdIsOpen ? R.mipmap.eye_open : R.mipmap.eye_close);
                etKeyPwd.setInputType(pwdIsOpen ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        : (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD));
                break;
            case R.id.tv_key_agree:
                hideSoftInput();
                start(UserAgreementFragment.newInstance());
                break;
        }
    }

    @OnClick(R.id.tv_key_import)
    void onConfimClick() {
        if (isEditTextValid()) {
            transfer();
        }
    }

    private boolean isEditTextValid() {
        String pwd = etKeyPwd.getText().toString();

        if (TextUtils.isEmpty(mScanData)) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.correct_keystore));
            return false;
        }

        if (TextUtils.isEmpty(pwd)) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.e_password));
            return false;
        }

        if (!cbKeyTerms.isChecked()) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.terms_agreement));
            return false;
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.RequestCode.CODE_SCAN_ZXING) {
                mScanData = data.getStringExtra(Constant.IntentKey.Scan_Zxing);
                try {
                    JSONObject jsonObject = new JSONObject(mScanData);
                    tvAddress.setText(jsonObject.getString("address"));
                    ivQr.setImageResource(R.mipmap.click_result);
                    String name = jsonObject.getString("nickName");
                    if(TextUtils.isEmpty(name)) {
                        tvName.setVisibility(View.GONE);
                    }else {
                        tvName.setVisibility(View.VISIBLE);
                        tvName.setText(name);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.empty_keystore));
                }
            }
        }
    }

    void transfer() {
        loadDialog = DialogUtils.showDialog(ToastDialog.class, getFragmentManager());
        loadDialog.setLoading(true);
        loadDialog.setCancelable(false);
        WalletBean bean = new WalletBean();
//        bean.keystore = "{\"type\":\"aelf\",\"nickName\":\"\",\"address\":\"JSskxPS2UKZ87Fr4ZvxDw1hjpmYXFfN7iATfsuBYt5cXseQcB\",\"crypto\":{\"version\":1,\"cipher\":\"AES256\",\"cipherparams\":{\"iv\":\"4b1d19ba5435ef0b9d2a80ba903f45ed\"},\"mnemonicEncrypted\":\"U2FsdGVkX19QNr2vO7G6TfZA4XM+YP3SHsduJ0pxHH4=\",\"privateKeyEncrypted\":\"U2FsdGVkX19ej1qzojGljI8km2KeHFA0CA1TZ4xZQG5GDKsh1vivURznmElVYlRpKwGC1FiWDTvSXiBvmaHZxiNwB8NeG2IayAglgtsOvtO0z99Q06SRs6kkwuiyKhR1\",\"kdf\":\"scrypt\",\"kdfparams\":{\"r\":1,\"N\":262144,\"p\":8,\"dkLen\":64,\"salt\":\"a0b7dfce68113d139a0d83fa96c1fae93519664435fd18b46317caf4cc0dcf09\"},\"mac\":\"ac7954ac62649a86569cd310ba651c3cc7065f8ab8c9f6457fe444762addc620\"}}";
        bean.keystore = mScanData;
//        Log.d("send ===>", bean.keystore);
        bean.password = etKeyPwd.getText().toString();
        String msg = JsonUtils.objToJson(bean);
        //数据转型················· 很重要
        Log.d("send===>", msg);
        mDWebView.callHandler("importWalletKeyStoreJS", msg, new CallBackFunction() {

            @Override
            public void onCallBack(String data) {
            }
        });

    }

    /**
     * 绑定、解绑用户币资产
     */
    private void bindCoin() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("address", CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS));
        jsonObject.addProperty("contract_address", "");
        jsonObject.addProperty("flag", "1");
        jsonObject.addProperty("symbol", "ELF");
        jsonObject.addProperty("signed_address", CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_SIGNED_ADDRESS));
        jsonObject.addProperty("public_key", CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_PUBLIC_KEY));
        presenter.bindCoin(jsonObject);
    }

    @Override
    public void onUpdateIdentitySuccess(String path, ResultBean resultBean) {

    }

    @Override
    public void onUpdateIdentityNameSuccess(String name, ResultBean resultBean) {

    }

    @Override
    public void onUpdateIdentityNameError(int code, String msg) {

    }

    @Override
    public void onChaininfoSuccess(ResultBean<ChainBean> resultBean) {
        mChainId = resultBean.getData().getChainId();
        if (TextUtils.isEmpty(mChainId)) {
            mChainId = Constant.DEFAULT_CHAIN_ID;
        }
        CacheUtil.getInstance().setProperty(Constant.Sp.CHAIN_ID, mChainId);
        CacheUtil.getInstance().setProperty(Constant.Sp.COIN_CHAIN_ID, mChainId);
        createWallet();
    }

    /**
     * 通过keystore 创建钱包
     */
    private void createWallet() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDWebView.addJavascriptObject(new JsApi(new HandleCallback() {
                    @Override
                    public void onHandle(Object o) {
                        try {
                            String data = (String) o;
                            //返回参数同getWalletByMnemonicJS
                            JSONObject jsonObject = new JSONObject(data);
//                            Log.d("result ===>", data);
                            String privateKey = jsonObject.optString("privateKey");
                            String public_key = jsonObject.optString("publicKey");
                            String public_key_dapp = jsonObject.optString("publicKeyDapp");
                            String signed_address = jsonObject.optString("signedAddress");
                            String address = jsonObject.optString("address");
//                            Log.d("address ===>", address);
                            if (TextUtils.isEmpty(address)) {
                                DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(R.string.import_fail);
                                if (loadDialog != null) {
                                    loadDialog.dismiss();
                                }
                                return;
                            }
                            WalletBean bean = new WalletBean();
                            bean.name = Constant.DEFAULT_CHAIN_ID;
                            bean.password = etKeyPwd.getText().toString().trim();
                            bean.coinAddress = Constant.DEFAULT_PREFIX + address + "_" + mChainId;
                            bean.address = address;

                            CacheUtil.getInstance().setProperty(Constant.Sp.WALLET_ADDRESS, address);
                            CacheUtil.getInstance().setProperty(Constant.Sp.WALLET_SIGNED_ADDRESS, signed_address);
                            CacheUtil.getInstance().setProperty(Constant.Sp.WALLET_PUBLIC_KEY, public_key);
                            CacheUtil.getInstance().setProperty(Constant.Sp.WALLET_PUBLIC_KEY_DAPP, public_key_dapp);
                            CacheUtil.getInstance().setProperty(Constant.Sp.WALLET_HINT, bean.hint);
                            CacheUtil.getInstance().setProperty(Constant.Sp.WALLET_COIN_ADDRESS, bean.coinAddress);
                            CacheUtil.getInstance().setProperty(Constant.Sp.ACCOUNT_NAME, bean.name);

                            CacheUtil.getInstance().setProperty(bean.password + "private", privateKey);
                            CacheUtil.getInstance().setProperty(bean.password + "mnemonic", bean.mnemonic);

                            mTransferBundle = new Bundle();
                            mTransferBundle.putString("bean", new Gson().toJson(bean));
                            mTransferBundle.putString(Constant.BundleKey.BACKUP, Constant.BundleValue.CREATE_IMPORT_PAGE);
                            CacheUtil.getInstance().setProperty(Constant.Sp.IS_WALLET_EXISTS, true);
                            uploadAccountInfo(bean.name, address);
                            uploadData();
                            bindCoin();
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Log.d("result ===>", e.toString() + "");
                            loadDialog.setLoading(false);
                            loadDialog.setToast(getString(R.string.load_fail));
                            loadDialog.show(getFragmentManager(), "ToastDialog");
                        }
                    }
                }), null);
            }
        });

    }

    @Override
    public void onChaininfoError(int code, String msg) {
        mChainId = Constant.DEFAULT_CHAIN_ID;
        CacheUtil.getInstance().setProperty(Constant.Sp.CHAIN_ID, mChainId);
        CacheUtil.getInstance().setProperty(Constant.Sp.COIN_CHAIN_ID, mChainId);
    }

    /**
     * 保存用户token
     */
    private void uploadData() {
        UploadDataParam param = new UploadDataParam();
        param.address = CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS);
        if (TextUtils.isEmpty(param.address)) {
            return;
        }
        param.androidNoticeToken = CacheUtil.getInstance().getProperty(Constant.Sp.DEVICE_TOKEN);
        if (TextUtils.isEmpty(param.androidNoticeToken)) {
            return;
        }
        param.parent = "elf";
        param.deviceInfo = android.os.Build.BRAND + ":" + android.os.Build.MODEL + ":" + android.os.Build.VERSION.RELEASE;
        presenter.uploadData(param);
    }

    /**
     * 修改用户信息
     *
     * @param name
     * @param address
     */
    private void uploadAccountInfo(String name, String address) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("address", address);
        presenter.updateIdentityName(jsonObject);
    }

    @Override
    public void onUploadDataSuccess(ResultBean<Object> beanList) {
//        Log.d("onUploadDataSuccess===>", "----------");

    }

    @Override
    public void onUploadDataFail(int code, String msg) {
        Log.d("onUploadDataFail===>", msg);
    }

    @Override
    public void onBindCoinSuccess(ResultBean resultBean) {
        if (loadDialog != null) {
            loadDialog.dismiss();
        }

        CacheUtil.getInstance().setProperty(Constant.Sp.is_KeyStore, true);
        CacheUtil.getInstance().setProperty(Constant.Sp.WALLET_BACKUP, true);
        startActivity(new Intent(_mActivity, MainActivity.class));
        _mActivity.finish();
    }

    @Override
    public void onBindCoinFail(int code, String msg) {
//        Log.d("onBindCoinFail===>", msg);
        if (loadDialog != null) {
            loadDialog.dismiss();
        }
        CacheUtil.getInstance().setProperty(Constant.Sp.is_KeyStore, true);
        startActivity(new Intent(_mActivity, MainActivity.class));
        _mActivity.finish();
    }

    @Override
    public void onUpdateIdentityError(int code, String msg) {
//        Log.d("onUpdateError===>", msg);
    }
}
