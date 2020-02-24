package com.legendwd.hyperpay.aelf.util;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;


/**
 * Created by LS on 2019/1/21.
 */

public class MyToast {
    private static final int ANIMATION_DURATION = 1500;

    private int HIDE_DELAY = 5000;

    private View mContainer;

    private int type;

    private int gravity = Gravity.CENTER;

    private TextView mTextView;

    private ImageView iv_type;

    private Handler mHandler;

    private AlphaAnimation mFadeInAnimation;

    private AlphaAnimation mFadeOutAnimation;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mContainer.startAnimation(mFadeOutAnimation);
            mContainer.setVisibility(View.GONE);
        }
    };
    private Context context;

    public MyToast(Context context, int HIDE_DELAY, int gravity, int type) {
        ViewGroup container = (ViewGroup) ((Activity) context)
                .findViewById(android.R.id.content);
        View v = ((Activity) context).getLayoutInflater().inflate(
                R.layout.newmb__messagebar, container);
        this.HIDE_DELAY = HIDE_DELAY;
        this.gravity = gravity;
        this.type = type;
        this.context = context;
        init(v);
    }

    private void init(View v) {
        mContainer = v.findViewById(R.id.mbContainer);
        mContainer.setVisibility(View.GONE);
        mTextView = (TextView) v.findViewById(R.id.mbMessage);
        iv_type = v.findViewById(R.id.iv_type);
        mFadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
        mFadeOutAnimation = new AlphaAnimation(1.0f, 0.0f);
        mFadeOutAnimation.setDuration(ANIMATION_DURATION);
        mFadeOutAnimation
                .setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mContainer.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

        mHandler = new Handler();

    }

    public void show(String message) {
        mContainer.setVisibility(View.VISIBLE);

        ((LinearLayout) mContainer).setGravity(gravity
                | Gravity.CENTER_VERTICAL);

        mTextView.setText(message);

        if (type == 0) {
            iv_type.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_info));
        } else if (type == 1) {
            iv_type.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_success));
        } else if (type == 2) {
            iv_type.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_error));
        }

        mFadeInAnimation.setDuration(ANIMATION_DURATION);

        mContainer.startAnimation(mFadeInAnimation);
        mHandler.postDelayed(mHideRunnable, HIDE_DELAY);
    }
}
