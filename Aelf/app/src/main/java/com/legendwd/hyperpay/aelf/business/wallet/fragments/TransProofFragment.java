package com.legendwd.hyperpay.aelf.business.wallet.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.assets.fragments.TransactionRecordFragment;
import com.legendwd.hyperpay.aelf.dialogfragments.ToastDialog;
import com.legendwd.hyperpay.aelf.model.MessageEvent;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.TransactionBean;
import com.legendwd.hyperpay.aelf.model.bean.TransferBalanceBean;
import com.legendwd.hyperpay.aelf.model.param.TransferBalanceParam;
import com.legendwd.hyperpay.aelf.model.param.TransferCrossChainParam;
import com.legendwd.hyperpay.aelf.presenters.impl.TransactionRecordPresenter;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.aelf.util.StringUtil;
import com.legendwd.hyperpay.aelf.views.ITransactionRecordView;
import com.legendwd.hyperpay.aelf.widget.LoadDialog;
import com.legendwd.hyperpay.lib.Constant;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Colin
 * @date 2019/10/10.
 * description：跨链交易凭证
 */
public class TransProofFragment extends BaseFragment implements ITransactionRecordView {
    @BindView(R.id.tv_detail)
    TextView tv_detail;
    @BindView(R.id.tv_balance)
    TextView tv_balance;
    @BindView(R.id.tv_miner_fee)
    TextView tv_miner_fee;
    @BindView(R.id.tv_copy_to)
    TextView tv_copy_to;
    @BindView(R.id.tv_copy_from)
    TextView tv_copy_from;
    @BindView(R.id.tv_memo_tx)
    TextView tv_memo;
    @BindView(R.id.tv_copy_txid)
    TextView tv_copy_txid;
    @BindView(R.id.tv_sign_transaction)
    TextView tv_sign_transaction;
    private TransactionRecordPresenter mTransactionRecordPresenter;
    private TransferCrossChainParam transProofFragment;
    private LoadDialog mLoadDialog;

    public static TransProofFragment newInstance(Bundle args) {
        TransProofFragment transProofFragment = new TransProofFragment();
        transProofFragment.setArguments(args);
        return transProofFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_transaction_proof;
    }

    @Override
    public void process() {
        initToolbarNav(getView().findViewById(R.id.toolbar), getResources().getString(R.string.trans_proof_title), true);
        mLoadDialog = new LoadDialog(getContext(), false);
        String json = getArguments().getString(Constant.BundleKey.TransData);
        transProofFragment = new Gson().fromJson(json, TransferCrossChainParam.class);
        if (transProofFragment == null) return;
        mTransactionRecordPresenter = new TransactionRecordPresenter(this);
        tv_balance.setText(transProofFragment.amount + transProofFragment.symbol);

        tv_miner_fee.setText(TextUtils.isEmpty(transProofFragment.fee) ? "0.00 " +
                transProofFragment.symbol : StringUtil.formatDataNoZero(Constant.DEFAULT_DECIMALS,
                transProofFragment.fee) + " " + transProofFragment.symbol);
        String tv_to = "<font color='#641EB0'><small>" + transProofFragment.to_chain + "</small></font>  " + transProofFragment.to_address;
        tv_copy_to.setText(Html.fromHtml(tv_to));
        String tv_from = "<font color='#F4A11C'><small>" + transProofFragment.from_chain + "</small></font>  " + transProofFragment.from_address;
        tv_copy_from.setText(Html.fromHtml(tv_from));
        tv_memo.setText(transProofFragment.memo);
        tv_copy_txid.setText(transProofFragment.txid);
        tv_sign_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadDialog.show();
                getTransferBalance();
            }
        });
    }

    public void getTransferBalance() {
        TransferBalanceParam param = new TransferBalanceParam();
        param.address = transProofFragment.from_address;
        param.contractAddress = transProofFragment.toContractAddress;
        param.symbol = transProofFragment.toMianSymbol;
        param.chainid = transProofFragment.to_chain;
        mTransactionRecordPresenter.getTransferBalance(param);
    }

    private void checkToChainBalance(TransferBalanceBean balanceBean) {
        double balance = Double.valueOf(balanceBean.balance.getBalance());
        double fee = 0;
        if (balanceBean.fee != null) {
            fee = Double.valueOf(balanceBean.fee.get(0).fee);
        }
        if(balance < fee) {
            if (mLoadDialog != null && mLoadDialog.isShowing()) {
                mLoadDialog.dismiss();
            }
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                    .setToast(getString(R.string.transfer_not_enough, transProofFragment.to_chain, transProofFragment.symbol));
        }else {
            mTransactionRecordPresenter.addIndex(transProofFragment);
        }
    }

    @Override
    public void onCrossChainSuccess(ResultBean resultBean) {
        if (mLoadDialog != null && mLoadDialog.isShowing()) {
            mLoadDialog.dismiss();
        }
        if (resultBean.getStatus() == 200) {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.BundleKey.TXID, transProofFragment.txid);
            bundle.putString(Constant.BundleKey.CHAIN_ID, transProofFragment.chainid_c);
            startWithPop(TransactionRecordFragment.newInstance(bundle));
            EventBus.getDefault().post(new MessageEvent(Constant.Event.REFRSH_TRANSATION));
        }
    }

    @Override
    public void onAddIndexSuccess(ResultBean resultBean) {
        if (resultBean.getStatus() == 200) {
            mTransactionRecordPresenter.getCrossChainDetail(transProofFragment);
        }
    }

    @Override
    public void onAddIndexError(int i, String message) {
        if (mLoadDialog != null && mLoadDialog.isShowing()) {
            mLoadDialog.dismiss();
        }
    }

    @Override
    public void onTransferBalanceSuccess(ResultBean<TransferBalanceBean> listResultBean) {
        if(listResultBean.getStatus() == 200) {
            checkToChainBalance(listResultBean.getData());
        }
    }

    @Override
    public void onTransferBalanceError(int i, String toString) {
        if (mLoadDialog != null && mLoadDialog.isShowing()) {
            mLoadDialog.dismiss();
        }
    }

    @Override
    public void onTransactionDetailSuccess(TransactionBean.ListBean bean) {

    }

    @Override
    public void onTransactionDetailError(int code, String msg) {

    }

    @Override
    public void onCrossChainError(int code, String msg) {
        if (mLoadDialog != null && mLoadDialog.isShowing()) {
            mLoadDialog.dismiss();
        }
    }

    @OnClick(R.id.tv_copy_to)
    void onCopyTo(){
        copyTextData(tv_copy_to.getText().toString());
    }

    @OnClick(R.id.tv_copy_from)
    void onCopyFrom(){
        copyTextData(tv_copy_from.getText().toString());
    }

    @OnClick(R.id.tv_copy_txid)
    void onCopyTxid(){
        copyTextData(tv_copy_txid.getText().toString());
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

}
