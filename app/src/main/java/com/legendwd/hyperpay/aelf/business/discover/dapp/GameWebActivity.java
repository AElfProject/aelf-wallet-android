/*
 * **************************************************************************************
 *  Copyright © 2014-2018 Ontology Foundation Ltd.
 *  All rights reserved.
 *
 *  This software is supplied only under the terms of a license agreement,
 *  nondisclosure agreement or other written agreement with Ontology Foundation Ltd.
 *  Use, redistribution or other disclosure of any parts of this
 *  software is prohibited except in accordance with the terms of such written
 *  agreement with Ontology Foundation Ltd. This software is confidential
 *  and proprietary information of Ontology Foundation Ltd.
 *
 * **************************************************************************************
 */

package com.legendwd.hyperpay.aelf.business.discover.dapp;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseActivity;
import com.legendwd.hyperpay.aelf.business.discover.cyano.Constant;
import com.legendwd.hyperpay.aelf.business.discover.cyano.CyanoWebView;
import com.legendwd.hyperpay.aelf.business.discover.cyano.DappUtils;
import com.legendwd.hyperpay.aelf.config.ApiUrlConfig;
import com.legendwd.hyperpay.aelf.dialogfragments.DappInputDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.DoubleButtonDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.ToastDialog;
import com.legendwd.hyperpay.aelf.listeners.OnTextCorrectCallback;
import com.legendwd.hyperpay.aelf.model.bean.ChainAddressBean;
import com.legendwd.hyperpay.aelf.model.bean.ChooseChainsBean;
import com.legendwd.hyperpay.aelf.model.bean.DiscoveryBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.TransferBalanceBean;
import com.legendwd.hyperpay.aelf.presenters.impl.DiscoveryPresenter;
import com.legendwd.hyperpay.aelf.presenters.impl.TransferPresenter;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.aelf.util.JsonFormatUtil;
import com.legendwd.hyperpay.aelf.views.IDiscoveryView;
import com.legendwd.hyperpay.aelf.views.ITransferView;
import com.legendwd.hyperpay.aelf.widget.webview.DWebView;
import com.legendwd.hyperpay.aelf.widget.webview.JsApi;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Logger;

import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.LazyECPoint;
import org.spongycastle.asn1.sec.SECNamedCurves;
import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.crypto.AsymmetricCipherKeyPair;
import org.spongycastle.crypto.generators.ECKeyPairGenerator;
import org.spongycastle.crypto.params.ECDomainParameters;
import org.spongycastle.crypto.params.ECKeyGenerationParameters;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.params.ECPublicKeyParameters;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

import static com.legendwd.hyperpay.aelf.AelfApplication.PRIVATEKEY_DAPP;
import static com.legendwd.hyperpay.aelf.business.discover.cyano.Constant.ACTION_GET_CONTRACT_METHODS;
import static com.legendwd.hyperpay.aelf.business.discover.cyano.Constant.ACTION_INVOKE;
import static com.legendwd.hyperpay.aelf.business.discover.cyano.Constant.ACTION_INVOKEREAD;


/**
 * Created by Administrator on 2015/9/17.
 */
public class GameWebActivity extends BaseActivity implements ITransferView, IDiscoveryView {

    public static final ECDomainParameters CURVE;
    public static final BigInteger HALF_CURVE_ORDER;
    public static final BigInteger SECP256K1N = new BigInteger("fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141", 16);
    private static final SecureRandom secureRandom;

    private JSONObject webData;

    static {
        X9ECParameters params = SECNamedCurves.getByName("secp256k1");
        CURVE = new ECDomainParameters(params.getCurve(), params.getG(),
                params.getN(), params.getH());
        HALF_CURVE_ORDER = params.getN().shiftRight(1);
        secureRandom = new SecureRandom();
    }

    private final String TAG = "SocialMediaWebview";
    public static String SUPPORTED_EC = "secp256k1";
    protected String mUrl = "";
    protected String mTitle = "";
    ProgressBar pg;
    FrameLayout frameLayout;
    @BindView(R.id.tv_title)
    TextView tv_title;
    private CyanoWebView mWebView = null;

