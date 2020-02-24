package com.legendwd.hyperpay.aelf.business.assets.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.assets.adapters.TransactionNoticeAdapter;
import com.legendwd.hyperpay.aelf.dialogfragments.ConfirmDialog;
import com.legendwd.hyperpay.aelf.listeners.HandleCallback;
import com.legendwd.hyperpay.aelf.listeners.OnItemClickListener;
import com.legendwd.hyperpay.aelf.model.bean.MessageBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.TransactionNoticeBean;
import com.legendwd.hyperpay.aelf.presenters.INotificationsPresenter;
import com.legendwd.hyperpay.aelf.presenters.impl.NotificationPresenter;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.aelf.views.INotificationsView;
import com.legendwd.hyperpay.aelf.widget.ClassicsFooter;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by YoKeyword on 16/6/30.
 */
public class TransactionNoticeFragment extends BaseFragment implements OnItemClickListener, INotificationsView {
    private static final String TYPE_PULL_UP = "TYPE_PULL_UP";
    private static final String TYPE_PULL_DOWN = "TYPE_PULL_DOWN";
    private static int PAGE_COUNT = 10;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    List<TransactionNoticeBean.NoticeBean> mBeans = new ArrayList<>();
    private INotificationsPresenter mNotificationsPresenter;
    private TransactionNoticeAdapter mAdapter;
    private int mUnreadCount = 0;
    private int mCurrentPage = 1;

    public static TransactionNoticeFragment newInstance() {

        Bundle args = new Bundle();
        TransactionNoticeFragment fragment = new TransactionNoticeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_transaction_notice;
    }

    @Override
    public void process() {

        mNotificationsPresenter = new NotificationPresenter(this);

        getTransactionMessage();
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mCurrentPage = 1;
                refresh.setEnableLoadMore(true);
                getTransactionMessage();
            }
        });

        refresh.setEnableLoadMore(true);
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                mCurrentPage++;
                getTransactionMessage(TYPE_PULL_UP);
            }
        });
        refresh.setRefreshFooter(new ClassicsFooter(getContext()));
    }

    private void getTransactionMessage() {
        getTransactionMessage(TYPE_PULL_DOWN);
    }

    private void getTransactionMessage(String refreshType) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("p", String.valueOf(mCurrentPage));
        jsonObject.addProperty("address", CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS));

        mNotificationsPresenter.getTransactionMessage(jsonObject, refreshType);
    }

    public void clearItem() {
        if (null != mAdapter && mBeans != null && mBeans.size() > 0) {

            DialogUtils.showDialog(ConfirmDialog.class, getFragmentManager())
                    .setHandleCallback(new HandleCallback() {
                        @Override
                        public void onHandle(Object o) {
                            if ((boolean) o) {
                                List<TransactionNoticeBean.NoticeBean> tempList = new ArrayList(mBeans);
                                mBeans.clear();
                                mAdapter.refreshData(mBeans);
                                int unReadCount = 0;
                                for (TransactionNoticeBean.NoticeBean bean : tempList) {
                                    if ("0".equals(bean.getIsRead())) {
                                        unReadCount++;
                                    }
                                }
                                mNotificationsPresenter.clearNotice(tempList, 0, unReadCount);
                            }
                        }
                    });
        }
    }

    @Override
    public void onItemClick(Object o) {
        if (o instanceof TransactionNoticeBean.NoticeBean) {
            TransactionNoticeBean.NoticeBean noticeBean = (TransactionNoticeBean.NoticeBean) o;
            if ("0".equals(noticeBean.getIsRead())) {
                mNotificationsPresenter.setReadNotice(noticeBean);
                noticeBean.setIsRead("1");
                mAdapter.notifyDataSetChanged();
            }
            Bundle bundle = new Bundle();
            bundle.putString("bean", new Gson().toJson(o));
            ((BaseFragment) getParentFragment()).start(TransactionRecordFragment.newInstance(bundle));
        }
    }

    @Override
    public void onMessageSuccess(ResultBean<MessageBean> resultBean, String refreshType) {

    }

    @Override
    public void onMessageError(int code, String msg, String refreshType) {

    }

    @Override
    public void onNotificationsSuccess(ResultBean<TransactionNoticeBean> resultListBean, String refreshType) {
        refresh.finishRefresh();
        refresh.finishLoadMore();
        if (resultListBean == null || resultListBean.getData().getList() == null) {
            return;
        }

        List<TransactionNoticeBean.NoticeBean> list = resultListBean.getData().getList();

        if (TYPE_PULL_UP.equals(refreshType)) {
            if (list.size() < PAGE_COUNT) {
                refresh.setEnableLoadMore(false);
            }
            mBeans.addAll(list);
        } else {
            if (mCurrentPage == 1) {
                PAGE_COUNT = list.size();
            }
            mBeans.clear();
            mBeans.addAll(list);
        }

//        mBeans = resultListBean.getData().getList();
        if (mAdapter == null) {
            mAdapter = new TransactionNoticeAdapter(_mActivity, mBeans, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
            recyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }

        mUnreadCount = resultListBean.getData().getUnreadCount();
        ((NotificationsFragment) getParentFragment()).setUnread(0, String.valueOf(resultListBean.getData().getUnreadCount()));

    }

    @Override
    public void onNotificationsError(int code, String msg, String refreshType) {
        refresh.finishRefresh();
        refresh.finishLoadMore();
    }

    @Override
    public void onMessageReadError(int code, String msg, MessageBean.SystemMessageBean systemMessageBean) {

    }

    @Override
    public void onMessageReadSuccess(ResultBean r) {

    }

    @Override
    public void onNoticeReadError(int code, String msg, TransactionNoticeBean.NoticeBean noticeBean) {
        noticeBean.setIsRead("0");
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNoticeReadSuccess(ResultBean r) {
        mUnreadCount--;
        int unRead = Integer.parseInt(CacheUtil.getInstance().getProperty(Constant.Sp.UNREAD_COUNT, "0"));
        CacheUtil.getInstance().setProperty(Constant.Sp.UNREAD_COUNT, String.valueOf(Math.max(0, unRead - 1)));
        ((NotificationsFragment) getParentFragment()).setUnread(0, String.valueOf(Math.max(0, mUnreadCount)));
    }

    @Override
    public void onClearNoticeSuccess(ResultBean resultBean, int index, int unReadCount) {
        int unRead = Integer.parseInt(CacheUtil.getInstance().getProperty(Constant.Sp.UNREAD_COUNT, "0"));
        CacheUtil.getInstance().setProperty(Constant.Sp.UNREAD_COUNT, String.valueOf(Math.max(0, unRead - unReadCount)));
        ((NotificationsFragment) getParentFragment()).setUnread(index, String.valueOf(0));
    }

    @Override
    public void onClearNoticeError(int code, String msg, List list) {
        mBeans.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClearMessageSuccess(ResultBean resultBean, int index, int unReadCount) {

    }

    @Override
    public void onClearMessageError(int code, String msg, List list) {

    }


}
