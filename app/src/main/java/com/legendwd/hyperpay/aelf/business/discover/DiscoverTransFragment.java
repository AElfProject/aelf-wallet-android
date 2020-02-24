//package com.legendwd.hyperpay.aelf.business.discover;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.Toolbar;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.webkit.JsPromptResult;
//import android.webkit.WebChromeClient;
//import android.webkit.WebView;
//
//import com.github.lzyzsd.jsbridge.CallBackFunction;
//import com.google.gson.Gson;
//import com.huawei.hms.api.Api;
//import com.legendwd.hyperpay.aelf.R;
//import com.legendwd.hyperpay.aelf.base.ApiUrl;
//import com.legendwd.hyperpay.aelf.base.BaseFragment;
//import com.legendwd.hyperpay.aelf.business.discover.dapp.GameData;
//import com.legendwd.hyperpay.aelf.business.discover.dapp.GameListBean;
//import com.legendwd.hyperpay.aelf.business.discover.dapp.GameTrBean;
//import com.legendwd.hyperpay.aelf.business.discover.dapp.GameTransData;
//import com.legendwd.hyperpay.aelf.business.discover.dapp.GetAccountBean;
//import com.legendwd.hyperpay.aelf.dialogfragments.ToastDialog;
//import com.legendwd.hyperpay.aelf.dialogfragments.TransGameDialog;
//import com.legendwd.hyperpay.aelf.listeners.HandleCallback;
//import com.legendwd.hyperpay.aelf.listeners.OnTextCorrectCallback;
//import com.legendwd.hyperpay.aelf.model.bean.ChooseChainsBean;
//import com.legendwd.hyperpay.aelf.model.bean.DiscoveryBean;
//import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
//import com.legendwd.hyperpay.aelf.model.bean.TransferBean;
//import com.legendwd.hyperpay.aelf.presenters.impl.DiscoveryPresenter;
//import com.legendwd.hyperpay.aelf.util.CryptUtil;
//import com.legendwd.hyperpay.aelf.util.DialogUtils;
//import com.legendwd.hyperpay.aelf.views.IDiscoveryView;
//import com.legendwd.hyperpay.aelf.widget.LoadDialog;
//import com.legendwd.hyperpay.aelf.widget.webview.DWebView;
//import com.legendwd.hyperpay.aelf.widget.webview.JsApi;
//import com.legendwd.hyperpay.lib.CacheUtil;
//import com.legendwd.hyperpay.lib.Constant;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//
//import butterknife.BindView;
//
//public class DiscoverTransFragment
//        extends BaseFragment implements IDiscoveryView {
//    public String TAG = getClass().getSimpleName();
//    @BindView(R.id.webview_discover)
//    DWebView webview_discover;
//    @BindView(R.id.webview_discover_js)
//    DWebView webview_discover_js;
//    private DiscoveryPresenter mDiscoveryPresenter;
//    private String mTaxId;
//    private LoadDialog mLoadDialog;
//    private String gameTraJson;
//    private GameTransData gameTransData;
//    private String acJson = "";
//    private String gameName;
//    private String gameUrl;
//    private String privatekey;
//    private List<ChooseChainsBean> allChainBean;
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 1:
//                    break;
//                default:
//                    break;
//            }
//        }
//    };
//
//
//    public static DiscoverTransFragment newInstance(Bundle bundle) {
//        DiscoverTransFragment discoverTransFragment = new DiscoverTransFragment();
//        discoverTransFragment.setArguments(bundle);
//        return discoverTransFragment;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        initToolbarNav((Toolbar) view.findViewById(R.id.toolbar), "游戏交易", false);
//    }
//
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.fragment_trans_discovery;
//    }
//
//    @Override
//    public void process() {
//        webview_discover_js.loadUrl(ApiUrl.AssetsUrl);
//        webview_discover.loadUrl(ApiUrl.DappTransUrl);
//        mDiscoveryPresenter = new DiscoveryPresenter(this);
//        mDiscoveryPresenter.getDapp();
//        mDiscoveryPresenter.getCrossChains();
//        mLoadDialog = new LoadDialog(getContext());
//        String json = getArguments().getString("bean");
//        DiscoveryBean.DappBean dappBean = new Gson().fromJson(json, DiscoveryBean.DappBean.class);
//        gameName = dappBean.getName();
//        gameUrl = dappBean.getUrl();
//        webview_discover_js.addJavascriptObject(new JsApi(new HandleCallback() {
//            @Override
//            public void onHandle(Object o) {
//                if (mLoadDialog != null && mLoadDialog.isShowing()) {
//                    mLoadDialog.dismiss();
//                }
//                try {
//                    Log.e("result ====>", (String) o);
//                    JSONObject jsonObject = new JSONObject((String) o);
//                    int success = jsonObject.optInt("success");
//                    if (success == 1) {
//                        Log.d("====3333", getSystemTime() + "___" + Thread.currentThread().getName());
//                        mTaxId = jsonObject.optString("txId");
//                    } else {
//                        String err = jsonObject.optString("err");
//                        DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
//                                .setToast(TextUtils.isEmpty(err) ? getString(R.string.transfer_fail) : err);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }), null);
//        webview_discover.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
//                if ("getAccount".equals(message)) {
//                    GetAccountBean getAccountBean = new GetAccountBean();
//                    getAccountBean.setCode(0);
//                    GameData gameData = new GameData();
//                    gameData.setAccountName("XXX");
//                    gameData.setBalance(1.45);
//                    gameData.setSymbol("ELF");
//                    getAccountBean.setData(gameData);
//                    getAccountBean.setError(new ArrayList());
//                    String acJson = new Gson().toJson(getAccountBean);
//                    Log.d("=======getAccount: ", acJson);
//                    result.confirm(acJson);
//                    return true;
//                } else if (message.contains("transfer?params=")) {
//                    String json = message.substring(message.indexOf("{"));
//                    gameTransData = new Gson().fromJson(json, GameTransData.class);
////                    mHandler.sendEmptyMessage(1);
//                    Log.d("====1111", getSystemTime() + "___" + Thread.currentThread().getName());
//                    String white = CryptUtil.getWhite(gameUrl);
//                    if (TextUtils.isEmpty(white)) {
//                        showPasswordDialogForPrivate(new OnTextCorrectCallback() {
//                            @Override
//                            public void onTextCorrect(Object... obj) {
//                                privatekey = (String) obj[1];
//                                if (TextUtils.isEmpty(privatekey)) return;
//                                TransGameDialog transGameDialog = new TransGameDialog();
//                                String address = CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS);
//                                transGameDialog.setChainData("1.234elf", address, gameTransData.recieverAddress, mTaxId, "7499223");
//                                transGameDialog.setClock(new TransGameDialog.TransGameClick() {
//                                    @Override
//                                    public void transClick(boolean isCheck) {
//                                        if (isCheck) {  //加入白名单
//                                            CryptUtil.saveWhite(gameUrl, privatekey);
//                                            return;
//                                        }
//                                        transGameDialog.dismiss();
//                                    }
//                                });
//                                transGameDialog.show(getFragmentManager(), "toast");
//                            }
//                        });
//                    } else {
//                        privatekey = white;
//                    }
////                    transfer("11", "21gvYK1e2Zf4yYLvzj9afBUr4M3hd1FZsqpgP9LDaYjQyef38y", "1", "3535b584db98dc5e26da4da4e9970a2456525924c0caf3e3b9ee7e9da814e341");
//
//                    transfer("11", "21gvYK1e2Zf4yYLvzj9afBUr4M3hd1FZsqpgP9LDaYjQyef38y", "1", "3535b584db98dc5e26da4da4e9970a2456525924c0caf3e3b9ee7e9da814e341");
//                    GetAccountBean getAccountBean = new GetAccountBean();
//                    getAccountBean.setCode(0);
//                    GameData gameData = new GameData();
//                    gameData.setTxId(mTaxId);
//                    getAccountBean.setData(gameData);
//                    getAccountBean.setError(new ArrayList());
//                    acJson = new Gson().toJson(getAccountBean);
//                    Log.d("====transfer?params=: ", acJson);
//
//                    Log.d("====2222", getSystemTime() + "___" + Thread.currentThread().getName());
//                    if (TextUtils.isEmpty(acJson)) {
//                        HashMap hashMap = new HashMap();
//                        hashMap.put("code", "-1");
//                        result.confirm(new Gson().toJson(hashMap));
//                    } else {
//                        result.confirm(acJson);
//                    }
//                    Log.d("====4444", getSystemTime() + "___" + Thread.currentThread().getName());
//                    return true;
//                } else if (message.contains("queryTx?params=")) {
//                    Log.d("====queryTx?params=: ", "queryTx?params=:" + json);
//                    String json = "";
//                    if (message.contains("{")) {
//                        json = message.substring(message.indexOf("{"));
//                    }
//                    if (TextUtils.isEmpty(json)) return true;
//                    try {
//                        JSONObject jsonObject = new JSONObject(json);
//                        String txId = jsonObject.getString("txId");
//                        if (mDiscoveryPresenter != null && !TextUtils.isEmpty(txId)) {
////                            mDiscoveryPresenter.getGameTrans(txId);
//                        }
//                        Log.d("====queryTx?params=: ", gameTraJson + "");
//                        if (TextUtils.isEmpty(gameTraJson)) {
//                            result.confirm();
//                        } else {
//                            result.confirm(gameTraJson);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    return true;
//                } else {
//                    return super.onJsPrompt(view, url, message, defaultValue, result);
//                }
//            }
//        });
//
//    }
//
//
//    public void transfer(String amount, String toAddress, String memo, String key) {
//        TransferBean bean = new TransferBean();
//        bean.setPrivateKey(key);
//        bean.setAmount(amount);
//        bean.setToAddress(toAddress);
//        bean.setSymbol("ELF");
//        bean.setMemo(memo);
//        //"http://161.117.34.124:8000"
//        if (allChainBean == null) return;
//        String nodeUrl = "";
//        String contract_address = "";
//        for (ChooseChainsBean chooseChainsBean : allChainBean) {
//            if (chooseChainsBean.getType().toLowerCase().equals("main")) {
//                nodeUrl = chooseChainsBean.getNode();
//                contract_address = chooseChainsBean.getContract_address();
//            }
//        }
//        String node;
//        if (nodeUrl.endsWith("/")) {
//            node = nodeUrl.substring(0, nodeUrl.length() - 1);
//        } else {
//            node = nodeUrl;
//        }
//        //"http://161.117.34.124:8000"
//        bean.setNodeUrl(node);
//        if (!TextUtils.isEmpty(contract_address)) {
//            //25CecrU94dmMdbhC3LWMKxtoaL4Wv8PChGvVJM6PxkHAyvXEhB
//            bean.setContractAt(contract_address);
//        }
//        webview_discover_js.callHandler("transferElfJS", new Gson().toJson(bean), new CallBackFunction() {
//
//
//            @Override
//            public void onCallBack(String data) {
//            }
//        });
//    }
//
//
//    @Override
//    public void onGameTransSuccess(String json) {
//        if (TextUtils.isEmpty(json)) return;
//        GameTrBean.TransData data = new Gson().fromJson(json, GameTrBean.TransData.class);
//        GameTrBean gameTrBean = new GameTrBean();
//        gameTrBean.code = 0;
//        gameTrBean.data = data;
//        gameTrBean.error = new ArrayList();
//        gameTraJson = new Gson().toJson(gameTrBean);
////        Log.d("gameTraJson", gameTraJson);
//    }
//
//    @Override
//    public void onGameTransError(int code, String msg) {
//
//    }
//
//    @Override
//    public void onChainsSuccess(ResultBean<List<ChooseChainsBean>> resultBean) {
//        if (resultBean == null) return;
//        this.allChainBean = resultBean.getData();
//    }
//
//    @Override
//    public void onChainsError(int code, String msg) {
//
//    }
//
//    @Override
//    public void onDappSuccess(DiscoveryBean discoveryBean) {
//    }
//
//    @Override
//    public void onDappError(int code, String msg) {
//    }
//
//    @Override
//    public void onGameListSuccess(GameListBean dapps) {
//
//    }
//
//    @Override
//    public void onGameListError(int code, String msg) {
//
//    }
//
//    @Override
//    public void onApiSuccess(String json) {
//
//    }
//
//    @Override
//    public void onApiError(int code, String msg) {
//
//    }
//
//    /**
//     * 获取系统时间
//     *
//     * @return
//     */
//    public String getSystemTime() {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
//        //获取当前时间
//        Date date = new Date(System.currentTimeMillis());
//        return simpleDateFormat.format(date);
//    }
//}