    private TransferPresenter presenter;
    private DiscoveryPresenter mDiscoveryPresenter;
    List<ChooseChainsBean> chooseChainsBeans;

    @BindView(R.id.wv_bridge)
    DWebView mWvbridge;

    private String transfer_privateKey;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        mWebView = new CyanoWebView(this);
        frameLayout.addView(mWebView);
        initWebView();
        initAction();

//        test();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mUrl = extras.getString(Constant.KEY);
            DiscoveryBean.DappBean dapp = new Gson().fromJson(extras.getString("bean"), DiscoveryBean.DappBean.class);
            mTitle = dapp.getName();
            tv_title.setText(mTitle);
        }


        DialogUtils.showDialog(DoubleButtonDialog.class, getSupportFragmentManager())
                .setTitle(String.format(getString(R.string.title_03), mTitle))
                .setMessage(String.format(getString(R.string.title_04), mTitle))
                .setHandleCallback(o -> {
                    if ((boolean) o) {
                        initData();
                        presenter = new TransferPresenter(GameWebActivity.this);
                        mDiscoveryPresenter = new DiscoveryPresenter(GameWebActivity.this);
                    } else {
                        finish();
                    }

                });


    }

    /**
     * 初始化桥接动作
     */
    private void initAction() {
        if (mWebView.getNativeJsBridge() != null) {
            mWebView.getNativeJsBridge().setHandleConnect(data -> actionConnect(data));
            mWebView.getNativeJsBridge().setHandleAccount(data -> actionAccount(data));
            mWebView.getNativeJsBridge().setHandleInvokeRead(data -> actionInvokeRead(data));
            mWebView.getNativeJsBridge().setHandleInvoke(data -> actionInvoke(data));
            mWebView.getNativeJsBridge().setHandleApi(data -> actionApi(data));
            mWebView.getNativeJsBridge().setHandleDisconnect(data -> actionDisconnect(data));
            mWebView.getNativeJsBridge().setHandleGetContractMethos(data -> actionGetContractMethos(data));
        }
    }

    @OnClick(R.id.img_back)
    public void clickView() {
        finish();
    }

    @Override
    public void onBackPressedSupport() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game_web;
    }

    private void initView() {
        pg = findViewById(R.id.progress_loading);
        frameLayout = findViewById(R.id.frame);
    }

    private void initData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mUrl = extras.getString(Constant.KEY);
            DiscoveryBean.DappBean dapp = new Gson().fromJson(extras.getString("bean"), DiscoveryBean.DappBean.class);
            tv_title.setText(dapp.getName());
        }
