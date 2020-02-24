package com.legendwd.hyperpay.aelf.business.my.fragments;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.BuildConfig;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.dialogfragments.OpenCompInfoDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.ToastDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.UpdateDialog;
import com.legendwd.hyperpay.aelf.listeners.HandleCallback;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.UpdateBean;
import com.legendwd.hyperpay.aelf.model.param.BaseParam;
import com.legendwd.hyperpay.aelf.presenters.IAboutUsPresenter;
import com.legendwd.hyperpay.aelf.presenters.impl.AboutUsPresenter;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.aelf.util.ShowMessage;
import com.legendwd.hyperpay.aelf.views.IAboutUsView;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;

@SuppressWarnings("ALL")
public class AboutUsFragment extends BaseFragment implements IAboutUsView {

    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_telegram)
    TextView tv_telegram;
    @BindView(R.id.tv_twitter)
    TextView tv_twitter;
    @BindView(R.id.tv_facebook)
    TextView tv_facebook;
    @BindView(R.id.tv_wechat)
    TextView tv_wechat;
    private IAboutUsPresenter presenter;

    public static SupportFragment newInstance() {
        return new AboutUsFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_about_us;
    }

    @Override
    public void process() {
        initToolbarNav(mToolbar, R.string.about_us, true);
        presenter = new AboutUsPresenter(this);

        PackageInfo info;
        try {
            info = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            tvVersion.setText("V " + info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings("SpellCheckingInspection")
    @OnClick({R.id.tv_version_log, R.id.tv_version_update, R.id.tv_wechat, R.id.tv_telegram, R.id.tv_twitter, R.id.tv_facebook})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.tv_version_log:
                start(VersionLogFragment.newInstance());
                break;

            case R.id.tv_version_update:
                presenter.update(new BaseParam());
                break;

            case R.id.tv_wechat:
                showCompInfoDialog(tv_wechat.getText().toString(), getString(R.string.aelf_community));
                break;

            case R.id.tv_telegram:
                showCompInfoDialog(tv_telegram.getText().toString(), "https://t.me/aelfblockchain");
                break;
            case R.id.tv_twitter:

                showCompInfoDialog(tv_twitter.getText().toString(), "https://twitter.com/aelfblockchain");
                break;
            case R.id.tv_facebook:
                showCompInfoDialog(tv_facebook.getText().toString(), "https://www.facebook.com/aelfofficial/");
                break;
        }
    }


    private void showCompInfoDialog(String title, String message) {
        DialogUtils.showDialog(OpenCompInfoDialog.class, getFragmentManager())
                .setTitle(title)
                .setMessage(message)
                .setHandleCallback(o -> {

                    String data = (String) o;
                    copyText(data);
                });
    }


    @Override
    public void onUpdateSuccess(ResultBean<UpdateBean> bean) {
        if (200 == bean.getStatus()) {


            if (bean!=null
                    &&bean.getData()!=null
                    && !TextUtils.isEmpty(bean.getData().verNo)
                    &&isNewVersion(BuildConfig.VERSION_NAME,bean.getData().verNo)) {
                showNoticeDialog(bean.getData());
            } else {
                DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(R.string.latest_version);
            }
        }
    }


    public static boolean isNewVersion(String localVersion, String onlineVersion) {
        if (localVersion == null || onlineVersion == null) {
            throw new IllegalArgumentException("argument may not be null !");
        }
        if (localVersion.equals(onlineVersion)) {
            return false;
        }
        String[] localArray = localVersion.replaceAll("[^0-9.]", "").split("[.]");
        String[] onlineArray = onlineVersion.replaceAll("[^0-9.]", "").split("[.]");
        int length = localArray.length < onlineArray.length ? localArray.length : onlineArray.length;
        for (int i = 0; i < length; i++) {
            if (Integer.parseInt(onlineArray[i]) > Integer.parseInt(localArray[i])) {
                return true;
            } else if (Integer.parseInt(onlineArray[i]) < Integer.parseInt(localArray[i])) {
                return false;
            }
            // 相等 比较下一组值
        }
        //比较最后差异组数值
        if (localArray.length < onlineArray.length) {
            return Integer.parseInt(onlineArray[onlineArray.length - 1]) > 0;
        } else if (localArray.length > onlineArray.length) {
            return 0 > Integer.parseInt(localArray[localArray.length - 1]);
        }
        return true;
    }

    @Override
    public void onUpdateError(int code, String msg) {

    }

    private void showNoticeDialog(UpdateBean updateBean) {

        UpdateDialog updateDialog = DialogUtils.showDialog(UpdateDialog.class, getFragmentManager());
        updateDialog.setCloseable(true);
        updateDialog.setContent(updateBean);
        updateDialog.setHandleCallback(new HandleCallback() {
            @Override
            public void onHandle(Object o) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(updateBean.appUrl);
                intent.setData(content_url);
                startActivity(intent);
            }
        });


//
//        //新版本更新弹框
//        if (getActivity() != null) {
//            final NewVersionDialog dialog1 = new NewVersionDialog(getActivity(), updateBean.is_force);
//
//            dialog1.setPositiveListener(() -> {
//                // 采用浏览器 下载更新
//                CacheUtil.getInstance().setProperty(Constant.Sp.KEY_IS_FORCE, "0");
//                Intent intent = new Intent();
//                intent.setAction("android.intent.action.VIEW");
//                Uri content_url = Uri.parse(updateBean.appUrl);
//                intent.setData(content_url);
//                startActivity(intent);
//            });
//            dialog1.setNegativeListener(() -> {
//                CacheUtil.getInstance().setProperty(Constant.Sp.KEY_IS_FORCE, "1");
//                dialog1.dialog.dismiss();
//            });
//            if (!getActivity().isFinishing()) {
//                dialog1.initDialog(updateBean.intro, updateBean.verNo).show();
//            }
//        }
    }
}
