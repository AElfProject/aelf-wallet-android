package com.legendwd.hyperpay.aelf.business.wallet.fragments;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.common.EmptyViewHolder;
import com.legendwd.hyperpay.aelf.listeners.OnItemClickListener;
import com.legendwd.hyperpay.aelf.model.bean.TransactionBean;
import com.legendwd.hyperpay.aelf.util.StringUtil;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransferChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_EMPTY = 1;
    private static final int TYPE_DATA = 2;
    List<TransactionBean.ListBean> mDatas;
    private OnItemClickListener mClickListener;

    public TransferChildAdapter(List<TransactionBean.ListBean> datas, OnItemClickListener clickListener) {
        mDatas = datas;
        mClickListener = clickListener;
    }

    public void refreshView(List<TransactionBean.ListBean> datas) {
        mDatas = datas;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (i == TYPE_EMPTY) {
            return new EmptyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_no_data, viewGroup, false));
        }
        return new TransferViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_transaction, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {


        if (holder instanceof EmptyViewHolder) {
            ((EmptyViewHolder) holder).tv_retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (null != mClickListener) {
                        mClickListener.onItemClick("");
                    }
                }
            });
        } else if (holder instanceof TransferViewHolder) {
            TransferViewHolder viewHolder = (TransferViewHolder) holder;

            TransactionBean.ListBean bean = mDatas.get(i);

            if (null != bean) {
                if ("receive".equals(bean.category)) {
                    viewHolder.iv_transaction.setBackgroundResource(R.mipmap.receive);
                    viewHolder.tv_address.setText(StringUtil.formatAddress(bean.from, bean.from_chainid));
                } else {
                    viewHolder.iv_transaction.setBackgroundResource(R.mipmap.transfer);
                    viewHolder.tv_address.setText(StringUtil.formatAddress(bean.to, bean.to_chainid));
                }

                viewHolder.tv_from.setText(String.format(viewHolder.tv_from.getResources()
                        .getString(R.string.from_to), bean.from_chainid, bean.to_chainid));
//                viewHolder.tv_chain.setText(bean.chain);
                viewHolder.tv_chain.setVisibility(View.INVISIBLE);

                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                date.setTime(Long.valueOf(bean.time) * 1000);//java里面应该是按毫秒
                viewHolder.tv_time.setText(sdf.format(date));


                viewHolder.tv_state.setText(bean.statusText);
                if ("-1".equals(bean.status)) {
                    viewHolder.tv_state.setTextColor(viewHolder.tv_state.getResources().getColor(R.color.color_FF4946));
                } else if ("0".equals(bean.status)) {
                    viewHolder.tv_state.setTextColor(viewHolder.tv_state.getResources().getColor(R.color.color_F9B74B));
                } else if("1".equals(bean.status)){
                    if ("receive".equals(bean.category)) {
                        viewHolder.tv_state.setTextColor(viewHolder.tv_state.getResources().getColor(R.color.color_316FF6));
                    }else {
                        viewHolder.tv_state.setTextColor(viewHolder.tv_state.getResources().getColor(R.color.blue_641eb0));
                    }
                }else {
                    viewHolder.tv_state.setTextColor(viewHolder.tv_state.getResources().getColor(R.color.color_F9B74B));
                }


                boolean isMode = CacheUtil.getInstance().getProperty(Constant.Sp.PRIVATE_MODE, false);
                if (isMode) {
                    viewHolder.tv_amount.setText("****");
                    viewHolder.tv_money.setText("****");
                } else {
                    String amount = bean.getAmount();
                    viewHolder.tv_amount.setText(amount);

                    double money = Double.valueOf(amount) * Double.valueOf(bean.rate.price);
                    viewHolder.tv_money.setText("≈ " + StringUtil.formatDataNoZero(2, money)
                            + " " + CacheUtil.getInstance().getProperty(Constant.Sp.PRICING_CURRENCY_ID_DEFAULT));
                }

                viewHolder.itemView.setOnClickListener(v -> {
                    if (null != mClickListener) {
                        mClickListener.onItemClick(bean);
                    }
                });
            }

        }

    }

    @Override
    public int getItemViewType(int position) {
        if (null == mDatas || mDatas.size() <= 0) {
            return TYPE_EMPTY;
        } else {
            return TYPE_DATA;
        }
    }

    @Override
    public int getItemCount() {

        return null == mDatas || mDatas.size() <= 0 ? 1 : mDatas.size();
    }


    class TransferViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_transaction)
        ImageView iv_transaction;
        @BindView(R.id.tv_amount)
        TextView tv_amount;
        @BindView(R.id.tv_money)
        TextView tv_money;
        @BindView(R.id.tv_chain)
        TextView tv_chain;
        @BindView(R.id.tv_address)
        TextView tv_address;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.tv_state)
        TextView tv_state;
        @BindView(R.id.tv_from)
        TextView tv_from;

        public TransferViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
