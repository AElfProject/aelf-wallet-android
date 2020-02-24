package com.legendwd.hyperpay.aelf.business.wallet.fragments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.common.EmptyViewHolder;
import com.legendwd.hyperpay.aelf.listeners.OnWaitItemClickListener;
import com.legendwd.hyperpay.aelf.model.bean.WaitTransactionBean;
import com.legendwd.hyperpay.aelf.util.StringUtil;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransferWaitAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_EMPTY = 1;
    private static final int TYPE_DATA = 2;
    List<WaitTransactionBean.WaitListBean> mDatas;
    private OnWaitItemClickListener mClickListener;

    public TransferWaitAdapter(List<WaitTransactionBean.WaitListBean> datas, OnWaitItemClickListener clickListener) {
        mDatas = datas;
        mClickListener = clickListener;
    }

    public void refreshView(List<WaitTransactionBean.WaitListBean> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (i == TYPE_EMPTY) {
            return new EmptyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_no_data, viewGroup, false));
        }
        return new TransferViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_transaction_wait, viewGroup, false));
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

            WaitTransactionBean.WaitListBean bean = mDatas.get(i);

            if (null != bean) {

                viewHolder.tv_from.setText(bean.symbol.toUpperCase());
                viewHolder.tv_chain.setText(String.format(viewHolder.tv_from.getResources()
                        .getString(R.string.from_to), bean.from_chain, bean.to_chain));
//                viewHolder.iv_transaction.setBackgroundResource(R.mipmap.transfer);
                viewHolder.tv_copy_from.setText(StringUtil.formatAddress(bean.from_address));
                viewHolder.tv_copy_to.setText(StringUtil.formatAddress(bean.to_address));
                if(!TextUtils.isEmpty(bean.memo)) {
                    viewHolder.tv_memo.setText(bean.memo);
                }
                viewHolder.tv_copy_txid.setText(StringUtil.formatAddress(bean.txid));

                viewHolder.tv_copy_from.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mClickListener.onCopyClick(bean.from_address);
                    }
                });
                viewHolder.tv_copy_to.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mClickListener.onCopyClick(bean.to_address);
                    }
                });
                viewHolder.tv_copy_txid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mClickListener.onCopyClick(bean.txid);
                    }
                });
                viewHolder.tv_url.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mClickListener.onUrlClick(bean);
                    }
                });
                viewHolder.tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mClickListener.onConfimeClick(bean);
                    }
                });

                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                date.setTime(Long.valueOf(bean.time) * 1000);//java里面应该是按毫秒
                viewHolder.tv_time.setText(sdf.format(date));

                boolean isMode = CacheUtil.getInstance().getProperty(Constant.Sp.PRIVATE_MODE, false);
                if (isMode) {
                    viewHolder.tv_amount.setText("****");
                } else {
                    viewHolder.tv_amount.setText(StringUtil.formatDataNoZero(Constant.DEFAULT_DECIMALS, bean.amount));
                }

                viewHolder.itemView.setOnClickListener(v -> {
                    int visi = viewHolder.more_ll.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
                    viewHolder.more_ll.setVisibility(visi);
                    viewHolder.tv_cancel.setVisibility(visi);
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
        @BindView(R.id.tv_from)
        TextView tv_from;
        @BindView(R.id.tv_chain)
        TextView tv_chain;
        @BindView(R.id.tv_amount)
        TextView tv_amount;

        @BindView(R.id.more_ll)
        LinearLayout more_ll;
        @BindView(R.id.tv_copy_from)
        TextView tv_copy_from;
        @BindView(R.id.tv_copy_to)
        TextView tv_copy_to;
        @BindView(R.id.tv_memo)
        TextView tv_memo;
        @BindView(R.id.tv_copy_txid)
        TextView tv_copy_txid;
        @BindView(R.id.tv_url)
        TextView tv_url;

        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.tv_cancel)
        TextView tv_cancel;


        public TransferViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
