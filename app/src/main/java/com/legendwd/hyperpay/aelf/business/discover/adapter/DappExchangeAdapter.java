package com.legendwd.hyperpay.aelf.business.discover.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.business.discover.cyano.Constant;
import com.legendwd.hyperpay.aelf.business.discover.dapp.GameWebActivity;
import com.legendwd.hyperpay.aelf.model.bean.DappBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by haohz
 */
public class DappExchangeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private SupportFragment mFragment;
    private List<DappBean> mDatas;


    public DappExchangeAdapter(SupportFragment context, List<DappBean> dataList) {
        this.mDatas = dataList;
        this.mFragment = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ExchangeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discover_dapp_exchange, parent, false));

    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ExchangeViewHolder viewHolder = (ExchangeViewHolder) holder;
        if (mDatas == null || mDatas.size() <= 0) {
            return;
        }
        DappBean groupBean = mDatas.get(position);
        viewHolder.title.setText(groupBean.getName());
        if (!TextUtils.isEmpty(groupBean.getDesc())) {
            viewHolder.tv_dec.setText(groupBean.getDesc());
        } else {
            viewHolder.tv_dec.setText("");
        }
        RoundedCorners roundedCorners = new RoundedCorners(20);
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
        Glide.with(mFragment).load(groupBean.getLogo()).apply(options).into(viewHolder.iv_logo);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mFragment.getParentFragment().getActivity(), GameWebActivity.class);
            intent.putExtra(Constant.KEY, groupBean.getUrl());
            intent.putExtra("bean", new Gson().toJson(groupBean));
            mFragment.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        if (mDatas == null) {
            return 0;
        }
        return mDatas.size();
    }


    class ExchangeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView title;
        @BindView(R.id.tv_dec)
        TextView tv_dec;

        @BindView(R.id.iv_logo)
        ImageView iv_logo;

        public ExchangeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
