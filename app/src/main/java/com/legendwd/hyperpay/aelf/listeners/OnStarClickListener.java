package com.legendwd.hyperpay.aelf.listeners;

import android.view.View;

import com.legendwd.hyperpay.aelf.model.bean.MarketDataBean;

/**
 * @author lovelyzxing
 * @date 2019/6/4
 * @Description
 */
public interface OnStarClickListener {
    void onStarPosition(MarketDataBean listBean, int position, View view);
}
