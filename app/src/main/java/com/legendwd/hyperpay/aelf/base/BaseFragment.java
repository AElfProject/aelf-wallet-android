package com.legendwd.hyperpay.aelf.base;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.legendwd.hyperpay.aelf.business.wallet.CreateImportWalletActivity;
import com.legendwd.hyperpay.aelf.dialogfragments.FingerDialog;
import com.legendwd.hyperpay.aelf.ui.fragment.MainFragment;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.dialogfragments.InputDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.ToastDialog;
import com.legendwd.hyperpay.aelf.listeners.HandleCallback;
import com.legendwd.hyperpay.aelf.listeners.OnTextCorrectCallback;
import com.legendwd.hyperpay.aelf.model.MessageEvent;
import com.legendwd.hyperpay.aelf.util.AESUtil;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.aelf.util.LanguageUtil;
import com.legendwd.hyperpay.aelf.util.ScreenUtils;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

public abstract class BaseFragment extends SwipeBackFragment implements LifecycleProvider<ActivityEvent> {
    protected Toolbar mToolbar;
    protected ImmersionBar mImmersionBar;
    private final BehaviorSubject<ActivityEvent> mLifecycleSubject = BehaviorSubject.create();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LanguageUtil.setLanguage(getContext());
        View view = inflater.inflate(getLayoutId(), container, false);
        mToolbar = view.findViewById(R.id.toolbar);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        setFragmentAnimator(new DefaultHorizontalAnimator());
        if (enableSwipeBack()) {
            return attachToSwipeBack(view);
        }
        return view;
    }

    protected int setStatusBarView() {
        return 0;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
    }

    public void initImmersionBar() {
        ImmersionBar.with(this).keyboardEnable(true)
                .statusBarColorInt(Color.WHITE)
                .navigationBarColorInt(Color.WHITE)
                .autoDarkModeEnable(true, 0.2f).init();
    }

    protected boolean isImmersionBarEnabled() {
        return true;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mToolbar != null) {
            mToolbar.setPadding(0, ScreenUtils.getStatusBarHeight(_mActivity), 0, 0);
        }
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                process();
            }
        }, 300);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && mImmersionBar != null) {
            mImmersionBar.init();
        }
    }

    protected boolean needTransparentStatus() {
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLifecycleSubject.onNext(ActivityEvent.CREATE);
    }

    @Override
    public void onStart() {
        super.onStart();
        mLifecycleSubject.onNext(ActivityEvent.START);
    }

    @Override
    public void onResume() {
        super.onResume();
        mLifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    public void onPause() {
        mLifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
    }

    @Override
    public void onStop() {
        mLifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mLifecycleSubject.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
    }

    public abstract int getLayoutId();

    public abstract void process();

    /**
     * swipe back 状态值
     */
    protected boolean enableSwipeBack() {
        return true;
    }

    public void startBrotherFragment(SupportFragment targetFragment) {
        if (getParentFragment() == null) {
            return;
        }

        if (getParentFragment() instanceof MainFragment) {
            ((MainFragment) getParentFragment()).start(targetFragment);
        }
    }

    protected void initToolbarNav(Toolbar toolbar, String title, boolean backAble) {
        if (backAble) {
            toolbar.findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pop();
                }
            });

        } else {
            toolbar.findViewById(R.id.img_back).setVisibility(View.INVISIBLE);
        }
        ((TextView) toolbar.findViewById(R.id.tv_title)).setText(title);

    }

    protected void initToolbarNav(Toolbar toolbar, int title, boolean backAble) {
        if (backAble) {
            toolbar.findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pop();
                }
            });

        } else {
            toolbar.findViewById(R.id.img_back).setVisibility(View.INVISIBLE);
        }
        ((TextView) toolbar.findViewById(R.id.tv_title)).setText(title);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        EventBus.getDefault().unregister(this);
        hideSoftInput();
    }

    protected void copyText(String text) {

        ClipboardManager clipboardManager = (ClipboardManager) _mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager == null || TextUtils.isEmpty(text)) {
            return;
        }
        clipboardManager.setPrimaryClip(ClipData.newPlainText(null, text));
        if (clipboardManager.hasPrimaryClip() && clipboardManager.getPrimaryClip() != null) {
            clipboardManager.getPrimaryClip().getItemAt(0).getText();
        }

        DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.copy_success));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {

    }

    protected void clearCache() {
        String udid = CacheUtil.getInstance().getProperty(Constant.Sp.UDID);
        CacheUtil.getInstance().clearAllData();
        CacheUtil.getInstance().setProperty(Constant.Sp.UDID, udid);
    }

    /**
     * private key
     *
     * @param callback
     */
    protected void showPasswordDialogForPrivate(OnTextCorrectCallback callback) {
        if (CacheUtil.getInstance().getProperty(Constant.Sp.TOUCH_ID, false)) {
            showTouchDialogForPirvate(callback);
        } else {
            showPasswordForPirvate(callback);
        }
    }

    private void showPasswordForPirvate(OnTextCorrectCallback callback) {
        InputDialog dialogFragment = DialogUtils.showDialog(InputDialog.class, getFragmentManager())
                .showForgetPassword(true);
        dialogFragment.setInputCancelable(false);
        dialogFragment.setHandleCallback(new HandleCallback() {
            @Override
            public void onHandle(Object object) {
                if (object instanceof Boolean && (Boolean) object) {
                    exitApp();
                } else {
                    decodePrivateData(callback, object, dialogFragment);
                }
            }
        });
    }

    private void decodePrivateData(OnTextCorrectCallback callback, Object object, InputDialog dialogFragment) {
        String pwd = (String) object;
        String data = CacheUtil.getInstance().getProperty(pwd + "private");
        if (TextUtils.isEmpty(data)) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.pwd_not_correct));
        } else {
            if (null != callback) {
                if (dialogFragment != null) {
                    dialogFragment.dismiss();
                }
                callback.onTextCorrect(pwd, data);
            }
        }
    }

    private void exitApp() {
        clearCache();
        startActivity(new Intent(_mActivity, CreateImportWalletActivity.class));
        _mActivity.finish();
    }

    /**
     * mnemonic
     *
     * @param callback
     */
    protected void showPasswordDialogForMnemonic(OnTextCorrectCallback callback) {
        if (CacheUtil.getInstance().getProperty(Constant.Sp.TOUCH_ID, false)) {
            showTouchDialogForMnemonic(callback);
        } else {
            showPasswordForMnemonic(callback);
        }
    }

    private void showTouchDialogForPirvate(OnTextCorrectCallback callback) {
        FingerDialog fingerDialog = DialogUtils.showDialog(FingerDialog.class, getFragmentManager());
        fingerDialog.setHandleCallback(o -> {
            if (o instanceof Boolean) {
                fingerDialog.dismiss();
                decodePrivateData(callback, AESUtil.getInstance().decrypt(CacheUtil.getInstance().getProperty(Constant.Sp.TOUCH_PASSWORD)), null);
            } else if ("usePassword".equals(o)) {
                fingerDialog.dismiss();
                showPasswordForPirvate(callback);
            } else {
                fingerDialog.dismiss();
                DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                        .setToast((String) o);
            }
        });
    }

    private void showTouchDialogForMnemonic(OnTextCorrectCallback callback) {
        FingerDialog fingerDialog = DialogUtils.showDialog(FingerDialog.class, getFragmentManager());
        fingerDialog.setHandleCallback(o -> {
            if (o instanceof Boolean) {
                fingerDialog.dismiss();
                decodeMnemonicData(callback, AESUtil.getInstance().decrypt(CacheUtil.getInstance().getProperty(Constant.Sp.TOUCH_PASSWORD)), null);
            } else if ("usePassword".equals(o)) {
                fingerDialog.dismiss();
                showPasswordForMnemonic(callback);
            } else {
                fingerDialog.dismiss();
                DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                        .setToast((String) o);
            }
        });
    }

    private void showPasswordForMnemonic(OnTextCorrectCallback callback) {
        InputDialog dialogFragment = DialogUtils.showDialog(InputDialog.class, getFragmentManager())
                .showForgetPassword(true);
        dialogFragment.setCancelable(false);
        dialogFragment.setHandleCallback(object -> {
            if (object instanceof Boolean && (Boolean) object) {
                exitApp();
            } else {
                decodeMnemonicData(callback, object, dialogFragment);
            }
        });
    }

    private void decodeMnemonicData(OnTextCorrectCallback callback, Object object, InputDialog dialogFragment) {
        String pwd = (String) object;
        String data = CacheUtil.getInstance().getProperty(pwd + "mnemonic");
        if (TextUtils.isEmpty(data)) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.pwd_not_correct));
        } else {
            if (null != callback) {
                callback.onTextCorrect(pwd, data);
            }
        }
    }


    @Override
    @NonNull
    @CheckResult
    public final Observable<ActivityEvent> lifecycle() {
        return mLifecycleSubject.hide();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(mLifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(mLifecycleSubject);
    }


}
