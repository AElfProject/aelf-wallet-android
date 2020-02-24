package com.legendwd.hyperpay.aelf.business.assets.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.model.bean.ChainAddressBean;
import com.legendwd.hyperpay.aelf.util.BitmapUtil;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;
import com.legendwd.hyperpay.lib.FileProvider7;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class ReceiveFragment extends BaseFragment {

    private static final int REQUEST_WRITE_STORAGE_PERMISSION = 121;
    @BindView(R.id.img_code)
    ImageView imgCode;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_logo)
    ImageView iv_logo;
    File file;

    public static ReceiveFragment newInstance(Bundle bundle) {
        ReceiveFragment fragment = new ReceiveFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_receive;
    }

    @Override
    public void process() {

        ChainAddressBean bean = new Gson().fromJson(getArguments().getString("bean"), ChainAddressBean.class);

        initToolbarNav(mToolbar, "", true);

//        tv_title.setText(CacheUtil.getInstance().getProperty(Constant.Sp.CHAIN_ID) + " " + bean.getSymbol() + " " + getString(R.string.receive));

        tv_title.setText(bean.getChain_id() + " " + bean.getSymbol() + " " + getString(R.string.receive));
        ((TextView) mToolbar.findViewById(R.id.tv_title_right)).setText(R.string.share);

        tv_address.setText(CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_COIN_ADDRESS));
        BitmapUtil.create2QR(_mActivity, imgCode, CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_COIN_ADDRESS));

        if (ContextCompat.checkSelfPermission(_mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE_PERMISSION);
            } else {
            }
        }

        Glide.with(_mActivity)
                .load(bean.getLogo())
                .placeholder(R.mipmap.logo_mark)
                .into(iv_logo);

    }

    @OnClick({R.id.tv_copy, R.id.tv_title_right})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.tv_copy:
                copyText(CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_COIN_ADDRESS));
                break;

            case R.id.tv_title_right:

                Intent intent = new Intent(Intent.ACTION_SEND);
                Uri imageUri = null;
                if (file != null) {
                    imageUri = Uri.fromFile(file);
                } else {
                    screenshot();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = FileProvider7.getUriForFile(_mActivity.getApplicationContext(), file);//通过FileProvider创建一个content类型的Uri
                }
                if (imageUri != null) {
                    intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    intent.setType("image/*");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(Intent.createChooser(intent, "Popup"));
                }

                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                screenshot();
            }
        }
    }

    private void screenshot() {

        Bitmap temBitmap;
        //找到当前页面的跟布局
        View view = _mActivity.getWindow().getDecorView().getRootView();
        //设置缓存
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        //从缓存中获取当前屏幕的图片
        temBitmap = view.getDrawingCache();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh-mm");
        String time = df.format(new Date());
        file = new File(_mActivity.getFilesDir() + Constant.sharePath, time + ".png");
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            temBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
