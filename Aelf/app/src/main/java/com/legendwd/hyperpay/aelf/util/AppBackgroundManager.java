package com.legendwd.hyperpay.aelf.util;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class AppBackgroundManager {

    static final AppBackgroundManager mInstance = new AppBackgroundManager();
    public static boolean sToOuther = false;
    private final int STATE_OPEN = 0;

    private final int STATE_RESUMED = 1;

    private final int STATE_STOPPED = 2;
    /**
     * 一些状态基用于判断用户是否在前台
     **/
    private boolean isAppForeground = false;
    private int mActivityStated;
    private IAppStateChangeListener mListener;
    private AtomicBoolean mLastResume = new AtomicBoolean(false);//上一次是否调用resume方法
    private AtomicInteger mMultiStart = new AtomicInteger(0);
    private String mLastStartActivityName;//上一次触发resume的页面

    private AppBackgroundManager() {
    }


    public static AppBackgroundManager getInstance() {
        return mInstance;
    }

    /**
     * 在Application的onActivityStarted中调用
     */
    public void onActivityStarted(String activityName) {
        //如果跟上一次是同一个activity，则不认为是多次resume
        boolean isTheSame = activityName.equals(mLastStartActivityName);
        if (!isTheSame && mLastResume.get()) {
            mMultiStart.incrementAndGet();
        }
        mLastStartActivityName = activityName;
        mLastResume.set(true);
        if (!isAppForeground) {//如果是切换进前台
            mActivityStated = STATE_OPEN;//第一次打开状态
            onAppForegroundStateChange(true);
        } else {
            mActivityStated = STATE_RESUMED;
        }
        isAppForeground = true;
    }

    /**
     * 在Application的onActivityStopped中调用
     */
    public void onActivityStopped() {//连续两次stop会触发进入后台，如果是程序本身快速关闭两个页面导致的连续stop，需要过滤掉
        if (mMultiStart.get() > 1) {//上一次是stop，且上一次之前有连续多次不同activity的resume
            mMultiStart.decrementAndGet();
            return;
        }
        mLastResume.set(false);
        if (mActivityStated == STATE_RESUMED) { //可以理解为最新的Activity在应用内
            mActivityStated = STATE_STOPPED;
            return;
        }
        if (isAppForeground) {
            mMultiStart.set(0);
            isAppForeground = false;
            onAppForegroundStateChange(false);
        }
    }

    public boolean isAppOnForeground() {
        return isAppForeground;
    }


    //App前后台切换
    private void onAppForegroundStateChange(boolean isAppForeground) {
        if (mListener == null) {
            return;
        }
        mListener.onAppStateChanged(isAppForeground);
    }

    public void setAppStateListener(IAppStateChangeListener listener) {
        mListener = listener;
    }

    public interface IAppStateChangeListener {

        void onAppStateChanged(boolean isAppForceground);
    }
}
