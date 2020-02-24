package com.legendwd.hyperpay.aelf.business.my.fragments;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.dialogfragments.ToastDialog;
import com.legendwd.hyperpay.aelf.listeners.HandleCallback;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.param.FeedbackParam;
import com.legendwd.hyperpay.aelf.presenters.IFeedbackPresenter;
import com.legendwd.hyperpay.aelf.presenters.impl.FeedbackPresenter;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.aelf.util.StringUtil;
import com.legendwd.hyperpay.aelf.views.IFeedbackView;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import butterknife.BindView;
import butterknife.OnClick;

public class FeedbackFragment extends BaseFragment implements IFeedbackView {

    @BindView(R.id.et_title)
    EditText et_title;
    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.tv_submit)
    TextView tv_submit;
    private IFeedbackPresenter presenter;

    public static FeedbackFragment newInstance() {
        return new FeedbackFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_feedback;
    }

    @Override
    public void process() {
        initToolbarNav(mToolbar, R.string.submit_feedback, true);

        presenter = new FeedbackPresenter(this);
    }


    @OnClick({R.id.tv_submit})
    public void clickView() {

        if (isValid()) {
            FeedbackParam param = new FeedbackParam();
            param.address = CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS);
            param.title = et_title.getText().toString();
            param.email = et_email.getText().toString();
            param.desc = et_content.getText().toString();
            presenter.feedback(param);
        }
    }

    private boolean isValid() {
        String title = et_title.getText().toString().trim();
        String desc = et_content.getText().toString().trim();
        String email = et_email.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.feedback_title_null));
            return false;
        }

        if (TextUtils.isEmpty(desc)) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.feedback_content_null));
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.feedback_email_null));
            return false;
        }

        if (!StringUtil.isEmail(email)) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.email_not_correct));
            return false;
        }

        return true;
    }


    @Override
    public void onFeedbackSuccess(ResultBean<Object> bean) {

        if (200 == bean.getStatus()) {
            pop();
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.submit_success));
        } else {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.submit_fail));
        }

    }

    @Override
    public void onFeedbackFail(int code, String msg) {
        DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.submit_fail));
    }
}
