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

package com.github.ont.connector.ontid;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.ont.connector.R;
import com.github.ont.connector.base.CyanoBaseActivity;
import com.github.ont.connector.update.ImageUtil;
import com.github.ont.connector.update.NetUtil;
import com.github.ont.connector.utils.SDKCallback;
import com.github.ont.connector.utils.SDKWrapper;
import com.github.ont.connector.utils.SPWrapper;
import com.github.ont.connector.utils.ToastUtil;
import com.github.ont.cyano.Constant;
import com.github.ont.cyano.CyanoWebView;
import com.github.ont.cyano.NativeJsBridge;
import com.github.ontio.OntSdk;
import com.github.ontio.sdk.manager.WalletMgr;
import com.github.ontio.sdk.wallet.Wallet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/17.
 */
public class OntIdWebActivity extends CyanoBaseActivity implements View.OnClickListener {
    public static final int REQUEST_ALBUM_CODE = 103;
    private static final String TAG = "OntIdWebActivity";
    private static final int REQUEST_FACE_CODE = 104;
    private static final int MY_PERMISSIONS = 2;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 2001;
    private static ValueCallback<Uri[]> filePathCallback;
    //    protected String mUrl = "http://192.168.3.31:8080/#/mgmtHome?ontid=did:ont:Ab3nRoXwqBJxJq3batNU3a2uNfrLdtJKwW";
//    protected String mUrl = "http://192.168.50.123:8080/#/mgmtHome?ontid=" + SPWrapper.getDefaultOntId();
//    protected String mUrl = "http://192.168.50.123:8080/#/authHome";
    protected String mUrl = "http://192.168.50.123:8081/#/";
    ProgressBar pg;
    FrameLayout frameLayout;
    String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权
    List<String> mPermissionList = new ArrayList<>();
    private String ONTID;
    private LinearLayout layoutBack;
    private LinearLayout layoutFinish;
    private CyanoWebView mWebView = null;
    private String reqData;
    private JSONObject params;

    public static void deleteIdentity(String ontid) {
        WalletMgr walletMgr = OntSdk.getInstance().getWalletMgr();
        Wallet wallet = walletMgr.getWallet();
        wallet.removeIdentity(ontid);
        try {
            walletMgr.writeWallet();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_web);
        initView();
        mWebView = new CyanoWebView(this);
        frameLayout.addView(mWebView);
        initWebView();
        initData();
    }

    private void initView() {
        pg = findViewById(R.id.progress_loading);
        frameLayout = findViewById(R.id.frame);
        layoutBack = findViewById(R.id.layout_back);
        layoutFinish = findViewById(R.id.layout_finish);
        layoutBack.setOnClickListener(this);
        layoutFinish.setOnClickListener(this);
    }

