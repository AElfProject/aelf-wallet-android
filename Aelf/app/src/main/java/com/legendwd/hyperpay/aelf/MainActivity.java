package com.legendwd.hyperpay.aelf;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.legendwd.hyperpay.aelf.base.BaseActivity;
import com.legendwd.hyperpay.aelf.ui.activity.FingerActivity;
import com.legendwd.hyperpay.aelf.ui.fragment.MainFragment;
import com.legendwd.hyperpay.aelf.util.AppBackgroundManager;
import com.legendwd.hyperpay.aelf.util.ScreenUtils;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import me.yokeyword.fragmentation.ISupportFragment;

public class MainActivity extends BaseActivity {
    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showFinger();

        AppBackgroundManager.getInstance().setAppStateListener(isAppForceground -> {
            if (AelfApplication.getTopActivity() != null) {
                if (AppBackgroundManager.sToOuther && isAppForceground) {
                    AppBackgroundManager.sToOuther = false;
                    return;
                }
                if (isAppForceground && CacheUtil.getInstance().getProperty(Constant.Sp.TOUCH_ID, false)) {
                    if (!(AelfApplication.getTopActivity() instanceof FingerActivity)) {
                        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                        boolean isScreenOn = pm.isScreenOn();
                        if (isScreenOn && isTouchable()) {
                            startActivity(new Intent(MainActivity.this, FingerActivity.class));
                            overridePendingTransition(0, 0);
                        }
                    }
                }
            }
        });
//        HttpService service = ServiceGenerator.createService(HttpService.class);
//        String username ="";
//        service.getUserFollowingObservable(username)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<User>>() {
//
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(List<User> users) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });

//        Fragmentation.builder()
//                .stackViewMode(Fragmentation.BUBBLE)
//                .debug(BuildConfig.DEBUG)
//                .install();

        if (findFragment(MainFragment.class) == null) {
            loadRootFragment(R.id.fl_tab_container, MainFragment.newInstance());  //load root Fragment
        }
    }

    void showFinger() {
        if (CacheUtil.getInstance().getProperty(Constant.Sp.TOUCH_ID, false)) {
            if (!(AelfApplication.getTopActivity() instanceof FingerActivity)) {
                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                boolean isScreenOn = pm.isScreenOn();
                if (isScreenOn && isTouchable()) {
                    startActivity(new Intent(MainActivity.this, FingerActivity.class));
                    overridePendingTransition(0, 0);
                }
            }
        }
    }

    private boolean isTouchable(){
        FingerprintManager fingerprintManager = null;
        KeyguardManager keyguardManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            fingerprintManager = getSystemService(FingerprintManager.class);
            keyguardManager = getSystemService(KeyguardManager.class);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                || fingerprintManager == null
                || keyguardManager == null
                || !fingerprintManager.isHardwareDetected()) {//
            //不支持
            CacheUtil.getInstance().setProperty(Constant.Sp.TOUCH_ID, false);
          return false;
        } else if (!keyguardManager.isKeyguardSecure()) {
            CacheUtil.getInstance().setProperty(Constant.Sp.TOUCH_ID, false);
            return false;
        } else if (!fingerprintManager.hasEnrolledFingerprints()) {
            CacheUtil.getInstance().setProperty(Constant.Sp.TOUCH_ID, false);
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Constant.sScreenH == 0) {
            getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    Rect r = new Rect();
                    getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                    Constant.sScreenH = r.height() + ScreenUtils.getStatusBarHeight(MainActivity.this) + ScreenUtils.getNavigationBarHeight(MainActivity.this);
                }
            });
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (intent.hasExtra(Constant.BundleKey.SET_LANGUAGE) && intent.getBooleanExtra(Constant.BundleKey.SET_LANGUAGE,false)) {
            loadRootFragment(R.id.fl_tab_container, MainFragment.newInstance());  //load root Fragment
//            recreate();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onBackPressedSupport() {

        ISupportFragment supportFragment = getTopFragment();
        if (supportFragment == null) {
            return;
        }

        if (supportFragment instanceof MainFragment) {
            if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                finish();
            } else {
                TOUCH_TIME = System.currentTimeMillis();
                Toast.makeText(this, getString(R.string.next_click_logout), Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onBackPressedSupport();

    }

}
