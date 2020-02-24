package com.legendwd.hyperpay.aelf.business.wallet.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.listeners.OnItemClickListener;
import com.legendwd.hyperpay.aelf.model.bean.MnemonicWord;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author lovelyzxing
 * @date 2019/6/13
 * @Description
 */
public class MnemonicAdapter extends RecyclerView.Adapter<MnemonicAdapter.MnemonicViewHolder> {

    private List<MnemonicWord> mDataList;

    private OnItemClickListener mClickListener;

    public MnemonicAdapter(List<MnemonicWord> list, OnItemClickListener clickListener) {
        this.mDataList = list;
        this.mClickListener = clickListener;
    }

    public void refreshView(List<MnemonicWord> list) {
        this.mDataList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MnemonicViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MnemonicAdapter.MnemonicViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mnemonic, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MnemonicViewHolder holder, int i) {
        MnemonicWord bean = mDataList.get(i);

        if (null != bean) {
            holder.tv_word.setText(bean.word);
            holder.tv_word.setSelected(bean.isSelected ? true : false);
            holder.rel_word.setSelected(bean.isSelected ? true : false);

            holder.itemView.setOnClickListener(v -> {
                if (null != mClickListener) {
                    mClickListener.onItemClick(bean);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class MnemonicViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_word)
        TextView tv_word;
        @BindView(R.id.rel_word)
        RelativeLayout rel_word;

        public MnemonicViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
