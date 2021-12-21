package com.legendwd.hyperpay.aelf.business.discover.cyano;

import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.webkit.JavascriptInterface;

import org.json.JSONException;
import org.json.JSONObject;

import static com.legendwd.hyperpay.aelf.business.discover.cyano.Constant.ACTION_ACCOUNT;
import static com.legendwd.hyperpay.aelf.business.discover.cyano.Constant.ACTION_API;
import static com.legendwd.hyperpay.aelf.business.discover.cyano.Constant.ACTION_CONNECT;
import static com.legendwd.hyperpay.aelf.business.discover.cyano.Constant.ACTION_DISCONNECT;
import static com.legendwd.hyperpay.aelf.business.discover.cyano.Constant.ACTION_GET_CONTRACT_METHODS;
import static com.legendwd.hyperpay.aelf.business.discover.cyano.Constant.ACTION_INVOKE;
import static com.legendwd.hyperpay.aelf.business.discover.cyano.Constant.ACTION_INVOKEREAD;
import static com.legendwd.hyperpay.aelf.business.discover.cyano.Constant.CYANO_SPLIT_TAG;
import static com.legendwd.hyperpay.aelf.business.discover.cyano.Constant.CYANO_WEB_TAG;
import static com.legendwd.hyperpay.aelf.business.discover.cyano.Constant.ACTION_KEY_PAIR_UTILS;


public class NativeJsBridge {

    private HandleConnect handleConnect;
    private HandleApi handleApi;
    private HandleInvoke handleInvoke;
    private HandleInvokeRead handleInvokeRead;
    private HandleAccount handleAccount;
    private HandleDisconnect handleDisconnect;
    private HandleGetContractMethos handleGetContractMethos;
    private CyanoWebView cyanoWebView;
    private HandleKeyPairUtils handleKeyPairUtils;

    public NativeJsBridge(CyanoWebView cyanoWebView) {
        this.cyanoWebView = cyanoWebView;
    }
    @JavascriptInterface
    public void postMessage(String userInfo) {
        Log.e("message",userInfo);
        if (userInfo.contains(CYANO_WEB_TAG)) {
            final String[] split = userInfo.split(CYANO_SPLIT_TAG);
            if (cyanoWebView != null) {
                cyanoWebView.post(() -> handleAction(split[split.length - 1]));
            }
        }
    }

    private void handleAction(String message) {
        byte[] decode = Base64.decode(message, Base64.NO_WRAP);
        String result = Uri.decode(new String(decode));
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(result);
            String action = jsonObject.getString("action");
            switch (action) {
                case ACTION_CONNECT:
                    if(handleConnect!=null){
                        handleConnect.handleAction(result);
                    }
                    break;
                case ACTION_ACCOUNT:
                    if(handleAccount!=null){
                        handleAccount.handleAction(result);
                    }
                    break;
                case ACTION_INVOKEREAD:
                    if(handleInvokeRead!=null){
                        handleInvokeRead.handleAction(result);
                    }
                    break;
                case ACTION_INVOKE:
                    if(handleInvoke!=null){
                        handleInvoke.handleAction(result);
                    }
                    break;
                case ACTION_API:
                    if(handleApi!=null){
                        handleApi.handleAction(result);
                    }
                    break;
                case ACTION_DISCONNECT:
                    if(handleDisconnect!=null){
                        handleDisconnect.handleAction(result);
                    }
                    break;
                case ACTION_GET_CONTRACT_METHODS:
                    if(handleGetContractMethos!=null){
                        handleGetContractMethos.handleAction(result);
                    }
                    break;
                case ACTION_KEY_PAIR_UTILS:
                    if (handleKeyPairUtils != null) {
                        handleKeyPairUtils.handleAction(result);
                    }
                    break;
                default:
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public interface HandleInvoke {
        void handleAction(String data);
    }

    public interface HandleConnect {
        void handleAction(String data);
    }
    public interface HandleApi {
        void handleAction(String data);
    }
    public interface HandleInvokeRead {
        void handleAction(String data);
    }
    public interface HandleAccount {
        void handleAction(String data);
    }

    public interface HandleDisconnect {
        void handleAction(String data);
    }

    public interface HandleGetContractMethos {
        void handleAction(String data);
    }

    public interface HandleKeyPairUtils {
        void handleAction(String data);
    }


    public void setHandleInvokeRead(HandleInvokeRead handleInvokeRead) {
        this.handleInvokeRead = handleInvokeRead;
    }

    public void setHandleInvoke(HandleInvoke handleInvoke) {
        this.handleInvoke = handleInvoke;
    }

    public void setHandleConnect(HandleConnect handleConnect) {
        this.handleConnect = handleConnect;
    }

    public void setHandleApi(HandleApi handleApi) {
        this.handleApi = handleApi;
    }

    public void setHandleAccount(HandleAccount handleAccount) {
        this.handleAccount = handleAccount;
    }

    public void setHandleDisconnect(HandleDisconnect handleDisconnect) {
        this.handleDisconnect = handleDisconnect;
    }

    public void setHandleGetContractMethos(HandleGetContractMethos handleGetContractMethos) {
        this.handleGetContractMethos = handleGetContractMethos;
    }
    public void setHandleKeyPairUtils(HandleKeyPairUtils handleKeyPairUtils) {
        this.handleKeyPairUtils = handleKeyPairUtils;
    }
}