//        mWebView.loadUrl("http://54.249.197.246:9876/dapp.html");
        mWebView.loadUrl(mUrl);
    }

    private void initWebView() {

        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(title)) {
                    tv_title.setText(title);
                }
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                if (result != null) {
                    result.confirm("");
                }
                if (result != null) {
                    result.cancel();
                }
                return true;
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    pg.setVisibility(View.GONE);
                } else {
                    pg.setVisibility(View.VISIBLE);
                    pg.setProgress(newProgress);
                }
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                return super.onConsoleMessage(consoleMessage);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }
        });


        mWvbridge.loadUrl(ApiUrlConfig.AssetsUrl);
        mWvbridge.addJavascriptObject(new JsApi(o -> {
            dismissLoading();
            try {
                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject((String) o);
                int success = jsonObject.getInteger("success");
                if (success == 1) {
                    mWebView.sendSuccessToWeb(jsonObject.get("data"),
                            jsonObject.getString("id"));
                } else {
                    mWebView.sendFailToWeb(jsonObject.getString("msg"),
                            jsonObject.getString("id"),
                            jsonObject.getString("error"), 1100);
                }

            } catch (Exception e) {
                mWebView.sendFailToWeb("", "Data parsing failed", 1002);
            }
        }), null);


    }

    /**
     * 停止webview的加载
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mWebView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {//点击返回按钮的时候判断有没有上一页
            mWebView.goBack(); // goBack()表示返回webView的上一页面
            return true;
        } else {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.destorySelf();
            mWebView = null;
        }
        super.onDestroy();
    }

    public void showLoading() {
//        if (mToastDialog == null) {
//            mToastDialog = DialogUtils.showDialog(ToastDialog.class, getSupportFragmentManager())
//                    .setToast(R.string.loading)
//                    .setLoading(true)
//                    .setToastCancelable(false);
//        } else {
//            mToastDialog.setToast(R.string.loading);
//            mToastDialog.show(getSupportFragmentManager(), "ToastDialog");
//        }

    }

    public void dismissLoading() {
//        if (mToastDialog != null) {
//            mToastDialog.dismiss();
//        }
    }


    /**
     * 链接
     */
    private void actionConnect(String data) {


        showLoading();
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(data);
        String id = jsonObject.getString("id");
        JSONObject params = jsonObject.getJSONObject("params");
        String encryptAlgorithm = params.getString("encryptAlgorithm");
        String publicKey = params.getString("publicKey");
        DappUtils.setPublicKey(publicKey);
        String signature = params.getString("signature");
        String timestamp = params.getString("timestamp");
        if (!SUPPORTED_EC.contains(encryptAlgorithm)) {
            dismissLoading();
            return;
        }
        if (!DappUtils.checkTimestamp(timestamp)) {
            dismissLoading();
            mWebView.sendFailToWeb(id, "Invalid timestamp", 1001);
            return;
        }

        DappUtils.VerifySignParam(timestamp, signature, o -> {
            if ((boolean) o) {
                runOnUiThread(() -> handlerConnect(id));
            } else {
                runOnUiThread(() -> dismissLoading());
                mWebView.sendFailToWeb(id, "Data parsing failed", 1002);
            }
        });


    }

    /**
     * 链接
     */
    private void actionDisconnect(String webdata) {
        showLoading();
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(webdata);
        String id = jsonObject.getString("id");
        JSONObject params = jsonObject.getJSONObject("params");
        String originalParams = params.getString("originalParams");
        String signature = params.getString("signature");
        byte[] decode = Base64.decode(originalParams, Base64.NO_WRAP);
        String result = Uri.decode(new String(decode));
        com.alibaba.fastjson.JSONObject resultJson = JSON.parseObject(result);
        String timestamp = resultJson.getString("timestamp");
        if (!DappUtils.checkTimestamp(timestamp)) {
            dismissLoading();
            mWebView.sendFailToWeb(id, "Invalid timestamp", 1001);
            return;
        }
        DappUtils.VerifySignParam(Base64.decode(originalParams, Base64.NO_WRAP), signature, o -> {
            if ((boolean) o) {
                runOnUiThread(() -> mWebView.sendSuccessToWeb("", id));
            } else {
                runOnUiThread(() -> dismissLoading());
                mWebView.sendFailToWeb(id, "Data parsing failed", 1002);
            }
        });


    }

    private void actionGetContractMethos(String webData) {
        showLoading();
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(webData);
        String id = jsonObject.getString("id");
        JSONObject params = jsonObject.getJSONObject("params");
        String originalParams = params.getString("originalParams");
        String signature = params.getString("signature");
        byte[] decode = Base64.decode(originalParams, Base64.NO_WRAP);
        String result = Uri.decode(new String(decode));
        com.alibaba.fastjson.JSONObject resultJson = JSON.parseObject(result);
        Logger.d(new Gson().toJson(resultJson));
        String timestamp = resultJson.getString("timestamp");
        if (!DappUtils.checkTimestamp(timestamp)) {
            dismissLoading();
            mWebView.sendFailToWeb(id, "Invalid timestamp", 1001);
            return;
        }
        DappUtils.VerifySignParam(Base64.decode(originalParams, Base64.NO_WRAP), signature, o -> {
            if ((boolean) o) {
                runOnUiThread(() -> {
                    String nodeUrl = "";
                    if (chooseChainsBeans != null && chooseChainsBeans.size() > 0) {
                        for (ChooseChainsBean chooseChainsBean : chooseChainsBeans) {
                            boolean isMain = "main".equals(chooseChainsBean.getType());
                            if (isMain) {
                                nodeUrl = chooseChainsBean.getNode();
                                break;
                            }
                        }
                    }
                    resultJson.put("nodeUrl", nodeUrl);
                    resultJson.put("action", ACTION_GET_CONTRACT_METHODS);
                    resultJson.put("id", id);
                    mWvbridge.callHandler("getContractMethods", new Gson().toJson(resultJson), data -> {
                    });

                });
            } else {
                mWebView.sendFailToWeb(id, "Data parsing failed", 1002);
                runOnUiThread(() -> dismissLoading());
            }
        });
    }


    public interface Callback {
        void callback(Object o);
    }


    private void handlerConnect(String id) {

        String random = UUID.randomUUID().toString().replace("-", "");
        try {
            org.spongycastle.crypto.generators.ECKeyPairGenerator generator = new ECKeyPairGenerator();
            ECKeyGenerationParameters keygenParams = new ECKeyGenerationParameters(CURVE, secureRandom);
            generator.init(keygenParams);
            AsymmetricCipherKeyPair keypair = generator.generateKeyPair();
            ECPrivateKeyParameters privParams = (ECPrivateKeyParameters) keypair.getPrivate();
            ECPublicKeyParameters pubParams = (ECPublicKeyParameters) keypair.getPublic();
            LazyECPoint pub = new LazyECPoint(CURVE.getCurve(), pubParams.getQ().getEncoded(false));
            DappUtils.setPub(pub);
            DappUtils.setPrivParams(privParams);
            webData = new JSONObject();

            DappUtils.doSign(random, o -> runOnUiThread(() -> {
                webData.put("signature", o.toString());
                webData.put("random", random);
                webData.put("publicKey", Utils.HEX.encode(pub.getEncoded()));
                mWebView.sendSuccessToWebNoSign(webData, id);
                dismissLoading();
            }));

        } catch (Exception ex) {
            ex.printStackTrace();
            mWebView.sendFailToWeb(id, "Signature failure", 1007);
            dismissLoading();
            return;
        }

    }


    /**
     * 获取账户
     *
     * @param data
     */
    private void actionAccount(String data) {

        showLoading();
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(data);
        String id = jsonObject.getString("id");
        JSONObject params = jsonObject.getJSONObject("params");


        String originalParams = params.getString("originalParams");
        String signature = params.getString("signature");


        byte[] decode = Base64.decode(originalParams, Base64.NO_WRAP);
        String result = Uri.decode(new String(decode));
        com.alibaba.fastjson.JSONObject resultJson = JSON.parseObject(result);
        String timestamp = resultJson.getString("timestamp");

        if (!DappUtils.checkTimestamp(timestamp)) {
            dismissLoading();
            mWebView.sendFailToWeb(id, "Invalid timestamp", 1001);
            return;
        }


        DappUtils.VerifySignParam(Base64.decode(originalParams, Base64.NO_WRAP), signature, o -> {
            if ((boolean) o) {
                runOnUiThread(() -> presenter.getCrossChains_forDapp(id));
            } else {
                runOnUiThread(() -> dismissLoading());
                mWebView.sendFailToWeb(id, "Data parsing failed", 1002);
            }
        });

    }


    /**
     * 获取账户
     *
     * @param webdata
     */
    private void actionInvokeRead(String webdata) {
        showLoading();
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(webdata);
        String id = jsonObject.getString("id");
        JSONObject params = jsonObject.getJSONObject("params");
        String originalParams = params.getString("originalParams");
        String signature = params.getString("signature");
        byte[] decode = Base64.decode(originalParams, Base64.NO_WRAP);
        String result = Uri.decode(new String(decode));
        com.alibaba.fastjson.JSONObject resultJson = JSON.parseObject(result);
        String timestamp = resultJson.getString("timestamp");
        if (!DappUtils.checkTimestamp(timestamp)) {
            dismissLoading();
            mWebView.sendFailToWeb(id, "Invalid timestamp", 1001);
            return;
        }
        DappUtils.VerifySignParam(Base64.decode(originalParams, Base64.NO_WRAP), signature, o -> {
            if ((boolean) o) {
                runOnUiThread(() -> {
                    resultJson.put("privateKey", "bdb3b39ef4cd18c2697a920eb6d9e8c3cf1a930570beb37d04fb52400092c42b");
                    String nodeUrl = "";
                    if (chooseChainsBeans != null && chooseChainsBeans.size() > 0) {
                        for (ChooseChainsBean chooseChainsBean : chooseChainsBeans) {
                            boolean isMain = "main".equals(chooseChainsBean.getType());
                            if (isMain) {
                                nodeUrl = chooseChainsBean.getNode();
                                break;
                            }
                        }
                    }
                    resultJson.put("nodeUrl", nodeUrl);
                    resultJson.put("action", ACTION_INVOKEREAD);
                    resultJson.put("id", id);
                    mWvbridge.callHandler("invokeOrinvokeReadJs", new Gson().toJson(resultJson), data -> {
                    });

                });
            } else {
                mWebView.sendFailToWeb(id, "Data parsing failed", 1002);
                runOnUiThread(() -> dismissLoading());
            }
        });

    }


