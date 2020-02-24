package com.legendwd.hyperpay.aelf.listeners;

import com.legendwd.hyperpay.aelf.model.bean.WaitTransactionBean;

/**
 * @author lovelyzxing
 * @date 2019/6/4
 * @Description
 */
public interface OnWaitItemClickListener<T> {
    void onItemClick(T t);

    void onUrlClick(WaitTransactionBean.WaitListBean bean);

    void onConfimeClick(WaitTransactionBean.WaitListBean bean);

    void onCopyClick(String copyText);
}
