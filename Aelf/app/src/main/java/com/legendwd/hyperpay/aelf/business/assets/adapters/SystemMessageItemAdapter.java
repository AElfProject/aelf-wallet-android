package com.legendwd.hyperpay.aelf.business.assets.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.listeners.OnItemClickListener;
import com.legendwd.hyperpay.aelf.model.bean.MessageBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by haohz
 */
public class SystemMessageItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    SimpleDateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Activity mActivity;
    private List<MessageBean.SystemMessageBean> mDatas = new ArrayList<>();
    private OnItemClickListener mClickListener;

    public SystemMessageItemAdapter(Activity context, List<MessageBean.SystemMessageBean> dataList) {
        this.mDatas = dataList;
        this.mActivity = context;
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_system_message, parent, false));
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.itemView.setOnClickListener(v -> {
            if (mClickListener != null) {
                mClickListener.onItemClick(mDatas.get(position));
            }
        });

        MessageBean.SystemMessageBean bean = mDatas.get(position);
        if (null != bean) {
            viewHolder.tv_title.setText(bean.getTitle());
            viewHolder.tv_desc.setText(bean.getDesc());
            viewHolder.tv_time.setText(mDateFormat.format(new Date(Long.parseLong(bean.getCreateTime()) * 1000)));
            viewHolder.itemView.setBackgroundColor("0".equals(bean.getIs_read()) ? Color.parseColor("#F3F5F9") : Color.TRANSPARENT);


        }

    }

    @Override
    public int getItemCount() {
        if (mDatas == null) {
            return 0;
        }
        return mDatas.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.tv_desc)
        TextView tv_desc;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
