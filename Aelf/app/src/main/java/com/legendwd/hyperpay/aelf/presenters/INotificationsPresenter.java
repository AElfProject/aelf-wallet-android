package com.legendwd.hyperpay.aelf.presenters;

import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.model.bean.MessageBean;
import com.legendwd.hyperpay.aelf.model.bean.TransactionNoticeBean;

import java.util.List;

public interface INotificationsPresenter {

    void getSystemMessage(JsonObject jsonObject, String refreshType);

    void getTransactionMessage(JsonObject jsonObject, String refreshType);

    void clearNotice(List<TransactionNoticeBean.NoticeBean> list, int index, int unReadCount);

    void clearMessage(List<MessageBean.SystemMessageBean> list, int index, int unReadCount);

    void setReadNotice(TransactionNoticeBean.NoticeBean notice);

    void setReadMessage(MessageBean.SystemMessageBean systemMessageBean);

}
