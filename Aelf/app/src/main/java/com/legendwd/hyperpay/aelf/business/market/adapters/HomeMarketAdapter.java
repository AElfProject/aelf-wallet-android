package com.legendwd.hyperpay.aelf.business.market.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.model.bean.CoinDetailBean;
import com.legendwd.hyperpay.aelf.model.bean.MarketDetailBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by joseph at 2019/6/17
 */

public class HomeMarketAdapter extends RecyclerView.Adapter<HomeMarketAdapter.HomeMarketViewHolder> {
    List<MarketDetailBean> mDatas;

    public HomeMarketAdapter(List<MarketDetailBean> datas) {
        mDatas = datas;
    }

    @NonNull
    @Override
    public HomeMarketViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HomeMarketViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_home_market, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeMarketViewHolder homeMarketViewHolder, int i) {
        MarketDetailBean siteBean = mDatas.get(i);
        homeMarketViewHolder.mTvValue.setText(siteBean.value);
        homeMarketViewHolder.mTvName.setText(siteBean.name);
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class HomeMarketViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_value)
        TextView mTvValue;

        public HomeMarketViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
