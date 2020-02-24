package com.legendwd.hyperpay.aelf.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.legendwd.hyperpay.aelf.R;

import java.util.ArrayList;
import java.util.List;

public class PasswordStrengthView extends LinearLayout {

    private List<View> mViews;
    private Strength mStrength = Strength.WEEK;
    private int mLevels = 3;
    private int mChildH;

    public PasswordStrengthView(Context context) {
        this(context, null);
    }

    public PasswordStrengthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PasswordStrengthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PasswordStrengthView);
        mChildH = (int) typedArray.getDimension(R.styleable.PasswordStrengthView_childHeight, 0);
        typedArray.recycle();

        setOrientation(VERTICAL);
        mViews = new ArrayList<>(mLevels);
        for (int i = 0; i < mLevels; i++) {
            View view = new View(getContext());
            LinearLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mChildH);
            if (i != 0) {
                layoutParams.topMargin = mChildH;
            }
            view.setLayoutParams(layoutParams);
            view.setBackgroundColor(Color.parseColor("#EBEBEB"));
            addView(view);
            mViews.add(view);
        }
    }

    public Strength getStrength() {
        return mStrength;
    }

    public void setStrength(Strength strength) {
        mStrength = strength;
        switch (strength) {
            case EMPTY:
                for (int i = 0; i < mViews.size(); i++) {
                    mViews.get(i).setBackgroundColor(Color.parseColor("#EBEBEB"));
                }
                break;
            case WEEK:
                for (int i = 0; i < mViews.size(); i++) {
                    mViews.get(i).setBackgroundColor(i == mViews.size() - 1 ? Color.parseColor("#47BF6C") : Color.parseColor("#EBEBEB"));
                }
                break;
            case NORMAL:
                for (int i = 0; i < mViews.size() / 2; i++) {
                    mViews.get(i).setBackgroundColor(Color.parseColor("#EBEBEB"));
                }
                for (int i = mViews.size() / 2; i < mViews.size(); i++) {
                    mViews.get(i).setBackgroundColor(Color.parseColor("#47BF6C"));
                }
                break;
            case STRONG:
                for (int i = 0; i < mViews.size(); i++) {
                    mViews.get(i).setBackgroundColor(Color.parseColor("#47BF6C"));
                }
                break;
        }
        requestLayout();
    }

    public enum Strength {
        EMPTY,
        WEEK,
        NORMAL,
        STRONG
    }

}
