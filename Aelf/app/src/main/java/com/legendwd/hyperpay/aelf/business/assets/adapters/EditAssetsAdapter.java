package com.legendwd.hyperpay.aelf.business.assets.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.common.EmptyViewHolder;
import com.legendwd.hyperpay.aelf.common.HeaderAdapter;
import com.legendwd.hyperpay.aelf.listeners.HandleCallback;
import com.legendwd.hyperpay.aelf.listeners.OnItemClickListener;
import com.legendwd.hyperpay.aelf.model.bean.AssetsBean;
import com.legendwd.hyperpay.aelf.util.CommonItemTouchHelper;
import com.legendwd.hyperpay.aelf.util.StringUtil;
import com.legendwd.hyperpay.aelf.widget.RoundImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditAssetsAdapter extends HeaderAdapter<AssetsBean> {

    private CommonItemTouchHelper mItemTouchHelper;
    private SmartRefreshLayout mSmartRefreshLayout;
    private OnItemClickListener mOnItemClickListener;
    private boolean mDragAble = false;

    public EditAssetsAdapter(List datas, CommonItemTouchHelper itemTouchHelper, SmartRefreshLayout smartRefreshLayout, HandleCallback handleCallback) {
        super(datas, handleCallback);
        mItemTouchHelper = itemTouchHelper;
        mSmartRefreshLayout = smartRefreshLayout;
        mHandleCallback = handleCallback;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof EmptyViewHolder) {
            super.onBindViewHolder(viewHolder, i);
        } else {
            EditChildViewHolder holder = (EditChildViewHolder) viewHolder;
            holder.ivDrag.setVisibility(mDragAble ? View.VISIBLE : View.GONE);
            if (mDragAble) {
                holder.ivDrag.setOnTouchListener((v, event) -> {
                    mItemTouchHelper.startDrag(holder);
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        mSmartRefreshLayout.setEnabled(false);
                    }
                    return true;
                });
            }
            holder.ivDel.setOnClickListener(v -> {
                if (mHandleCallback != null) {
                    mHandleCallback.onHandle(i);
                }
            });
            AssetsBean aelfBean = mDatas.get(i);
            holder.tvTitle.setText(aelfBean.getChainId() + "-" + aelfBean.getSymbol());
            holder.tvAddress.setText(StringUtil.formatAddress(aelfBean.getContractAddress(), aelfBean.getChainId()));
            holder.tvBalance.setText(holder.tvBalance.getContext().getResources().getString(R.string.balance) + " :" + String.format("%.2f", Float.parseFloat(aelfBean.getBalance())));

            boolean bHidden = "inner".equals(aelfBean.getBlockHash());
            holder.ivDel.setVisibility(bHidden ? View.INVISIBLE : View.VISIBLE);

            Glide.with(holder.iv_cover)
                    .load(aelfBean.getLogo())
                    .into(holder.iv_cover);
        }
    }

    @Override
    protected RecyclerView.ViewHolder getViewHolder(View view) {
        return new EditChildViewHolder(view);
    }

    @Override
    protected int getLayoutid() {
        return R.layout.item_edit_assets;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void updateDragAble(boolean dragAble) {
        mDragAble = dragAble;
        notifyDataSetChanged();
    }

    class EditChildViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_drag)
        ImageView ivDrag;
        @BindView(R.id.iv_del)
        ImageView ivDel;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.tv_balance)
        TextView tvBalance;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.iv_cover)
        RoundImageView iv_cover;

        public EditChildViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
