package com.legendwd.hyperpay.aelf.business.my;

import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.RequiresApi;

import com.legendwd.hyperpay.aelf.AelfApplication;
import com.legendwd.hyperpay.aelf.R;
import android.text.TextUtils;

/**
 * 指纹认证辅助类
 * Created by wangyu on 2018/7/10.
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintDialogController extends FingerprintManager.AuthenticationCallback {

    private final FingerprintManager mFingerprintManager;
    private final Callback mCallback;
    private CancellationSignal mCancellationSignal;

    private boolean mSelfCancelled;

    public FingerprintDialogController(FingerprintManager fingerprintManager, Callback callback) {
        mFingerprintManager = fingerprintManager;
        mCallback = callback;
    }

    /**
     * 指纹认证是否可用，需要硬件支持以及至少录入一个指纹
     *
     * @return
     */
    public boolean isFingerprintAuthAvailable() {
        return mFingerprintManager.isHardwareDetected()
                && mFingerprintManager.hasEnrolledFingerprints();
    }

    /**
     * 开始监听认证结果
     *
     * @param cryptoObject
     */
    public void startListening(FingerprintManager.CryptoObject cryptoObject) {
        if (!isFingerprintAuthAvailable()) {
            return;
        }
        mCancellationSignal = new CancellationSignal();
        mSelfCancelled = false;
        // The line below prevents the false positive inspection from Android Studio
        // noinspection ResourceType
        mFingerprintManager
                .authenticate(cryptoObject, mCancellationSignal, 0 /* flags */, this, null);
    }

    /**
     * 停止监听
     */
    public void stopListening() {
        if (mCancellationSignal != null) {
            mSelfCancelled = true;
            mCancellationSignal.cancel();
            mCancellationSignal = null;
        }
    }

    @Override
    public void onAuthenticationError(int errMsgId, final CharSequence errString) {
        if (!mSelfCancelled) {
            mCallback.onCancelled(errMsgId, errString.toString());
        }
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        if(!TextUtils.isEmpty(helpString)){
            showError(helpString);
        }
    }

    @Override
    public void onAuthenticationFailed() {
        //
        mCallback.onError(AelfApplication.getTopActivity().getString(R.string.please_try_again));
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        //
        mCallback.onAuthenticated();
    }

    private void showError(CharSequence error) {
        //
        if (error != null) {
            mCallback.onError(error.toString());
        }
    }

    public interface Callback {

        void onAuthenticated();

        void onError(String error);

        void onCancelled(int errorId, String error);
    }

}
