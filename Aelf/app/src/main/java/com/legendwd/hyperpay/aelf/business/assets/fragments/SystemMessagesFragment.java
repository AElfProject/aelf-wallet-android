package com.legendwd.hyperpay.aelf.business.assets.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.assets.adapters.SystemMessageAdapter;
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
import com.legendwd.hyperpay.aelf.widget.webview.WebviewFragment;
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
public class SystemMessagesFragment extends BaseFragment implements OnItemClickListener, INotificationsView {

    private static final String TYPE_PULL_UP = "TYPE_PULL_UP";
    private static final String TYPE_PULL_DOWN = "TYPE_PULL_DOWN";
    private static int PAGE_COUNT = 10;
    @BindView(R.id.refresh)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private int mUnreadCount = 0;
    private INotificationsPresenter mNotificationsPresenter;
    private int mCurrentPage = 1;
    private SystemMessageAdapter mAdapter;
    private List<MessageBean.SystemMessageBean> mDataList = new ArrayList<>();

    public static SystemMessagesFragment newInstance() {
        Bundle args = new Bundle();
        SystemMessagesFragment fragment = new SystemMessagesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_system_message;
    }

    @Override
    protected boolean enableSwipeBack() {
        return false;
    }

    @Override
    public void process() {
        mNotificationsPresenter = new NotificationPresenter(this);

        getSystemMessage();

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mCurrentPage = 1;
                refreshLayout.setEnableLoadMore(true);
                getSystemMessage();
            }
        });

        refreshLayout.setEnableLoadMore(true);

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                mCurrentPage++;
                getSystemMessage(TYPE_PULL_UP);
            }
        });
        refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
    }

    public void clearItem() {
        if (null != mAdapter && mDataList != null && mDataList.size() > 0) {


            DialogUtils.showDialog(ConfirmDialog.class, getFragmentManager())
                    .setHandleCallback(new HandleCallback() {
                        @Override
                        public void onHandle(Object o) {
                            if ((boolean) o) {
                                List<MessageBean.SystemMessageBean> tempList = new ArrayList(mDataList);
                                mDataList.clear();
                                mAdapter.refreshData(mDataList);
                                int unReadCount = 0;
                                for (MessageBean.SystemMessageBean bean : tempList) {
                                    if ("0".equals(bean.getIs_read())) {
                                        unReadCount++;
                                    }
                                }
                                mNotificationsPresenter.clearMessage(tempList, 1, unReadCount);
                            }
                        }
                    });

        }
    }

    private void getSystemMessage() {
        getSystemMessage(TYPE_PULL_DOWN);
    }

    private void getSystemMessage(String refreshType) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("p", mCurrentPage);
        jsonObject.addProperty("type", "1");
        jsonObject.addProperty("address", CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS));
        mNotificationsPresenter.getSystemMessage(jsonObject, refreshType);
    }

    @Override
    public void onItemClick(Object o) {
        if (o instanceof MessageBean.SystemMessageBean) {
            MessageBean.SystemMessageBean messageBean = (MessageBean.SystemMessageBean) o;
            if ("0".equals(messageBean.getIs_read())) {
                mNotificationsPresenter.setReadMessage(messageBean);
                messageBean.setIs_read("1");
                mAdapter.notifyDataSetChanged();
            }

            Bundle bundle = new Bundle();
            bundle.putString(Constant.BundleKey.KEY_WEBVIEW_DATA, ((MessageBean.SystemMessageBean) o).getMessage());
            bundle.putString(Constant.BundleKey.KEY_WEBVIEW_TITLE, ((MessageBean.SystemMessageBean) o).getTitle());
            ((BaseFragment) getParentFragment()).start(WebviewFragment.newInstance(bundle));
        }
    }

    @Override
    public void onMessageSuccess(ResultBean<MessageBean> resultBean, String refreshType) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        if (resultBean == null || resultBean.getData() == null) {
            return;
        }
        List<MessageBean.SystemMessageBean> list = resultBean.getData().getList();

        if (TYPE_PULL_UP.equals(refreshType)) {
            if (list.size() < PAGE_COUNT) {
                refreshLayout.setEnableLoadMore(false);
            }
            mDataList.addAll(list);
        } else {
            if (mCurrentPage == 1) {
                PAGE_COUNT = list.size();
            }
            mDataList.clear();
            mDataList.addAll(list);
        }

        if (mAdapter == null) {
            mAdapter = new SystemMessageAdapter(_mActivity, mDataList, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
            recyclerView.setAdapter(mAdapter);

        } else {
            mAdapter.notifyDataSetChanged();
        }
        mUnreadCount = Integer.parseInt(resultBean.getData().getUnreadCount());
//        int unRead = Integer.parseInt(CacheUtil.getInstance().getProperty(Constant.Sp.UNREAD_COUNT, "0"));
//        CacheUtil.getInstance().setProperty(Constant.Sp.UNREAD_COUNT, String.valueOf(Math.max(0, unRead - 1)));
        ((NotificationsFragment) getParentFragment()).setUnread(1, resultBean.getData().getUnreadCount());
    }

    @Override
    public void onMessageError(int code, String msg, String refreshType) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();

    }

    @Override
    public void onNotificationsSuccess(ResultBean<TransactionNoticeBean> resultListBean, String refreshType) {

    }

    @Override
    public void onNotificationsError(int code, String msg, String refreshType) {

    }

    @Override
    public void onMessageReadError(int code, String msg, MessageBean.SystemMessageBean systemMessageBean) {
        systemMessageBean.setIs_read("0");
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onMessageReadSuccess(ResultBean r) {
        mUnreadCount--;
        int unRead = Integer.parseInt(CacheUtil.getInstance().getProperty(Constant.Sp.UNREAD_COUNT, "0"));
        CacheUtil.getInstance().setProperty(Constant.Sp.UNREAD_COUNT, String.valueOf(Math.max(0, unRead - 1)));
        ((NotificationsFragment) getParentFragment()).setUnread(1, String.valueOf(Math.max(0, mUnreadCount)));

    }

    @Override
    public void onNoticeReadError(int code, String msg, TransactionNoticeBean.NoticeBean noticeBean) {

    }


    @Override
    public void onNoticeReadSuccess(ResultBean r) {

    }

    @Override
    public void onClearNoticeSuccess(ResultBean resultBean, int index, int unReadCount) {

    }

    @Override
    public void onClearNoticeError(int code, String msg, List list) {

    }


    @Override
    public void onClearMessageSuccess(ResultBean resultBean, int index, int unReadCount) {
        int unRead = Integer.parseInt(CacheUtil.getInstance().getProperty(Constant.Sp.UNREAD_COUNT, "0"));
        CacheUtil.getInstance().setProperty(Constant.Sp.UNREAD_COUNT, String.valueOf(Math.max(0, unRead - unReadCount)));
        ((NotificationsFragment) getParentFragment()).setUnread(index, String.valueOf(0));
    }

    @Override
    public void onClearMessageError(int code, String msg, List list) {
        mDataList.add((MessageBean.SystemMessageBean) list);
        mAdapter.notifyDataSetChanged();
    }


}