    private void initData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mUrl = extras.getString(Constant.KEY);
        }
        ONTID = SPWrapper.getDefaultOntId();
        mWebView.loadUrl(mUrl);
    }

    private boolean getPermissions() {
        mPermissionList.clear();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        if (!mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //判断是否勾选禁止后不再询问
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
                    if (showRequestPermission) {
                        ToastUtil.showToast(this, "permission refused");
//                        finish();
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initWebView() {
        mWebView.setWebChromeClient(new WebChromeClient() {

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
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
//                https://blog.csdn.net/qq_34650238/article/details/79923661
                OntIdWebActivity.filePathCallback = filePathCallback;
                if (getPermissions()) {
                    ImageUtil.setImage(OntIdWebActivity.this);
                } else {
                    filePathCallback.onReceiveValue(new Uri[]{Uri.EMPTY});
                }
                return true;
                //                return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
            }

        });


        mWebView.getNativeJsBridge().setAuthentication(new NativeJsBridge.HandleAuthentication() {
            @Override
            public void handleAction(String data) {
                JSONObject jsonObject = JSON.parseObject(data);
                String subAction = jsonObject.getJSONObject("params").getString("subaction");
                switch (subAction) {
                    case "faceRecognition":
                        handleFaceRecognition(data);
                        break;
                    case "getIdentity":
                        handleGetIdentity(data);
                        break;
                    case "submit":
                        handleSubmit(data);
                        break;
                    case "getRegistryOntidTx":
                        handleRegistry(data);
                    default:
                }
            }
        });

        mWebView.getNativeJsBridge().setAuthorization(new NativeJsBridge.HandleAuthorization() {
            @Override
            public void handleAction(String data) {
                JSONObject jsonObject = JSON.parseObject(data);
                String subAction = jsonObject.getJSONObject("params").getString("subaction");
                switch (subAction) {
                    case "requestAuthorization":
                        handleRequestAuthorization(data);
                        break;
                    case "getAuthorizationInfo":
                        handleAuthorization(data);
                        break;
                    case "decryptClaim":
                        handleDecryptMessage(data);
                        break;
                    case "exportOntid":
                        handleExport(data);
                        break;
                    case "deleteOntid":
                        handleDelete(data);
                        break;
                    default:
                }
            }
        });

        mWebView.getNativeJsBridge().setDecryptMessage(new NativeJsBridge.HandleDecryptMessage() {
            @Override
            public void handleAction(String data) {
                handleDecryptMessage(data);
            }
        });


        mWebView.getNativeJsBridge().setHandleGetIdentity(new NativeJsBridge.HandleGetIdentity() {
            @Override
            public void handleAction(String data) {
                handleGetIdentity(data);
            }
        });
    }

    private void handleRequestAuthorization(String data) {
//        {"action":"authorization","version":"1.0.0","params":
//            {"subaction":"requestAuthorization","seqNo":"0001",
//                    "userOntid":"did:ont:AL2yjtLZJmRmQ4muFiVexYcyVsYb4DkyYL",
//                    "dappOntid":"did:ont:AL2yjtLZJmRmQ4muFiVexYcyVsYb4DkyYL",
//                    "dappName":"candy box","callback":"http://cybox.com/callbackand"
//                ,"dappUrl":"http://www.baidu.com/","authTemplete":"authtemplate_kyc01"},"id":"sob9y29b"}
        JSONObject jsonObject = (JSONObject) JSONObject.parse(data);
        params = jsonObject.getJSONObject("params");
        params.put("subaction", "getAuthorizationInfo");
        mWebView.loadUrl(Constant.CYANO_AUTH_URL);
    }

    private void handleGetIdentity(String data) {
        final JSONObject jsonObject = JSON.parseObject(data);
        if (TextUtils.isEmpty(SPWrapper.getDefaultOntId())) {
//            startActivity(new Intent(this, CreateOntIdActivity.class));
//            finish();
            createOntID();
        } else {
            mWebView.sendSuccessToWeb(jsonObject.getString("action"), jsonObject.getString("version"), jsonObject.getString("id"), SPWrapper.getDefaultOntId());
        }
    }

    private void createOntID() {
        final String defaultAccountAddress = OntSdk.getInstance().getWalletMgr().getWallet().getDefaultAccountAddress();
        if (TextUtils.isEmpty(defaultAccountAddress)) {
            ToastUtil.showToast(baseActivity, getString(R.string.no_wallet));
        }
        setGetDialogPwd(new CyanoBaseActivity.GetDialogPassword() {
            @Override
            public void handleDialog(String pwd) {
                showLoading();
                SDKWrapper.createIdentityWithAccount(new SDKCallback() {
                    @Override
                    public void onSDKSuccess(String tag, Object message) {
                        dismissLoading();
                        SPWrapper.setDefaultOntId((String) message);
                        mWebView.loadUrl(Constant.CYANO_MANAGER_URL + SPWrapper.getDefaultOntId());
                    }

                    @Override
                    public void onSDKFail(String tag, String message) {
                        dismissLoading();
                    }
                }, TAG, defaultAccountAddress, pwd);
            }
        });
        showPasswordDialog(getString(R.string.enter_your_wallet_password));

    }

    private void handleExport(final String data) {
        setGetDialogPwd(new GetDialogPassword() {
            @Override
            public void handleDialog(String pwd) {
                final JSONObject jsonObject = JSON.parseObject(data);
                showLoading();
                SDKWrapper.exportIdentity(new SDKCallback() {
                    @Override
                    public void onSDKSuccess(String tag, Object message) {
                        dismissLoading();
//                        (String) message
                        Intent intent = new Intent(OntIdWebActivity.this, ExportOntIdActivity.class);
                        intent.putExtra(Constant.KEY, (String) message);
                        startActivity(intent);
                        //                        mWebView.sendSuccessToWeb(jsonObject.getString("action"), jsonObject.getString("version"), jsonObject.getString("id"), (String) message);
                    }

                    @Override
                    public void onSDKFail(String tag, String message) {
                        dismissLoading();
                        ToastUtil.showToast(baseActivity, message);
                        mWebView.sendFailToWeb(jsonObject.getString("action"), com.github.ont.cyano.Constant.PARAMS_ERROR, jsonObject.getString("version"), jsonObject.getString("id"), message);
                    }
                }, TAG, pwd);
            }
        });
        showPasswordDialog("Export Ontid");
    }

    private void handleDelete(final String data) {
        setGetDialogPwd(new GetDialogPassword() {
            @Override
            public void handleDialog(String pwd) {
                final JSONObject jsonObject = JSON.parseObject(data);
                showLoading();
                SDKWrapper.exportIdentity(new SDKCallback() {
                    @Override
                    public void onSDKSuccess(String tag, Object message) {
                        deleteIdentity(SPWrapper.getDefaultOntId());
                        SPWrapper.setDefaultOntId("");
                        dismissLoading();
                        finish();
                    }

                    @Override
                    public void onSDKFail(String tag, String message) {
                        dismissLoading();
                        ToastUtil.showToast(baseActivity, message);
                        mWebView.sendFailToWeb(jsonObject.getString("action"), com.github.ont.cyano.Constant.PARAMS_ERROR, jsonObject.getString("version"), jsonObject.getString("id"), message);
                    }
                }, TAG, pwd);
            }
        });
        showPasswordDialog("Delete Ontid");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ALBUM_CODE:
                    if (data != null && filePathCallback != null) {
//                        ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                        List<Uri> uris = ImageUtil.getChooseResult(data);

//                        String imgPath = photos.get(0);
                        // 返回信息给调用方
//                        Uri uri = Uri.parse(imgPath);
                        filePathCallback.onReceiveValue(new Uri[]{uris.get(0)});
                    }
                    break;
                case REQUEST_FACE_CODE:
                    JSONObject jsonObject = JSON.parseObject(reqData);
                    JSONObject map = new JSONObject();
                    map.put("subaction", "faceRecognition");
//                    map.put("data",getSenseData());
                    mWebView.sendSuccessToWeb(jsonObject.getString("action"), jsonObject.getString("version"), jsonObject.getString("id"), map);
                    break;
                default:
                    break;
            }

        } else if (resultCode == RESULT_CANCELED && requestCode == REQUEST_ALBUM_CODE && filePathCallback != null) {
            filePathCallback.onReceiveValue(new Uri[]{Uri.EMPTY});
        }
    }

    private void handleRegistry(String data) {
        JSONObject jsonObject = JSON.parseObject(data);
        JSONObject map = new JSONObject();
        map.put("subaction", "getRegistryOntidTx");
        map.put("ontid", ONTID);
        map.put("registryOntidTx", SPWrapper.getOntIdTransaction());
        mWebView.sendSuccessToWeb(jsonObject.getString("action"), jsonObject.getString("version"), jsonObject.getString("id"), map);
    }

    private void handleDecryptMessage(final String data) {
        setGetDialogPwd(new GetDialogPassword() {
            @Override
            public void handleDialog(String pwd) {
                final JSONObject jsonObject = JSON.parseObject(data);
                JSONArray parse = jsonObject.getJSONObject("params").getJSONArray("message");
                String[] datas = new String[parse.size()];
                for (int i = 0; i < parse.size(); i++) {
                    datas[i] = parse.getString(i);
                }
                showLoading();
                SDKWrapper.decryptData(new SDKCallback() {
                    @Override
                    public void onSDKSuccess(String tag, Object message) {
                        dismissLoading();
                        mWebView.sendSuccessToWeb(jsonObject.getString("action"), jsonObject.getString("version"), jsonObject.getString("id"), (String) message);
                    }

                    @Override
                    public void onSDKFail(String tag, String message) {
                        dismissLoading();
                        ToastUtil.showToast(baseActivity, message);
                        mWebView.sendFailToWeb(jsonObject.getString("action"), com.github.ont.cyano.Constant.PARAMS_ERROR, jsonObject.getString("version"), jsonObject.getString("id"), message);
                    }
                }, TAG, datas, pwd);
            }
        });
        showPasswordDialog("Decrypt Message");
    }

    private void handleAuthorization(String data) {
        JSONObject jsonObject = JSON.parseObject(data);
//        JSONObject map = new JSONObject();
//        map.put("subaction", "getAuthorizationInfo");
//        map.put("seqNo", "0001");
//        map.put("userOntid", ONTID);
//        map.put("dappOntid", ONTID);
//        map.put("dappName", "Candy Box");
//        map.put("callback", "http://candybox.com/callback");
//        map.put("dappUrl", "http://www.baidu.com/");
//        map.put("authTemplate", "authtemplate_kyc01");
        if (params == null) {
            //TODO error
            mWebView.sendFailToWeb(jsonObject.getString("action"), com.github.ont.cyano.Constant.PARAMS_ERROR, jsonObject.getString("version"), jsonObject.getString("id"), com.github.ont.cyano.Constant.PARAMS_ERROR);
            return;
        }
        mWebView.sendSuccessToWeb(jsonObject.getString("action"), jsonObject.getString("version"), jsonObject.getString("id"), params);
    }

    private void handleSubmit(final String data) {
        showLoading();
        final JSONObject jsonObject = JSON.parseObject(data);
        NetUtil.setCyanoNetResponse(new NetUtil.CyanoNetResponse() {
            @Override
            public void handleSuccessResponse(String data) {
                dismissLoading();
                mWebView.sendSuccessToWeb(jsonObject.getString("action"), jsonObject.getString("version"), jsonObject.getString("id"), true);
            }

            @Override
            public void handleFailResponse(String data) {
                dismissLoading();
                mWebView.sendFailToWeb(jsonObject.getString("action"), com.github.ont.cyano.Constant.PARAMS_ERROR, jsonObject.getString("version"), jsonObject.getString("id"), data);
            }
        });
        //钱包服务器地址
        NetUtil.post(Constant.WALLET_RECEIVER_URL, data);
    }

    private void handleFaceRecognition(String data) {
        reqData = data;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mWebView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.destorySelf();
            mWebView = null;
        }
        NetUtil.destory();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.layout_back) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                finish();
            }
        } else if (i == R.id.layout_finish) {
            finish();
        }
    }
}
