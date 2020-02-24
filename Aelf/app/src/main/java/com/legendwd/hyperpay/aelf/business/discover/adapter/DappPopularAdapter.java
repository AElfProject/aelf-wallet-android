package com.legendwd.hyperpay.aelf.business.discover.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
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
public class DappPopularAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Dapp> mDapps;
    private Activity mActivity;

    public DappPopularAdapter(Activity context, ArrayList<Dapp> dappList) {
        this.mActivity = context;
        this.mDapps = dappList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dapp_popular,
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
            viewHolder.tv_position.setText(position + "");
            switch (position) {
                case 0:
                    viewHolder.tv_position.setTextColor(Color.parseColor("#FE4302"));
                    break;
                case 1:
                    viewHolder.tv_position.setTextColor(Color.parseColor("#FD7302"));
                    break;
                case 2:
                    viewHolder.tv_position.setTextColor(Color.parseColor("#FEA702"));
                    break;
                default:
                    viewHolder.tv_position.setTextColor(Color.parseColor("#9BA9C6"));
                    break;
            }

            viewHolder.tv_name.setText(dapp.name);

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


        @BindView(R.id.tv_position)
        TextView tv_position;
        @BindView(R.id.tv_name)
        TextView tv_name;
        View view;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }
    }


}
