package com.legendwd.hyperpay.aelf.business.assets.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.common.EmptyViewHolder;
import com.legendwd.hyperpay.aelf.common.HeaderAdapter;
import com.legendwd.hyperpay.aelf.listeners.HandleCallback;
import com.legendwd.hyperpay.aelf.model.bean.AssetsBean;
import com.legendwd.hyperpay.aelf.presenters.impl.AssetsManagePresenter;
import com.legendwd.hyperpay.aelf.util.StringUtil;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddAssetsChildAdapter extends HeaderAdapter<AssetsBean> {
    private AssetsManagePresenter mAssetsPresenter;

    public AddAssetsChildAdapter(List<AssetsBean> datas, AssetsManagePresenter assetsPresenter, HandleCallback handleCallback) {
        super(datas, handleCallback);
        this.mDatas = datas;
        mAssetsPresenter = assetsPresenter;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        if (holder instanceof EmptyViewHolder) {
            super.onBindViewHolder(holder, i);
            return;
        } else {
            AddAssetsViewHolder viewHolder = (AddAssetsViewHolder) holder;
            AssetsBean bean = mDatas.get(i);
            if (null != bean) {
                viewHolder.tv_name.setText(bean.getChainId() + "-" + bean.getSymbol());
                viewHolder.tv_address.setText(StringUtil.formatAddress(bean.getContractAddress(), bean.getChainId()));
                viewHolder.iv_add.setImageResource(bean.getIn() == 1 ? R.mipmap.added : R.mipmap.add);
                Glide.with(viewHolder.iv_cover)
                        .load(bean.getLogo())
                        .into(viewHolder.iv_cover);
                if (bean.getIn() != 1) {
                    viewHolder.iv_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("address", CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS));
                            jsonObject.addProperty("contract_address", bean.getContractAddress());
                            jsonObject.addProperty("flag", "1");
                            jsonObject.addProperty("symbol", bean.getSymbol());
                            jsonObject.addProperty("signed_address", CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_SIGNED_ADDRESS));
                            jsonObject.addProperty("public_key", CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_PUBLIC_KEY));
                            bean.setIn(1);
                            notifyDataSetChanged();
                            mAssetsPresenter.bind(jsonObject, bean, i);
                        }
                    });
                } else {
                    viewHolder.iv_add.setOnClickListener(null);
                }
            }
        }
    }

    @Override
    protected RecyclerView.ViewHolder getViewHolder(View view) {
        return new AddAssetsViewHolder(view);
    }

    @Override
    protected int getLayoutid() {
        return R.layout.item_assets_add;
    }

    class AddAssetsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_address)
        TextView tv_address;
        @BindView(R.id.iv_add)
        ImageView iv_add;
        @BindView(R.id.iv_cover)
        ImageView iv_cover;

        public AddAssetsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
