package com.github.ont.connector.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.github.ont.cyano.Constant;
import com.github.ontio.OntSdk;
import com.github.ontio.common.Common;
import com.github.ontio.sdk.manager.WalletMgr;
import com.github.ontio.sdk.wallet.Identity;
import com.github.ontio.sdk.wallet.Wallet;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

public class SPWrapper {
    private static Context mContext;

    public static void setContext(Context mContext) {
        SPWrapper.mContext = mContext;
    }

    public static SharedPreferences getSharedPreferences() {
        return mContext.getSharedPreferences(Constant.WALLET_FILE, Context.MODE_PRIVATE);
    }

    public static String getDefaultOntId() {
        WalletMgr walletMgr = OntSdk.getInstance().getWalletMgr();
        if (walletMgr == null) {
            SDKWrapper.initOntSDK(SPWrapper.getDefaultNet(), getSharedPreferences());
        }
        Wallet wallet = OntSdk.getInstance().getWalletMgr().getWallet();
        String ontid = String.format("%s%s", Common.didont, wallet.getDefaultAccountAddress());
        Identity identity = wallet.getIdentity(ontid);
        if (identity == null) {
            return "";
        }
        return identity.ontid;
//        return getSharedPreferences().getString(Constant.DEFAULT_ONTID, "");
    }

    public static void setDefaultOntId(String address) {
        WalletMgr walletMgr = OntSdk.getInstance().getWalletMgr();
        if (walletMgr == null) {
            SDKWrapper.initOntSDK(SPWrapper.getDefaultNet(), getSharedPreferences());
        }
        Wallet wallet = OntSdk.getInstance().getWalletMgr().getWallet();
        wallet.setDefaultIdentity(address);
        try {
            OntSdk.getInstance().getWalletMgr().writeWallet();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        getSharedPreferences().edit().putString(Constant.DEFAULT_ONTID, address).apply();
    }

    public static String getDefaultNet() {
        return getSharedPreferences().getString(Constant.DEFAULT_NET, "");
    }

    public static void setDefaultNet(String address) {
        getSharedPreferences().edit().putString(Constant.DEFAULT_NET, address).apply();
    }

    public static void addTestNet(String testNet) {
        JSONArray jsonArray = getTestNets();
        if (jsonArray == null) {
            jsonArray = new JSONArray();
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                if (TextUtils.equals(jsonArray.getString(i), testNet)) {
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        jsonArray.put(testNet);
        getSharedPreferences().edit().putString(Constant.TEST_PRIVATE_NETS, jsonArray.toString()).apply();
    }

    public static JSONArray getTestNets() {
        try {
            return new JSONArray(getSharedPreferences().getString(Constant.TEST_PRIVATE_NETS, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getOntIdTransaction() {
        return getSharedPreferences().getString(Constant.ONT_ID_TRANSACTION, "");
    }

    public static void setOntIdTransaction(String transaction) {
        getSharedPreferences().edit().putString(Constant.ONT_ID_TRANSACTION, transaction).apply();
    }
}
