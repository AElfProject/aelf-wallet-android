package com.legendwd.hyperpay.aelf.business.wallet.fragments;

import android.os.Bundle;

import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.ApiUrl;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.dialogfragments.ToastDialog;
import com.legendwd.hyperpay.aelf.listeners.HandleCallback;
import com.legendwd.hyperpay.aelf.model.bean.WalletBean;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.aelf.widget.webview.DWebView;
import com.legendwd.hyperpay.aelf.widget.webview.JsApi;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class ExportKeystoreFragment extends BaseFragment {

    @BindView(R.id.webview)
    DWebView webview;

    private ToastDialog loadDialog;

    public static ExportKeystoreFragment newInstance(Bundle bundle) {

        ExportKeystoreFragment fragment = new ExportKeystoreFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_export_keystore;
    }

    @Override
    public void process() {
        initToolbarNav(mToolbar, R.string.backup_notes, true);
        webview.loadUrl(ApiUrl.AssetsUrl);
        webview.addJavascriptObject(new JsApi(new HandleCallback() {
            @Override
            public void onHandle(Object o) {
                loadDialog.dismiss();

                try {
                    JSONObject jo = new JSONObject((String) o);

                    String keystore = jo.optString("keystore");
                    int success = jo.optInt("success", -1);
                    if (1 == success) {
                        Bundle bundle = new Bundle();
                        bundle.putString("data", keystore);
                        start(ExportKeystoreCopyFragment.newInstance(bundle));
                    } else {
                        loadDialog.setLoading(false);
                        loadDialog.setToast(getString(R.string.load_fail));
                        loadDialog.show(getFragmentManager(), "ToastDialog");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }), null);
    }

    @OnClick(R.id.tv_next)
    public void clickView() {
        loadDialog = DialogUtils.showDialog(ToastDialog.class, getFragmentManager());
        loadDialog.setLoading(true);
        loadDialog.setCancelable(false);
        WalletBean bean = new Gson().fromJson(getArguments().getString("bean"), WalletBean.class);
        bean.address = CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS);

        webview.callHandler("getWalletKeyStoreJS", new Gson().toJson(bean), new CallBackFunction() {

            @Override
            public void onCallBack(String data) {

            }
        });


    }

}