//    public void getBalance(String contractAddress,String symbol){
//
//        /**
//         * 获取余额
//         */
//
//        String chainid = "";
//        if(chooseChainsBeans!=null&&chooseChainsBeans.size()>0){
//            for(ChooseChainsBean chooseChainsBean:chooseChainsBeans){
//                boolean isMain = "main".equals(chooseChainsBean.getType());
//                if(isMain){
//                    chainid = chooseChainsBean.getName();
//                }
//            }
//        }
//
//        TransferBalanceParam param = new TransferBalanceParam();
//        param.address = CacheUtil.getInstance().getProperty(com.legendwd.hyperpay.lib.Constant.Sp.WALLET_ADDRESS);
//        param.contractAddress = contractAddress;
//        param.symbol = symbol;
//        param.chainid = chainid;
//        presenter.getTransferBalance(param);
//    }


    /**
     * 获取账户
     *
     * @param webdata
     */
    private void actionInvoke(String webdata) {
        showLoading();
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(webdata);
        String id = jsonObject.getString("id");
        JSONObject params = jsonObject.getJSONObject("params");
        String originalParams = params.getString("originalParams");
        String signature = params.getString("signature");
        byte[] decode = Base64.decode(originalParams, Base64.NO_WRAP);
        String result = Uri.decode(new String(decode));
        com.alibaba.fastjson.JSONObject resultJson = JSON.parseObject(result);
        String timestamp = resultJson.getString("timestamp");
        if (!DappUtils.checkTimestamp(timestamp)) {
            dismissLoading();
            mWebView.sendFailToWeb(id, "Invalid timestamp", 1001);
            return;
        }


        DappUtils.VerifySignParam(Base64.decode(originalParams, Base64.NO_WRAP), signature, o -> {
            if ((boolean) o) {
                runOnUiThread(() -> {
                    String privateKey = PRIVATEKEY_DAPP.get(mUrl);
                    if (TextUtils.isEmpty(privateKey)) {
                        showPasswordDialogForPrivate(obj -> {
                            if ("".equalsIgnoreCase(obj[0].toString())) {
                                mWebView.sendFailToWeb(id, "Cancelled", 1101);
                            } else {
                                String nodeUrl = "";
                                if (chooseChainsBeans != null && chooseChainsBeans.size() > 0) {
                                    for (ChooseChainsBean chooseChainsBean : chooseChainsBeans) {
                                        boolean isMain = "main".equals(chooseChainsBean.getType());
                                        if (isMain) {
                                            nodeUrl = chooseChainsBean.getNode();
                                            break;
                                        }
                                    }
                                }
                                resultJson.put("nodeUrl", nodeUrl);
                                resultJson.put("action", ACTION_INVOKE);
                                resultJson.put("id", id);
                                resultJson.put("privateKey", transfer_privateKey);
                                mWvbridge.callHandler("invokeOrinvokeReadJs", new Gson().toJson(resultJson), data -> {
                                });
                            }

                        }, resultJson.getString("contractAddress"), result);
                    } else {

                        String nodeUrl = "";
                        if (chooseChainsBeans != null && chooseChainsBeans.size() > 0) {
                            for (ChooseChainsBean chooseChainsBean : chooseChainsBeans) {
                                boolean isMain = "main".equals(chooseChainsBean.getType());
                                if (isMain) {
                                    nodeUrl = chooseChainsBean.getNode();
                                    break;
                                }
                            }
                        }
                        transfer_privateKey = privateKey;
                        resultJson.put("privateKey", transfer_privateKey);
                        resultJson.put("nodeUrl", nodeUrl);
                        resultJson.put("action", ACTION_INVOKE);
                        resultJson.put("id", id);
                        mWvbridge.callHandler("invokeOrinvokeReadJs", new Gson().toJson(resultJson), data -> {
                        });
                    }
                });
            } else {
                runOnUiThread(() -> dismissLoading());
                mWebView.sendFailToWeb(id, "Data parsing failed", 1002);
            }
        });

    }


    /**
     * @param webData
     */
    private void actionApi(String webData) {
        showLoading();
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(webData);
        String id = jsonObject.getString("id");
        JSONObject params = jsonObject.getJSONObject("params");
        String originalParams = params.getString("originalParams");
        String signature = params.getString("signature");
        byte[] decode = Base64.decode(originalParams, Base64.NO_WRAP);
        String result = Uri.decode(new String(decode));
        com.alibaba.fastjson.JSONObject resultJson = JSON.parseObject(result);

        String timestamp = resultJson.getString("timestamp");

        if (!DappUtils.checkTimestamp(timestamp)) {
            dismissLoading();
            mWebView.sendFailToWeb(id, "Invalid timestamp", 1001);
            return;
        }


        DappUtils.VerifySignParam(Base64.decode(originalParams, Base64.NO_WRAP), signature, o -> {
            if ((boolean) o) {
                runOnUiThread(() -> {

                    String nodeUrl = "";
                    if (chooseChainsBeans != null && chooseChainsBeans.size() > 0) {
                        for (ChooseChainsBean chooseChainsBean : chooseChainsBeans) {
                            boolean isMain = "main".equals(chooseChainsBean.getType());
                            if (isMain) {
                                nodeUrl = chooseChainsBean.getNode();
                                break;
                            }
                        }
                    }
                    resultJson.put("nodeUrl", nodeUrl);
                    resultJson.put("id", id);
                    mWvbridge.callHandler("apiJs", new Gson().toJson(resultJson), data -> {
//                        Logger.d(data);

                    });

                });
            } else {
                runOnUiThread(() -> dismissLoading());
                mWebView.sendFailToWeb(id, "Data parsing failed", 1002);
            }
        });
    }


    /**
     * private key
     *
     * @param callback
     */
    protected void showPasswordDialogForPrivate(OnTextCorrectCallback callback, String contractAddress, String result) {
        DappInputDialog dialogFragment = DialogUtils.showDialog(DappInputDialog.class, getSupportFragmentManager())
                .setTv01Str(getString(R.string.sign_text01) + ": " + contractAddress)
                .setTv02Str(getString(R.string.sign_text02) + ": \n" + JsonFormatUtil.formatJson(result))
                .setTitle(getString(R.string.sign_title))
                .setInputCancelable(false);
        dialogFragment.setHandleCallback(o -> {
            if ("close".equalsIgnoreCase((String) o)) {
                if (null != callback) {
                    callback.onTextCorrect("");
                }
            } else {
                String pwd = (String) o;
                String data = CacheUtil.getInstance().getProperty(pwd + "private");
                if (TextUtils.isEmpty(data)) {
                    runOnUiThread(() -> DialogUtils.showDialog(ToastDialog.class, getSupportFragmentManager()).setToast(getString(R.string.pwd_not_correct)));
                } else {
                    transfer_privateKey = data;

                    if (dialogFragment.isAddWhite()) {
                        PRIVATEKEY_DAPP.put(mUrl, data);
                    } else {
                        PRIVATEKEY_DAPP.put(mUrl, "");
                    }
                    if (null != callback) {
                        dialogFragment.dismiss();
                        callback.onTextCorrect(pwd, data);
                    }
                }
            }

        });
    }


    @Override
    public void onTransferBalanceSuccess(ResultBean<TransferBalanceBean> bean, String chainid) {
    }

    @Override
    public void onTransferBalanceError(int code, String msg) {
    }

    @Override
    public void onConcurrentSuccess(ResultBean<List<ChainAddressBean>> resultBean) {

    }

    @Override
    public void onConcurrentError(int code, String msg) {

    }

    @Override
    public void onDappSuccess(DiscoveryBean discoveryBean) {

    }

    @Override
    public void onDappError(int code, String msg) {

    }

    @Override
    public void onGameListSuccess(GameListBean gameListBean, String refreshType) {

    }

    @Override
    public void onGameListError(int code, String msg) {

    }

    @Override
    public void onApiSuccessForDapp(String id, String json) {
    }

    @Override
    public void onApiErrorForDapp(String id, int code, String msg) {
    }

    @Override
    public void onChainsSuccess(ResultBean<List<ChooseChainsBean>> resultBean) {

    }

    @Override
    public void onChainsError(int code, String msg) {
    }

    @Override
    public void onChainsSuccessForDapp(String id, ResultBean<List<ChooseChainsBean>> resultBean) {
        if (resultBean == null) {
            dismissLoading();
            return;
        }

        dismissLoading();
        DialogUtils.showDialog(DoubleButtonDialog.class, getSupportFragmentManager())
                .setTitle(getString(R.string.title_01))
                .setLeftText(getString(R.string.Refused))
                .setMessage(String.format(getString(R.string.title_02), mTitle))
                .setHandleCallback(o -> {
                    if ((boolean) o) {
                        showLoading();
                        chooseChainsBeans = resultBean.getData();
                        JSONArray accounts = new JSONArray();
                        com.alibaba.fastjson.JSONObject account = new JSONObject();
                        account.put("name", CacheUtil.getInstance().getProperty(com.legendwd.hyperpay.lib.Constant.Sp.ACCOUNT_NAME));
                        account.put("address", (CacheUtil.getInstance().getProperty(com.legendwd.hyperpay.lib.Constant.Sp.WALLET_COIN_ADDRESS)).split("_")[1]);
                        account.put("publicKey", CacheUtil.getInstance().getProperty(com.legendwd.hyperpay.lib.Constant.Sp.WALLET_PUBLIC_KEY_DAPP));
                        accounts.add(account);

                        JSONArray chains = new JSONArray();
                        if (chooseChainsBeans != null && chooseChainsBeans.size() > 0) {
                            for (ChooseChainsBean chooseChainsBean : chooseChainsBeans) {
                                com.alibaba.fastjson.JSONObject chain = new JSONObject();
                                chain.put("chainId", chooseChainsBean.getSymbol());
                                boolean isMain = "main".equals(chooseChainsBean.getType());
                                chain.put("isMainChain", isMain);
                                chain.put("url", chooseChainsBean.getNode());
                                chains.add(chain);
                            }
                        }
                        com.alibaba.fastjson.JSONObject webData = new JSONObject();
                        webData.put("accounts", accounts);
                        webData.put("chains", chains);
                        mWebView.sendSuccessToWeb(webData, id);
                        dismissLoading();
                    } else {
                        mWebView.sendFailToWeb(id, "Cancelled", 1101);
                    }

                });


    }

    @Override
    public void onChainsErrorForDapp(String id, int code, String msg) {
        dismissLoading();
        mWebView.sendFailToWeb(id, " Data parsing failed", 1002);
    }


}




