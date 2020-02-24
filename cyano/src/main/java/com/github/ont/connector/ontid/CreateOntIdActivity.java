package com.github.ont.connector.ontid;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.github.ont.connector.R;
import com.github.ont.connector.base.CyanoBaseActivity;
import com.github.ont.connector.utils.CommonUtil;
import com.github.ont.connector.utils.SDKCallback;
import com.github.ont.connector.utils.SDKWrapper;
import com.github.ont.connector.utils.SPWrapper;
import com.github.ont.connector.utils.ToastUtil;
import com.github.ont.connector.view.ArcTitleView;
import com.github.ont.cyano.Constant;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;


/**
 * @author zhugang
 */
public class CreateOntIdActivity extends CyanoBaseActivity implements View.OnClickListener {
    private static final String TAG = "CreateOntIdActivity";

    private EditText etPwd;
    private EditText etConfirm;
    private TextView tvService;
    private CheckBox ckService;
    private TextView btnCreate;
    private ImageView imgPwd;
    private boolean isShowPwd = false;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ont_id);
        initView();
    }

    private void initView() {
        btnCreate = (TextView) findViewById(R.id.btn_create);
        etPwd = (EditText) findViewById(R.id.et_pwd);
        etConfirm = (EditText) findViewById(R.id.et_confirm);
        ArcTitleView titleView = (ArcTitleView) findViewById(R.id.title);
        tvService = (TextView) findViewById(R.id.tv_agree_service);
        ckService = (CheckBox) findViewById(R.id.ck_agree_service);
        View layoutCheck = findViewById(R.id.layout_check);
        View tvPwdInfo = findViewById(R.id.tv_pwd_info);
        imgPwd = (ImageView) findViewById(R.id.img_pwd);

        layoutCheck.setOnClickListener(this);
        imgPwd.setOnClickListener(this);
        tvPwdInfo.setOnClickListener(this);
        titleView.setArkTitleLeftListener(new ArcTitleView.ArkTitleLeftListener() {
            @Override
            public void leftButtonClick() {
                finish();
            }
        });

        initData();
    }

    private void initData() {
        tvService.setMovementMethod(LinkMovementMethod.getInstance());
        tvService.setText(getIdentityAgreementSpan());
        ckService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnCreate.setOnClickListener(CreateOntIdActivity.this);
                    btnCreate.setBackgroundColor(ContextCompat.getColor(CreateOntIdActivity.this, R.color.colorBlack));
                } else {
                    btnCreate.setOnClickListener(null);
                    btnCreate.setBackgroundColor(ContextCompat.getColor(CreateOntIdActivity.this, R.color.grey_9B9B9B));
                }
            }
        });
    }

    private SpannableString getIdentityAgreementSpan() {
        String tip_1 = getString(R.string.create_account_policy_1);
        String tip_2 = getString(R.string.create_account_policy_2);
        String tip_3 = getString(R.string.create_account_policy_3);

        SpannableString spanableInfo = new SpannableString(tip_1);
        spanableInfo.setSpan(new Clickable(0), tip_1.indexOf(tip_2), tip_1.indexOf(tip_2) + tip_2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanableInfo.setSpan(new Clickable(1), tip_1.indexOf(tip_3), tip_1.indexOf(tip_3) + tip_3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanableInfo;
    }

    @Override
    public void onClick(View view) {
        if (!CommonUtil.isFastClick()) {
            return;
        }
        int i = view.getId();
        if (i == R.id.btn_create) {
            final String pwd = etPwd.getText().toString();
            String pwdConfirm = etConfirm.getText().toString();
            if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwdConfirm)) {
                ToastUtil.showToast(this, "Please fill in the blanks.");
            } else if (!pwd.equals(pwdConfirm)) {
                ToastUtil.showToast(this, "Passwords must be the same.");
            } else {
                createWallet(pwd);
            }
        } else if (i == R.id.layout_check) {
            if (ckService.isChecked()) {
                ckService.setChecked(false);
            } else {
                ckService.setChecked(true);
            }
        } else if (i == R.id.tv_pwd_info) {
            showInfo(view);
        } else if (i == R.id.img_pwd) {
            changePwd();
        }
    }

    private void changePwd() {
        isShowPwd = !isShowPwd;
        if (isShowPwd) {
            imgPwd.setImageResource(R.drawable.show_pwd);
            etPwd.setInputType(InputType.TYPE_CLASS_NUMBER);
            etConfirm.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else {
            imgPwd.setImageResource(R.drawable.hide_pwd);
            etPwd.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            etConfirm.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        }
    }

    private void showInfo(View v) {
        View view = View.inflate(this, R.layout.pop_info, null);

        //第一个参数为要显示的view，后边为popuwindown的宽和高，也可以是具体数值
        PopupWindow pupWindow = new PopupWindow();
        pupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        pupWindow.setWidth((int) getResources().getDimension(R.dimen.pop_info_width));
        pupWindow.setContentView(view);
        pupWindow.setFocusable(true);
        pupWindow.setOutsideTouchable(true);
        pupWindow.setBackgroundDrawable(new BitmapDrawable());
        pupWindow.showAsDropDown(v);
    }

    private void createWallet(String password) {
        showLoading();
        DisposableObserver<String> observer = SDKWrapper.createIdentity(new SDKCallback() {
            @Override
            public void onSDKSuccess(String tag, Object message) {
                dismissLoading();
                SPWrapper.setDefaultOntId((String) message);
                ToastUtil.showToast(CreateOntIdActivity.this, "Create Success");
                finish();
            }

            @Override
            public void onSDKFail(String tag, String message) {
                dismissLoading();
                ToastUtil.showToast(CreateOntIdActivity.this, message);
            }
        }, TAG, password);
        disposable.add(observer);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }

    class Clickable extends ClickableSpan {
        private int type;

        public Clickable(int type) {
            this.type = type;
        }

        /**
         * 重写父类点击事件
         */
        @Override
        public void onClick(View v) {
            if (!CommonUtil.isFastClick()) {
                return;
            }
            if (type == 0) {
                toUrl(Constant.TERMS_CONDITIONS);
            } else {
                toUrl(Constant.PRIVACY_POLICY);
            }
        }

        /**
         * 重写父类updateDrawState方法  我们可以给TextView设置字体颜色,背景颜色等等...
         */
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ContextCompat.getColor(getBaseContext(), R.color.green_33A4BE));
            ds.setFakeBoldText(true);
        }

        private void toUrl(String data) {
            Intent i = new Intent(CreateOntIdActivity.this, OntIdWebActivity.class);
            i.putExtra(Constant.KEY, data);
            startActivity(i);
        }
    }
}
