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
import com.legendwd.hyperpay.aelf.model.bean.NetWorkBean;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author lovelyzxing
 * @date 2019/6/10
 * @Description
 */
public class NetworkAdapter extends RecyclerView.Adapter<NetworkAdapter.AssetDisplayViewHolder> {

    private Context mContext;
    private List<NetWorkBean> mDataList;
    private OnItemClickListener mClickListener;
    private int selectIndex;

    public NetworkAdapter(Context mContext, List<NetWorkBean> list, OnItemClickListener clickListener) {
        this.mContext = mContext;
        this.mDataList = list;
        this.mClickListener = clickListener;
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    @NonNull
    @Override
    public AssetDisplayViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new AssetDisplayViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_network_config, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AssetDisplayViewHolder holder, int i) {
        NetWorkBean bean = mDataList.get(i);
        if (null != bean) {
            String language = CacheUtil.getInstance().getProperty(Constant.Sp.SET_LANGUAGE);
            if ("en".equals(language)) {
                holder.tv_title.setText(bean.getNameEn());
            } else {
                holder.tv_title.setText(bean.getName());
            }
            holder.tv_default.setSelected(i == selectIndex);
            holder.tv_title.setSelected(i == selectIndex);
            holder.itemView.setOnClickListener(v -> {
                if (selectIndex != i) {
                    selectIndex = i;
                    notifyDataSetChanged();
                    if (null != mClickListener) {
                        mClickListener.onItemClick(i);
                    }
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
