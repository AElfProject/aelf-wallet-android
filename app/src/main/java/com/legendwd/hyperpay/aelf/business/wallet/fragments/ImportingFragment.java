package com.legendwd.hyperpay.aelf.business.wallet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.MainActivity;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
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
import com.legendwd.hyperpay.aelf.util.RxScheduler;
import com.legendwd.hyperpay.aelf.util.Task;
import com.legendwd.hyperpay.aelf.util.WalletUtil;
import com.legendwd.hyperpay.aelf.views.IMyAccountView;
import com.legendwd.hyperpay.aelf.widget.webview.DWebView;
import com.legendwd.hyperpay.aelf.widget.webview.JsApi;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

public class ImportingFragment extends BaseFragment implements IMyAccountView {

    @BindView(R.id.webview)
    DWebView webview;
    //    @BindView(R.id.tv_importing)
//    TextView tvTip;
    private IMyAccountPresenter presenter;
    private Bundle mTransferBundle;
    private String mChainId;
    private String fromType;
    private WalletBean walletBean;

    public static ImportingFragment newInstance(Bundle bundle) {

        ImportingFragment fragment = new ImportingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_importing;
    }

    @Override
    public void process() {
        webview.loadUrl(ApiUrlConfig.AssetsUrl);
        presenter = new MyAccountPresenter(this);
        presenter.getChainInfo();
        walletBean = new Gson().fromJson(getArguments().getString("bean"), WalletBean.class);
        if (walletBean != null) {
            fromType = walletBean.fromType;
//            tvTip.setText("0".equals(fromType) ? R.string.create_aelf_wallet : R.string.importing_aelf_wallet);
        }
    }

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

    private void uploadAccountInfo(String name, String address) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("address", address);
        presenter.updateIdentityName(jsonObject);
    }

    private void createWallet() {
        RxScheduler.doTask(new Task<WalletBean>() {
            @Override
            public void doOnUIThread(WalletBean o) {
                createWalletSuccess(walletBean);
            }

            @Override
            public WalletBean doOnIOThread() {

                String mnemonic = "";

                if (TextUtils.isEmpty(walletBean.mnemonic)) {
                    mnemonic = WalletUtil.generateMnemonic();//生成助记词
                } else {
                    mnemonic = walletBean.mnemonic;
                    walletBean.name = Constant.DEFAULT_CHAIN_ID;
                }

                walletBean.mnemonic = mnemonic;
                return walletBean;

            }
        });
    }

    private void createWalletSuccess(WalletBean walletBean) {
        webview.addJavascriptObject(new JsApi(new HandleCallback() {
            @Override
            public void onHandle(Object o) {

                int resId = "0".equals(fromType) ? R.string.create_fail : R.string.import_fail;
                try {
                    JSONObject jo = new JSONObject((String) o);

                    String address = jo.optString("address");
                    String public_key = jo.optString("publicKey");
                    String public_key_dapp = jo.optString("publicKeyDapp");
                    String signed_address = jo.optString("signedAddress");
                    String privatekey = jo.optString("privateKey");

                    if (TextUtils.isEmpty(address)) {
                        DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(resId);
                        return;
                    }
                    walletBean.coinAddress = Constant.DEFAULT_PREFIX + address + "_" + mChainId;
                    walletBean.address = address;

                    CacheUtil.getInstance().setProperty(Constant.Sp.WALLET_ADDRESS, address);
                    CacheUtil.getInstance().setProperty(Constant.Sp.WALLET_SIGNED_ADDRESS, signed_address);
                    CacheUtil.getInstance().setProperty(Constant.Sp.WALLET_PUBLIC_KEY_DAPP, public_key_dapp);
                    CacheUtil.getInstance().setProperty(Constant.Sp.WALLET_PUBLIC_KEY, public_key);
                    CacheUtil.getInstance().setProperty(Constant.Sp.WALLET_HINT, walletBean.hint);
                    CacheUtil.getInstance().setProperty(Constant.Sp.WALLET_COIN_ADDRESS, walletBean.coinAddress);
                    CacheUtil.getInstance().setProperty(Constant.Sp.ACCOUNT_NAME, walletBean.name);

                    CacheUtil.getInstance().setProperty(walletBean.password + "private", privatekey);
                    CacheUtil.getInstance().setProperty(walletBean.password + "mnemonic", walletBean.mnemonic);

                    mTransferBundle = new Bundle();
                    mTransferBundle.putString("bean", new Gson().toJson(walletBean));
                    mTransferBundle.putString(Constant.BundleKey.BACKUP, Constant.BundleValue.CREATE_IMPORT_PAGE);
                    uploadAccountInfo(walletBean.name, address);
                    uploadData();
                    bindCoin();
                    if (!"0".equals(fromType)) {
                        CacheUtil.getInstance().setProperty(Constant.Sp.WALLET_BACKUP, true);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(resId);
                    return;
                }
            }
        }), null);

        String tag = walletBean.getKeyType() == 0 ? "getWalletByMnemonicJS" : "importWalletPrivateKeyJS";
        webview.callHandler(tag, new Gson().toJson(walletBean), data -> {

        });
    }

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

    @Override
    public void onChaininfoError(int code, String msg) {
        mChainId = Constant.DEFAULT_CHAIN_ID;
        CacheUtil.getInstance().setProperty(Constant.Sp.COIN_CHAIN_ID, mChainId);
    }

    @Override
    public void onUploadDataSuccess(ResultBean<Object> beanList) {

    }

    @Override
    public void onUploadDataFail(int code, String msg) {

    }

    @Override
    public void onBindCoinSuccess(ResultBean resultBean) {
        if ("0".equals(fromType)) {
            start(NotificationFragment.newInstance(mTransferBundle));
        } else {
            startActivity(new Intent(_mActivity, MainActivity.class));
            _mActivity.finish();
            CacheUtil.getInstance().setProperty(Constant.Sp.IS_WALLET_EXISTS, true);
            CacheUtil.getInstance().setProperty(Constant.Sp.is_KeyStore, walletBean.getKeyType() != 0);
        }
    }

    @Override
    public void onBindCoinFail(int code, String msg) {
        if ("0".equals(fromType)) {
            start(NotificationFragment.newInstance(mTransferBundle));
        } else {
            startActivity(new Intent(_mActivity, MainActivity.class));
            _mActivity.finish();
            CacheUtil.getInstance().setProperty(Constant.Sp.IS_WALLET_EXISTS, true);
            CacheUtil.getInstance().setProperty(Constant.Sp.is_KeyStore, walletBean.getKeyType() != 0);
        }
    }

    @Override
    public void onUpdateIdentityError(int code, String msg) {

    }
}
