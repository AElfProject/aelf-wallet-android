package com.legendwd.hyperpay.aelf.business.assets.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.listeners.OnItemClickListener;
import com.legendwd.hyperpay.aelf.model.bean.MessageBean;
import com.legendwd.hyperpay.aelf.widget.LinearSpacingItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SystemMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_EMPTY = 1; // 轮播图
    private static final int TYPE_RECORD = 2;
    private Activity mActivity;
    private LayoutInflater layoutInflater;
    private SystemMessageItemAdapter mSystemMessageItemAdapter;
    private OnItemClickListener mClickListener;
    private List<MessageBean.SystemMessageBean> mDataList;

    public SystemMessageAdapter(Activity mActivity, List<MessageBean.SystemMessageBean> list, OnItemClickListener clickListener) {
        this.mActivity = mActivity;
        layoutInflater = LayoutInflater.from(mActivity);
        this.mClickListener = clickListener;
        this.mDataList = list;
        mSystemMessageItemAdapter = new SystemMessageItemAdapter(mActivity, mDataList);
    }

    public void refreshData(List<MessageBean.SystemMessageBean> list) {
        if (null == list)
            mDataList.clear();
        else
            mDataList = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case TYPE_EMPTY:
                viewHolder = new EmptyHolder(layoutInflater.inflate(R.layout.item_assets_notice_empty, parent, false));
                break;
            case TYPE_RECORD:
                viewHolder = new NoticeRecordViewHolder(layoutInflater.inflate(R.layout.layout_recycler, parent, false));
                break;
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (null == mDataList || mDataList.size() <= 0) {
            return TYPE_EMPTY;
        } else {
            return TYPE_RECORD;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EmptyHolder) {


        } else {
            ((NoticeRecordViewHolder) holder).recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
            ((NoticeRecordViewHolder) holder).recyclerView.setAdapter(mSystemMessageItemAdapter);
            mSystemMessageItemAdapter.setOnItemClickListener(mClickListener);
        }

    }


    @Override
    public int getItemCount() {
        return 1;
    }

    class EmptyHolder extends RecyclerView.ViewHolder {

        public EmptyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class NoticeRecordViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;

        public NoticeRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            recyclerView.addItemDecoration(new LinearSpacingItemDecoration(mActivity, 1, Color.parseColor("#E0E0E0")));
        }
    }

}
