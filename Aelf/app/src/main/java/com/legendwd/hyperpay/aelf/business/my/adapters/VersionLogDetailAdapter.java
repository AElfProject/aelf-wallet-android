package com.legendwd.hyperpay.aelf.business.my.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author lovelyzxing
 * @date 2019/6/10
 * @Description
 */
public class VersionLogDetailAdapter extends RecyclerView.Adapter<VersionLogDetailAdapter.VersionLogViewHolder> {

    SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
    private List<String> mDataList;

    public VersionLogDetailAdapter(List<String> list) {
        this.mDataList = list;
    }

    public void refreshView(List<String> list) {
        this.mDataList = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public VersionLogViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new VersionLogViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_version_log_detail, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VersionLogViewHolder holder, int i) {
        String bean = mDataList.get(i);
        if (!TextUtils.isEmpty(bean)) {
            holder.tv_content.setText(bean);
        }

    }

    @Override
    public int getItemCount() {
        return null == mDataList ? 0 : mDataList.size();
    }

    class VersionLogViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_content)
        TextView tv_content;

        public VersionLogViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
