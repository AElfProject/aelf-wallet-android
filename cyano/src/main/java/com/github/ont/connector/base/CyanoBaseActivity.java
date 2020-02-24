package com.github.ont.connector.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.github.ont.connector.R;
import com.github.ont.connector.utils.ToastUtil;
import com.github.ont.connector.view.PasswordDialog;

public abstract class CyanoBaseActivity extends AppCompatActivity {
    public static final String TAG = "BaseActivity";
    public Activity baseActivity;
    public GetDialogPassword getDialogPassword;
    private Dialog loadingDialog;
    private PasswordDialog passwordDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoading();
    }

    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new Dialog(this, R.style.dialog);
            View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_cyano_loading, null);
            loadingDialog.setContentView(inflate);
        }
        if (loadingDialog.isShowing()) {
            return;
        }
        loadingDialog.show();
    }

    public void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    public void copyAddress(String data, String des) {
        ClipboardManager clipboard = (ClipboardManager) baseActivity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("key", data);
        clipboard.setPrimaryClip(clip);
        ToastUtil.showToast(this, des);
    }

    public void setGetDialogPwd(GetDialogPassword getDialogPwd) {
        this.getDialogPassword = getDialogPwd;
    }

    //显示密码输入
    public void showPasswordDialog(String des) {
        if (getDialogPassword == null) {
            ToastUtil.showToast(baseActivity, "System error ,Please restart");
            return;
        }
        if (passwordDialog != null && passwordDialog.isShowing()) {
            return;
        }
        passwordDialog = new PasswordDialog(this);
        passwordDialog.setDes(des).setConfirmListener(new PasswordDialog.ConfirmListener() {
            @Override
            public void passwordConfirm(String password) {
                passwordDialog.dismiss();
                if (getDialogPassword != null) {
                    getDialogPassword.handleDialog(password);
                }
            }
        });
        passwordDialog.show();
    }

    //隐藏付款
    public void dismissPwdDialog() {
        if (passwordDialog != null) {
            passwordDialog.dismiss();
        }
    }

    public interface GetDialogPassword {
        public void handleDialog(String pwd);
    }

}
