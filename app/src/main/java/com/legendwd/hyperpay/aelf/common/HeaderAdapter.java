package com.legendwd.hyperpay.aelf.common;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseAdapterModel;
import com.legendwd.hyperpay.aelf.listeners.HandleCallback;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class HeaderAdapter<T extends BaseAdapterModel> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected List<T> mDatas;
    protected HandleCallback mHandleCallback;


    public HeaderAdapter(List<T> datas, HandleCallback handleCallback){
        mDatas = datas;
        mHandleCallback = handleCallback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == BaseAdapterModel.ItemType.EMPTY) {
            return new EmptyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(getEmptyLayoutid(), viewGroup, false));
        } else {
            return getViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(getLayoutid(),viewGroup,false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getItemType();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof EmptyViewHolder) {
            EmptyViewHolder emptyViewHolder = (EmptyViewHolder) viewHolder;
            if (mHandleCallback != null) {
                emptyViewHolder.tv_retry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mHandleCallback.onHandle(null);
                    }
                });
            }
        }
    }


    @Override
    public int getItemCount() {
        if (mDatas.size() == 0){
            Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            try {
                T entity = entityClass.newInstance();
                entity.setItemType(BaseAdapterModel.ItemType.EMPTY);
                mDatas.add(entity);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }else if (mDatas.size() > 1){
            if (mDatas.get(0).getItemType() == BaseAdapterModel.ItemType.EMPTY){
                mDatas.remove(0);
            }
        }
        return mDatas.size();
    }

    protected abstract RecyclerView.ViewHolder getViewHolder(View view);

    protected  int getEmptyLayoutid(){
        return R.layout.item_no_data;
    }

    protected abstract int getLayoutid();

    public List getDatas(){
        return mDatas;
    }

    public void update(List<T> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    public void updateItem(int i) {
        notifyItemChanged(i);
    }
}
