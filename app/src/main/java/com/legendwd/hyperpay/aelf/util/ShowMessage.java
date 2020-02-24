package com.legendwd.hyperpay.aelf.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.legendwd.hyperpay.aelf.R;


/**
 * 提示工具类
 */
public class ShowMessage {

    private static final int DEFAULT_DURATION = Toast.LENGTH_SHORT;

    public static Toast toast;
    //	public static ToastCompat toast;
    public static MyToast m;

    public static boolean isFront = true;

    public static void toastMsg(Context context, int msgId) {
        if (context != null)
            toastMsg(context, context.getString(msgId), DEFAULT_DURATION);
    }

    public static void toastMsg(Context context, String msg) {
        if (context != null)
            toastMsg(context, msg, DEFAULT_DURATION);
    }

    public static void toastInfoMsg(Context context, String msg) {
        if (context != null)
            toastMsg(context, msg, DEFAULT_DURATION, 0);
    }

    public static void toastSuccessMsg(Context context, String msg) {
        if (context != null)
            toastMsg(context, msg, DEFAULT_DURATION, 1);
    }

    public static void toastErrorMsg(Context context, String msg) {
        if (context != null)
            toastMsg(context, msg, DEFAULT_DURATION, 2);
    }


    //onchain eos复制成功提示框
    public static void toastOnchain(Context context, String msg) {
        if (context != null)
            toastMsg(context, msg, DEFAULT_DURATION, 3);
    }

    public static void toastMsg(Context context, String msg, int duration) {
        if (context != null) {
//			Toast toast = Toast.makeText(context, msg, duration);
//			toast.setGravity(Gravity.CENTER, 0, 0);
//
//			View layout  = LayoutInflater.from(context).inflate(R.layout.layout_toast, null);
//			TextView text = (TextView) layout.findViewById(R.id.text);
//			ImageView img = (ImageView) layout.findViewById(R.id.iv_type);
//			text.setText(msg);
//
//			toast.setView(layout);
//			toast.show();

            toastMsg(context, msg, DEFAULT_DURATION, 0);
        }
    }

    // type = 0 info ， type = 1 success ，type = 2 error
    public static void toastMsg(Context context, String msg, int duration, int type) {
        if (context != null) {

            toast = Toast.makeText(context, msg, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);

            // 使用自定义界面
            View layout = LayoutInflater.from(context).inflate(R.layout.layout_toast, null);
            TextView text = (TextView) layout.findViewById(R.id.text);
            ImageView img = (ImageView) layout.findViewById(R.id.iv_type);
            LinearLayout mbContainer = layout.findViewById(R.id.mbContainer);
            mbContainer.setVisibility(View.VISIBLE);

            LinearLayout ll_copy = layout.findViewById(R.id.ll_copy);
            TextView tv_copy = (TextView) layout.findViewById(R.id.tv_copy);
            ImageView iv_copy = (ImageView) layout.findViewById(R.id.iv_copy);
            ll_copy.setVisibility(View.GONE);

            if (type == 0) {
                img.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_info));
            } else if (type == 1) {
                img.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_success));
            } else if (type == 2) {
                img.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_error));
            } else if (type == 3) {
                mbContainer.setVisibility(View.GONE);
                ll_copy.setVisibility(View.VISIBLE);
                tv_copy.setText(msg);
            }

            text.setText(msg);
            if (toast != null && isFront) {
                toast.setView(layout);

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        Looper.prepare();
                        toast.show();
                        Looper.loop();
                    }
                }.start();
            }
        }
    }

    /**
     * 显示Dialog
     */
    public static AlertDialog showDialog(Context context, String title,
                                         String msg, OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(msg))
            builder.setMessage(msg);
        if (listener != null) {
            builder.setNegativeButton("cancel", listener);
            builder.setPositiveButton("OK", listener);
        }
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }


    /**
     * 显示列表
     */
    public static void showList(Context context, String[] items,
                                boolean hasConfirm, OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setAdapter(new ArrayAdapter<CharSequence>(context,
                android.R.layout.simple_list_item_1, items), listener);
        if (hasConfirm)
            builder.setPositiveButton(android.R.string.ok, listener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
