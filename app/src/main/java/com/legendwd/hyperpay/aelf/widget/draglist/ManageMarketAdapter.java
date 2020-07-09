package com.legendwd.hyperpay.aelf.widget.draglist;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.model.bean.MarketDataBean;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Xionghu on 2018/4/7.
 * Desc:
 */

public class ManageMarketAdapter extends RecyclerView.Adapter<ManageMarketAdapter.MyViewHolder> implements ItemTouchMoveListener {

    private List<MarketDataBean> mListBeans;

    public ManageMarketAdapter(List<MarketDataBean> list) {
        this.mListBeans = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourites_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        MarketDataBean listBean = mListBeans.get(position);
        holder.tx_currency.setText(listBean.getName());
        holder.iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemRemove(position);
                notifyDataSetChanged();
            }
        });
        holder.iv_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                onItemTop(adapterPosition, 0);
            }
        });

    }

    /**
     * 取回操作后的数据
     *
     * @return
     */
    public List<MarketDataBean> getListBeans() {
        return mListBeans;
    }

    @Override
    public int getItemCount() {
        return mListBeans.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        //1.数据交换 2.刷新
        Collections.swap(mListBeans, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public void onItemTop(int fromPosition, int toPosition) {
        //1.数据交换 2.刷新
        try {
            Collections.swap(mListBeans, fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);
            notifyItemRangeChanged(Math.min(fromPosition, toPosition), Math.abs(fromPosition - toPosition) + 1);
        } catch (Exception e) {
            Log.d("====", "onItemTop: ");
        }
    }

    @Override
    public boolean onItemRemove(int position) {
        mListBeans.remove(position);
        notifyItemRemoved(position);
        return true;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_remove)
        ImageView iv_remove;
        @BindView(R.id.tx_currency)
        TextView tx_currency;
        @BindView(R.id.iv_drag)
        ImageView iv_drag;
        @BindView(R.id.iv_top)
        ImageView iv_top;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
