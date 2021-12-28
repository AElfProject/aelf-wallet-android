package com.legendwd.hyperpay.aelf;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.activeandroid.ActiveAndroid;
import com.github.ont.connector.utils.SPWrapper;
import com.legendwd.hyperpay.aelf.db.UpgradeHelper;
import com.legendwd.hyperpay.aelf.db.greendao.DaoMaster;
import com.legendwd.hyperpay.aelf.db.greendao.DaoSession;
import com.legendwd.hyperpay.aelf.httpservices.MainService;
import com.legendwd.hyperpay.aelf.model.MessageEvent;
import com.legendwd.hyperpay.aelf.model.bean.NetWorkBean;
import com.legendwd.hyperpay.aelf.util.AppBackgroundManager;
import com.legendwd.hyperpay.aelf.util.JsonUtils;
import com.legendwd.hyperpay.aelf.util.LanguageUtil;
import com.legendwd.hyperpay.aelf.util.StringUtil;
import com.legendwd.hyperpay.lib.AppConfig;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;
import com.legendwd.hyperpay.lib.Logger;
import com.legendwd.hyperpay.lib.Util;
import com.legendwd.hyperpay.router.IMainService;
import com.legendwd.hyperpay.router.Router;
import com.tencent.bugly.crashreport.CrashReport;
// import com.tencent.smtt.sdk.QbSdk;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AelfApplication extends MultiDexApplication {

    private static Context sContext;
    private static WeakReference<Activity> sTopActivity;
    private static DaoSession mDaoSession;

    public static HashMap<String,String> PRIVATEKEY_DAPP = new HashMap();

    public static Activity getTopActivity() {
        if (sTopActivity != null) {
            return sTopActivity.get();
        }
        return null;
    }

    public static Context getSContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppConfig.init(this);
        LanguageUtil.setLanguage(this);

        initUmeng();

        CrashReport.initCrashReport(getApplicationContext(), "00071bffac", false);

        sContext = this;
        String udid = Util.getUDID(AelfApplication.this);
        CacheUtil.getInstance().setProperty(Constant.Sp.UDID, udid);

        Router.register(IMainService.class, new MainService());


        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                PushAgent.getInstance(activity).onAppStart();
            }

            @Override
            public void onActivityStarted(Activity activity) {
                AppBackgroundManager.getInstance().onActivityStarted(activity.getLocalClassName());

            }

            @Override
            public void onActivityResumed(Activity activity) {
                sTopActivity = new WeakReference<>(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                if (isToOuther(activity)) {
                    AppBackgroundManager.sToOuther = true;
                }
                AppBackgroundManager.getInstance().onActivityStopped();
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }

            public boolean isToOuther(Context context) {
                ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(2);
                for (ActivityManager.RunningTaskInfo runningTaskInfo : tasks) {
                    if (runningTaskInfo != null) {
                        if (runningTaskInfo.baseActivity.getPackageName().equals(context.getPackageName())
                                && !runningTaskInfo.baseActivity.getPackageName().equals(runningTaskInfo.topActivity.getPackageName())) {
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                SPWrapper.setContext(sContext);


                // //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。


                // QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

                //     @Override
                //     public void onViewInitFinished(boolean arg0) {
                //         //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                //     }

                //     @Override
                //     public void onCoreInitFinished() {
                //     }
                // };
                // //x5内核初始化接口
                // QbSdk.initX5Environment(getApplicationContext(), cb);
            }
        });
        initGreenDao();
        intNetwork();

    }

    public static DaoSession getDaoSession() {
        return mDaoSession;
    }

    private void initGreenDao() {
        UpgradeHelper devOpenHelper = new UpgradeHelper(this, Constant.DB_NAME, null);
        SQLiteDatabase database = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        mDaoSession = daoMaster.newSession();
    }

    private void intNetwork() {
        String url = CacheUtil.getInstance().getProperty(Constant.Sp.NETWORK_BASE_URL);
        if(TextUtils.isEmpty(url)){
            String json = StringUtil.getAssetsJson(this);
            List<NetWorkBean> list = JsonUtils.jsonToList(json, NetWorkBean.class);
            if(list != null && !list.isEmpty()) {
                NetWorkBean bean = list.get(0);
                String baseUrl = bean.getUrl();
                String node = bean.getNode();
                if(!TextUtils.isEmpty(node)){
                    baseUrl += node;
                }
                CacheUtil.getInstance().setProperty(Constant.Sp.NETWORK_BASE_URL, baseUrl);
            }
        }
    }

    void initUmeng() {

        UMConfigure.init(this, "5d1495e63fc19562a9000b30", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "da44124aa3ae4a818ee762896bc343aa");
        PushAgent mPushAgent = PushAgent.getInstance(this);
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public Notification getNotification(Context context, UMessage msg) {
//                Map<String,String> map = msg.extra;
//                for (String value : map.layout()) {
//                    Log.d("value = ",  value);
//                }
                return super.getNotification(context, msg);
            }
        };
        mPushAgent.setMessageHandler(messageHandler);


        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {

            @Override
            public void openUrl(Context context, UMessage uMessage) {
                super.openUrl(context, uMessage);
            }

            @Override
            public void launchApp(Context context, UMessage uMessage) {
                super.launchApp(context, uMessage);
            }

            @Override
            public void dealWithCustomAction(Context context, UMessage uMessage) {
                super.dealWithCustomAction(context, uMessage);
            }

            @Override
            public void openActivity(Context context, UMessage uMessage) {
                super.openActivity(context, uMessage);
                Map<String, String> map = uMessage.extra;
                try {
                    JSONObject jsonObject = new JSONObject(map.get("extra"));
                    String txid = jsonObject.optString("txid");
                    int type = jsonObject.optInt("type");
                    String address = jsonObject.optString("address");
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.BundleKey.TXID, txid);
                    bundle.putString(Constant.BundleKey.ADDRESS, address);
                    bundle.putInt(Constant.BundleKey.NOTIFICATION_TYPE, type);
                    MessageEvent messageEvent = new MessageEvent(bundle);
                    messageEvent.setMessage(Constant.Event.NOTIFICATION);
                    EventBus.getDefault().postSticky(messageEvent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
//                Message message = mHandler.obtainMessage();
//                message.obj = deviceToken;
//                mHandler.sendMessage(message);
                Logger.e("onSuccess：deviceToken：-------->  " + deviceToken);

                CacheUtil.getInstance().setProperty(Constant.Sp.DEVICE_TOKEN, deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                Logger.e("onFailure：-------->  " + "s:" + s + ",s1:" + s1);
            }
        });


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }

}
