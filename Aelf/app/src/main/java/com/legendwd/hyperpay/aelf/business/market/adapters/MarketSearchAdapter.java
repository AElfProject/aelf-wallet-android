package com.legendwd.hyperpay.aelf.business.market.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseAdapterModel;
import com.legendwd.hyperpay.aelf.common.EmptyViewHolder;
import com.legendwd.hyperpay.aelf.listeners.OnItemClickListener;
import com.legendwd.hyperpay.aelf.listeners.OnStarClickListener;
import com.legendwd.hyperpay.aelf.model.bean.MarketListBean;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import java.util.List;

public class MarketSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<MarketListBean.ListBean> mDatas;
    private OnItemClickListener mClickListener;
    private OnStarClickListener onStarClickListener;

    public MarketSearchAdapter(List<MarketListBean.ListBean> datas) {
        this.mDatas = datas;
    }

    public void refreshView(List<MarketListBean.ListBean> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    /**
     * 获取数据
     *
     * @return
     */
    public List<MarketListBean.ListBean> getData() {
        return mDatas;
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getItemType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int itemType) {
        if (itemType == BaseAdapterModel.ItemType.EMPTY) {
            return new EmptyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_no_data, viewGroup, false));
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_favourites, viewGroup, false);
            return new MarketViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MarketListBean.ListBean bean = mDatas.get(position);
        if (bean.getItemType() == BaseAdapterModel.ItemType.EMPTY) {
            EmptyViewHolder emptyViewHolder = (EmptyViewHolder) viewHolder;
            emptyViewHolder.tv_retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (null != mClickListener) {
                        mClickListener.onItemClick(null);
                    }
                }
            });
            return;
        }
        MarketViewHolder marketViewHolder = (MarketViewHolder) viewHolder;
        marketViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onItemClick(position);
                }
            }
        });
        marketViewHolder.mTvName.setText(bean.getName());
        String symbol = CacheUtil.getInstance().getProperty(Constant.Sp.PRICING_CURRENCY_SYMBOL_DEFAULT);

        if (TextUtils.isEmpty(symbol)) {
            symbol = "$";
        }

        marketViewHolder.mTvPrice.setText(symbol + bean.getLast_price());
        float increase = Float.parseFloat(bean.getIncrease()) * 100;
        StringBuilder stringBuilder = new StringBuilder();
        if (increase >= 0) {
            stringBuilder.append("+")
                    .append(String.format("%.2f", increase))
                    .append("%");
        } else {
            stringBuilder.append(String.format("%.2f", increase))
                    .append("%");
        }
        ImageView iv_star = marketViewHolder.iv_star;
        Log.d("=========>", "isStar:  " + bean.isStar());
        iv_star.setImageResource(bean.isStar() ? R.mipmap.favor_solid : R.mipmap.favour_empty_state);
        iv_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onStarClickListener != null) {
                    onStarClickListener.onStarPosition(bean, position, v);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    /**
     * 收藏回调接口
     *
     * @param onStarClickListener
     */
    public void setOnStarClickListener(OnStarClickListener onStarClickListener) {
        this.onStarClickListener = onStarClickListener;
    }

    class MarketViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvName, mTvPrice;
        public ImageView iv_star;

        public MarketViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
            iv_star = itemView.findViewById(R.id.iv_add_star);
            mTvPrice = itemView.findViewById(R.id.tv_last_price);
        }
    }
}
