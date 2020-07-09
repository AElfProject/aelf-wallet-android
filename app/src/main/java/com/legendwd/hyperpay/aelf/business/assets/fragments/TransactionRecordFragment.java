package com.legendwd.hyperpay.aelf.business.assets.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.config.ApiUrlConfig;
import com.legendwd.hyperpay.aelf.dialogfragments.ToastDialog;
import com.legendwd.hyperpay.aelf.listeners.HandleCallback;
import com.legendwd.hyperpay.aelf.model.bean.ChainAddressBean;
import com.legendwd.hyperpay.aelf.model.bean.ChooseChainsBean;
import com.legendwd.hyperpay.aelf.model.bean.JsResultBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.TransactionBean;
import com.legendwd.hyperpay.aelf.model.bean.TransferBalanceBean;
import com.legendwd.hyperpay.aelf.model.bean.TransferBean;
import com.legendwd.hyperpay.aelf.presenters.impl.TransactionRecordPresenter;
import com.legendwd.hyperpay.aelf.presenters.impl.TransferPresenter;
import com.legendwd.hyperpay.aelf.util.BitmapUtil;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.aelf.util.LanguageUtil;
import com.legendwd.hyperpay.aelf.util.StringUtil;
import com.legendwd.hyperpay.aelf.views.ITransactionRecordView;
import com.legendwd.hyperpay.aelf.views.ITransferView;
import com.legendwd.hyperpay.aelf.widget.webview.DWebView;
import com.legendwd.hyperpay.aelf.widget.webview.JsApi;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;
import com.legendwd.hyperpay.lib.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

public class TransactionRecordFragment extends BaseFragment implements View.OnClickListener, ITransactionRecordView, ITransferView {

    @BindView(R.id.stub_transaction_fail)
    ViewStub stub_transaction_fail;
    @BindView(R.id.stub_transaction_packing)
    ViewStub stub_transaction_packing;
    @BindView(R.id.stub_transaction_success)
    ViewStub stub_transaction_success;
    @BindView(R.id.wv_bridge)
    DWebView mWvbridge;

