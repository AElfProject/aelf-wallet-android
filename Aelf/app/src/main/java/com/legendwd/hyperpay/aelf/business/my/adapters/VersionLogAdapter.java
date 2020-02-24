package com.legendwd.hyperpay.aelf.business.my.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.model.bean.VersionLogBean;
import com.legendwd.hyperpay.aelf.util.ScreenUtils;
import com.legendwd.hyperpay.aelf.widget.LinearSpacingItemDecoration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author lovelyzxing
 * @date 2019/6/10
 * @Description
 */
public class VersionLogAdapter extends RecyclerView.Adapter<VersionLogAdapter.VersionLogViewHolder> {

    SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
    private List<VersionLogBean.ListBean> mDataList;

    public VersionLogAdapter(List<VersionLogBean.ListBean> list) {
        this.mDataList = list;
    }

    public void refreshView(List<VersionLogBean.ListBean> list) {
        this.mDataList = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public VersionLogViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new VersionLogViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_version_log, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VersionLogViewHolder holder, int i) {
        VersionLogBean.ListBean bean = mDataList.get(i);
        if (null != bean) {
            holder.tv_version.setText(holder.itemView.getContext().getString(R.string.version_code, bean.verNo, df.format(new Date(Long.parseLong(bean.upgrade_time) * 1000))));

            VersionLogDetailAdapter adapter = new VersionLogDetailAdapter(bean.intro);
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
            holder.recyclerView.setAdapter(adapter);
        }

    }

    @Override
    public int getItemCount() {
        return null == mDataList ? 0 : mDataList.size();
    }

    class VersionLogViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_version)
        TextView tv_version;
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;

        public VersionLogViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            recyclerView.addItemDecoration(new LinearSpacingItemDecoration(itemView.getContext(), ScreenUtils.dip2px(itemView.getContext(), 5), Color.parseColor("#00000000")));

        }
    }
}
