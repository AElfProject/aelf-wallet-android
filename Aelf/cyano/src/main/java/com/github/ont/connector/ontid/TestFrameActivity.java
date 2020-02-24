package com.github.ont.connector.ontid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.github.ont.connector.R;
import com.github.ont.connector.base.CyanoBaseActivity;
import com.github.ont.connector.utils.SPWrapper;
import com.github.ont.connector.view.ArcTitleView;
import com.github.ontio.OntSdk;

import java.io.IOException;


/**
 * @author zhugang
 */
public class TestFrameActivity extends CyanoBaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_frame);
        initData();
        initView();
    }

    private void initData() {
        try {
            OntSdk.getInstance().openWalletFile(SPWrapper.getSharedPreferences());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        ArcTitleView title = (ArcTitleView) findViewById(R.id.title);
        title.setArkTitleLeftListener(new ArcTitleView.ArkTitleLeftListener() {
            @Override
            public void leftButtonClick() {
                finish();
            }
        });
        OntIdFragment mTab2 = new OntIdFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame, mTab2);
        transaction.commit();
    }
}
