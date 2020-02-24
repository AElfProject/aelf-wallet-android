package com.legendwd.hyperpay.aelf.business.my.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.my.adapters.VersionLogAdapter;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.VersionLogBean;
import com.legendwd.hyperpay.aelf.model.param.BaseParam;
import com.legendwd.hyperpay.aelf.presenters.IVersionLogPresenter;
import com.legendwd.hyperpay.aelf.presenters.impl.VersionLogPresenter;
import com.legendwd.hyperpay.aelf.views.IVersionLogView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class VersionLogFragment extends BaseFragment implements IVersionLogView {

    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private IVersionLogPresenter presenter;
    private VersionLogAdapter mAdapter;
    private List<VersionLogBean.ListBean> mDataList;

    public static VersionLogFragment newInstance() {
        return new VersionLogFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_version_log;
    }

    @Override
    public void process() {
        initToolbarNav(mToolbar, R.string.version_log, true);

        getVersionLog();

        mDataList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        mAdapter = new VersionLogAdapter(mDataList);
        recyclerView.setAdapter(mAdapter);

        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getVersionLog();
            }
        });
    }

    private void getVersionLog() {
        presenter = new VersionLogPresenter(this);
        presenter.getVersionLog(new BaseParam());
    }


    @Override
    public void onVersionLogSuccess(ResultBean<VersionLogBean> bean) {

        if (null != refresh) {
            refresh.finishRefresh();
        }
        if (200 == bean.getStatus()) {

//            List<VersionLogBean.ListBean> list = new ArrayList<>();
//            list.add(bean.getData().list.get(0));
//            list.add(bean.getData().list.get(0));
//            list.add(bean.getData().list.get(0));
//            list.add(bean.getData().list.get(0));
//            mAdapter.refreshView(list);

            mAdapter.refreshView(bean.getData().list);
        }
    }

    @Override
    public void onVersionLogError(int code, String msg) {
        if (null != refresh) {
            refresh.finishRefresh();
        }
    }
}
