package com.legendwd.hyperpay.aelf.business.my.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.listeners.OnItemClickListener;
import com.legendwd.hyperpay.aelf.model.bean.CurrenciesBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PricingCurrencyAdapter extends RecyclerView.Adapter<PricingCurrencyAdapter.PricingCurrencyViewHolder> {
    private List<CurrenciesBean.ListBean> mDatas;
    private OnItemClickListener mClickListener;

    public PricingCurrencyAdapter(List<CurrenciesBean.ListBean> datas, OnItemClickListener clickListener) {
        mDatas = datas;
        this.mClickListener = clickListener;
    }

    public void refreshView(List<CurrenciesBean.ListBean> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PricingCurrencyAdapter.PricingCurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PricingCurrencyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pricing_currency, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PricingCurrencyAdapter.PricingCurrencyViewHolder viewHolder, int i) {

        CurrenciesBean.ListBean model = mDatas.get(i);
        if (null != model) {

            viewHolder.tv_title.setText(model.id);
            viewHolder.iv_default.setVisibility(model.isSelected ? View.VISIBLE : View.GONE);

            viewHolder.itemView.setOnClickListener(v -> {
                if (null != mClickListener) {
                    mClickListener.onItemClick(model);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class PricingCurrencyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_default)
        ImageView iv_default;
        @BindView(R.id.tv_title)
        TextView tv_title;

        public PricingCurrencyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
