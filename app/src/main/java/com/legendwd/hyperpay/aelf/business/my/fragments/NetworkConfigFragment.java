package com.legendwd.hyperpay.aelf.business.my.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.my.adapters.NetworkAdapter;
import com.legendwd.hyperpay.aelf.dialogfragments.ToastDialog;
import com.legendwd.hyperpay.aelf.listeners.OnItemClickListener;
import com.legendwd.hyperpay.aelf.model.bean.NetWorkBean;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.aelf.util.JsonUtils;
import com.legendwd.hyperpay.aelf.util.StringUtil;
import com.legendwd.hyperpay.httputil.ServiceGenerator;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

public class NetworkConfigFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.et_custom)
    EditText etCustom;
    @BindView(R.id.iv_custom)
    ImageView ivCustom;

    private NetworkAdapter mAdapter;

    private List<NetWorkBean> mDataList;
    private int mSelectIndex;

    public static NetworkConfigFragment newInstance() {
        Bundle args = new Bundle();
        NetworkConfigFragment tabFourFragment = new NetworkConfigFragment();
        tabFourFragment.setArguments(args);
        return tabFourFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initToolbarNav((Toolbar) view.findViewById(R.id.toolbar), R.string.switch_network, true);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_network_config;
    }

    @Override
    public void process() {
        mSelectIndex = CacheUtil.getInstance().getProperty(Constant.Sp.NETWORK_SELECT_KEY, 0);
        String json = StringUtil.getAssetsJson(getContext());
        mDataList = JsonUtils.jsonToList(json, NetWorkBean.class);
        mAdapter = new NetworkAdapter(_mActivity, mDataList, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o) {
                mSelectIndex = (int) o;
                checkCustomImg();
            }
        });
        mAdapter.setSelectIndex(mSelectIndex);
//        recyclerView.addItemDecoration(new LinearSpacingItemDecoration(_mActivity,1, Color.parseColor("#E0E0E0")));
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        recyclerView.setAdapter(mAdapter);
        if (mSelectIndex < 0) {
            ivCustom.setSelected(true);
            etCustom.setSelected(true);
            etCustom.setText(CacheUtil.getInstance().getProperty(Constant.Sp.NETWORK_BASE_URL));
        }
    }

    private void checkCustomImg() {
        if (ivCustom.isSelected()) {
            ivCustom.setSelected(false);
            etCustom.setSelected(false);
        }
    }

    @OnClick({R.id.iv_custom, R.id.tv_next})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.iv_custom:
                if (!ivCustom.isSelected()) {
                    ivCustom.setSelected(true);
                    etCustom.setSelected(true);
                    mSelectIndex = -1;
                    mAdapter.setSelectIndex(mSelectIndex);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.tv_next:
                int index = CacheUtil.getInstance().getProperty(Constant.Sp.NETWORK_SELECT_KEY, 0);
                if (mSelectIndex != index || mSelectIndex == -1) {
                    changeNet(mSelectIndex);
                }else {
                    pop();
                }
                break;
            default:
                break;
        }
    }

    private void changeNet(int index) {
        if (index < 0) {
            customNet();
        } else {
            NetWorkBean bean = mDataList.get(index);
            String url = bean.getUrl();
            String node = bean.getNode();
            if (!TextUtils.isEmpty(node)) {
                url += node;
            }
            changeBaseUrl(index, url);
        }
    }

    private void customNet() {
        String url = etCustom.getText().toString().trim();
        if (TextUtils.isEmpty(url)) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                    .setToast(getString(R.string.network_tip));
        } else if (chcekUrl(url)) {
            changeBaseUrl(-1, url);
        } else {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                    .setToast(getString(R.string.network_tip_error));
        }
    }

    private boolean chcekUrl(String url) {
        Pattern patt = Pattern.compile(Constant.URL_MATCHER);
        return patt.matcher(url).matches();
    }

    private void changeBaseUrl(int index, String url) {
        ServiceGenerator.setBuilder(url);
        CacheUtil.getInstance().setProperty(Constant.Sp.NETWORK_SELECT_KEY, index);
        CacheUtil.getInstance().setProperty(Constant.Sp.NETWORK_BASE_URL, url);
        pop();
    }


}
