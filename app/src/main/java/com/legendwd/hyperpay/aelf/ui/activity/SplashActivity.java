package com.legendwd.hyperpay.aelf.ui.activity;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseActivity;
import com.legendwd.hyperpay.aelf.ui.adapter.SplashAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 开屏页
 */
public class SplashActivity extends BaseActivity {

    ViewPager mVpager;
    LinearLayout mDots;
    @BindView(R.id.view_stub)
    ViewStub mViewStub;


    void showPager() {

        mViewStub.inflate();
        mDots = findViewById(R.id.dot_layout);
        mVpager = findViewById(R.id.view_pager);

        List<View> dates = new ArrayList<>(2);
        LayoutInflater.from(this).inflate(R.layout.layout_splash_1, null);
        LayoutInflater.from(this).inflate(R.layout.layout_splash_2, null);
        mVpager.setAdapter(new SplashAdapter(dates));
        mVpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        break;
                    case 1:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

}
