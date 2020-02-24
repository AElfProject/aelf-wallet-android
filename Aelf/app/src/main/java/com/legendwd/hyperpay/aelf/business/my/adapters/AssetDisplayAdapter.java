package com.legendwd.hyperpay.aelf.business.my.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.listeners.OnItemClickListener;
import com.legendwd.hyperpay.aelf.model.AssetDisplayBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author lovelyzxing
 * @date 2019/6/10
 * @Description
 */
public class AssetDisplayAdapter extends RecyclerView.Adapter<AssetDisplayAdapter.AssetDisplayViewHolder> {

    private Context mContext;
    private List<AssetDisplayBean> mDataList;
    private OnItemClickListener mClickListener;

    public AssetDisplayAdapter(Context mContext, List<AssetDisplayBean> list, OnItemClickListener clickListener) {
        this.mContext = mContext;
        this.mDataList = list;
        this.mClickListener = clickListener;
    }

    @NonNull
    @Override
    public AssetDisplayViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new AssetDisplayViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_asset_display, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AssetDisplayViewHolder holder, int i) {
        AssetDisplayBean bean = mDataList.get(i);
        if (null != bean) {
            holder.tv_title.setText(bean.title);
            holder.tv_default.setVisibility(bean.isSelected ? View.VISIBLE : View.GONE);

            holder.itemView.setOnClickListener(v -> {
                if (null != mClickListener) {
                    mClickListener.onItemClick(bean);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return null == mDataList ? 0 : mDataList.size();
    }

    class AssetDisplayViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_default)
        TextView tv_default;

        public AssetDisplayViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
