package com.legendwd.hyperpay.aelf.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.PDialogFragment;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.listeners.HandleCallback;
import com.legendwd.hyperpay.lib.Constant;

import butterknife.ButterKnife;

public abstract class BaseDialogFragment extends PDialogFragment {

    protected HandleCallback mHandleCallback;
    protected View mCardView;
    protected View mIvClose;
    protected boolean mCloseable = true;

    //是否允许点击屏幕外取消
    protected boolean mBoolCloseable = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white_e6f3f5f9)));
        View view = inflater.inflate(getLayoutId(), null);
        ButterKnife.bind(this, view);
        mIvClose = view.findViewById(R.id.iv_close);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            if (viewGroup.getChildCount() > 0) {
                if (viewGroup.getChildAt(0) instanceof CardView) {
                    mCardView = viewGroup.getChildAt(0);
                }
            }
        }


        if (mCardView != null) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && cancelEnable()) {
                        if (mBoolCloseable && !isTouchPointInView(mCardView, (int) event.getRawX(), (int) event.getRawY())) {
                            dismiss();
                            return true;
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN && !cancelEnable()) {
                        if (!isTouchPointInView(mCardView, (int) event.getRawX(), (int) event.getRawY())) {
                            hideSoftInput();
                            return false;
                        }
                    }
                    return false;
                }
            });
        }
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if (mIvClose != null && !cancelEnable()) {
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
        return view;
    }

    public void setBoolCloseable(boolean boolCloseable) {
        this.mBoolCloseable = boolCloseable;
        setCloseable(boolCloseable);
    }

    //(x,y)是否在view的区域内
    private boolean isTouchPointInView(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        //view.isClickable() &&
        if (y >= top && y <= bottom && x >= left
                && x <= right) {
            return true;
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    public BaseDialogFragment setHandleCallback(HandleCallback handleCallback) {
        mHandleCallback = handleCallback;
        return this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.fragment_dialog_animation;
        return dialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        super.onActivityCreated(savedInstanceState);

        int dialogHeight = Constant.sScreenH;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        setStatusBarColor();

        getDialog().setOnKeyListener((anInterface, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (cancelEnable()) {
                    dismiss();
                    return true;
                }
            }
            return false;
        });

        setCloseable(mCloseable);

    }


    public void setStatusBarColor() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getDialog().getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

            window.setStatusBarColor(getResources().getColor(R.color.white_e6f3f5f9));
            window.setNavigationBarColor(getResources().getColor(R.color.white_e6f3f5f9));
        }

    }

    public BaseDialogFragment setCloseable(boolean closeable) {
        mCloseable = closeable;
        if (mIvClose != null && closeable) {
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

    public abstract int getLayoutId();

    protected boolean cancelEnable() {
        return true;
    }

    protected void hideSoftInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
