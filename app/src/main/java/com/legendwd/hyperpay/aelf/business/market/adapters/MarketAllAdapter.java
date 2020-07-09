package com.legendwd.hyperpay.aelf.business.market.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseAdapterModel;
import com.legendwd.hyperpay.aelf.common.EmptyViewHolder;
import com.legendwd.hyperpay.aelf.listeners.OnItemClickListener;
import com.legendwd.hyperpay.aelf.model.bean.MarketDataBean;
import com.legendwd.hyperpay.aelf.util.StringUtil;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import java.util.List;

public class MarketAllAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<MarketDataBean> mDatas;
    private OnItemClickListener mClickListener;

    public MarketAllAdapter(List<MarketDataBean> datas) {
        this.mDatas = datas;
    }

    public void refreshView(List<MarketDataBean> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
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
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_market, viewGroup, false);
            return new MarketViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        MarketDataBean bean = mDatas.get(i);
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
                    mClickListener.onItemClick(i);
                }
            }
        });
        marketViewHolder.mTvName.setText(bean.getName());

        String currency = CacheUtil.getInstance().getProperty(Constant.Sp.PRICING_CURRENCY_ID_DEFAULT, Constant.DEFAULT_CURRENCY);
        String symbol = Constant.DEFAULT_CURRENCY.equals(currency) ? "$" : "¥";

        marketViewHolder.mTvPrice.setText(symbol + bean.getCurrentPrice());
        double increase = StringUtil.parseDouble(bean.getPriceChangePercentage24h());
        StringBuilder stringBuilder = new StringBuilder();
        if (increase >= 0) {
            stringBuilder.append("+")
                    .append(String.format("%.2f", increase))
                    .append("%");
        } else {
            stringBuilder.append(String.format("%.2f", increase))
                    .append("%");
        }
        marketViewHolder.mTvIncrease.setText(stringBuilder);
        marketViewHolder.mTvIncrease.setBackgroundResource(increase >= 0 ? R.drawable.shape_r3_c1abb97 : R.drawable.shape_r3_ce83323);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    class MarketViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvName, mTvIncrease, mTvPrice;

        public MarketViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvIncrease = itemView.findViewById(R.id.tv_increase);
            mTvPrice = itemView.findViewById(R.id.tv_last_price);
        }
    }
}
