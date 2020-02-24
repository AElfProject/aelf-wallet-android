package com.legendwd.hyperpay.aelf.business.wallet.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.assets.fragments.TransactionRecordFragment;
import com.legendwd.hyperpay.aelf.listeners.OnItemClickListener;
import com.legendwd.hyperpay.aelf.model.MessageEvent;
import com.legendwd.hyperpay.aelf.model.bean.ChainAddressBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.TransactionBean;
import com.legendwd.hyperpay.aelf.model.param.TransactionParam;
import com.legendwd.hyperpay.aelf.presenters.ITransactionPresenter;
import com.legendwd.hyperpay.aelf.presenters.impl.TransactionPresenter;
import com.legendwd.hyperpay.aelf.views.ITransactionView;
import com.legendwd.hyperpay.aelf.widget.ClassicsFooter;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TransferChildFragment extends BaseFragment implements ITransactionView {

    public String tag = "0"; // 0 全部 1发送 2 接收
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private ITransactionPresenter presenter;
    private TransferChildAdapter mAdapter;
    private List<TransactionBean.ListBean> mDataList = new ArrayList<>();

    private static final String TYPE_PULL_UP = "TYPE_PULL_UP";
    private static final String TYPE_PULL_DOWN = "TYPE_PULL_DOWN";
    private static int PAGE_COUNT = 10;
    private int mCurrentPage = 1;

    public static TransferChildFragment newInstance(Bundle bundle) {

        TransferChildFragment fragment = new TransferChildFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_child_transfer;
    }

    @Override
    public void process() {

        presenter = new TransactionPresenter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
//        recyclerView.addItemDecoration(new LinearSpacingItemDecoration(_mActivity, ScreenUtils.dip2px(_mActivity, 1), Color.parseColor("#E0E0E0")));
        refresh.setOnRefreshListener(refreshLayout -> {
            mCurrentPage = 1;
            refresh.setEnableLoadMore(true);
            ((TransferReceiveFragment) getParentFragment()).getTransferBalance();
            getTransaction(TYPE_PULL_DOWN);
        });

        refresh.setOnLoadMoreListener(refreshLayout -> {
            mCurrentPage++;
            ((TransferReceiveFragment) getParentFragment()).getTransferBalance();
            getTransaction(TYPE_PULL_UP);
        });

        refresh.setRefreshFooter(new ClassicsFooter(getContext()));
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        refresh.autoRefresh();
    }

    private void getTransaction(String refreshType) {

        ChainAddressBean bean = new Gson().fromJson(getArguments().getString("bean"), ChainAddressBean.class);

        TransactionParam param = new TransactionParam();
        param.symbol = bean.getSymbol();
        param.chainid = bean.getChain_id();
        param.contractAddress = bean.getContractAddress();
        param.address = CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS);
        param.p = mCurrentPage+"";
        param.type = tag;
        presenter.getTransaction(param, tag,refreshType);
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    @Override
    protected boolean enableSwipeBack() {
        return false;
    }

    @Override
    public void onSuccess(ResultBean<TransactionBean> resultBean, String page, String refreshType) {

        if (null != refresh) {
            refresh.finishRefresh();
            refresh.finishLoadMore();
        }


        if (resultBean == null || resultBean.getData() == null) {
            return;
        }




        if (200 == resultBean.getStatus()) {
            refreshView(page, resultBean.getData().list,refreshType);
        }

    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    private void refreshView(String page, List<TransactionBean.ListBean> beanList,String refreshType) {
        if (null == beanList || beanList.size() <= 0)
            beanList = new ArrayList<>();
        setDataList(page, beanList, refreshType);
        if (null == mAdapter) {
            mAdapter = new TransferChildAdapter(mDataList, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o) {

                    if (o instanceof TransactionBean.ListBean) {
                        Bundle bundle = new Bundle();
                        bundle.putString("bean", new Gson().toJson(o));
                        ((TransferReceiveFragment) getParentFragment()).start(TransactionRecordFragment.newInstance(bundle));
                    } else {
                        refresh.autoRefresh();
                    }

                }
            });
            recyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.refreshView(mDataList);
        }
    }

    private void setDataList(String tag, List<TransactionBean.ListBean> beanList, String refreshType) {
//        List<TransactionBean.ListBean> list = new ArrayList<>();
//        for (TransactionBean.ListBean bean : beanList) {
//            if ("2".equals(tag) && "receive".equals(bean.category)) {
//                list.add(bean);
//            } else if ("1".equals(tag) && "send".equals(bean.category)) {
//                list.add(bean);
//            } else if ("0".equals(tag)) {
//                list.add(bean);
//            }
//        }

        if(beanList==null){
            return;
        }

        if (TYPE_PULL_UP.equals(refreshType)) {
            if (beanList.size() < PAGE_COUNT) {
                refresh.setEnableLoadMore(false);
            }
            mDataList.addAll(beanList);
        } else {
            if (mCurrentPage == 1) {
                PAGE_COUNT = beanList.size();
            }
            mDataList.clear();
            mDataList.addAll(beanList);
        }
    }

    @Override
    public void onError(int code, String msg, String page,String refreshType) {

    }

    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        if (event.getMessage() == Constant.Event.REFRSH_TRANSATION) {
            refresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refresh.autoRefresh();
                }
            }, 1500);

        }
    }
}
