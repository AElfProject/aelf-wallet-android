package com.legendwd.hyperpay.aelf.dialogfragments;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseDialogFragment;
import com.legendwd.hyperpay.aelf.business.my.FingerprintDialogController;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import butterknife.BindView;
import butterknife.OnClick;

public class FingerDialog extends BaseDialogFragment implements FingerprintDialogController.Callback {
    private static final String DEFAULT_KEY_NAME = "default_key";
    private FingerprintDialogController mFingerprintDialogController;
    private Cipher mDefaultCipher;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_note)
    TextView tvNote;

    @BindView(R.id.tv_create)
    TextView tvCreate;

    private boolean mBoolHide;

    @Override
    public int getLayoutId() {
        return R.layout.dialog_finger;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFingerprintDialogController = new FingerprintDialogController((FingerprintManager) getContext().getSystemService(Context.FINGERPRINT_SERVICE)
                , this);
        try {
            mDefaultCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get an instance of Cipher", e);
        }
        createKey(DEFAULT_KEY_NAME);
        initCipher(mDefaultCipher, DEFAULT_KEY_NAME);
        mTvTitle.setText(getString(R.string.touch_id_title, getString(R.string.app_name)));
        mTvTitle.setVisibility(mBoolHide ? View.GONE : View.VISIBLE);
        tvNote.setVisibility(mBoolHide ? View.GONE : View.VISIBLE);
        tvCreate.setVisibility(mBoolHide ? View.GONE : View.VISIBLE);
    }

    public void setBoolHide(boolean mBoolHide) {
        this.mBoolHide = mBoolHide;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();
        FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(mDefaultCipher);
        mFingerprintDialogController.startListening(cryptoObject);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick(R.id.tv_create)
    void onClickConfirm() {
        if (mHandleCallback != null) {
            mFingerprintDialogController.stopListening();
            mHandleCallback.onHandle("usePassword");
            mHandleCallback = null;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createKey(String keyName) {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            KeyGenerator keyGenerator = KeyGenerator
                    .getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyStore.load(null);
            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(keyName,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    // Require the user to authenticate with a fingerprint to authorize every use
                    // of the key
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);
            keyGenerator.init(builder.build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean initCipher(Cipher cipher, String keyName) {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");

            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(keyName, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onPause() {
        super.onPause();
        if (mFingerprintDialogController != null) {
            mFingerprintDialogController.stopListening();
        }
    }

    @Override
    public void onAuthenticated() {
        CacheUtil.getInstance().setProperty(Constant.Sp.TOUCH_ID_FAILED, false);
        if (mHandleCallback != null) {
            mHandleCallback.onHandle(true);
        }
    }

    @Override
    public void onError(String error) {
        if (mHandleCallback != null) {
            mHandleCallback.onHandle(error);
        }
    }

    @Override
    public void onCancelled(int errorId, String error) {
        if (!TextUtils.isEmpty(error)) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                    .setToast(error);
        }
        dismiss();
    }

}
