package com.github.ont.connector.ontid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.github.ont.connector.R;
import com.github.ont.connector.base.CyanoBaseActivity;
import com.github.ont.connector.utils.ToastUtil;
import com.github.ont.connector.view.ArcTitleView;
import com.github.ont.cyano.Constant;

import java.util.Locale;

/**
 * @author zhugang
 */
public class ExportOntIdActivity extends CyanoBaseActivity implements View.OnClickListener {
    private String wif;
    private TextView tvWif;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_ont_id);
        initView();
        initData();
    }

    private void initData() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            wif = getIntent().getExtras().getString(Constant.KEY);
            tvWif.setText(wif);
        }
    }

    private void initView() {
        ArcTitleView titleView = (ArcTitleView) findViewById(R.id.title);
        titleView.setArkTitleLeftListener(new ArcTitleView.ArkTitleLeftListener() {
            @Override
            public void leftButtonClick() {
                finish();
            }
        });
        View tvWifLink = findViewById(R.id.tv_wif_link);
        View tvCopy = findViewById(R.id.tv_copy);
        tvWif = (TextView) findViewById(R.id.tv_wif);

        tvWifLink.setOnClickListener(this);
        tvCopy.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_wif_link) {
            Intent intent = new Intent(this, OntIdWebActivity.class);
            intent.putExtra(Constant.KEY, Locale.getDefault().getLanguage().contains("zh") ? Constant.WHAT_WIF_URL_CN : Constant.WHAT_WIF_URL_EN);
            startActivity(intent);
        } else if (i == R.id.tv_copy) {
            copyAddress(wif, getString(R.string.wif_copied));
            ToastUtil.showToast(this, getString(R.string.wif_copied));
        } else {
        }
    }
}
