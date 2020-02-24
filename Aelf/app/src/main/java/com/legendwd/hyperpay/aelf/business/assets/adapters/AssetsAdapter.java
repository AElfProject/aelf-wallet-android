package com.legendwd.hyperpay.aelf.business.assets.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.common.EmptyViewHolder;
import com.legendwd.hyperpay.aelf.common.HeaderAdapter;
import com.legendwd.hyperpay.aelf.listeners.HandleCallback;
import com.legendwd.hyperpay.aelf.listeners.OnItemClickListener;
import com.legendwd.hyperpay.aelf.model.bean.ChainAddressBean;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import java.util.List;

public class AssetsAdapter extends HeaderAdapter<ChainAddressBean> {

    java.text.DecimalFormat df = new java.text.DecimalFormat("######0.00");
    private OnItemClickListener mClickListener;

    public AssetsAdapter(List datas, HandleCallback handleCallback) {
        super(datas,handleCallback);
        mDatas = datas;
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof EmptyViewHolder) {
           super.onBindViewHolder(viewHolder,i);
            return;
        }
        AssetsViewHolder assetsViewHolder = (AssetsViewHolder) viewHolder;

        ChainAddressBean bean = mDatas.get(i);
        assetsViewHolder.itemView.setOnClickListener(v -> {

            if (null != mClickListener) {
                mClickListener.onItemClick(bean);
            }
        });

        if (null != bean) {
            assetsViewHolder.mTvSymbol.setText(bean.getSymbol());
            Glide.with(assetsViewHolder.mIvCover.getContext())
                    .load(bean.getLogo())
                    .into(assetsViewHolder.mIvCover);


            double money = Double.parseDouble(bean.getBalance()) * Double.parseDouble(bean.getRate().getPrice());

            String currency = CacheUtil.getInstance().getProperty(Constant.Sp.PRICING_CURRENCY_ID_DEFAULT, "USD");

            boolean isMode = CacheUtil.getInstance().getProperty(Constant.Sp.PRIVATE_MODE, false);
            if (isMode) {
                assetsViewHolder.tv_amount.setText("****");
                assetsViewHolder.tv_currency.setText("****");
            } else {
                assetsViewHolder.tv_amount.setText(bean.getBalance());
                assetsViewHolder.tv_currency.setText(df.format(money) + " " + currency);
            }
        }

    }


    @Override
    protected RecyclerView.ViewHolder getViewHolder(View view) {
        return new AssetsViewHolder(view);
    }

    @Override
    protected int getLayoutid() {
        return R.layout.item_assets;
    }

    class AssetsViewHolder extends RecyclerView.ViewHolder {

        public ImageView mIvCover;
        public TextView mTvSymbol;
        public TextView tv_currency;
        public TextView tv_amount;

        public AssetsViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvCover = itemView.findViewById(R.id.iv_cover);
            mTvSymbol = itemView.findViewById(R.id.tv_symbol);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_currency = itemView.findViewById(R.id.tv_currency);
        }
    }


}
