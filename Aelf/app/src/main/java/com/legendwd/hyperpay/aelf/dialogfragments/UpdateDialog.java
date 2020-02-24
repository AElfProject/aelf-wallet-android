package com.legendwd.hyperpay.aelf.dialogfragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseDialogFragment;
import com.legendwd.hyperpay.aelf.model.bean.UpdateBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdateDialog extends BaseDialogFragment {

    @BindView(R.id.tv_version_code)
    TextView tv_version_code;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ll_double_button)
    LinearLayout ll_double_button;
    @BindView(R.id.ll_single_button)
    LinearLayout ll_single_button;

    private UpdateAdapter mAdapter;

    private List<String> mDataList = new ArrayList<>();

    private UpdateBean mDataBean;

    @Override
    public int getLayoutId() {
        return R.layout.dialog_update;
    }

    @OnClick({R.id.tv_left, R.id.tv_right, R.id.tv_update})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                dismiss();
                break;

            case R.id.tv_right:
                if (null != mHandleCallback) {
                    mHandleCallback.onHandle(null);
                }
                dismiss();
                break;

            case R.id.tv_update:
                if (null != mHandleCallback) {
                    mHandleCallback.onHandle(null);
                }
                dismiss();
                break;
        }
    }


    public UpdateDialog setContent(UpdateBean bean) {
        this.mDataBean = bean;
        return this;
    }

    private void setAdapter() {
        if (null == mAdapter) {
            mAdapter = new UpdateAdapter(mDataList);
        } else {
            mAdapter.refreshView(mDataList);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tv_version_code.setText("v" + mDataBean.verNo);

        if ("1".equals(mDataBean.is_force)) {
            ll_single_button.setVisibility(View.VISIBLE);
            ll_double_button.setVisibility(View.GONE);
        } else {
            ll_single_button.setVisibility(View.GONE);
            ll_double_button.setVisibility(View.VISIBLE);
        }

        mDataList = mDataBean.intro;
        setAdapter();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
    }

    public class UpdateAdapter extends RecyclerView.Adapter<UpdateAdapter.UpdateViewHolder> {

        private List<String> mDataList;

        public UpdateAdapter(List<String> list) {
            mDataList = list;
        }

        public void refreshView(List<String> list) {
            mDataList = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public UpdateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new UpdateViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_update, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull UpdateViewHolder holder, int i) {

            String data = mDataList.get(i);
            if (!TextUtils.isEmpty(data)) {
                holder.tv_content.setText((i + 1) + ". " + data);
            }
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }

        class UpdateViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tv_content)
            TextView tv_content;

            public UpdateViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }


}
