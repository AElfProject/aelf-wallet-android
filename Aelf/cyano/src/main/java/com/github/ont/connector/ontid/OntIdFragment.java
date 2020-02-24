package com.github.ont.connector.ontid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.ont.connector.R;
import com.github.ont.connector.base.CyanoBaseActivity;
import com.github.ont.connector.base.CyanoBaseFragment;
import com.github.ont.connector.utils.CommonUtil;
import com.github.ont.connector.utils.SDKCallback;
import com.github.ont.connector.utils.SDKWrapper;
import com.github.ont.connector.utils.SPWrapper;
import com.github.ont.connector.utils.ToastUtil;
import com.github.ont.cyano.Constant;
import com.github.ontio.OntSdk;


/**
 * @author zhugang
 */
public class OntIdFragment extends CyanoBaseFragment implements View.OnClickListener {
    private static final String TAG = "IdentityFragment";

    private LinearLayout layoutNoIdentity;
    private LinearLayout layoutHasIdentity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ontid, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutNoIdentity = (LinearLayout) view.findViewById(R.id.layout_no_identity);
        layoutHasIdentity = (LinearLayout) view.findViewById(R.id.layout_has_identity);
        initViewNoIdentity(view);
    }

    private void initViewNoIdentity(View view) {
        View btnNew = view.findViewById(R.id.btn_new);
        View btnImport = view.findViewById(R.id.btn_import);
        btnNew.setOnClickListener(this);
        btnImport.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (CommonUtil.isFastClick()) {
            int i = view.getId();
            if (i == R.id.btn_new) {
//                baseActivity.startActivity(new Intent(baseActivity, CreateOntIdActivity.class));
                createOntID();
            } else if (i == R.id.btn_import) {
                baseActivity.startActivity(new Intent(baseActivity, ImportOntIdActivity.class));
            }
        }
    }

    private void createOntID() {
        final String defaultAccountAddress = OntSdk.getInstance().getWalletMgr().getWallet().getDefaultAccountAddress();
        if (TextUtils.isEmpty(defaultAccountAddress)) {
            ToastUtil.showToast(baseActivity, getString(R.string.no_wallet));
        }
        baseActivity.setGetDialogPwd(new CyanoBaseActivity.GetDialogPassword() {
            @Override
            public void handleDialog(String pwd) {
                baseActivity.showLoading();
                SDKWrapper.createIdentityWithAccount(new SDKCallback() {
                    @Override
                    public void onSDKSuccess(String tag, Object message) {
                        baseActivity.dismissLoading();
                        SPWrapper.setDefaultOntId((String) message);
                        initData();
                    }

                    @Override
                    public void onSDKFail(String tag, String message) {
                        baseActivity.dismissLoading();
                    }
                }, TAG, defaultAccountAddress, pwd);
            }
        });
        baseActivity.showPasswordDialog(getString(R.string.enter_your_wallet_password));

    }


    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    private void initData() {
        if (TextUtils.isEmpty(SPWrapper.getDefaultOntId())) {
            layoutNoIdentity.setVisibility(View.VISIBLE);
            layoutHasIdentity.setVisibility(View.GONE);
        } else {
            layoutNoIdentity.setVisibility(View.GONE);
            layoutHasIdentity.setVisibility(View.VISIBLE);
            Intent intent = new Intent(baseActivity, OntIdWebActivity.class);
            intent.putExtra(Constant.KEY, Constant.CYANO_MANAGER_URL + SPWrapper.getDefaultOntId());
            startActivity(intent);
            baseActivity.finish();
        }
    }


}
