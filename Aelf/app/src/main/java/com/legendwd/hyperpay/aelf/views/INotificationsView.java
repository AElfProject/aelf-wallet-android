package com.legendwd.hyperpay.aelf.views;

import com.legendwd.hyperpay.aelf.model.bean.MessageBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.TransactionNoticeBean;

import java.util.List;

public interface INotificationsView {
    void onMessageSuccess(ResultBean<MessageBean> resultBean, String refreshType);

    void onMessageError(int code, String msg, String refreshType);

    void onNotificationsSuccess(ResultBean<TransactionNoticeBean> resultListBean, String refreshType);

    void onNotificationsError(int code, String msg, String refreshType);

    void onMessageReadError(int code, String msg, MessageBean.SystemMessageBean systemMessageBean);

    void onMessageReadSuccess(ResultBean r);

    void onNoticeReadError(int code, String msg, TransactionNoticeBean.NoticeBean noticeBean);

    void onNoticeReadSuccess(ResultBean r);

    void onClearNoticeSuccess(ResultBean resultBean, int index, int unReadCount);

    void onClearNoticeError(int code, String msg, List<TransactionNoticeBean.NoticeBean> list);

    void onClearMessageSuccess(ResultBean resultBean, int index, int unReadCount);

    void onClearMessageError(int code, String msg, List<MessageBean.SystemMessageBean> list);


}
