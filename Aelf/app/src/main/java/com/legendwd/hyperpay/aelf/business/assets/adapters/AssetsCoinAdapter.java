package com.legendwd.hyperpay.aelf.business.assets.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.model.PropetyModel;

import java.util.List;

public class AssetsCoinAdapter extends RecyclerView.Adapter<AssetsCoinAdapter.PropetyViewHolder> {

    List<PropetyModel> mDatas;

    public AssetsCoinAdapter(List<PropetyModel> datas) {
        mDatas = datas;
    }


    @NonNull
    @Override
    public PropetyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_assets, viewGroup, false);
        return new PropetyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull PropetyViewHolder propetyViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class PropetyViewHolder extends RecyclerView.ViewHolder {

        public PropetyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
