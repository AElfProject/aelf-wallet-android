package com.legendwd.hyperpay.aelf.business.discover.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
public class DappUrlAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Activity mActivity;
    private ArrayList<Dapp> mDapps;

    public DappUrlAdapter(Activity context, ArrayList<Dapp> dappList) {
        this.mDapps = dappList;
        this.mActivity = context;
    }

    public void refreshDappList(ArrayList<Dapp> dappList) {
        this.mDapps = dappList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dapp_url,
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
            viewHolder.tv_url.setText(TextUtils.isEmpty(dapp.url) ? "" : dapp.url);

            viewHolder.itemView.setOnClickListener((view) -> {
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
        @BindView(R.id.tv_url)
        TextView tv_url;
        View view;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }
    }


}
