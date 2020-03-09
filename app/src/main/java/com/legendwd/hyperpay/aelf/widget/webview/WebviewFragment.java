package com.legendwd.hyperpay.aelf.widget.webview;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.lib.Constant;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;

public class WebviewFragment extends BaseFragment {

    @BindView(R.id.webview)
    DWebView mWebView;
    @BindView(R.id.progress_Bar)
    ProgressBar mProgressBar;
    private String mUrl;
    private String mTitle;
    private String mData;
    private android.webkit.ValueCallback<Uri[]> mUploadCallbackAboveL;
    private android.webkit.ValueCallback<Uri> mUploadCallbackBelow;
    private Uri imageUri;
    private int REQUEST_CODE = 1234;

    public static WebviewFragment newInstance(Bundle bundle) {
        WebviewFragment webviewFragment = new WebviewFragment();
        webviewFragment.setArguments(bundle);
        return webviewFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_web;
    }

    @Override
    public void process() {

        Bundle bundle = getArguments();
        mUrl = bundle.getString(Constant.BundleKey.KEY_WEBVIEW_URl);
        mTitle = bundle.getString(Constant.BundleKey.KEY_WEBVIEW_TITLE);
        mData = bundle.getString(Constant.BundleKey.KEY_WEBVIEW_DATA);

        initToolbarNav(mToolbar, mTitle, true);

        if (!TextUtils.isEmpty(mData)) {

            String htmlhead = "<html lang=\"zh-cn\"><head><meta charset=\"utf-8\"><meta name=\"viewport\" content=\"width=device-width, nickName-scalable=no\"></meta><style>img{max-width: 100%; width:auto; height:auto;}</style></head><body>";

            String htmlEnd = "</body></html>";

            String content = htmlhead + mData + htmlEnd;

            mWebView.loadData(content, "text/html", "UTF-8");
        } else {
            mWebView.loadUrl(mUrl);
        }


        mWebView.setWebChromeClient(new WebChromeClient() {

            // Android < 3.0 调用这个方法
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadCallbackBelow = uploadMsg;
                takePhoto();
            }

            // 3.0 + 调用这个方法
            public void openFileChooser(ValueCallback filePathCallback,
                                        String acceptType) {
                openFileChooser(filePathCallback);
            }

            //  / js上传文件的<input type="file" name="fileField" id="fileField" />事件捕获
            // Android > 4.1.1 调用这个方法
            public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                        String acceptType, String capture) {
                openFileChooser(uploadMsg);
            }

            @Override
            public boolean onShowFileChooser(WebView webView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             WebChromeClient.FileChooserParams fileChooserParams) {
                mUploadCallbackAboveL = filePathCallback;
                takePhoto();
                return true;
            }
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                if(!TextUtils.isEmpty(title)){
//                    initToolbarNav(mToolbar, title, true);
//                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }

            }
        });
    }

    /**
     * 调用相机
     */
    private void takePhoto() {
        // 指定拍照存储位置的方式调起相机
        String filePath = Environment.getExternalStorageDirectory() + File.separator
                + Environment.DIRECTORY_PICTURES + File.separator;
        String fileName = "IMG_" + DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
        imageUri = Uri.fromFile(new File(filePath + fileName));
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        Intent Photo = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        Intent chooserIntent = Intent.createChooser(Photo, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureIntent});

        startActivityForResult(chooserIntent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == REQUEST_CODE) {
            // 经过上边(1)、(2)两个赋值操作，此处即可根据其值是否为空来决定采用哪种处理方法
            if (mUploadCallbackBelow != null) {
                chooseBelow(resultCode, data);
            } else if (mUploadCallbackAboveL != null) {
                chooseAbove(resultCode, data);
            }
        }
    }

    /**
     * Android API < 21(Android 5.0)版本的回调处理
     * @param resultCode 选取文件或拍照的返回码
     * @param data 选取文件或拍照的返回结果
     */
    private void chooseBelow(int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            updatePhotos();

            if (data != null) {
                // 这里是针对文件路径处理
                Uri uri = data.getData();
                if (uri != null) {
                    mUploadCallbackBelow.onReceiveValue(uri);
                } else {
                    mUploadCallbackBelow.onReceiveValue(null);
                }
            } else {
                // 以指定图像存储路径的方式调起相机，成功后返回data为空
                mUploadCallbackBelow.onReceiveValue(imageUri);
            }
        } else {
            mUploadCallbackBelow.onReceiveValue(null);
        }
        mUploadCallbackBelow = null;
    }

    /**
     * Android API >= 21(Android 5.0) 版本的回调处理
     * @param resultCode 选取文件或拍照的返回码
     * @param data 选取文件或拍照的返回结果
     */
    private void chooseAbove(int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            updatePhotos();

            if (data != null) {
                // 这里是针对从文件中选图片的处理
                Uri[] results;
                Uri uriData = data.getData();
                if (uriData != null) {
                    results = new Uri[]{uriData};
                    for (Uri uri : results) {
                    }
                    mUploadCallbackAboveL.onReceiveValue(results);
                } else {
                    mUploadCallbackAboveL.onReceiveValue(null);
                }
            } else {
                mUploadCallbackAboveL.onReceiveValue(new Uri[]{imageUri});
            }
        } else {
            mUploadCallbackAboveL.onReceiveValue(null);
        }
        mUploadCallbackAboveL = null;
    }

    private void updatePhotos() {
        // 该广播即使多发（即选取照片成功时也发送）也没有关系，只是唤醒系统刷新媒体文件
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(imageUri);
        getActivity().sendBroadcast(intent);
    }

    /**
     * 权限申请
     */
    private void applyPermission(){

        //版本大于6.0的情况
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 103;
            String[] permissions = {Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissions, REQUEST_CODE_CONTACT);
        }
    }

}
