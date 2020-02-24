package com.legendwd.hyperpay.aelf.common;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmptyViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_retry)
    public TextView tv_retry;

    public EmptyViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
