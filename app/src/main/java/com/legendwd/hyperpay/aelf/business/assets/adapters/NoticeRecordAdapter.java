package com.legendwd.hyperpay.aelf.business.assets.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.listeners.OnItemClickListener;
import com.legendwd.hyperpay.aelf.model.bean.TransactionNoticeBean;
import com.legendwd.hyperpay.aelf.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by haohz
 */
public class NoticeRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    SimpleDateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private Activity mActivity;
    private List<TransactionNoticeBean.NoticeBean> dataList = new ArrayList<>();
    private OnItemClickListener mClickListener;

    public NoticeRecordAdapter(Activity context, List<TransactionNoticeBean.NoticeBean> dataList) {
        this.dataList = dataList;
        this.mActivity = context;

    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice_record, parent, false));
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;

        TransactionNoticeBean.NoticeBean bean = dataList.get(position);

        if (null != bean) {
            if ("receive".equals(bean.getCategory())) {
                Glide.with(mActivity).load(R.mipmap.receive).into(viewHolder.iv_record_state);
                viewHolder.tvAddress.setText(StringUtil.formatAddress(bean.getFrom(), bean.getFrom_chainid()));
                viewHolder.tvTitle.setText(R.string.send_address_tv);
            } else {
                Glide.with(mActivity).load(R.mipmap.transfer).into(viewHolder.iv_record_state);
                viewHolder.tvAddress.setText(StringUtil.formatAddress(bean.getTo(), bean.getTo_chainid()));
                viewHolder.tvTitle.setText(R.string.receipent_address);
            }
            viewHolder.tvFrom.setText(bean.getFrom_chainid());
            viewHolder.tvTo.setText(bean.getTo_chainid());

            viewHolder.tvAction.setText(bean.getStatusText());

            if ("-1".equals(bean.getStatus())) {
                viewHolder.tvAction.setTextColor(viewHolder.tvAction.getResources().getColor(R.color.color_FF4946));
            } else if ("0".equals(bean.getStatus())) {
                viewHolder.tvAction.setTextColor(viewHolder.tvAction.getResources().getColor(R.color.color_F9B74B));
            } else if ("1".equals(bean.getStatus())){
                if ("receive".equals(bean.getCategory())) {
                    viewHolder.tvAction.setTextColor(viewHolder.tvAction.getResources().getColor(R.color.color_316FF6));
                }else {
                    viewHolder.tvAction.setTextColor(viewHolder.tvAction.getResources().getColor(R.color.blue_641eb0));
                }
            }else {
                viewHolder.tvAction.setTextColor(viewHolder.tvAction.getResources().getColor(R.color.color_F9B74B));
            }

            viewHolder.tvTime.setText(mDateFormat.format(new Date(Long.parseLong(bean.getTime()) * 1000)));
            viewHolder.tvAmount.setText(bean.getSymbol().toUpperCase() + ": " + String.format("%.4f", Float.parseFloat(bean.getAmount())));

            viewHolder.itemView.setBackgroundColor("0".equals(bean.getIsRead()) ? Color.parseColor("#F3F5F9") : Color.TRANSPARENT);

            viewHolder.itemView.setOnClickListener(v -> {
                if (null != mClickListener) {
                    mClickListener.onItemClick(bean);
                }
            });


        }

    }

    @Override
    public int getItemCount() {
        if (dataList == null) {
            return 0;
        }
        return dataList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_record_state)
        ImageView iv_record_state;
        @BindView(R.id.tv_to)
        TextView tvTo;
        @BindView(R.id.tv_from)
        TextView tvFrom;
        @BindView(R.id.tv_action)
        TextView tvAction;
        @BindView(R.id.tv_rp_address)
        TextView tvAddress;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_amount)
        TextView tvAmount;
        @BindView(R.id.tv_address_title)
        TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
