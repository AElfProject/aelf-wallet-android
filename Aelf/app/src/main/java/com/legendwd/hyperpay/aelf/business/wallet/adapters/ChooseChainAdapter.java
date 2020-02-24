package com.legendwd.hyperpay.aelf.business.wallet.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseAdapterModel;
import com.legendwd.hyperpay.aelf.common.EmptyViewHolder;
import com.legendwd.hyperpay.aelf.listeners.OnItemClickListener;
import com.legendwd.hyperpay.aelf.model.bean.ChainAddressBean;
import com.legendwd.hyperpay.aelf.util.StringUtil;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import java.util.List;

public class ChooseChainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ChainAddressBean> mDatas;
    private OnItemClickListener mClickListener;
    private OnItemClickListener mClick;

    public ChooseChainAdapter(List<ChainAddressBean> datas) {
        this.mDatas = datas;
    }

    public void refreshView(List<ChainAddressBean> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    /**
     * 获取数据
     *
     * @return
     */
    public List<ChainAddressBean> getData() {
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
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_choose_chain_item, viewGroup, false);
            return new MarketViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ChainAddressBean bean = mDatas.get(position);
        if (bean == null) return;
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
                if (mClick != null) {
                    mClick.onItemClick(bean);
                }
            }
        });
        marketViewHolder.tx_chain_name.setText(bean.getChain_id() + "-" + bean.getSymbol());
        Glide.with(marketViewHolder.iv_chain.getContext())
                .load(bean.getLogo())
                .into(marketViewHolder.iv_chain);
        if (bean.getRate() == null) return;
        boolean isMode = CacheUtil.getInstance().getProperty(Constant.Sp.PRIVATE_MODE, false);
        if (isMode) {
            marketViewHolder.tx_chain_money.setText("****");
            marketViewHolder.tx_chain_company.setText("****");
        } else {
            marketViewHolder.tx_chain_money.setText(bean.getBalance());
            double money = Double.parseDouble(bean.getBalance()) * Double.parseDouble(bean.getRate().getPrice());
            String currency = CacheUtil.getInstance().getProperty(Constant.Sp.PRICING_CURRENCY_ID_DEFAULT, "CNY");
            String moneyString = StringUtil.formatDataNoZero(Constant.DEFAULT_DECIMALS, money);
            marketViewHolder.tx_chain_company.setText(moneyString + " " + currency);
        }
        int roundRadius = 10; // 10dp 圆角半径
        int strokeColor = Color.parseColor(bean.getColor());//边框颜色
        int fillColor = Color.parseColor(bean.getColor());//内部填充颜色

        GradientDrawable gd = new GradientDrawable();//创建drawable
        gd.setColor(fillColor);
        gd.setCornerRadius(roundRadius);
        gd.setStroke(0, strokeColor);
        marketViewHolder.rl_choose_chain.setBackground(gd);
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public void setOnItemClick(OnItemClickListener clickListener) {
        this.mClick = clickListener;
    }

    class MarketViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout rl_choose_chain;
        public TextView tx_chain_name, tx_chain_money, tx_chain_company;
        public ImageView iv_chain;

        public MarketViewHolder(@NonNull View itemView) {
            super(itemView);
            rl_choose_chain = itemView.findViewById(R.id.rl_choose_chain);
            iv_chain = itemView.findViewById(R.id.iv_chain);
            tx_chain_name = itemView.findViewById(R.id.tx_chain_name);
            tx_chain_money = itemView.findViewById(R.id.tx_chain_money);
            tx_chain_company = itemView.findViewById(R.id.tx_chain_company);
        }
    }
}
