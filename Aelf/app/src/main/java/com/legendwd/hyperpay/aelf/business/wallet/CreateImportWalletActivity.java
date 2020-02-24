package com.legendwd.hyperpay.aelf.business.wallet;

import android.content.Intent;
import android.os.Bundle;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseActivity;
import com.legendwd.hyperpay.aelf.business.wallet.fragments.CreateImportWalletFragment;

public class CreateImportWalletActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (findFragment(CreateImportWalletFragment.class) == null) {
            loadRootFragment(R.id.fl_container, CreateImportWalletFragment.newInstance());  //load root Fragment
        }


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_createimport_wallet;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        loadRootFragment(R.id.fl_container, CreateImportWalletFragment.newInstance());
    }
}
