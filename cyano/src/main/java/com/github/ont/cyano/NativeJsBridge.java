package com.github.ont.cyano;

import android.net.Uri;
import android.util.Base64;
import android.webkit.JavascriptInterface;

import org.json.JSONException;
import org.json.JSONObject;

import static com.github.ont.cyano.Constant.AUTHENTICATION;
import static com.github.ont.cyano.Constant.AUTHORIZATION;
import static com.github.ont.cyano.Constant.CONNECT;
import static com.github.ont.cyano.Constant.CYANO_SPLIT_TAG;
import static com.github.ont.cyano.Constant.CYANO_WEB_TAG;
import static com.github.ont.cyano.Constant.DECRYPT_MESSAGE;
import static com.github.ont.cyano.Constant.GET_ACCOUNT;
import static com.github.ont.cyano.Constant.GET_IDENTITY;
import static com.github.ont.cyano.Constant.INVOKE;
import static com.github.ont.cyano.Constant.INVOKE_PASSWORD_FREE;
import static com.github.ont.cyano.Constant.INVOKE_READ;
import static com.github.ont.cyano.Constant.LOGIN;
import static com.github.ont.cyano.Constant.KEY_PAIR_UTILS;

public class NativeJsBridge {
    private HandleAuthentication handleAuthentication;
    private HandleAuthorization handleAuthorization;
    private HandleDecryptMessage handleDecryptMessage;
    private HandleLogin handleLogin;
    private HandleInvoke handleInvoke;
    private HandleGetAccount handleGetAccount;
    private HandleInvokeRead handleInvokeRead;
    private HandleInvokePasswordFree handleInvokePasswordFree;
    private HandleGetIdentity handleGetIdentity;
    private HandleConnect handleConnect;
    private HandleKeyPairUtils handleKeyPairUtils;


    private CyanoWebView cyanoWebView;

    public NativeJsBridge(CyanoWebView cyanoWebView) {
        this.cyanoWebView = cyanoWebView;
    }

    @JavascriptInterface
    public void postMessage(String userInfo) {
        if (userInfo.contains(CYANO_WEB_TAG)) {
            final String[] split = userInfo.split(CYANO_SPLIT_TAG);
            if (cyanoWebView != null) {
                cyanoWebView.post(new Runnable() {
                    @Override
                    public void run() {
                        handleAction(split[split.length - 1]);
                    }
                });
            }
        }
    }


    private void handleAction(String message) {
        byte[] decode = Base64.decode(message, Base64.NO_WRAP);
        String result = Uri.decode(new String(decode));
//        {"action":"login","params":{"type":"account","dappName":"My dapp","message":"test message","expired":"201812181000","callback":""}}
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            String action = jsonObject.getString("action");
            switch (action) {
                case LOGIN:
                    if (handleLogin != null) {
                        handleLogin.handleAction(result);
                    }
                    break;
                case CONNECT:
                    if (handleConnect != null) {
                        handleConnect.handleConnect(result);
                    }
                    break;
                case INVOKE:
                    if (handleInvoke != null) {
                        handleInvoke.handleAction(result);
                    }
                    break;
                case GET_ACCOUNT:
                    if (handleGetAccount != null) {
                        handleGetAccount.handleAction(result);
                    }
                    break;
                case INVOKE_READ:
                    if (handleInvokeRead != null) {
                        handleInvokeRead.handleAction(result);
                    }
                    break;
                case INVOKE_PASSWORD_FREE:
                    if (handleInvokePasswordFree != null) {
                        handleInvokePasswordFree.handleAction(result, new JSONObject(result).getJSONObject("params").toString());
                    }
                    break;
                case AUTHENTICATION:
                    if (handleAuthentication != null) {
                        handleAuthentication.handleAction(result);
                    }
                    break;
                case AUTHORIZATION:
                    if (handleAuthorization != null) {
                        handleAuthorization.handleAction(result);
                    }
                    break;
                case DECRYPT_MESSAGE:
                    if (handleDecryptMessage != null) {
                        handleDecryptMessage.handleAction(result);
                    }
                    break;
                case GET_IDENTITY:
                    if (handleGetIdentity != null) {
                        handleGetIdentity.handleAction(result);
                    }
                    break;
                case KEY_PAIR_UTILS:
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


    public void setAuthentication(HandleAuthentication handleAuthentication) {
        this.handleAuthentication = handleAuthentication;
    }

    public void setAuthorization(HandleAuthorization handleAuthorization) {
        this.handleAuthorization = handleAuthorization;
    }

    public void setDecryptMessage(HandleDecryptMessage handleDecryptMessage) {
        this.handleDecryptMessage = handleDecryptMessage;
    }

    public void setHandleGetIdentity(HandleGetIdentity handleGetIdentity) {
        this.handleGetIdentity = handleGetIdentity;
    }
    public void setHandleConnect(HandleConnect handleConnect) {
        this.handleConnect = handleConnect;
    }

    public void setHandleLogin(HandleLogin handleLogin) {
        this.handleLogin = handleLogin;
    }

    public void setHandleInvoke(HandleInvoke handleInvoke) {
        this.handleInvoke = handleInvoke;
    }


    public void setHandleGetAccount(HandleGetAccount handleGetAccount) {
        this.handleGetAccount = handleGetAccount;
    }

    public void setHandleInvokeRead(HandleInvokeRead handleInvokeRead) {
        this.handleInvokeRead = handleInvokeRead;
    }

    public void setHandleInvokePasswordFree(HandleInvokePasswordFree handleInvokePasswordFree) {
        this.handleInvokePasswordFree = handleInvokePasswordFree;
    }

    public void setHandleKeyPairUtils(HandleKeyPairUtils handleKeyPairUtils) {
        this.handleKeyPairUtils = handleKeyPairUtils;
    }

    public interface HandleAuthentication {
        public void handleAction(String data);
    }

    public interface HandleAuthorization {
        public void handleAction(String data);
    }

    public interface HandleDecryptMessage {
        public void handleAction(String data);
    }

    public interface HandleGetIdentity {
        public void handleAction(String data);
    }
    //与客户端进行初始化连接交换公钥
    public interface HandleConnect {
        public void handleConnect(String data);
    }

    public interface HandleLogin {
        public void handleAction(String data);
    }

    public interface HandleInvoke {
        public void handleAction(String data);
    }

    public interface HandleGetAccount {
        public void handleAction(String data);
    }

    public interface HandleInvokeRead {
        public void handleAction(String data);
    }

    public interface HandleInvokePasswordFree {
        public void handleAction(String data, String message);
    }
    
    public interface HandleKeyPairUtils {
        public void handleAction(String data);
    }


}
