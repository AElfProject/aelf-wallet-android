package com.legendwd.hyperpay.aelf.business.discover.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.legendwd.hyperpay.aelf.business.discover.dapp.Dapp;
import com.legendwd.hyperpay.aelf.business.discover.dapp.GameWebActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by haohz
 */
public class DappNameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Activity mActivity;
    private ArrayList<Dapp> mDapps;

    public DappNameAdapter(Activity context, ArrayList<Dapp> dappList) {
        this.mDapps = dappList;
        this.mActivity = context;
    }

    public void refreshDappList(ArrayList<Dapp> dappList) {
        this.mDapps = dappList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dapp_name,
                parent, false));
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if (mDapps == null || mDapps.size() <= 0) {
            return;
        }
        Dapp dapp = mDapps.get(position);

        if (null != dapp) {
            RoundedCorners roundedCorners = new RoundedCorners(30);
            //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
            RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
            Glide.with(mActivity).load(dapp.logo).apply(options).into(viewHolder.imgLogo);
            viewHolder.tvTitle.setText(dapp.name);
            viewHolder.tvDesc.setText(dapp.desc);
            viewHolder.view.setOnClickListener(v -> {
                Intent intent = new Intent(mActivity, GameWebActivity.class);
                intent.putExtra(Constant.KEY, dapp.url);
                intent.putExtra("bean", new Gson().toJson(dapp));
                mActivity.startActivity(intent);
            });
        }

    }

    @Override
    public int getItemCount() {
        if (mDapps == null) {
            return 0;
        }
        return mDapps.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.img_logo)
        ImageView imgLogo;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_desc)
        TextView tvDesc;
        View view;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }
    }


}