    TextView tv_copy_txid, tv_memo, tv_copy_from, tv_copy_to, tv_block, tv_miner_fee,
            tv_balance, tv_time, tv_title, tv_url, tv_detail, tv_state, tv_sign_transaction;
    ImageView iv_qr;
    LinearLayout ll_detail;
    SimpleDateFormat mDateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm a", Locale.ENGLISH);
    SimpleDateFormat mDateFormatChinese = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA);

    private TransactionRecordPresenter mTransactionRecordPresenter;
    private TransactionBean.ListBean bean;
    private Timer mTime;//计时器
    private TimerTask mTimer;//计时器
    private TransferPresenter presenter;
    private String mTxid;
    private String mChainId;
    private String mExplorer;
    private String mNodeUrl;
    private String mJsFee;


    public static TransactionRecordFragment newInstance(Bundle args) {
        TransactionRecordFragment transactionRecordFragment = new TransactionRecordFragment();
        transactionRecordFragment.setArguments(args);
        return transactionRecordFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initToolbarNav(mToolbar, R.string.transaction_record, true);
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_transaction_record;
    }

    @Override
    public void process() {
        initWeb();
        mTransactionRecordPresenter = new TransactionRecordPresenter(this);
        presenter = new TransferPresenter(this);
        initTxid();
        presenter.getCrossChains();
        getTransData();
        if (mTimer == null && mTime == null) {
            mTime = new Timer();
            mTimer = new TimerTask() {
                @Override
                public void run() {
                    getTransData();
                }
            };
            mTime.schedule(mTimer, 3000, 3000);
        }
    }

    private void initWeb() {
        mWvbridge.loadUrl(ApiUrlConfig.AssetsUrl);
        mWvbridge.addJavascriptObject(new JsApi(new HandleCallback() {
            @Override
            public void onHandle(Object o) {
                String data = (String) o;
                Logger.d("result ===>", (String) o);
                try {
                    JsResultBean bean = new Gson().fromJson(data, JsResultBean.class);
                    if (bean.getSuccess() == 1) {
                        if(bean.getFee() != null && !bean.getFee().isEmpty()) {
                            JsResultBean.Fee fee = bean.getFee().get(0);
                            mJsFee = fee.getAmount();
                        }
                        if(!TextUtils.isEmpty(mJsFee)) {
                            tv_miner_fee.setText(mJsFee);
                        }
                    } else {
                        String err = bean.getErr();
                        DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                                .setToast(TextUtils.isEmpty(err) ? getString(R.string.transfer_fail) : err);
                    }
                }catch (JsonSyntaxException e){
                    e.printStackTrace();
                    DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                            .setToast(getString(R.string.transfer_fail)+ e.getMessage());
                    e.printStackTrace();
                }}}), null);
    }

    private void getJsFee() {
        TransferBean bean = new TransferBean();
        bean.setNodeUrl(mNodeUrl);
        bean.setTxid(mTxid);
        mWvbridge.callHandler("chainGetTxResultJS", new Gson().toJson(bean), new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
            }
        });
    }

    private void initTxid() {
        Bundle bundle = getArguments();
        bean = new Gson().fromJson(bundle.getString("bean"), TransactionBean.ListBean.class);
        String txid;
        String chainId = "";
        if (null == bean) {
            txid = getArguments().getString(Constant.BundleKey.TXID);
            chainId = getArguments().getString(Constant.BundleKey.CHAIN_ID);
            if (TextUtils.isEmpty(txid)) {
                pop();
                return;
            }
        } else {
            txid = bean.txid;
            chainId = bean.chain;
        }
        mTxid = txid;
        mChainId = chainId;
    }

    /**
     * 重新刷新获取数据
     */
    private void getTransData() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("txid", mTxid);
        jsonObject.addProperty("chainid_c", mChainId);
        mTransactionRecordPresenter.getTransactionDetail(jsonObject);

    }

    private void initData() {
        if ("-1".equals(bean.status)) {
            stub_transaction_packing.setVisibility(View.GONE);
            LinearLayout layout = (LinearLayout) stub_transaction_fail.inflate();
            initSameView(layout);

            ll_detail = layout.findViewById(R.id.ll_detail);
            tv_state = layout.findViewById(R.id.tv_state);
            tv_sign_transaction = layout.findViewById(R.id.tv_sign_transaction);
            tv_state.setText(bean.statusText);
        } else if ("0".equals(bean.status)) {
            LinearLayout layout = (LinearLayout) stub_transaction_packing.inflate();
            initSameView(layout);

            tv_state = layout.findViewById(R.id.tv_state);
            tv_block = layout.findViewById(R.id.tv_block);
            tv_url = layout.findViewById(R.id.tv_url);
            iv_qr = layout.findViewById(R.id.iv_qr);

            tv_state.setText(bean.statusText);
            tv_block.setText(bean.block + "");
            BitmapUtil.create2QR(_mActivity, iv_qr, bean.txid);

        } else {
            stub_transaction_packing.setVisibility(View.GONE);
            LinearLayout layout = (LinearLayout) stub_transaction_success.inflate();
            initSameView(layout);

            tv_title = layout.findViewById(R.id.tv_title);
            tv_title.setText(bean.statusText);
            tv_time = layout.findViewById(R.id.tv_time);

            tv_block = layout.findViewById(R.id.tv_block);
            tv_url = layout.findViewById(R.id.tv_url);
            iv_qr = layout.findViewById(R.id.iv_qr);

            String lang = CacheUtil.getInstance().getProperty(Constant.Sp.SET_LANGUAGE);

            if (TextUtils.equals(lang, LanguageUtil.LANG_CN)) {
                tv_time.setText(mDateFormatChinese.format(new Date(Long.valueOf(bean.time) * 1000)));
            } else {
                tv_time.setText(mDateFormat.format(new Date(Long.valueOf(bean.time) * 1000)));
            }

            tv_block.setText(bean.block + "");
            BitmapUtil.create2QR(_mActivity, iv_qr, bean.txid);
            if (mTime != null) {
                mTime.cancel();
                mTime = null;
            }
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }
        }
    }

    private void initSameView(LinearLayout layout) {
        tv_balance = layout.findViewById(R.id.tv_balance);
        tv_miner_fee = layout.findViewById(R.id.tv_miner_fee);
        tv_copy_to = layout.findViewById(R.id.tv_copy_to);
        tv_copy_from = layout.findViewById(R.id.tv_copy_from);
        tv_memo = layout.findViewById(R.id.tv_memo);
        tv_copy_txid = layout.findViewById(R.id.tv_copy_txid);

        tv_copy_from.setOnClickListener(this);
        tv_copy_to.setOnClickListener(this);
        tv_copy_txid.setOnClickListener(this);
        String symbol = bean.symbol;
        if (TextUtils.isEmpty(symbol)) {
            symbol = "ELF";
        }
        tv_balance.setText(bean.getAmount() + " " + symbol);
        String fee = mJsFee;
        if(TextUtils.isEmpty(fee)) {
            fee = "0.00";
        }
        tv_miner_fee.setText(fee);
        tv_copy_to.setText(StringUtil.formatAddress(bean.to, bean.to_chainid));
        tv_copy_from.setText(StringUtil.formatAddress(bean.from, bean.from_chainid));
        tv_copy_txid.setText(bean.txid);
        tv_memo.setText(bean.memo);

    }

    @Override
    public void onClick(View view) {
        String text = "";
        switch (view.getId()) {

            case R.id.tv_copy_from:
                text = bean.from;
                break;
            case R.id.tv_copy_to:
                text = bean.to;
                break;
            case R.id.tv_copy_txid:
                text = tv_copy_txid.getText().toString();
                break;
        }

        copyText(text);
    }

    @Override
    public void onTransactionDetailSuccess(TransactionBean.ListBean bean) {
        this.bean = bean;
        initData();

    }

    @Override
    public void onTransactionDetailError(int code, String msg) {

    }

    @Override
    public void onCrossChainSuccess(ResultBean resultBean) {

    }

    @Override
    public void onCrossChainError(int code, String msg) {

    }

    @Override
    public void onAddIndexSuccess(ResultBean resultBean) {

    }

    @Override
    public void onAddIndexError(int i, String message) {

    }

    @Override
    public void onTransferBalanceSuccess(ResultBean<TransferBalanceBean> listResultBean) {

    }

    @Override
    public void onTransferBalanceSuccess(ResultBean<TransferBalanceBean> bean, String chainid) {

    }

    @Override
    public void onTransferBalanceError(int i, String toString) {

    }

    @Override
    public void onConcurrentSuccess(ResultBean<List<ChainAddressBean>> resultBean) {

    }

    @Override
    public void onConcurrentError(int code, String msg) {

    }
    @Override
    public void onChainsSuccess(ResultBean<List<ChooseChainsBean>> resultBean) {

        for(ChooseChainsBean chooseChainsBean:resultBean.getData()){
            if(chooseChainsBean.getName().equalsIgnoreCase(mChainId)){
                mExplorer = chooseChainsBean.getExplorer();
                mNodeUrl = chooseChainsBean.getNode();
            }
        }
        getJsFee();
        if(!TextUtils.isEmpty(mExplorer)&&!TextUtils.isEmpty(mTxid)){
            tv_url.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    start(ExplorerFragment.newInstance(mChainId+" explorer",mExplorer+"/tx/"+mTxid));
                }
            });
            tv_url.setVisibility(View.VISIBLE);
        }else{
            tv_url.setVisibility(View.GONE);
        }


    }

    @Override
    public void onChainsError(int code, String msg) {

    }

    @Override
    public void onChainsSuccessForDapp(String id, ResultBean<List<ChooseChainsBean>> resultBean) {

    }

    @Override
    public void onChainsErrorForDapp(String id, int code, String msg) {

    }
}
