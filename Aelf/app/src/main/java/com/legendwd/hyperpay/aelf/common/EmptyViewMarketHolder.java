package com.legendwd.hyperpay.aelf.common;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmptyViewMarketHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_add_favourites)
    public TextView tv_add_favourites;

    public EmptyViewMarketHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
