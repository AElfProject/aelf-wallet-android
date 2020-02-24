package com.legendwd.hyperpay.aelf.business.discover.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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
import com.legendwd.hyperpay.aelf.model.bean.DiscoveryBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by haohz
 */
public class DappRecommendItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private SupportFragment mFragment;
    private List<DiscoveryBean.DappBean> dataList = new ArrayList<>();

    public DappRecommendItemAdapter(SupportFragment context, List<DiscoveryBean.DappBean> dataList) {
        this.dataList = dataList;
        this.mFragment = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discover_dapp_recommend, parent, false));
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;

        DiscoveryBean.DappBean dapp = dataList.get(position);

        if (null != dapp) {
            viewHolder.tv_name.setText(dapp.getName());
            RoundedCorners roundedCorners= new RoundedCorners(20);
            RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
            Glide.with(mFragment).load(dapp.getLogo()).apply(options).into(viewHolder.iv_logo);
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(mFragment.getParentFragment().getActivity(), GameWebActivity.class);
                intent.putExtra(Constant.KEY, dapp.getUrl());
                intent.putExtra("bean", new Gson().toJson(dapp));
                mFragment.startActivity(intent);
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
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.iv_logo)
        ImageView iv_logo;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
