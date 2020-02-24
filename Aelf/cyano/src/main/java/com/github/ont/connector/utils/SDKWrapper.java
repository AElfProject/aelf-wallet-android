package com.github.ont.connector.utils;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.github.ontio.OntSdk;
import com.github.ontio.account.Account;
import com.github.ontio.common.Helper;
import com.github.ontio.core.transaction.Transaction;
import com.github.ontio.sdk.manager.ECIES;
import com.github.ontio.sdk.wallet.Identity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.github.ont.cyano.Constant.PAYER;
import static com.github.ont.cyano.Constant.WIF_LENGTH;

public class SDKWrapper {
    private static final String TAG = "SDKWrapper";
    private static final long GAS_LIMIT = 20000;
    private static final long GAS_PRICE = 500;
    private static OntSdk ontSdk = OntSdk.getInstance();


    public static void initOntSDK(final String restUrl, final SharedPreferences path) {
        try {
            ontSdk.setRestful(restUrl);
            ontSdk.setDefaultConnect(ontSdk.getRestful());
            ontSdk.openWalletFile(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DisposableObserver<String> createIdentity(final SDKCallback callback, final String tag, final String password) {
        DisposableObserver<String> observer = getStringDisposableObserver(callback, tag);
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                Identity identity = ontSdk.getWalletMgr().createIdentity(password);
                Transaction transaction = ontSdk.nativevm().ontId().makeRegister(identity.ontid, password, identity.controls.get(0).getSalt(), PAYER, GAS_LIMIT, GAS_PRICE);
                ontSdk.signTx(transaction, identity.ontid, password, identity.controls.get(0).getSalt());
                SPWrapper.setOntIdTransaction(transaction.toHexString());
                ontSdk.getWalletMgr().writeWallet();
                emitter.onNext(identity.ontid);
                emitter.onComplete();
            }

        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
        return observer;
    }

    @NonNull
    private static DisposableObserver<String> getStringDisposableObserver(final SDKCallback callback, final String tag) {
        return new DisposableObserver<String>() {

            @Override
            public void onNext(String res) {
                callback.onSDKSuccess(tag, res);
            }

            @Override
            public void onError(Throwable e) {
                if (e.getMessage() == null) {
                    callback.onSDKFail(tag, "");
                } else {
                    callback.onSDKFail(tag, e.getMessage());
                }
            }

            @Override
            public void onComplete() {

            }
        };
    }

    public static void importIdentity(final SDKCallback callback, final String tag, final String key, final String password) {
        DisposableObserver<String> observer = getStringDisposableObserver(callback, tag);
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Identity identity;
                if (key.length() == WIF_LENGTH) {
                    //wif
                    byte[] bytes = com.github.ontio.account.Account.getPrivateKeyFromWIF(key);
                    identity = ontSdk.getWalletMgr().createIdentityFromPriKey(password, Helper.toHexString(bytes));
                } else {
                    identity = ontSdk.getWalletMgr().createIdentityFromPriKey(password, key);
                }
//                查询DDO
//                String s = ontSdk.nativevm().ontId().sendGetDDO(identity.ontid);
//                if (TextUtils.isEmpty(s)) {
//                    emitter.onError(new Throwable(""));
//                }
                ontSdk.getWalletMgr().writeWallet();
                emitter.onNext(identity.ontid);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }


    public static void decryptData(final SDKCallback callback, final String tag, final String[] data, final String pwd) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

//                String[] datas = new String[]{"a2a67823772705704ec8620baad6eedc", "04048c3ca9837d8217970d750890e1dcd0454223ac1159550d91573eba11be12e6bcb749bb3fbbc61c2522708844cf35091ab43994f38c8cb4d5ee96e3c4f112f1", "ee7c7fe6cfe8fefb898b55ac86fd3ac2"};
                Identity identity = ontSdk.getWalletMgr().getWallet().getIdentity(SPWrapper.getDefaultOntId());
                Account account = ontSdk.getWalletMgr().getAccount(SPWrapper.getDefaultOntId(), pwd, identity.controls.get(0).getSalt());
                String s = account.exportWif();
                byte[] decrypt = ECIES.Decrypt(account, data);
                emitter.onNext(new String(decrypt));
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(getStringDisposableObserver(callback, tag));
    }

    public static void exportIdentity(final SDKCallback callback, final String tag, final String pwd) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Identity identity = ontSdk.getWalletMgr().getWallet().getIdentity(SPWrapper.getDefaultOntId());
                String s = ontSdk.getWalletMgr().getAccount(identity.ontid, pwd, identity.controls.get(0).getSalt()).exportWif();
                emitter.onNext(s);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(getStringDisposableObserver(callback, tag));
    }

    public static DisposableObserver<String> createIdentityWithAccount(final SDKCallback callback, final String tag, final String address, final String password) {
        DisposableObserver<String> observer = getStringDisposableObserver(callback, tag);
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                com.github.ontio.sdk.wallet.Account account = ontSdk.getWalletMgr().getWallet().getAccount(address);
                String wif = ontSdk.getWalletMgr().getAccount(address, password, account.getSalt()).exportWif();
                byte[] privateKeyFromWIF = Account.getPrivateKeyFromWIF(wif);
                Identity identity = ontSdk.getWalletMgr().createIdentityFromPriKey(password, Helper.toHexString(privateKeyFromWIF));
                Transaction transaction = ontSdk.nativevm().ontId().makeRegister(identity.ontid, password, identity.controls.get(0).getSalt(), PAYER, GAS_LIMIT, GAS_PRICE);
                ontSdk.signTx(transaction, identity.ontid, password, identity.controls.get(0).getSalt());
                SPWrapper.setOntIdTransaction(transaction.toHexString());
                ontSdk.getWalletMgr().writeWallet();
                emitter.onNext(identity.ontid);
                emitter.onComplete();
            }

        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
        return observer;
    }

}
