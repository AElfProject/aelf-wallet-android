package com.legendwd.hyperpay.aelf.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.PDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.dialogfragments.BackupDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.BottomAssetsWaringDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.BottomSortDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.ChoosePhotoDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.ConfirmDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.CrossChainDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.DappInputDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.DontScreenShotDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.DoubleButtonDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.FingerDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.FingerPwdDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.IncorrectDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.InputDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.OpenCompInfoDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.SingleButtonDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.ToastDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.UpdateDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.WalletTutorialDialog;
import com.legendwd.hyperpay.aelf.listeners.HandleCallback;

public class DialogUtils {

    //    public void showDontScreenShot(Context context){
//       View view = initDialog(context,R.layout.dialog_waring);
//    }
    public static <T extends DialogFragment> T showDialog(Class c, FragmentManager fragmentManager, HandleCallback handleCallback) {
        DialogFragment dialogFragment = null;
        String tag;
        if (c == BottomSortDialog.class) {
            tag = "BottomSortDialog";
            new BottomSortDialog().setHandleCallback(handleCallback).show(fragmentManager, tag);
        }
        return (T) dialogFragment;
    }

    public static <T extends PDialogFragment> T showDialog(Class<T> c, FragmentManager fragmentManager) {

        if (null == fragmentManager) {
            throw new RuntimeException("fragmentManager is null");
        }

        PDialogFragment dialogFragment = null;
        String tag = null;
        if (c == DontScreenShotDialog.class) {
            dialogFragment = new DontScreenShotDialog();
            tag = "DontScreenShotDialog";
        } else if (c == FingerDialog.class) {
            dialogFragment = new FingerDialog();
            tag = "FingerDialog";
        } else if (c == ConfirmDialog.class) {
            dialogFragment = new ConfirmDialog();
            tag = "ConfirmDialog";
        } else if (c == FingerPwdDialog.class) {
            dialogFragment = new FingerPwdDialog();
            tag = "FingerPwdDialog";
        } else if (c == BottomAssetsWaringDialog.class) {
            dialogFragment = new BottomAssetsWaringDialog();
            tag = "BottomAssetsWaringDialog";
        } else if (c == IncorrectDialog.class) {
            dialogFragment = new IncorrectDialog();
            tag = "IncorrectDialog";
        } else if (c == WalletTutorialDialog.class) {
            dialogFragment = new WalletTutorialDialog();
            tag = "WalletTutorialDialog";
        } else if (c == InputDialog.class) {
            dialogFragment = new InputDialog();
            tag = "InputDialog";
        } else if (c == CrossChainDialog.class) {
            dialogFragment = new CrossChainDialog();
            tag = "CrossChainDialog";
        } else if (c == BottomSortDialog.class) {
            dialogFragment = new BottomSortDialog();
            tag = "BottomSortDialog";
        } else if (c == ToastDialog.class) {
            dialogFragment = new ToastDialog();
            tag = "ToastDialog";
        } else if (c == BackupDialog.class) {
            dialogFragment = new BackupDialog();
            tag = "BackupDialog";
        } else if (c == ChoosePhotoDialog.class) {
            dialogFragment = new ChoosePhotoDialog();
            tag = "ChoosePhotoDialog";
        } else if (c == DoubleButtonDialog.class) {
            dialogFragment = new DoubleButtonDialog();
            tag = "BackupDialog";
        } else if (c == UpdateDialog.class) {
            dialogFragment = new UpdateDialog();
            tag = "UpdateDialog";
        } else if (c == OpenCompInfoDialog.class) {
            dialogFragment = new OpenCompInfoDialog();
            tag = "OpenCompInfoDialog";
        } else if (c == DappInputDialog.class) {
            dialogFragment = new DappInputDialog();
            tag = "DappInputDialog";
        } else if (c == SingleButtonDialog.class) {
            dialogFragment = new SingleButtonDialog();
            tag = "SingleButtonDialog";
        }
        dialogFragment.show(fragmentManager, tag);
        return (T) dialogFragment;
    }

