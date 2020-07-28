package com.legendwd.hyperpay.aelf.business.wallet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.assets.fragments.TransactionRecordFragment;
import com.legendwd.hyperpay.aelf.business.my.fragments.AddressBookFragment;
import com.legendwd.hyperpay.aelf.config.ApiUrlConfig;
import com.legendwd.hyperpay.aelf.dialogfragments.ChainDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.SingleButtonDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.ToastDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.TransMsgDialog;
import com.legendwd.hyperpay.aelf.listeners.HandleCallback;
import com.legendwd.hyperpay.aelf.listeners.OnTextCorrectCallback;
import com.legendwd.hyperpay.aelf.model.MessageEvent;
import com.legendwd.hyperpay.aelf.model.bean.AddressBookBean;
import com.legendwd.hyperpay.aelf.model.bean.ChainAddressBean;
import com.legendwd.hyperpay.aelf.model.bean.ChooseChainsBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.TransferBalanceBean;
import com.legendwd.hyperpay.aelf.model.bean.TransferBean;
import com.legendwd.hyperpay.aelf.model.param.TransferBalanceParam;
import com.legendwd.hyperpay.aelf.model.param.TransferCrossChainParam;
import com.legendwd.hyperpay.aelf.presenters.ITransferPresenter;
import com.legendwd.hyperpay.aelf.presenters.impl.TransferPresenter;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.aelf.util.StringUtil;
import com.legendwd.hyperpay.aelf.util.zxing.CaptureActivity;
import com.legendwd.hyperpay.aelf.views.ITransferView;
import com.legendwd.hyperpay.aelf.widget.LoadDialog;
import com.legendwd.hyperpay.aelf.widget.webview.DWebView;
import com.legendwd.hyperpay.aelf.widget.webview.JsApi;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;
import com.legendwd.hyperpay.lib.Logger;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class TransferFragment extends BaseFragment implements ITransferView, ChainDialog.ChainClick, TransMsgDialog.TransClick {
    @BindView(R.id.et_address)
    EditText et_address;
    @BindView(R.id.et_amount)
    EditText et_amount;
    @BindView(R.id.iv_contact)
    ImageView iv_contact;
    @BindView(R.id.et_note)
    EditText et_note;
    @BindView(R.id.tv_balance_value)
    TextView tv_balance_value;
    @BindView(R.id.tv_balance)
    TextView tv_balance;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.wv_bridge)
    DWebView mWvbridge;


    @BindView(R.id.linearbtn_info)
    LinearLayout linearbtnInfo;


    ToastDialog mToastDialog;
    private double mBalance;
    private double mfee;

    private ITransferPresenter presenter;

    private String mSymbol;
    private List<ChainAddressBean> mAddressList = new ArrayList<>();
    private ChainDialog mChainDialog;
    private TransMsgDialog mTransMsgDialog;
    private boolean transToChain = false; //是否是跨链
    private String fromChainId = "";
    private String toChainId;
    private List<ChooseChainsBean> allChainBean = new ArrayList<>();
    private String toAddress;//最终的address
    private LoadDialog mLoadDialog;
    private ChainAddressBean mDataBean;
    private String mFromNode;

    public static TransferFragment newInstance(Bundle bundle) {
        TransferFragment fragment = new TransferFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_transfer;
    }

    @Override
    public void process() {
        mDataBean = new Gson().fromJson(getArguments().getString("bean"), ChainAddressBean.class);

        mWvbridge.loadUrl(ApiUrlConfig.AssetsUrl);
        mWvbridge.addJavascriptObject(new JsApi(new HandleCallback() {
            @Override
            public void onHandle(Object o) {
                try {
                    Logger.d("result ===>", (String) o);
                    JSONObject jsonObject = new JSONObject((String) o);
                    int success = jsonObject.optInt("success");
                    if (success == 1) {
                        String txId = jsonObject.optString("txId");
                        String result = jsonObject.optString("params");
                        if (TextUtils.isEmpty(result)) {
                            checkTxid(txId);
                        } else {
                            dismissDialog();
                            if (!transToChain) {
                                Bundle bundle = new Bundle();
                                bundle.putString(Constant.BundleKey.TXID, txId);
                                bundle.putString(Constant.BundleKey.CHAIN_ID, mDataBean.getChain_id());
                                startWithPop(TransactionRecordFragment.newInstance(bundle));
                                EventBus.getDefault().post(new MessageEvent(Constant.Event.REFRSH_TRANSATION));
                            } else {
                                Bundle bundle = new Bundle();
                                TransferCrossChainParam transferCrossChainParam = new TransferCrossChainParam();
                                transferCrossChainParam.txid = txId;
                                transferCrossChainParam.from_chain = fromChainId;
                                transferCrossChainParam.to_chain = toChainId;
                                transferCrossChainParam.from_address = CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS);
                                transferCrossChainParam.to_address = toAddress;
                                //测试TELF
                                transferCrossChainParam.symbol = mSymbol;
                                transferCrossChainParam.amount = et_amount.getText().toString();
                                transferCrossChainParam.memo = et_note.getText().toString();
                                transferCrossChainParam.chainid_c = fromChainId;
                                transferCrossChainParam.fee = mfee + "";
                                ChooseChainsBean bean = getContractAddress(toChainId);
                                transferCrossChainParam.toContractAddress = bean.getContract_address();
                                transferCrossChainParam.toMianSymbol = bean.getSymbol();
                                bundle.putString(Constant.BundleKey.TransData, new Gson().toJson(transferCrossChainParam));
                                startWithPop(TransProofFragment.newInstance(bundle));
                            }
                        }
                    } else {
                        dismissDialog();
                        String err = jsonObject.optString("err");
                        DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                                .setToast(TextUtils.isEmpty(err) ? getString(R.string.transfer_fail) : err);
                    }
                } catch (JSONException e) {
                    dismissDialog();
                    DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                            .setToast(getString(R.string.transfer_fail) + e.getMessage());
                    e.printStackTrace();
                }
            }
        }), null);


        presenter = new TransferPresenter(this);

        initToolbarNav(mToolbar, "", true);

        mSymbol = mDataBean.getSymbol();
        tv_title.setText(mDataBean.getChain_id() + " " + mDataBean.getSymbol() + " " + getString(R.string.transfer));
        TextView tvRight = mToolbar.findViewById(R.id.tv_title_right);
        tvRight.setBackgroundResource(R.mipmap.scan);

        showCoinBalance(mDataBean.getBalance());

        tv_balance.setText(mDataBean.getChain_id() + " " + mDataBean.getSymbol() + " " + getString(R.string.balance));

        TransferBalanceParam param = new TransferBalanceParam();
        param.address = CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS);
        param.contractAddress = mDataBean.getContractAddress();
        param.symbol = mDataBean.getSymbol();
        param.chainid = mDataBean.getChain_id();
        presenter.getTransferBalance(param);

        presenter.getCrossChains();

        presenter.getConcurrentAddress();

        mLoadDialog = new LoadDialog(getContext(), false);

        linearbtnInfo.setOnClickListener(v -> DialogUtils.showDialog(SingleButtonDialog.class, getFragmentManager())
                .setTitle(getString(R.string.transferfragment_tip01))
                .setMessage(getString(R.string.transferfragment_tip02)));
    }

    private void dismissDialog() {
        if (mToastDialog != null) {
            mToastDialog.dismiss();
        }
        if (mLoadDialog != null && mLoadDialog.isShowing()) {
            mLoadDialog.dismiss();
        }
    }

    private void checkTxid(String txId) {
        tv_balance.postDelayed(new Runnable() {
            @Override
            public void run() {
                TransferBean bean = new TransferBean();
                bean.setNodeUrl(mFromNode);
                bean.setTxid(txId);
                mWvbridge.callHandler("chainGetTxResultJS", new Gson().toJson(bean), new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                    }
                });
            }
        }, 500);
    }

    @OnClick({R.id.tv_title_right, R.id.iv_contact})
    void clickView(View view) {
//        start(ScanFragment.newInstance());

        switch (view.getId()) {
            case R.id.iv_contact:
                hideSoftInput();
                Bundle bundle = new Bundle();
                bundle.putString(Constant.BundleKey.ADDRESS_BOOK, Constant.BundleValue.TRANSFER_ADDRESS_BOOK);
                startForResult(AddressBookFragment.newInstance(bundle), Constant.RequestCode.CODE_CHOOSE_CONTACT);
                break;

            case R.id.tv_title_right:
                hideSoftInput();
                Intent intent = new Intent(_mActivity, CaptureActivity.class);
                intent.putExtra(Constant.IntentKey.Scan_Zxing, Constant.IntentValue.SCAN_TRANSFER);
                startActivityForResult(intent, Constant.RequestCode.CODE_SCAN_ZXING);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.RequestCode.CODE_SCAN_ZXING) {
                String scan = data.getStringExtra(Constant.IntentKey.Scan_Zxing);
                et_address.setText(TextUtils.isEmpty(scan) ? "" : scan);
            }
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.RequestCode.CODE_CHOOSE_CONTACT && null != data) {
                AddressBookBean.ListBean bean = new Gson().fromJson(data.getString("bean"), AddressBookBean.ListBean.class);

                if (null != bean) {
                    et_note.setText(bean.note);
                    et_address.setText(bean.address);
                }
            }
        }
    }

    @Override
    public void onTransferBalanceSuccess(ResultBean<TransferBalanceBean> bean, String chainid) {
        if (mLoadDialog != null && mLoadDialog.isShowing()) {
            mLoadDialog.dismiss();
        }
        if (200 == bean.getStatus()) {
            TransferBalanceBean balanceBean = bean.getData();
            if (transToChain && toChainId.equals(chainid)) {
                checkToChainBalance(balanceBean);
            } else {
                showCoinBalance(balanceBean.balance.getBalance());
                mBalance = Double.valueOf(balanceBean.balance.getBalance());
                if (balanceBean.fee != null) {
                    mfee = Double.valueOf(balanceBean.fee.get(0).fee);
                }
            }
        }
    }

    private void checkToChainBalance(TransferBalanceBean balanceBean) {
        double balance = Double.valueOf(balanceBean.balance.getBalance());
        double fee = 0;
        if (balanceBean.fee != null) {
            fee = Double.valueOf(balanceBean.fee.get(0).fee);
        }
        if (balance < fee) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                    .setToast(getString(R.string.transfer_not_enough, toChainId, mSymbol));
        } else {
            showTransDialog();
        }
    }

    private void showCoinBalance(String balance) {

        boolean isMode = CacheUtil.getInstance().getProperty(Constant.Sp.PRIVATE_MODE, false);
        if (isMode) {
            tv_balance_value.setText("****");
        } else {
            tv_balance_value.setText(balance + " " + mSymbol);
        }
    }

    void transfer(String key) {
        if (mToastDialog == null) {
            mToastDialog = DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                    .setToast(R.string.loading)
                    .setLoading(true)
                    .setToastCancelable(false);
        } else {
            mToastDialog.setToast(R.string.loading);
            mToastDialog.show(getFragmentManager(), "ToastDialog");
        }
        String senNode = "";
        String receiveNode = "";
        if (allChainBean == null) {
            return;
        }
        ChooseChainsBean fromBean = null;
        ChooseChainsBean toBean = null;
        ChooseChainsBean symbolBean = null;
        for (ChooseChainsBean chooseChainsBean : allChainBean) {
            if (fromChainId.toLowerCase().equals(chooseChainsBean.getName().toLowerCase())) {
                senNode = chooseChainsBean.getNode();
                fromBean = chooseChainsBean;
            }
            if (toChainId.toLowerCase().equals(chooseChainsBean.getName().toLowerCase())) {
                receiveNode = chooseChainsBean.getNode();
                toBean = chooseChainsBean;
            }
        }
        if (mAddressList.isEmpty()) {
            return;
        }
        if (mLoadDialog != null && !mLoadDialog.isShowing()) {
            mLoadDialog.show();
        }
        if (!transToChain) {
            TransferBean bean = new TransferBean();
            bean.setPrivateKey(key);
            bean.setAmount(StringUtil.multiplyDataString(mDataBean.getDecimals(), et_amount.getText().toString()));
            bean.setToAddress(toAddress);
            bean.setSymbol(mSymbol);
            bean.setMemo(et_note.getText().toString());
            String send;
            if (senNode.endsWith("/")) {
                send = senNode.substring(0, senNode.length() - 1);
            } else {
                send = senNode;
            }
            bean.setNodeUrl(send);
            if (mDataBean != null) {
                //25CecrU94dmMdbhC3LWMKxtoaL4Wv8PChGvVJM6PxkHAyvXEhB
                bean.setContractAt(mDataBean.getContractAddress());
            }
            mFromNode = bean.getNodeUrl();
            mWvbridge.callHandler("transferElfJS", new Gson().toJson(bean), new CallBackFunction() {

                @Override
                public void onCallBack(String data) {
                }
            });
        } else {
            TransferBean bean = new TransferBean();
            bean.setPrivateKey(key);
            bean.setAmount(StringUtil.multiplyDataString(mDataBean.getDecimals(), et_amount.getText().toString()));
            bean.setToAddress(toAddress);
            bean.setSymbol(mSymbol);
            String sendUrl;
            if (!TextUtils.isEmpty(senNode) && senNode.endsWith("/")) {
                sendUrl = senNode.substring(0, senNode.length() - 1);
            } else {
                sendUrl = senNode;
            }
            String receiveUrl;
            if (!TextUtils.isEmpty(receiveNode) && receiveNode.endsWith("/")) {
                receiveUrl = receiveNode.substring(0, receiveNode.length() - 1);
            } else {
                receiveUrl = receiveNode;
            }
            bean.setFromNode(sendUrl);
            bean.setToNode(receiveUrl);
            bean.setMainChainId(mAddressList.get(0).getChain_id());
            bean.setIssueChainId(mAddressList.get(0).getIssue_chain_id());
            bean.setMemo(et_note.getText().toString());
            bean.setFromChain(fromBean.getName());
            bean.setFromTokenContractAddres(fromBean.getContract_address());
            bean.setFromChainContractAddres(fromBean.getCrossChainContractAddress());
            bean.setToChain(toBean.getName());
            bean.setToTokenContractAddres(toBean.getContract_address());
            bean.setToChainContractAddres(toBean.getCrossChainContractAddress());
            mFromNode = bean.getFromNode();
            mWvbridge.callHandler("transferCrossChainJS", new Gson().toJson(bean), new CallBackFunction() {

                @Override
                public void onCallBack(String data) {
                }
            });
        }
    }

    @OnClick(R.id.tv_next)
    void onClickNext() {
        checkData();
    }

    /**
     * 效验数据
     */
    private void checkData() {
        try {
            String amount = et_amount.getText().toString().trim();
            if (TextUtils.isEmpty(amount)) {
                DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                        .setToast(R.string.please_enter_transfer_amount);
                return;
            }

            if (Double.valueOf(amount) == 0) {
                DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                        .setToast(R.string.please_enter_transfer_amount);
                return;
            }
            double amountValue = StringUtil.multiplyDataDouble(mDataBean.getDecimals(), amount);

            if (amountValue < 1) {
                DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                        .setToast(R.string.transfer_value_low);
                return;
            }

            if (mBalance == 0 || mBalance < (Double.valueOf(et_amount.getText().toString()) + mfee)) {
                DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                        .setToast(getString(R.string.transfer_not_enough, mDataBean.getChain_id(), mSymbol));
                return;
            }
            String etAddress = et_address.getText().toString();

            if (etAddress.length() == 0) {
                DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                        .setToast(R.string.please_enter_transfer_address);
                return;
            }
            String newAddress = "";
            if (etAddress.contains("_") && etAddress.length() > 1) {
                String str = etAddress.substring(etAddress.indexOf("_") + 1);
                if (str.contains("_")) {
                    String str2 = str.substring(0, str.indexOf("_"));
                    newAddress = str2;
                    toAddress = str2;
                }
            } else {
                newAddress = etAddress;
            }
            if (!StringUtil.isAELFAddress(newAddress)) {
                DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                        .setToast(R.string.not_elf_address);
                return;
            }

            String address = CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS);
            if (TextUtils.equals(etAddress, address)) {
                DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                        .setToast(R.string.same_addr_from_to);
                return;
            }
            fromChainId = mDataBean.getChain_id();
            if (!TextUtils.isEmpty(etAddress) && etAddress.contains("_")) {
                String addr1 = etAddress.substring(etAddress.indexOf("_") + 1);
                if (addr1.contains("_")) {
                    toChainId = addr1.substring(addr1.indexOf("_") + 1);
                    if (fromChainId.toLowerCase().equals(toChainId.toLowerCase())) { //chainID 相同则链相同
                        transToChain = false;
                    } else {
                        transToChain = true;
                    }
                }
            } else {
                mChainDialog = new ChainDialog();
                mChainDialog.show(getFragmentManager(), "ToastDialog");
                mChainDialog.setChainData(allChainBean);
                mChainDialog.setClock(this::clickItem);
                return;
            }
            if (transToChain) {
                if (isValileChain()) {
                    postBalance(toChainId);
                } else {
                    DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                            .setToast(getString(R.string.transfer_chain_tip1, fromChainId, toChainId, mSymbol));
                }
            } else {
                showPasswordDialogForPrivate(new OnTextCorrectCallback() {
                    @Override
                    public void onTextCorrect(Object... obj) {
                        String key = (String) obj[1];
                        transfer(key);
                    }
                });
            }
        } catch (Exception e) {
            Logger.d("====TransferFragment", e.getMessage() + "");
        }
    }

    private boolean isValileChain() {
        ChooseChainsBean bean = getContractAddress(toChainId);
        if (bean == null) {
            return false;
        }
        String tag = bean.getTransferCoins();
        if (TextUtils.isEmpty(tag)) {
            return false;
        }
        if ("*".equals(tag)) {
            return true;
        }
        String[] values = tag.split(",");
        for (String key : values) {
            if (mSymbol.equals(key)) {
                return true;
            }
        }
        return false;
    }

    private void postBalance(String chainId) {
        ChooseChainsBean bean = getContractAddress(chainId);
        if (bean == null) {
            return;
        }
        mLoadDialog.show();
        TransferBalanceParam param = new TransferBalanceParam();
        param.address = CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS);
        param.contractAddress = bean.getContract_address();
        param.symbol = bean.getSymbol();
        param.chainid = chainId;
        presenter.getTransferBalance(param);
    }

    private ChooseChainsBean getContractAddress(String chainId) {
        for (ChooseChainsBean bean : allChainBean) {
            if (chainId.equals(bean.getName())) {
                return bean;
            }
        }
        DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                .setToast(getString(R.string.transfer_chain_tip, chainId));
        return null;
    }

    private void showTransDialog() {
        mTransMsgDialog = new TransMsgDialog();
        mTransMsgDialog.show(getFragmentManager(), "ToastDialog");
        mTransMsgDialog.setChainData(fromChainId, toChainId);
        mTransMsgDialog.setClock(this::trans);
    }


    @Override
    public void onTransferBalanceError(int code, String msg) {
        if (mLoadDialog != null && mLoadDialog.isShowing()) {
            mLoadDialog.dismiss();
        }
    }

    @Override
    public void onConcurrentSuccess(ResultBean<List<ChainAddressBean>> resultBean) {
        if (resultBean.getStatus() == 200) {
            List<ChainAddressBean> list = resultBean.getData();
            mAddressList.clear();
            for (ChainAddressBean bean : list) {
                if (bean.getSymbol().equals(mDataBean.getSymbol())) {
                    mAddressList.add(bean);
                }
            }
        }
    }

    @Override
    public void onConcurrentError(int code, String msg) {

    }

    @Override
    public void onChainsSuccess(ResultBean<List<ChooseChainsBean>> resultBean) {
        if (resultBean.getStatus() == 200) {
            this.allChainBean = resultBean.getData();
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

    @Override
    public void clickItem(ChooseChainsBean itemData) {
        if (itemData == null) {
            return;
        }
        String edit_address = et_address.getText().toString();
        String address = Constant.DEFAULT_PREFIX + edit_address + "_" + itemData.getName();
        et_address.setText(address);
        checkData();
    }

    @Override
    public void trans() {
        showPasswordDialogForPrivate(new OnTextCorrectCallback() {
            @Override
            public void onTextCorrect(Object... obj) {
                String key = (String) obj[1];
                transfer(key);
            }
        });
    }
}
