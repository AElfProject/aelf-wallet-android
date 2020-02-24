package com.legendwd.hyperpay.aelf.listeners;

import android.view.View;

import com.legendwd.hyperpay.aelf.model.bean.MarketListBean;

/**
 * @author lovelyzxing
 * @date 2019/6/4
 * @Description
 */
public interface OnStarClickListener {
    void onStarPosition(MarketListBean.ListBean listBean, int position, View view);
}
