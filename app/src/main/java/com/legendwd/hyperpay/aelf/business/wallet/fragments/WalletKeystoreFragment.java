package com.legendwd.hyperpay.aelf.business.wallet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.BuildConfig;
import com.legendwd.hyperpay.aelf.MainActivity;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.my.fragments.UserAgreementFragment;
import com.legendwd.hyperpay.aelf.config.ApiUrlConfig;
import com.legendwd.hyperpay.aelf.dialogfragments.ToastDialog;
import com.legendwd.hyperpay.aelf.listeners.HandleCallback;
import com.legendwd.hyperpay.aelf.model.bean.ChainBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.WalletBean;
import com.legendwd.hyperpay.aelf.model.param.UploadDataParam;
import com.legendwd.hyperpay.aelf.presenters.IMyAccountPresenter;
import com.legendwd.hyperpay.aelf.presenters.impl.MyAccountPresenter;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.aelf.util.JsonUtils;
import com.legendwd.hyperpay.aelf.views.IMyAccountView;
import com.legendwd.hyperpay.aelf.widget.webview.DWebView;
import com.legendwd.hyperpay.aelf.widget.webview.JsApi;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Colin
 * @date 2019/8/2.
 * description： keyStore 找回账户
 */
public class WalletKeystoreFragment extends BaseFragment implements IMyAccountView {
    @BindView(R.id.et_keystore)
    EditText et_keystore;
    @BindView(R.id.et_key_pwd)
    EditText et_key_pwd;
    @BindView(R.id.iv_key_eyes)
    ImageView iv_key_eyes;
    @BindView(R.id.chb_key_terms)
    CheckBox chb_key_terms;
    @BindView(R.id.tv_key_agree)
    TextView tv_key_agree;
    @BindView(R.id.tv_key_import)
    TextView tv_key_import;
    @BindView(R.id.wb_keystore_bridge)
    DWebView mDWebView;
    private boolean pwdIsOpen = false;
    private ToastDialog loadDialog;
    private String mChainId;
    private IMyAccountPresenter presenter;
    private Bundle mTransferBundle;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_wallet_keystore;
    }

    public static WalletKeystoreFragment newInstance() {
        Bundle args = new Bundle();
        WalletKeystoreFragment fragment = new WalletKeystoreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static WalletKeystoreFragment newInstance(Bundle args) {
        WalletKeystoreFragment transactionRecordFragment = new WalletKeystoreFragment();
        transactionRecordFragment.setArguments(args);
        return transactionRecordFragment;
    }

    @Override
    public void process() {

        mDWebView.loadUrl(ApiUrlConfig.AssetsUrl);

        presenter = new MyAccountPresenter(this);
        //获取当前链信息
        presenter.getChainInfo();
        if (BuildConfig.DEBUG) {
            et_key_pwd.setText("Aa111111111.");
        }

    }

    @OnClick(R.id.tv_key_import)
    void onClickImport() {
        if (isEditTextValid()) {
            transfer();
        }
    }

    private boolean isEditTextValid() {
        String keystore = et_keystore.getText().toString().trim();
        String pwd = et_key_pwd.getText().toString();

        if (TextUtils.isEmpty(keystore)) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.correct_keystore));
            return false;
        }

        if (TextUtils.isEmpty(pwd)) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.e_password));
            return false;
        }

        if (!chb_key_terms.isChecked()) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.terms_agreement));
            return false;
        }

        return true;
    }

    @OnClick({R.id.iv_key_eyes, R.id.tv_key_agree})
    void onClickEyes(View view) {
        switch (view.getId()) {
            case R.id.iv_key_eyes:
                pwdIsOpen = !pwdIsOpen;
                iv_key_eyes.setImageResource(pwdIsOpen ? R.mipmap.eye_open : R.mipmap.eye_close);
                et_key_pwd.setInputType(pwdIsOpen ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        : (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD));
                break;
            case R.id.tv_key_agree:
                hideSoftInput();
                ((BaseFragment) getParentFragment()).start(UserAgreementFragment.newInstance());
                break;
        }
    }

    /**
     * @param qCode
     */
    protected void setEt_keystore(String qCode) {
        et_keystore.setText(qCode);
    }


    /**
     * 效验keystore
     */
    void transfer() {
        loadDialog = DialogUtils.showDialog(ToastDialog.class, getFragmentManager());
        loadDialog.setLoading(true);
        loadDialog.setCancelable(false);
        WalletBean bean = new WalletBean();
//        bean.keystore = "{\"type\":\"aelf\",\"nickName\":\"\",\"address\":\"JSskxPS2UKZ87Fr4ZvxDw1hjpmYXFfN7iATfsuBYt5cXseQcB\",\"crypto\":{\"version\":1,\"cipher\":\"AES256\",\"cipherparams\":{\"iv\":\"4b1d19ba5435ef0b9d2a80ba903f45ed\"},\"mnemonicEncrypted\":\"U2FsdGVkX19QNr2vO7G6TfZA4XM+YP3SHsduJ0pxHH4=\",\"privateKeyEncrypted\":\"U2FsdGVkX19ej1qzojGljI8km2KeHFA0CA1TZ4xZQG5GDKsh1vivURznmElVYlRpKwGC1FiWDTvSXiBvmaHZxiNwB8NeG2IayAglgtsOvtO0z99Q06SRs6kkwuiyKhR1\",\"kdf\":\"scrypt\",\"kdfparams\":{\"r\":1,\"N\":262144,\"p\":8,\"dkLen\":64,\"salt\":\"a0b7dfce68113d139a0d83fa96c1fae93519664435fd18b46317caf4cc0dcf09\"},\"mac\":\"ac7954ac62649a86569cd310ba651c3cc7065f8ab8c9f6457fe444762addc620\"}}";
        bean.keystore = et_keystore.getText().toString().trim();
//        Log.d("send ===>", bean.keystore);
        bean.password = et_key_pwd.getText().toString();
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
        if (TextUtils.isEmpty(mChainId))
            mChainId = Constant.DEFAULT_CHAIN_ID;
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
                            bean.password = et_key_pwd.getText().toString().trim();
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
        if (TextUtils.isEmpty(param.address))
            return;
        param.androidNoticeToken = CacheUtil.getInstance().getProperty(Constant.Sp.DEVICE_TOKEN);
        if (TextUtils.isEmpty(param.androidNoticeToken))
            return;
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
