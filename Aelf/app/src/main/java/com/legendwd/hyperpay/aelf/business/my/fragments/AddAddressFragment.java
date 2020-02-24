package com.legendwd.hyperpay.aelf.business.my.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.wallet.fragments.MnemonicFragment;
import com.legendwd.hyperpay.aelf.dialogfragments.ToastDialog;
import com.legendwd.hyperpay.aelf.model.bean.AddressBookBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.WalletBean;
import com.legendwd.hyperpay.aelf.model.param.AddContactParam;
import com.legendwd.hyperpay.aelf.presenters.IAddContactPresenter;
import com.legendwd.hyperpay.aelf.presenters.impl.AddContactPresenter;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.aelf.util.StringUtil;
import com.legendwd.hyperpay.aelf.util.zxing.CaptureActivity;
import com.legendwd.hyperpay.aelf.views.IAddContactView;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;

public class AddAddressFragment extends BaseFragment implements IAddContactView {
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_remark)
    EditText et_remark;
    @BindView(R.id.et_address)
    EditText et_address;
    @BindView(R.id.iv_scan)
    ImageView scan;

    private IAddContactPresenter presenter;

    public static SupportFragment newInstance(Bundle bundle) {
        AddAddressFragment fragment = new AddAddressFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_add_address;
    }

    @Override
    public void process() {

        Bundle bundle = getArguments();
        AddressBookBean.ListBean bean = new Gson().fromJson(bundle.getString("bean"), AddressBookBean.ListBean.class);
        presenter = new AddContactPresenter(this);

        if(bean != null){
            initToolbarNav(mToolbar, R.string.edit_contact, true);
            et_name.setText(bean.name);
            et_address.setText(bean.address);
            et_remark.setText(bean.note);
        }else {
            initToolbarNav(mToolbar, R.string.new_contact, true);
        }
        TextView tvRight = mToolbar.findViewById(R.id.tv_title_right);
        tvRight.setText(R.string.save);
        tvRight.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

    }

    @OnClick(R.id.iv_scan)
    void onClickScan() {
        Intent intent = new Intent(_mActivity, CaptureActivity.class);
        intent.putExtra(Constant.IntentKey.Scan_Zxing, Constant.IntentValue.SCAN_ADD_ADDRESS);
        startActivityForResult(intent, Constant.RequestCode.CODE_SCAN_ZXING);
    }

    @OnClick(R.id.tv_title_right)
    public void clickView() {
        String name = et_name.getText().toString().trim();
        String remark = et_remark.getText().toString();
        String address = et_address.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                    .setToast(R.string.not_empty_name);
            return;
        }
        if (TextUtils.isEmpty(address)) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                    .setToast(R.string.not_empty_address);
            return;
        }

        if (!StringUtil.isAELFAddress(address)) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                    .setToast(R.string.not_elf_address);
            return;
        }

        AddContactParam param = new AddContactParam();
        param.name = name;
        param.contact_address = address;
        param.note = remark;
        param.address = CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS);
        presenter.address_book(param);
    }


    @Override
    public void onSuccess(ResultBean resultBean) {
        DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                .setToast(resultBean.getMsg());
        if (200 == resultBean.getStatus()) {

            String name = et_name.getText().toString();
            String remark = et_remark.getText().toString();
            String address = et_address.getText().toString();

            AddressBookBean.ListBean bean = new AddressBookBean.ListBean();
            bean.remark = TextUtils.isEmpty(remark) ? "" : remark;
            bean.name = name;
            bean.address = address;
            Bundle bundle = new Bundle();
            bundle.putString("bean", new Gson().toJson(bean));
            setFragmentResult(RESULT_OK, bundle);
            pop();
        }
    }

    @Override
    public void onError(int code, String msg) {
//        Toast.makeText(_mActivity, )

        if (!TextUtils.isEmpty(msg)) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                    .setToast(msg);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.RequestCode.CODE_SCAN_ZXING) {
                String scan = data.getStringExtra(Constant.IntentKey.Scan_Zxing);
                et_address.setText(scan);
            }
        }
    }

}
