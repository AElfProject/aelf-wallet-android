package com.github.ont.connector.ontid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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


/**
 * @author zhugang
 */
public class ImportOntIdActivity extends CyanoBaseActivity implements View.OnClickListener {
    private static final String TAG = "ImportWalletActivity";

    private EditText etPwd;
    private EditText etConfirm;
    private EditText etKey;
    private View btnCreate;
    private TextView tvService;
    private CheckBox ckService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_ont_id);
        initView();
    }

    private void initView() {
        btnCreate = findViewById(R.id.btn_create);
        etPwd = (EditText) findViewById(R.id.et_pwd);
        etConfirm = (EditText) findViewById(R.id.et_confirm);
        etKey = (EditText) findViewById(R.id.et_key);
        ArcTitleView titleView = (ArcTitleView) findViewById(R.id.title);
        View layoutCheck = findViewById(R.id.layout_check);
        tvService = (TextView) findViewById(R.id.tv_agree_service);
        ckService = (CheckBox) findViewById(R.id.ck_agree_service);

        layoutCheck.setOnClickListener(this);
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
                    btnCreate.setOnClickListener(ImportOntIdActivity.this);
                    btnCreate.setBackgroundColor(ContextCompat.getColor(ImportOntIdActivity.this, R.color.colorBlack));
                } else {
                    btnCreate.setOnClickListener(null);
                    btnCreate.setBackgroundColor(ContextCompat.getColor(ImportOntIdActivity.this, R.color.grey_9B9B9B));
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (!CommonUtil.isFastClick()) {
            return;
        }
        int i = view.getId();
        if (i == R.id.btn_create) {
            String pwd = etPwd.getText().toString();
            String pwdConfirm = etConfirm.getText().toString();
            String key = etKey.getText().toString();
            if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwdConfirm) || TextUtils.isEmpty(key)) {
                ToastUtil.showToast(this, "Please fill in the blanks.");
            } else if (!pwd.equals(pwdConfirm)) {
                ToastUtil.showToast(this, "Passwords must be the same.");
            } else if (key.length() != Constant.KEY_LENGTH && key.length() != Constant.WIF_LENGTH) {
                ToastUtil.showToast(this, "The length of key should be 64 or 52.");
            } else {
                importWallet(key, pwd);
            }

        } else if (i == R.id.layout_check) {
            if (ckService.isChecked()) {
                ckService.setChecked(false);
            } else {
                ckService.setChecked(true);
            }
        }

    }

    private void importWallet(String key, String password) {
        showLoading();
        SDKWrapper.importIdentity(new SDKCallback() {
            @Override
            public void onSDKSuccess(String tag, Object message) {
                dismissLoading();
                SPWrapper.setDefaultOntId((String) message);
                ToastUtil.showToast(ImportOntIdActivity.this, "Import Success");
                finish();
            }

            @Override
            public void onSDKFail(String tag, String message) {
                dismissLoading();
                ToastUtil.showToast(ImportOntIdActivity.this, "Import Fail!" + message);
            }
        }, TAG, key, password);
    }

    private SpannableString getIdentityAgreementSpan() {
        String tip_1 = getString(R.string.create_account_policy_1);
        String tip_2 = getString(R.string.create_account_policy_2);
        String tip_3 = getString(R.string.create_account_policy_3);

        SpannableString spanableInfo = new SpannableString(tip_1);
        spanableInfo.setSpan(new ImportOntIdActivity.Clickable(0), tip_1.indexOf(tip_2), tip_1.indexOf(tip_2) + tip_2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanableInfo.setSpan(new ImportOntIdActivity.Clickable(1), tip_1.indexOf(tip_3), tip_1.indexOf(tip_3) + tip_3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanableInfo;
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
            Intent i = new Intent(ImportOntIdActivity.this, OntIdWebActivity.class);
            i.putExtra(Constant.KEY, data);
            startActivity(i);
        }
    }

}
