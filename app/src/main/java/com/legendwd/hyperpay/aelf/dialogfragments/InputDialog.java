package com.legendwd.hyperpay.aelf.dialogfragments;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.AelfApplication;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseDialogFragment;
import com.legendwd.hyperpay.aelf.listeners.HandleCallback;
import com.legendwd.hyperpay.aelf.listeners.SoftKeyBoardListener;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.aelf.util.ScreenUtils;
import com.legendwd.hyperpay.lib.Constant;

import butterknife.BindView;
import butterknife.OnClick;

public class InputDialog extends BaseDialogFragment implements SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
    @BindView(R.id.tv_create)
    TextView mtvCreate;
    @BindView(R.id.et_input)
    EditText mEtInput;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_forget_password)
    TextView mForgetPassword;
    ValueAnimator mValueAnimator;
    private String mTitle;
    private String mHint;
    private int mTitleId = -1;
    private int mHintId = -1;
    private int mInputType = -1;
    private int mKeybordH = 0;
    private int mMaxLength = -1;
    private boolean mCancelable = true;
    private boolean mBoolShow;

    @Override
    public int getLayoutId() {
        return R.layout.dialog_password;
    }

    public InputDialog setTitle(String title) {
        mTitle = title;
        return this;
    }

    public InputDialog setTitle(int titleId) {
        mTitleId = titleId;
        return this;
    }

    public InputDialog setHint(String hint) {
        mHint = hint;
        return this;
    }

    public InputDialog setHint(int hintId) {
        mHintId = hintId;
        return this;
    }

    public InputDialog setInputType(int inputType) {
        mInputType = inputType;
        return this;
    }

    public InputDialog setMaxLength(int maxLength) {
        mMaxLength = maxLength;
        return this;
    }

    public InputDialog showForgetPassword(boolean bshow) {
        mBoolShow = bshow;
        return this;
    }

    @OnClick(R.id.tv_forget_password)
    void onForgetPassword(){
        DialogUtils.showDialog(ConfirmDialog.class, getFragmentManager())
                .setDialogTitle(getString(R.string.forget_password_confim))
                .setConfirmText(getString(R.string.confirm))
                .setHandleCallback(new HandleCallback() {
                    @Override
                    public void onHandle(Object object) {
                        if(mHandleCallback != null) {
                            mHandleCallback.onHandle(object);
                        }
                    }
                });
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

        mForgetPassword.setVisibility(mBoolShow ? View.VISIBLE : View.GONE);

        if (mInputType != -1) {
            mEtInput.setInputType(mInputType);
        }

        if (!TextUtils.isEmpty(mTitle)) {
            mTvTitle.setText(mTitle);
        } else if (mTitleId != -1) {
            mTvTitle.setText(mTitleId);
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

    public InputDialog setInputCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
        mCancelable = cancelable;
        if (mIvClose != null && !cancelable) {
            mIvClose.setVisibility(View.VISIBLE);
            mIvClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        } else if (mIvClose != null) {
            mIvClose.setVisibility(View.GONE);
        }
        return this;
    }


    @Override
    protected boolean cancelEnable() {
        return mCancelable;
    }
}
