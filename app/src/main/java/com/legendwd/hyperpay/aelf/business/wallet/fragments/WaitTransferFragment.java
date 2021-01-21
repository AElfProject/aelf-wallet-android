package com.legendwd.hyperpay.aelf.business.wallet.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.assets.fragments.ExplorerFragment;
import com.legendwd.hyperpay.aelf.business.assets.fragments.TransactionRecordFragment;
import com.legendwd.hyperpay.aelf.config.ApiUrlConfig;
import com.legendwd.hyperpay.aelf.dialogfragments.ToastDialog;
import com.legendwd.hyperpay.aelf.listeners.HandleCallback;
import com.legendwd.hyperpay.aelf.listeners.OnTextCorrectCallback;
import com.legendwd.hyperpay.aelf.listeners.OnWaitItemClickListener;
import com.legendwd.hyperpay.aelf.model.bean.ChooseChainsBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.WaitTransactionBean;
import com.legendwd.hyperpay.aelf.presenters.IWaitTransferPresenter;
import com.legendwd.hyperpay.aelf.presenters.impl.WaitTransferPresenter;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.aelf.util.ScreenUtils;
import com.legendwd.hyperpay.aelf.views.IWaitTransferView;
import com.legendwd.hyperpay.aelf.widget.LinearSpacingItemDecoration;
import com.legendwd.hyperpay.aelf.widget.LoadDialog;
import com.legendwd.hyperpay.aelf.widget.webview.DWebView;
import com.legendwd.hyperpay.aelf.widget.webview.JsApi;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;
import com.legendwd.hyperpay.lib.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class WaitTransferFragment extends BaseFragment implements IWaitTransferView {
    private IWaitTransferPresenter presenter;
    ToastDialog mToastDialog;
    private LoadDialog mLoadDialog;
    @BindView(R.id.wait_bridge)
    DWebView mWvbridge;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private TransferWaitAdapter mAdapter;
    private WaitTransactionBean.WaitListBean mBean;
    private String mTotxId;
    private List<WaitTransactionBean.WaitListBean> mDataList;
    private List<ChooseChainsBean> mChainList;

    public static WaitTransferFragment newInstance(Bundle bundle) {
        WaitTransferFragment fragment = new WaitTransferFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_wait_transfer_chain;
    }

    @Override
    public void process() {
        mWvbridge.loadUrl(ApiUrlConfig.AssetsUrl);
        mWvbridge.addJavascriptObject(new JsApi(new HandleCallback() {
            @Override
            public void onHandle(Object o) {
                if (mToastDialog != null) {
                    mToastDialog.dismiss();
                }
                if (mLoadDialog != null && mLoadDialog.isShowing()) {
                    mLoadDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject((String) o);
                    int success = jsonObject.optInt("success");
                    if (success == 1) {
                        mTotxId = jsonObject.optString("txId");
                        presenter.rcvTxid(mBean.txid, mTotxId);
                    } else if(success == 2) {
                        DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                                .setToast(getString(R.string.request_timed_out));
                    } else {
                        String err = jsonObject.optString("err");
                        DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                                .setToast(TextUtils.isEmpty(err) ? getString(R.string.transfer_fail) : err);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }), null);

        presenter = new WaitTransferPresenter(this);

        initToolbarNav(mToolbar, getActivity().getString(R.string.wait_transfer_title), true);
        TextView tvRight = mToolbar.findViewById(R.id.tv_title_right);
        tvRight.setVisibility(View.INVISIBLE);
        mLoadDialog = new LoadDialog(getContext(), false);
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        recyclerView.addItemDecoration(new LinearSpacingItemDecoration(_mActivity, ScreenUtils.dip2px(_mActivity, 3), Color.parseColor("#E0E0E0")));
        WaitTransactionBean bean = new Gson().fromJson(getArguments().getString("bean"), WaitTransactionBean.class);
        if (bean == null) {
            mDataList = new ArrayList<>();
        } else {
            mDataList = bean.list;
        }
        mAdapter = new TransferWaitAdapter(mDataList, new OnWaitItemClickListener() {
            @Override
            public void onItemClick(Object o) {
            }

            @Override
            public void onUrlClick(WaitTransactionBean.WaitListBean bean) {
                ChooseChainsBean bean1 = getChainBean(bean.from_chain);
                start(ExplorerFragment.newInstance(bean.from_chain + " explorer", bean1.getExplorer() + "/tx/" + bean.txid));
            }

            @Override
            public void onConfimeClick(WaitTransactionBean.WaitListBean bean) {
                transferChechk(bean);
            }

            @Override
            public void onCopyClick(String copyText) {
                copyTextData(copyText);
            }
        });
        recyclerView.setAdapter(mAdapter);
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                presenter.getWaitTransList(CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS));
            }
        });
        presenter.getCrossChains();
        if (bean == null) {
            refresh.autoRefresh();
        }
    }

    protected void copyTextData(String text) {

        ClipboardManager clipboardManager = (ClipboardManager) _mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager == null || TextUtils.isEmpty(text)) {
            return;
        }
        clipboardManager.setPrimaryClip(ClipData.newPlainText(null, text));
        if (clipboardManager.hasPrimaryClip() && clipboardManager.getPrimaryClip() != null) {
            clipboardManager.getPrimaryClip().getItemAt(0).getText();
        }

        DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.copy_success));
    }

    private void transferChechk(WaitTransactionBean.WaitListBean bean) {
        showPasswordDialogForPrivate(new OnTextCorrectCallback() {
            @Override
            public void onTextCorrect(Object... obj) {
                String key = (String) obj[1];
                transfer(key, bean);
            }
        });
    }

    private void transfer(String key, WaitTransactionBean.WaitListBean bean) {
        if (mToastDialog == null) {
            mToastDialog = DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                    .setToast(R.string.loading)
                    .setLoading(true)
                    .setToastCancelable(false);
        } else {
            mToastDialog.setToast(R.string.loading);
            mToastDialog.show(getFragmentManager(), "ToastDialog");
        }
        if (mLoadDialog != null && !mLoadDialog.isShowing()) {
            mLoadDialog.show();
        }
        mBean = bean;
        ChooseChainsBean fromeChain = getChainBean(mBean.from_chain);
        ChooseChainsBean toChain = getChainBean(mBean.to_chain);
        if (fromeChain != null && toChain != null) {
            requestJs(key, fromeChain, toChain);
        } else {
            onChainsError(-1, null);
        }
    }

    @Override
    public void onRcvTxidSuccess(ResultBean resultBean) {
        if (resultBean.getStatus() == 200) {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.BundleKey.TXID, mTotxId);
            bundle.putString(Constant.BundleKey.CHAIN_ID, mBean.to_chain);
            startWithPop(TransactionRecordFragment.newInstance(bundle));
        }
    }

    @Override
    public void onRcvTxidError(int i, String message) {

    }

    @Override
    public void onWaitCrossTransSuccess(ResultBean<WaitTransactionBean> resultBean) {
        refresh.finishRefresh();
        if (resultBean.getStatus() == 200) {
            mAdapter.refreshView(resultBean.getData().list);
        }
    }

    @Override
    public void onWaitCrossTransError(int i, String message) {
        refresh.finishRefresh();
    }

    @Override
    public void onChainsSuccess(ResultBean<List<ChooseChainsBean>> bean) {
        if (bean.getStatus() == 200) {
            mChainList = bean.getData();
        } else {
            onChainsError(-1, bean.getMsg());
        }
    }

    private void requestJs(String key, ChooseChainsBean fromeChain, ChooseChainsBean toChain) {
        String form = "";
        String to = "";
        form = fromeChain.getNode();
        to = toChain.getNode();

        String fromUrl;
        if (!TextUtils.isEmpty(form) && form.endsWith("/")) {
            fromUrl = form.substring(0, form.length() - 1);
        } else {
            fromUrl = form;
        }
        String toUrl;
        if (!TextUtils.isEmpty(to) && to.endsWith("/")) {
            toUrl = to.substring(0, to.length() - 1);
        } else {
            toUrl = to;
        }
        
        JsonObject param = new JsonObject();
        param.addProperty("privateKey", key);
        param.addProperty("fromNode", fromUrl);
        param.addProperty("toNode", toUrl);
        param.addProperty("mainChainId", "9992731");
        param.addProperty("issueChainId", fromeChain.getIssueid());
        param.addProperty("txID", mBean.txid);

        param.addProperty("fromTokenContractAddres", fromeChain.getContract_address());
        param.addProperty("fromChainContractAddres", fromeChain.getCrossChainContractAddress());
        param.addProperty("toTokenContractAddres", toChain.getContract_address());
        param.addProperty("toChainContractAddres", toChain.getCrossChainContractAddress());
        param.addProperty("fromChain", fromeChain.getName());
        param.addProperty("toChain", toChain.getName());
        mWvbridge.callHandler("transferCrossChainReceiveJS", new Gson().toJson(param), new CallBackFunction() {

            @Override
            public void onCallBack(String data) {

            }
        });
    }

    private ChooseChainsBean getChainBean(String chain) {
        if (mChainList == null) {
            presenter.getCrossChains();
            return null;
        }
        for (ChooseChainsBean bean : mChainList) {
            if (chain.equals(bean.getName())) {
                return bean;
            }
        }
        return null;
    }

    @Override
    public void onChainsError(int i, String message) {
        if (mToastDialog != null) {
            mToastDialog.dismiss();
        }
        if (mLoadDialog != null && mLoadDialog.isShowing()) {
            mLoadDialog.dismiss();
        }
        DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                .setToast(TextUtils.isEmpty(message) ? getString(R.string.transfer_fail) : message);
    }
}
