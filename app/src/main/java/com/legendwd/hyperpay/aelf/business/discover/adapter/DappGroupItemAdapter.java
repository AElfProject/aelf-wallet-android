package com.legendwd.hyperpay.aelf.business.discover.adapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.discover.DappGameListFragment;
import com.legendwd.hyperpay.aelf.model.bean.DappGroupBean;
import com.legendwd.hyperpay.lib.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by haohz
 */
public class DappGroupItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private BaseFragment mFragment;
    private List<DappGroupBean> dataList = new ArrayList<>();

    public DappGroupItemAdapter(BaseFragment context, List<DappGroupBean> dataList) {
        this.dataList = dataList;
        this.mFragment = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dicover_exchange, parent, false));
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        DappGroupBean dappGroupBean = dataList.get(position);
        DappExchangeAdapter adapter = new DappExchangeAdapter(mFragment, dappGroupBean.getData());
        viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(mFragment.getContext()));
        viewHolder.tv_name.setText(dappGroupBean.getCategoryTitle());
        viewHolder.recyclerView.setAdapter(adapter);

        viewHolder.tx_game_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.BundleKey.DAPP_GROUP_NAME, dappGroupBean.getCategoryTitle());
                bundle.putString(Constant.BundleKey.DAPP_GROUP_CAT, dappGroupBean.getCat());
                mFragment.startBrotherFragment(DappGameListFragment.newInstance(bundle));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dataList == null) {
            return 0;
        }
        return dataList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;
        @BindView(R.id.tx_game_more)
        TextView tx_game_more;
        @BindView(R.id.tv_name)
        TextView tv_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
