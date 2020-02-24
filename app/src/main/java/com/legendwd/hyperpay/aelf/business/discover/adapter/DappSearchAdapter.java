package com.legendwd.hyperpay.aelf.business.discover.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.business.discover.dapp.GameListBean;
import com.legendwd.hyperpay.aelf.business.discover.dapp.SearchType;
import com.legendwd.hyperpay.aelf.widget.LinearSpacingItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DappSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity mActivity;

    private GameListBean mData;
    private LayoutInflater layoutInflater;

    private static final int TYPE_EMPTY = 0;
    private static final int TYPE_POPULAR = 1;
    private static final int TYPE_DAPP_NAME = 2;
    private static final int TYPE_DAPP_URL = 3;

    private DappNameAdapter mDappNameAdapter;
    private DappPopularAdapter mDappPopularAdapter;
    private DappUrlAdapter mDappUrlAdapter;

    public DappSearchAdapter(Activity mActivity, GameListBean dappGames) {
        this.mActivity = mActivity;
        this.mData = dappGames;
        layoutInflater = LayoutInflater.from(mActivity);
    }

    public void refreshData(GameListBean dappGames) {
        this.mData = dappGames;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case TYPE_EMPTY:
                viewHolder = new DappEmptyViewHolder(layoutInflater.inflate(R
                        .layout.item_no_data, parent, false));
                break;
            case TYPE_POPULAR:
                viewHolder = new DappPopularViewHolder(layoutInflater.inflate(R.layout.layout_dapp_popular, parent, false));
                break;
            case TYPE_DAPP_NAME:
                viewHolder = new DappNameViewHolder(layoutInflater.inflate(R.layout.layout_dapp_name, parent, false));
                break;
            case TYPE_DAPP_URL:
                viewHolder = new DappUrlViewHolder(layoutInflater.inflate(R.layout.layout_dapp_url, parent, false));
                break;
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        SearchType searchType = mData.searchType;
        if (searchType == null) {
            return TYPE_POPULAR;
        }
        switch (searchType) {
            case DAPP_NONE:
                return TYPE_EMPTY;
            case DAPP_POPULAR:
                return TYPE_POPULAR;
            case DAPP_NAME:
                return TYPE_DAPP_NAME;
            case DAPP_URL:
                return TYPE_DAPP_URL;
            default:
                return TYPE_EMPTY;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DappPopularViewHolder) {
            mDappPopularAdapter = new DappPopularAdapter(mActivity,mData.dapps);
            ((DappPopularViewHolder) holder).recyclerView.setAdapter(mDappPopularAdapter);
        } else if (holder instanceof DappNameViewHolder) {
            mDappNameAdapter = new DappNameAdapter(mActivity, mData.dapps);
            ((DappNameViewHolder) holder).recyclerView.setAdapter(mDappNameAdapter);
        } else if (holder instanceof DappUrlViewHolder) {
            mDappUrlAdapter = new DappUrlAdapter(mActivity, mData.dapps);
            ((DappUrlViewHolder) holder).recyclerView.setAdapter(mDappUrlAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class DappEmptyViewHolder extends RecyclerView.ViewHolder {
        View view;

        public DappEmptyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }

    }

    class DappPopularViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;
        View view;

        public DappPopularViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
            recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
            recyclerView.addItemDecoration(new LinearSpacingItemDecoration(mActivity, 1, Color.parseColor("#E0E0E0")));
        }
    }

    class DappNameViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;
        View view;

        public DappNameViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
            recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
            recyclerView.addItemDecoration(new LinearSpacingItemDecoration(mActivity, 1, Color.parseColor("#E0E0E0")));
        }
    }

    class DappUrlViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;
        View view;

        public DappUrlViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
            recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
            recyclerView.addItemDecoration(new LinearSpacingItemDecoration(mActivity, 1, Color.parseColor("#E0E0E0")));
        }
    }
}
