/*
 * **************************************************************************************
 *  Copyright © 2014-2018 Ontology Foundation Ltd.
 *  All rights reserved.
 *
 *  This software is supplied only under the terms of a license agreement,
 *  nondisclosure agreement or other written agreement with Ontology Foundation Ltd.
 *  Use, redistribution or other disclosure of any parts of this
 *  software is prohibited except in accordance with the terms of such written
 *  agreement with Ontology Foundation Ltd. This software is confidential
 *  and proprietary information of Ontology Foundation Ltd.
 *
 * **************************************************************************************
 */

package com.github.ont.connector.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ont.connector.R;

/**
 * Created by liutao17 on 2018/2/24.
 */
public class ToastUtil {
    private static Toast toast;

    /**
     * 普通的toast提示
     */
    public static void showToast(Context mContext, String message) {
        if (mContext == null) {
            return;
        }
        ToastUtil.cancel();
        if (toast == null) {
            toast = new Toast(mContext);
            toast.setDuration(Toast.LENGTH_SHORT);
            View view = LayoutInflater.from(mContext).inflate(R.layout.toast, null);
            TextView tv = view.findViewById(R.id.tv);
            tv.setText(message);
            toast.setView(view);
            toast.setGravity(Gravity.BOTTOM, 0, CommonUtil.dp2Px(mContext, 135));
            toast.show();
        }
    }

    public static void showToast(Context mContext, int message) {
        if (mContext == null) {
            return;
        }
        ToastUtil.cancel();
        if (toast == null) {
            toast = new Toast(mContext);
            toast.setDuration(Toast.LENGTH_SHORT);
            View view = LayoutInflater.from(mContext).inflate(R.layout.toast, null);
            TextView tv = view.findViewById(R.id.tv);
            tv.setText(message);
            toast.setView(view);
            toast.setGravity(Gravity.BOTTOM, 0, CommonUtil.dp2Px(mContext, 135));
            toast.show();
        }
    }

    public static void cancel() {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
    }
}
