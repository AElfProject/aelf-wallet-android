package com.legendwd.hyperpay.aelf.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.gyf.immersionbar.ImmersionBar;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseActivity;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.TransactionBean;
import com.legendwd.hyperpay.aelf.model.param.TestParam;
import com.legendwd.hyperpay.aelf.presenters.ITestPresenter;
import com.legendwd.hyperpay.aelf.presenters.impl.TestPresenter;
import com.legendwd.hyperpay.aelf.views.ITestView;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import butterknife.BindView;


public class TestActivity extends BaseActivity implements ITestView {

    @BindView(R.id.et_title)
    EditText et_title;
    @BindView(R.id.et_content)
    EditText et_content;

    private ITestPresenter presenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).keyboardEnable(true)
                .statusBarColorInt(Color.WHITE)
                .navigationBarColorInt(Color.WHITE)
                .autoDarkModeEnable(true, 0.2f).init();


        presenter = new TestPresenter(this);
    }

    public void onPushClick(View view) {

        String title = et_title.getText().toString();
        String content = et_content.getText().toString();

        TestParam param = new TestParam();
        param.title = title;
        param.content = content;
        param.token = CacheUtil.getInstance().getProperty(Constant.Sp.DEVICE_TOKEN);

        presenter.testPush(param);

    }

    @Override
    public void onTestSuccess(ResultBean<TransactionBean.ListBean> bean) {

    }
    //
    @Override
    public void onTestError(int code, String msg) {

    }
}
