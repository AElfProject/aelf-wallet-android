package com.legendwd.hyperpay.aelf.business.discover.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.legendwd.hyperpay.aelf.business.discover.dapp.Dapp;
import com.legendwd.hyperpay.aelf.business.discover.dapp.GameWebActivity;
import com.legendwd.hyperpay.aelf.common.EmptyViewHolder;
import com.legendwd.hyperpay.aelf.listeners.OnItemClickListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;

public class GamesChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_EMPTY = 1;
    private static final int TYPE_DATA = 2;
    private ArrayList<Dapp> mDatas;
    private OnItemClickListener mClickListener;
    private SupportFragment mFragment;

    public GamesChildAdapter(SupportFragment context, ArrayList<Dapp> datas, OnItemClickListener clickListener) {
        this.mFragment = context;
        this.mDatas = datas;
        this.mClickListener = clickListener;
    }

    public void refreshView(ArrayList<Dapp> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }


    public ArrayList<Dapp> getmDatas() {
        return mDatas;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (i == TYPE_EMPTY) {
            return new EmptyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_no_data, viewGroup, false));
        }
        return new GamesViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_dapp_without_label, viewGroup, false));
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
        } else if (holder instanceof GamesViewHolder) {
            GamesViewHolder viewHolder = (GamesViewHolder) holder;
            Dapp data = mDatas.get(i);
            RoundedCorners roundedCorners= new RoundedCorners(20);
            RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
            Glide.with(mFragment).load(data.logo).apply(options).into(viewHolder.img_logo);

            viewHolder.tv_title.setText(data.name);
            viewHolder.tv_desc.setText(data.desc);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mFragment.getParentFragment().getActivity(), GameWebActivity.class);
                    intent.putExtra(Constant.KEY, data.url);
                    intent.putExtra("bean", new Gson().toJson(data));
                    mFragment.startActivity(intent);
                }
            });
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


    class GamesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_logo)
        ImageView img_logo;
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_desc)
        TextView tv_desc;

        public GamesViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
