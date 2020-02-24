package com.legendwd.hyperpay.aelf.business.my.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.wallet.CreateImportWalletActivity;
import com.legendwd.hyperpay.aelf.business.wallet.fragments.ExportKeystoreFragment;
import com.legendwd.hyperpay.aelf.business.wallet.fragments.ExportMnemonicPhraseFragment;
import com.legendwd.hyperpay.aelf.business.wallet.fragments.ExportPrivateFragment;
import com.legendwd.hyperpay.aelf.dialogfragments.ChoosePhotoDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.DoubleButtonDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.InputDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.ToastDialog;
import com.legendwd.hyperpay.aelf.listeners.HandleCallback;
import com.legendwd.hyperpay.aelf.listeners.OnTextCorrectCallback;
import com.legendwd.hyperpay.aelf.model.bean.ChainBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.WalletBean;
import com.legendwd.hyperpay.aelf.presenters.impl.MyAccountPresenter;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.aelf.views.IMyAccountView;
import com.legendwd.hyperpay.aelf.widget.RoundImageView;
import com.legendwd.hyperpay.httputil.BuildConfig;
import com.legendwd.hyperpay.httputil.EncrptUtil;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;
import com.legendwd.hyperpay.lib.FileProvider7;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MyAccountFragment extends BaseFragment implements IMyAccountView {

    @BindView(R.id.iv_cover)
    RoundImageView mIvCover;
    @BindView(R.id.tv_name)
    TextView mTvName;
    ToastDialog mToastDialog;
    @BindView(R.id.rel_export_mnemonic_phrase)
    LinearLayout rel_export_mnemonic_phrase;
    private MyAccountPresenter mMyAccountPresenter;

    public static MyAccountFragment newInstance() {
        Bundle args = new Bundle();
        MyAccountFragment tabFourFragment = new MyAccountFragment();
        tabFourFragment.setArguments(args);
        return tabFourFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initToolbarNav(mToolbar, R.string.identity_management, true);
        super.onViewCreated(view, savedInstanceState);

        String path = CacheUtil.getInstance().getProperty(Constant.Sp.ACCOUNT_PORTRAIT);
        Glide.with(_mActivity).load(path).placeholder(R.mipmap.user_profile).into(mIvCover);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_account;
    }

    @Override
    public void process() {
        mMyAccountPresenter = new MyAccountPresenter(this);
        mMyAccountPresenter.getChainInfo();
        String path = CacheUtil.getInstance().getProperty(Constant.Sp.ACCOUNT_PORTRAIT);
        Glide.with(_mActivity).load(path).placeholder(R.mipmap.user_profile).into(mIvCover);

        String name = CacheUtil.getInstance().getProperty(Constant.Sp.ACCOUNT_NAME);
        if (!TextUtils.isEmpty(name)) {
            mTvName.setText(name);
        }

        if (CacheUtil.getInstance().getProperty(Constant.Sp.is_KeyStore, false)) {
            rel_export_mnemonic_phrase.setVisibility(View.GONE);
        } else {
            rel_export_mnemonic_phrase.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.rel_password_hint, R.id.rel_export_mnemonic_phrase, R.id.rel_export_keystore,
            R.id.rel_export_private_key, R.id.rel_export_qr_code, R.id.btn_logout})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.rel_password_hint:
                start(PwdHindFragment.newInstance());
                break;

            case R.id.rel_export_mnemonic_phrase:

                showPasswordDialogForMnemonic(view.getId());
                break;
            case R.id.rel_export_qr_code:
            case R.id.rel_export_private_key:
            case R.id.rel_export_keystore:
                showPasswordDialogForPrivate(view.getId());
                break;

            case R.id.btn_logout:
                showLogoutDialog();
                break;
        }
    }

    private void showLogoutDialog() {
        DialogUtils.showDialog(DoubleButtonDialog.class, getFragmentManager())
                .setTitle(getString(R.string.logout_title))
                .setMessage(getString(R.string.logout_message))
                .setHandleCallback(new HandleCallback() {
            @Override
            public void onHandle(Object o) {

                if((boolean)o){
                    showPasswordDialogForPrivate(new OnTextCorrectCallback() {
                        @Override
                        public void onTextCorrect(Object... obj) {
                            clearCache();
                            startActivity(new Intent(_mActivity, CreateImportWalletActivity.class));
                            _mActivity.finish();
                        }
                    });
                }
            }
        });
    }

    private void showPasswordDialogForMnemonic(int resId) {
        showPasswordDialogForMnemonic(new OnTextCorrectCallback() {
            @Override
            public void onTextCorrect(Object... obj) {
                String data = (String) obj[1];

                Bundle bundle = new Bundle();
                WalletBean bean = new WalletBean();
                bean.mnemonic = data;
                bundle.putString("bean", new Gson().toJson(bean));

                switch (resId) {
                    case R.id.rel_export_mnemonic_phrase:
                        bundle.putString(Constant.BundleKey.BACKUP, Constant.BundleValue.BACKUP_MY_ACCOUNT_PAGE);
                        start(ExportMnemonicPhraseFragment.newInstance(bundle));
                        break;

                }
            }
        });
    }

    private void showPasswordDialogForPrivate(int resId) {
        showPasswordDialogForPrivate(new OnTextCorrectCallback() {
            @Override
            public void onTextCorrect(Object... obj) {

                String pwd = (String) obj[0];
                String key = (String) obj[1];

                switch (resId) {
                    case R.id.rel_export_private_key: {
                        Bundle bundle = new Bundle();
                        WalletBean bean = new WalletBean();
                        bean.privateKey = key;
                        bundle.putString("bean", new Gson().toJson(bean));
                        start(ExportPrivateFragment.newInstance(bundle));
                        break;
                    }

                    case R.id.rel_export_keystore: {
                        Bundle bundle = new Bundle();
                        WalletBean bean = new WalletBean();
                        bean.password = pwd;
                        bean.privateKey = key;
                        bundle.putString("bean", new Gson().toJson(bean));
                        start(ExportKeystoreFragment.newInstance(bundle));
                        break;
                    }
                    case R.id.rel_export_qr_code: {
                        Bundle bundle = new Bundle();
                        WalletBean bean = new WalletBean();
                        bean.password = pwd;
                        bean.privateKey = key;
                        bundle.putString("bean", new Gson().toJson(bean));
                        start(QRCodeFragment.newInstance(bundle));
                        break;
                    }
                }
            }
        });

    }

    @OnClick(R.id.iv_copy)
    void onClickCopy() {
        copyText(mTvName.getText().toString());
    }

    @OnClick(R.id.rel_name)
    void onClickEdtName() {
        InputDialog dialogFragment = DialogUtils.showDialog(InputDialog.class, getFragmentManager())
                .setTitle(R.string.account_name)
                .setHint(R.string.account_name)
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .setMaxLength(20);
        dialogFragment.setHandleCallback(o -> {
            String name = (String) o;
            updateIdentity("", name);
            dialogFragment.dismiss();
        });
    }

    @OnClick(R.id.iv_cover)
    void onClickCover() {
        DialogUtils.showDialog(ChoosePhotoDialog.class, getFragmentManager()).setHandleCallback(new HandleCallback() {
            @Override
            public void onHandle(Object o) {
                String path;
                if (o instanceof Uri) {
                    path = FileProvider7.uriToFile((Uri) o, _mActivity).getAbsolutePath();
                } else {
                    path = (String) o;
                }
                if (TextUtils.isEmpty(path)) {
                    return;
                }

                updateIdentity(path, "");
            }
        });
    }

    void updateIdentity(String path, String name) {

        if (mToastDialog == null) {
            mToastDialog = DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                    .setToast(R.string.loading)
                    .setLoading(true);
        } else {
            mToastDialog.show(getFragmentManager(), "ToastDialog");
        }
//        Map<String, RequestBody> map = new HashMap<>();
//        if (!TextUtils.isEmpty(path)) {
//            File file = new File(path);
//            map.put("img",RequestBody.create(MediaType. parse("multipart/form-data;charset=UTF-8"), file));
//        }
//        if (!TextUtils.isEmpty(name)) {
//            map.put("name",RequestBody.create(MediaType. parse("text/plain;charset=UTF-8"), name));
//        }
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("img", file.getName(), requestFile);

            RequestBody address = RequestBody.create(MediaType.parse("text/plain"), EncrptUtil.enRSA(CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS)));
            RequestBody device = RequestBody.create(MediaType.parse("text/plain"), EncrptUtil.enRSA("Android"));
            RequestBody udid = RequestBody.create(MediaType.parse("text/plain"), EncrptUtil.enRSA(CacheUtil.getInstance().getProperty(Constant.Sp.UDID)));
            RequestBody version = RequestBody.create(MediaType.parse("text/plain"), EncrptUtil.enRSA(BuildConfig.VERSION_NAME));
            RequestBody img = RequestBody.create(MediaType.parse("text/plain"), file.getName());
            RequestBody test = RequestBody.create(MediaType.parse("text/plain"), "1");


            mMyAccountPresenter.updateIdentityCover(path, part, address, device, udid, version, img, test);
        } else if (!TextUtils.isEmpty(name)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", name);
            mMyAccountPresenter.updateIdentityName(jsonObject);
        }
    }

    @Override
    public void onUpdateIdentitySuccess(String path, ResultBean resultBean) {
        mToastDialog.dismiss();
        if (200 == resultBean.getStatus()) {
            if (path != null) {
                CacheUtil.getInstance().setProperty(Constant.Sp.ACCOUNT_PORTRAIT, path);
                Glide.with(_mActivity).load(path).placeholder(R.mipmap.user_profile).into(mIvCover);
            }
        }
    }

    @Override
    public void onUpdateIdentityNameSuccess(String name, ResultBean resultBean) {
        mToastDialog.dismiss();
        mTvName.setText(name);
        CacheUtil.getInstance().setProperty(Constant.Sp.ACCOUNT_NAME, name);

    }

    @Override
    public void onUpdateIdentityNameError(int code, String msg) {
        mToastDialog.dismiss();
    }

    @Override
    public void onChaininfoSuccess(ResultBean<ChainBean> resultBean) {

    }

    @Override
    public void onChaininfoError(int code, String msg) {

    }

    @Override
    public void onUploadDataSuccess(ResultBean<Object> beanList) {

    }

    @Override
    public void onUploadDataFail(int code, String msg) {

    }

    @Override
    public void onBindCoinSuccess(ResultBean resultBean) {

    }

    @Override
    public void onBindCoinFail(int code, String msg) {

    }

    @Override
    public void onUpdateIdentityError(int code, String msg) {
        mToastDialog.dismiss();

        DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(msg);
    }

}
