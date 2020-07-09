package com.legendwd.hyperpay.aelf.business.my.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.config.ApiUrlConfig;
import com.legendwd.hyperpay.aelf.dialogfragments.ToastDialog;
import com.legendwd.hyperpay.aelf.listeners.HandleCallback;
import com.legendwd.hyperpay.aelf.model.bean.WalletBean;
import com.legendwd.hyperpay.aelf.util.BitmapUtil;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.aelf.util.ImgUtils;
import com.legendwd.hyperpay.aelf.widget.RoundImageView;
import com.legendwd.hyperpay.aelf.widget.webview.DWebView;
import com.legendwd.hyperpay.aelf.widget.webview.JsApi;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;

public class QRCodeFragment extends BaseFragment {

    @BindView(R.id.tv_save)
    TextView mTvShave;
    @BindView(R.id.iv_qr_code)
    ImageView qrCode;
    @BindView(R.id.webview)
    DWebView webview;
    @BindView(R.id.iv_logo)
    RoundImageView iv_logo;
    private ToastDialog loadDialog;

    public static SupportFragment newInstance(Bundle bundle) {

        QRCodeFragment fragment = new QRCodeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_qr_code;
    }

    @Override
    public void process() {
        initToolbarNav(mToolbar, R.string.backup_desc, true);

        loadKeystoreValue();

        webview.loadUrl(ApiUrlConfig.AssetsUrl);

        webview.addJavascriptObject(new JsApi(new HandleCallback() {
            @Override
            public void onHandle(Object o) {
                _mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jo = new JSONObject((String) o);

                            String keystore = jo.optString("keystore");

                            create2QR(keystore);
                            int success = jo.optInt("success", -1);
                            if (1 == success) {

                            } else {
                                loadDialog.setLoading(false);
                                loadDialog.setToast(getString(R.string.load_fail));
                                loadDialog.show(getFragmentManager(), "ToastDialog");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadDialog.dismiss();
                        }
                    }
                });
            }
        }), null);

        Glide.with(_mActivity)
                .load(CacheUtil.getInstance().getProperty(Constant.Sp.ACCOUNT_PORTRAIT))
                .placeholder(R.mipmap.user_profile)
                .into(iv_logo);
    }

//    private Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            loadDialog.dismiss();
//        }
//    };

    private void create2QR(String data) {

        BitmapUtil.create2QR(_mActivity, qrCode, data);
        loadDialog.dismiss();
    }

    private void loadKeystoreValue() {
        loadDialog = DialogUtils.showDialog(ToastDialog.class, getFragmentManager());
        loadDialog.setLoading(true);
        loadDialog.setToastCancelable(false);
        WalletBean bean = new Gson().fromJson(getArguments().getString("bean"), WalletBean.class);
        bean.address = CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS);

        webview.callHandler("getWalletKeyStoreJS", new Gson().toJson(bean), new CallBackFunction() {

            @Override
            public void onCallBack(String data) {

            }
        });
    }

    @OnClick(R.id.tv_save)
    void onClickSave() {

        //版本大于6.0的情况
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            // 方法 checkSelfPermission  在 android 6.0以下没有该方法
            if (ContextCompat.checkSelfPermission(_mActivity, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                //申请权限
                requestPermissions(permissions, REQUEST_CODE_CONTACT);
            } else {
                screenshot();
            }

        } else {
            screenshot();
        }

    }

    private void screenshot() {

        //生成相同大小的图片
        Bitmap temBitmap;
        //找到当前页面的跟布局
        View view = _mActivity.getWindow().getDecorView().getRootView();
        mTvShave.setVisibility(View.GONE);
        //设置缓存
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        //从缓存中获取当前屏幕的图片
        temBitmap = view.getDrawingCache();
        mTvShave.setVisibility(View.VISIBLE);

        if (ImgUtils.saveImageToGallery(_mActivity, temBitmap, "qr_code")) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                    .setToast(R.string.save_success);
        } else {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                    .setToast(R.string.permisson_denied);
        }
    }


//    void saveQrCode() {
//        qrCode.setDrawingCacheEnabled(true);
//        qrCode.buildDrawingCache();
//        Bitmap bitmap = Bitmap.createBitmap(qrCode.getDrawingCache());
//        qrCode.setDrawingCacheEnabled(false);
//        if (ImgUtils.saveImageToGallery(_mActivity, bitmap, "qr_code")) {
//            ShowMessage.toastMsg(_mActivity, R.string.save_success);
//        } else {
//            ShowMessage.toastMsg(_mActivity, "Permission denied");
//
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (checkPermissionResult(grantResults)) {
            screenshot();
        } else {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                    .setToast(R.string.permisson_denied);
        }
    }

    /**
     * 检测请求结果码判定是否授权
     *
     * @param grantResults
     * @return
     */
    private boolean checkPermissionResult(int[] grantResults) {
        if (grantResults != null) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
