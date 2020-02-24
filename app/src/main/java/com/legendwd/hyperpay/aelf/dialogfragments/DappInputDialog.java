package com.legendwd.hyperpay.aelf.dialogfragments;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.AelfApplication;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseDialogFragment;
import com.legendwd.hyperpay.aelf.listeners.SoftKeyBoardListener;
import com.legendwd.hyperpay.aelf.util.ScreenUtils;
import com.legendwd.hyperpay.lib.Constant;

import butterknife.BindView;

public class DappInputDialog extends BaseDialogFragment implements SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
    @BindView(R.id.tv_create)
    TextView mtvCreate;

    @BindView(R.id.tv_01)
    TextView tv01;
    @BindView(R.id.tv_02)
    TextView tv02;


    @BindView(R.id.chb_white_name)
    CheckBox chbWhiteName;



    @BindView(R.id.et_input)
    EditText mEtInput;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    ValueAnimator mValueAnimator;
    private String mTitle;
    private String mHint;
    private String mTv01str;
    private String mTv02str;
    private int mTitleId = -1;
    private int mHintId = -1;
    private int mInputType = -1;
    private int mKeybordH = 0;
    private int mMaxLength = -1;
    private boolean mCancelable = true;




    @Override
    public int getLayoutId() {
        return R.layout.dialog_password_dapp;
    }

    public DappInputDialog setTitle(String title) {
        mTitle = title;
        return this;
    }

    public DappInputDialog setTitle(int titleId) {
        mTitleId = titleId;
        return this;
    }

    public DappInputDialog setHint(String hint) {
        mHint = hint;
        return this;
    }

    public DappInputDialog setHint(int hintId) {
        mHintId = hintId;
        return this;
    }

    public DappInputDialog setTv02Str(String tv02str) {
        mTv02str = tv02str;
        return this;
    }
    public DappInputDialog setTv01Str(String tv01str) {
        mTv01str = tv01str;
        return this;
    }

    public DappInputDialog setInputType(int inputType) {
        mInputType = inputType;
        return this;
    }

    public DappInputDialog setMaxLength(int maxLength) {
        mMaxLength = maxLength;
        return this;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SoftKeyBoardListener.setOnSoftKeyBoardChangeListener(getActivity(), this);
        mtvCreate.setOnClickListener(v -> {

            String pwd = mEtInput.getText().toString();
            if (TextUtils.isEmpty(pwd)) {
                return;
            }

            if (null != mHandleCallback) {
                mHandleCallback.onHandle(pwd);
            }
        });

        if (mInputType != -1) {
            mEtInput.setInputType(mInputType);
        }

        if (!TextUtils.isEmpty(mTitle)) {
            mTvTitle.setText(mTitle);
        } else if (mTitleId != -1) {
            mTvTitle.setText(mTitleId);
        }

        if(!TextUtils.isEmpty(mTv01str)){
            tv01.setText(mTv01str);
        }else{
            tv01.setText("签名地址：");
        }

        if(!TextUtils.isEmpty(mTv02str)){
            tv02.setText(mTv02str);
        }else{
            tv02.setText("签名内容：");
        }

        if (mMaxLength != -1) {
            mEtInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMaxLength)});
        }

        if (!TextUtils.isEmpty(mHint)) {
            mEtInput.setHint(mHint);
        } else if (mHintId != -1) {
            mEtInput.setHint(mHintId);
        }
        mCardView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                mCardView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (mKeybordH == 0) {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mCardView.getLayoutParams();
                    layoutParams.topMargin = (getActivity().getWindow().getDecorView().getHeight() - mCardView.getHeight()) / 2;
                    mCardView.requestLayout();
                    getActivity().getWindow().getDecorView().requestLayout();
                } else {
                    reLayoutCardView();
                }
            }
        });


        if (mIvClose != null && !mCancelable) {
            mIvClose.setVisibility(View.VISIBLE);
            mIvClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if(mHandleCallback!=null){
                        mHandleCallback.onHandle("close");
                    }
                }
            });
        } else if (mIvClose != null) {
            mIvClose.setVisibility(View.GONE);
        }


    }

    public boolean isAddWhite(){
        return chbWhiteName.isChecked();
    }

    void reLayoutCardView() {
        int screenHeright = Constant.sScreenH - ScreenUtils.getNavigationBarHeight(AelfApplication.getSContext());
        int[] location = new int[2];
        mEtInput.getLocationOnScreen(location);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mCardView.getLayoutParams();
        if (layoutParams.topMargin == 0) {
            layoutParams.topMargin = (screenHeright - mCardView.getHeight()) / 2;
        }
        if (location[1] + mEtInput.getHeight() > screenHeright - mKeybordH) {
            int start = layoutParams.topMargin;
            int end = layoutParams.topMargin - Math.abs((screenHeright - mKeybordH)
                    - (location[1] + mEtInput.getHeight()));
            mValueAnimator = ValueAnimator.ofInt(start, end);
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    layoutParams.topMargin = value;
                    mCardView.requestLayout();
                }
            });
            mValueAnimator.setDuration(300);
            mValueAnimator.setInterpolator(new DecelerateInterpolator());
            mValueAnimator.start();
        }
    }

    @Override
    public void keyBoardShow(int height) {
        mKeybordH = height;
        reLayoutCardView();

    }

    @Override
    public void keyBoardHide(int height) {
        if (getActivity() == null) {
            return;
        }
        mKeybordH = height;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mCardView.getLayoutParams();
        int screenHeright = getActivity().getWindow().getDecorView().getHeight();

        int start = layoutParams.topMargin;
        int end = (screenHeright - mCardView.getHeight()) / 2;
        mValueAnimator = ValueAnimator.ofInt(start, end);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                layoutParams.topMargin = value;
                mCardView.requestLayout();
            }
        });
        mValueAnimator.setDuration(300);
        mValueAnimator.setInterpolator(new DecelerateInterpolator());
        mValueAnimator.start();

    }

    @Override
    public void dismiss() {
        if (mValueAnimator != null) {
            mValueAnimator.removeAllUpdateListeners();
            mValueAnimator.cancel();
            mValueAnimator = null;
        }
        hideSoftInput();
        super.dismiss();

    }

    public DappInputDialog setInputCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
        mCancelable = cancelable;
        return this;
    }


    @Override
    protected boolean cancelEnable() {
        return mCancelable;
    }
}
