package com.legendwd.hyperpay.aelf.business.my.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.listeners.HandleCallback;
import com.legendwd.hyperpay.aelf.model.bean.LangsBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LanguageAdapter extends RecyclerView.Adapter {
    private List<LangsBean.ListBean> mDatas;
    private HandleCallback mHandleCallback;

    public LanguageAdapter(List<LangsBean.ListBean> beans, HandleCallback handleCallback) {
        mDatas = beans;
        mHandleCallback = handleCallback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LanguageViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_language_1, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        LangsBean.ListBean langsBean = mDatas.get(i);
        LanguageViewHolder languageViewHolder = (LanguageViewHolder) viewHolder;
        if (langsBean.isSelected()) {
            languageViewHolder.mIvChecked.setImageResource(R.mipmap.added);
        }
        languageViewHolder.mIvChecked.setVisibility(langsBean.isSelected() ? View.VISIBLE : View.GONE);
        languageViewHolder.mTvLanguage.setText(langsBean.getName());
        languageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHandleCallback != null) {
                    mHandleCallback.onHandle(i);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class LanguageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_language)
        TextView mTvLanguage;
        @BindView(R.id.iv_checked)
        ImageView mIvChecked;

        public LanguageViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