    View initDialog(Context context, int layoutId) {
        Dialog dialog = new Dialog(context, R.style.AelfDialog);
        dialog.show();//显示弹框
        //绘制弹出的UI
        LayoutInflater inflater = LayoutInflater.from(context);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);  //全屏的flag
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.white_e6f3f5f9)));
        WindowManager.LayoutParams attr = dialog.getWindow().getAttributes();
        attr.gravity = Gravity.BOTTOM;
        dialog.getWindow().setAttributes(attr);

        View viewDialog = inflater.inflate(layoutId, null);
        //获取当前屏幕的宽高
        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;
        //设置dialog的宽高为屏幕的宽高
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
        dialog.setContentView(viewDialog, layoutParams);
        return viewDialog;
    }

//    public void showDontScreenShot(FragmentManager fragmentManager){
//        new DontScreenShotDialog().show(fragmentManager,"DontScreenShotDialog");
//    }
//    public void showAssetsWaring(FragmentManager fragmentManager){
//        new BottomAssetsWaringDialog().show(fragmentManager,"BottomAssetsWaringDialog");
//    }
//
//    public void showIncorrect(FragmentManager fragmentManager){
//        new IncorrectDialog().show(fragmentManager,"IncorrectDialog");
//    }
//
//    public void showWalletTutorial(FragmentManager fragmentManager){
//        new WalletTutorialDialog().show(fragmentManager,"WalletTutorialDialog");
//    }
//
//    public void showPassword(FragmentManager fragmentManager){
//        new InputDialog().show(fragmentManager,"InputDialog");
//    }
//
//    public void showCrossChain(FragmentManager fragmentManager){
//        new CrossChainDialog().show(fragmentManager,"CrossChainDialog");
////        initSheetDialog(context,R.layout.dialog_cross_chain);
//    }
//
//    public void showSheetSort(FragmentManager fragmentManager, HandleCallback handleCallback){
//        new BottomSortDialog().setHandleCallback(handleCallback).show(fragmentManager,"BottomSortDialog");
//    }


//    public void initSheetDialog(Context context,int layoutId){
//        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
//        WindowManager.LayoutParams attr = bottomSheetDialog.getWindow().getAttributes();
//        attr.gravity = Gravity.CENTER;
//        bottomSheetDialog.getWindow().setAttributes(attr);
//        bottomSheetDialog.setContentView(layoutId);
//        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.white_e6f3f5f9)));
//        bottomSheetDialog.getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet)
//                .setBackgroundResource(android.R.color.transparent);
//        bottomSheetDialog.show();
//
//        View root= bottomSheetDialog.getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet);
//        BottomSheetBehavior behavior=BottomSheetBehavior.from(root);
//        behavior.setHideable(false);
//
//    }

    /**
     * show input pwd
     *
     * @param context
     * @param okClickListener
     */
    public void showInputPwdDialog(Context context, OnDialogButtonClickListener okClickListener) {
        View view = View.inflate(context, R.layout.dialog_input_pwd, null);

        EditText et_password = view.findViewById(R.id.et_password);
        TextView tv_confirm = view.findViewById(R.id.tv_confirm);
        FrameLayout inputLayout = view.findViewById(R.id.inputLayout);
        ImageView iv_password = view.findViewById(R.id.iv_password);

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(view);
        final AlertDialog dialog = builder.create();

        if (null == context || ((Activity) context).isFinishing()) {
            return;
        }

        dialog.show();

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        WindowManager m = ((Activity) context).getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用

//        layoutParams.width = (int) (d.getWidth() * 0.73);
//        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(layoutParams);
//        window.setDimAmount(0.6f);
        //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
        dialog.setCanceledOnTouchOutside(true);
        //设置按钮是否可以按返回键取消,false则不可以取消
        dialog.setCancelable(false);


        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                inputLayout.setBackgroundResource(R.drawable.shape_edittext);
            }
        });

        iv_password.setTag(false);

        iv_password.setOnClickListener(v -> {

            boolean tag = (boolean) iv_password.getTag();

            if (!tag) {
                iv_password.setImageResource(R.mipmap.eye_open);
                et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                iv_password.setImageResource(R.mipmap.eye_close);
                et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }

            iv_password.setTag(!tag);

        });

        tv_confirm.setOnClickListener(v ->
        {
            if (null != okClickListener) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_password.getWindowToken(), 0);

                if (null != dialog && dialog.isShowing() && !((Activity) context).isFinishing()) {
                    dialog.dismiss();
                }
            }
        });
    }

    public interface OnDialogButtonClickListener<T> {
        void onClick(T t);
    }


}
