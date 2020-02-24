package android.support.v4.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;

import com.legendwd.hyperpay.aelf.R;

public class PDialogFragment extends Fragment implements DialogInterface.OnCancelListener, DialogInterface.OnDismissListener {
    public static final int STYLE_NORMAL = 0;
    public static final int STYLE_NO_TITLE = 1;
    public static final int STYLE_NO_FRAME = 2;
    public static final int STYLE_NO_INPUT = 3;
    private static final String SAVED_DIALOG_STATE_TAG = "android:savedDialogState";
    private static final String SAVED_STYLE = "android:style";
    private static final String SAVED_THEME = "android:theme";
    private static final String SAVED_CANCELABLE = "android:cancelable";
    private static final String SAVED_SHOWS_DIALOG = "android:showsDialog";
    private static final String SAVED_BACK_STACK_ID = "android:backStackId";
    int mStyle = 0;
    int mTheme = 0;
    boolean mCancelable = true;
    boolean mShowsDialog = true;
    int mBackStackId = -1;
    Dialog mDialog;
    boolean mViewDestroyed;
    boolean mDismissed;
    boolean mShownByMe;

    public PDialogFragment() {
    }

    public void setStyle(int style, @StyleRes int theme) {
        this.mStyle = style;
        if (this.mStyle == 2 || this.mStyle == 3) {
            this.mTheme = 16973913;
        }

        if (theme != 0) {
            this.mTheme = theme;
        }

    }

    public void show(FragmentManager manager, String tag) {
        this.mDismissed = false;
        this.mShownByMe = true;
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    public int show(FragmentTransaction transaction, String tag) {
        this.mDismissed = false;
        this.mShownByMe = true;
        transaction.add(this, tag);
        this.mViewDestroyed = false;
        this.mBackStackId = transaction.commit();
        return this.mBackStackId;
    }

    public void showNow(FragmentManager manager, String tag) {
        this.mDismissed = false;
        this.mShownByMe = true;
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitNow();
    }

    public void dismiss() {
        dismissAllowingStateLoss();
    }

    public void dismissAllowingStateLoss() {
        this.dismissInternal(true);
    }

    void dismissInternal(boolean allowStateLoss) {
        if (!this.mDismissed) {
            this.mDismissed = true;
            this.mShownByMe = false;
            if (this.mDialog != null) {
                this.mDialog.dismiss();
            }

            this.mViewDestroyed = true;
            if (this.mBackStackId >= 0) {
                this.getFragmentManager().popBackStack(this.mBackStackId, 1);
                this.mBackStackId = -1;
            } else {
                FragmentTransaction ft = this.getFragmentManager().beginTransaction();
                ft.remove(this);
                if (allowStateLoss) {
                    ft.commitAllowingStateLoss();
                } else {
                    ft.commit();
                }
            }

        }
    }

    public Dialog getDialog() {
        return this.mDialog;
    }

    @StyleRes
    public int getTheme() {
        return this.mTheme;
    }

    public boolean isCancelable() {
        return this.mCancelable;
    }

    public void setCancelable(boolean cancelable) {
        this.mCancelable = cancelable;
        if (this.mDialog != null) {
            this.mDialog.setCancelable(cancelable);
        }

    }

    public boolean getShowsDialog() {
        return this.mShowsDialog;
    }

    public void setShowsDialog(boolean showsDialog) {
        this.mShowsDialog = showsDialog;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (!this.mShownByMe) {
            this.mDismissed = false;
        }

    }

    public void onDetach() {
        super.onDetach();
        if (!this.mShownByMe && !this.mDismissed) {
            this.mDismissed = true;
        }

    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mShowsDialog = this.mContainerId == 0;
        if (savedInstanceState != null) {
            this.mStyle = savedInstanceState.getInt("android:style", 0);
            this.mTheme = savedInstanceState.getInt("android:theme", 0);
            this.mCancelable = savedInstanceState.getBoolean("android:cancelable", true);
            this.mShowsDialog = savedInstanceState.getBoolean("android:showsDialog", this.mShowsDialog);
            this.mBackStackId = savedInstanceState.getInt("android:backStackId", -1);
        }

    }

    @SuppressLint("WrongConstant")
    @NonNull
    public LayoutInflater onGetLayoutInflater(@Nullable Bundle savedInstanceState) {
        if (!this.mShowsDialog) {
            return super.onGetLayoutInflater(savedInstanceState);
        } else {
            this.mDialog = this.onCreateDialog(savedInstanceState);
            if (this.mDialog != null) {
                this.setupDialog(this.mDialog, this.mStyle);
                return (LayoutInflater) this.mDialog.getContext().getSystemService("layout_inflater");
            } else {
                return (LayoutInflater) this.mHost.getContext().getSystemService("layout_inflater");
            }
        }
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public void setupDialog(Dialog dialog, int style) {
        switch (style) {
            case 3:
                dialog.getWindow().addFlags(24);
            case 1:
            case 2:
                dialog.requestWindowFeature(1);
            default:
        }
    }

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new Dialog(this.getActivity(), R.style.ThemeDialog);
    }

    public void onCancel(DialogInterface dialog) {
    }

    public void onDismiss(DialogInterface dialog) {
        if (!this.mViewDestroyed) {
            this.dismissInternal(true);
        }

    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (this.mShowsDialog) {
            View view = this.getView();
            if (view != null) {
                if (view.getParent() != null) {
                    throw new IllegalStateException("DialogFragment can not be attached to a container view");
                }

                this.mDialog.setContentView(view);
            }

            Activity activity = this.getActivity();
            if (activity != null) {
                this.mDialog.setOwnerActivity(activity);
            }

            this.mDialog.setCancelable(this.mCancelable);
            this.mDialog.setOnCancelListener(this);
            this.mDialog.setOnDismissListener(this);
            if (savedInstanceState != null) {
                Bundle dialogState = savedInstanceState.getBundle("android:savedDialogState");
                if (dialogState != null) {
                    this.mDialog.onRestoreInstanceState(dialogState);
                }
            }

        }
    }

    public void onStart() {
        super.onStart();
        if (this.mDialog != null) {
            this.mViewDestroyed = false;
            this.mDialog.show();
        }

    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (this.mDialog != null) {
            Bundle dialogState = this.mDialog.onSaveInstanceState();
            if (dialogState != null) {
                outState.putBundle("android:savedDialogState", dialogState);
            }
        }

        if (this.mStyle != 0) {
            outState.putInt("android:style", this.mStyle);
        }

        if (this.mTheme != 0) {
            outState.putInt("android:theme", this.mTheme);
        }

        if (!this.mCancelable) {
            outState.putBoolean("android:cancelable", this.mCancelable);
        }

        if (!this.mShowsDialog) {
            outState.putBoolean("android:showsDialog", this.mShowsDialog);
        }

        if (this.mBackStackId != -1) {
            outState.putInt("android:backStackId", this.mBackStackId);
        }

    }

    public void onStop() {
        super.onStop();
        if (this.mDialog != null) {
            this.mDialog.hide();
        }

    }

    public void onDestroyView() {
        super.onDestroyView();
        if (this.mDialog != null) {
            this.mViewDestroyed = true;
            this.mDialog.dismiss();
            this.mDialog = null;
        }

    }
}