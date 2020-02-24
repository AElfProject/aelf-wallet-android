package com.legendwd.hyperpay.aelf.base;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.BottomSheetDialogFragment;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.listeners.HandleCallback;

import butterknife.ButterKnife;

public abstract class BaseBottomSheetDialogFragment extends BottomSheetDialogFragment {

    protected HandleCallback mHandleCallback;
    private View mCardView, mCancelView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getContext() == null) {
            return super.onCreateDialog(savedInstanceState);
        }
        return new BottomSheetDialog(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white_e6f3f5f9)));
        View view = inflater.inflate(getLayoutId(), null);
        ButterKnife.bind(this, view);
        if (cancelEnable()) {
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                if (viewGroup.getChildCount() > 0) {
                    mCardView = ((ViewGroup) view).getChildAt(0);
                    if (viewGroup.getChildCount() > 1) {
                        mCancelView = viewGroup.getChildAt(1);
                    }
                }
            }
        }

        if (cancelEnable() && mCardView != null) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (isTouchPointInView(mCardView, (int) event.getRawX(), (int) event.getRawY())) {
                            return true;
                        }

                        if (mCancelView != null) {
                            if (isTouchPointInView(mCancelView, (int) event.getRawX(), (int) event.getRawY())) {
                                return true;
                            }
                        }
                        dismiss();
                    }
                    return true;
                }
            });
        }

        return view;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        super.onActivityCreated(savedInstanceState);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dm.heightPixels);

        getDialog().setOnKeyListener((anInterface, keyCode, event) -> {
            if (cancelEnable() && keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                dismiss();
                return true;
            }
            return false;
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            View bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            bottomSheet.getLayoutParams().height = getSheetHeight(); //可以写入自己想要的高度
        }
        dialog.setCanceledOnTouchOutside(cancelEnable());
        final View view = getView();
        view.post(new Runnable() {
            @Override
            public void run() {
                if (getContext() == null) {
                    return;
                }
                View parent = (View) view.getParent();
                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) (parent).getLayoutParams();
                CoordinatorLayout.Behavior behavior = params.getBehavior();
                BottomSheetBehavior bottomSheetBehavior = (BottomSheetBehavior) behavior;
                bottomSheetBehavior.setPeekHeight(view.getMeasuredHeight());
                parent.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            }
        });

        if (!hideEnable()) {
            View root = getDialog().findViewById(android.support.design.R.id.design_bottom_sheet);
            BottomSheetBehavior behavior = BottomSheetBehavior.from(root);
            behavior.setHideable(false);
        }
    }

    public BaseBottomSheetDialogFragment setHandleCallback(HandleCallback handleCallback) {
        mHandleCallback = handleCallback;
        return this;
    }

    protected int getSheetHeight() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    protected boolean hideEnable() {
        return true;
    }

    public abstract int getLayoutId();

    protected boolean cancelEnable() {
        return true;
    }

}
