package com.legendwd.hyperpay.aelf.business.assets.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingending.popuplayout.PopupLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.gyf.immersionbar.ImmersionBar;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseAdapterModel;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.assets.adapters.AssetsAdapter;
import com.legendwd.hyperpay.aelf.business.wallet.adapters.ChooseChainAdapter;
import com.legendwd.hyperpay.aelf.business.wallet.fragments.ChooseChainFragment;
import com.legendwd.hyperpay.aelf.business.wallet.fragments.TransferReceiveFragment;
import com.legendwd.hyperpay.aelf.business.wallet.fragments.WaitTransferFragment;
import com.legendwd.hyperpay.aelf.db.MarketCoindb;
import com.legendwd.hyperpay.aelf.db.dao.MarketCoinDao;
import com.legendwd.hyperpay.aelf.dialogfragments.ConfirmDialog;
import com.legendwd.hyperpay.aelf.listeners.HandleCallback;
import com.legendwd.hyperpay.aelf.listeners.OnItemClickListener;
import com.legendwd.hyperpay.aelf.model.MessageEvent;
import com.legendwd.hyperpay.aelf.model.bean.AssetsListBean;
import com.legendwd.hyperpay.aelf.model.bean.ChainAddressBean;
import com.legendwd.hyperpay.aelf.model.bean.CurrenciesBean;
import com.legendwd.hyperpay.aelf.model.bean.MarketDataBean;
import com.legendwd.hyperpay.aelf.model.bean.PublicMessageBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.UnreadBean;
import com.legendwd.hyperpay.aelf.model.bean.WaitTransactionBean;
import com.legendwd.hyperpay.aelf.model.param.AddressParam;
import com.legendwd.hyperpay.aelf.model.param.BaseParam;
import com.legendwd.hyperpay.aelf.model.param.MarketParam;
import com.legendwd.hyperpay.aelf.presenters.impl.AssetsPresenter;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.aelf.util.StringUtil;
import com.legendwd.hyperpay.aelf.views.IAssetsView;
import com.legendwd.hyperpay.aelf.widget.ClassicsFooter;
import com.legendwd.hyperpay.aelf.widget.LoadDialog;
import com.legendwd.hyperpay.aelf.widget.NoticeTextView;
import com.legendwd.hyperpay.httputil.ServiceGenerator;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AssetsFragment extends BaseFragment implements IAssetsView {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.ntv_notice)
    NoticeTextView noticeTextView;
    @BindView(R.id.img_bell)
    ImageView img_bell;
    @BindView(R.id.tv_un_read)
    TextView tvUnRead;
    @BindView(R.id.tv_address)
    TextView tv_address;

    @BindView(R.id.tv_assets)
    TextView tvAssets;
    @BindView(R.id.tv_main_title)
    TextView tv_main_title;
    private AssetsPresenter mAssetsPresenter;
    @BindView(R.id.tv_main_check)
    TextView tv_main_check;
    @BindView(R.id.iv_scan_code)
    ImageView iv_scan_code;

    private AssetsAdapter mAdapter;
    //true按资产 false按链
    private boolean mBoolAssets;
    private View view;
    private SmartRefreshLayout refresh_choose_search;
    private RecyclerView rv_choose_search;
    private EditText choose_et_search;
    private ChooseChainAdapter mChooseChainAdapter; //popup adapter
    private List<ChainAddressBean> mDataList = new ArrayList<>();
    private List<ChainAddressBean> mPopList = new ArrayList<>();
    private List<ChainAddressBean> serachData = new ArrayList<>();
    private ChainAddressBean chainAddressBean;
    private PopupLayout mPopupLayout;
    private TextView mOnchainMoneyTv;
    private TextView mOnchainCompanyTv;
    private TextView mPopupChainTitleTv;
    private LoadDialog mLoadDialog;

    public static AssetsFragment newInstance() {
        Bundle args = new Bundle();
        AssetsFragment assetsFragment = new AssetsFragment();
        assetsFragment.setArguments(args);
        return assetsFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = getContext();
        if (context == null) {
            return;
        }
        popupInitData();
        mLoadDialog = new LoadDialog(context, false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_assets;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void process() {
        mAssetsPresenter = new AssetsPresenter(this);
        JsonObject jsonObject = new JsonObject();
        mAssetsPresenter.getPublicMessage(jsonObject);
//        mAssetsPresenter.getCurrencies(new BaseParam());
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getChainId();
                getUnreadCount();
                mAssetsPresenter.getWaitTransList(CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS));
                refreshAsset();
            }
        });
        initAdapter();
        tv_address.setText(CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_COIN_ADDRESS));
        mBoolAssets = CacheUtil.getInstance().getProperty(Constant.Sp.ASSETS_DISPLAY_INT, 0) == 0;
        referUi();
        mAssetsPresenter.getCurrencies(new BaseParam());
    }

    private void refreshAsset() {
        if (mBoolAssets) {
            mAssetsPresenter.getConcurrentAddress(mBoolAssets);
        } else {
            mAssetsPresenter.getAssetsList(CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS));
        }
    }


    /**
     * 刷新UI
     */
    private void referUi() {
        //按资产
        if (mBoolAssets) {
            tv_address.setText(CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_COIN_ADDRESS));
            tv_main_check.setVisibility(View.GONE);
            tv_main_title.setVisibility(View.VISIBLE);
        } else {
            tv_main_check.setVisibility(View.VISIBLE);
            tv_main_title.setVisibility(View.GONE);
            tv_address.setText(CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_COIN_ADDRESS));

            String json = CacheUtil.getInstance().getProperty(Constant.Sp.By_Chain_Data);
            if (!TextUtils.isEmpty(json)) {
                chainAddressBean = new Gson().fromJson(json, ChainAddressBean.class);
                if (chainAddressBean != null && !TextUtils.isEmpty(chainAddressBean.getSymbol())) {
                    tv_main_check.setText(getString(R.string.current_chain, chainAddressBean.getChain_id()));
                }
            } else {
                if (mPopList == null || mPopList.size() == 0) {
                    tv_main_check.setText(getString(R.string.current_chain, ServiceGenerator.getChainId()));

                    mAssetsPresenter.getAssetsList(CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_COIN_ADDRESS));
                    return;
                }
                chainAddressBean = mPopList.get(0);
            }
            if (chainAddressBean == null || mDataList == null || mDataList.size() == 0) {
                return;
            }
            CacheUtil.getInstance().setProperty(Constant.Sp.CURRENT_CHAIN_ID, chainAddressBean.getChain_id());
        }
        showUiData();
    }

    private void getUnreadCount() {
        AddressParam param = new AddressParam();
        param.address = CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS);
        mAssetsPresenter.getUnreadCount(param);
    }

    @Override
    public void initImmersionBar() {
        ImmersionBar.with(this).keyboardEnable(true)
                .navigationBarColorInt(Color.WHITE)
                .statusBarDarkFont(true, 0.2f)
                .autoDarkModeEnable(true, 0.2f).init();
    }

    @OnClick(R.id.rl_add)
    void onClickAdd() {
        startBrotherFragment(AssetsManagementFragment.newInstance());
    }

    @OnClick({R.id.rl_notification, R.id.tv_address, R.id.tv_main_title, R.id.tv_main_check, R.id.iv_scan_code})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.rl_notification:
                startBrotherFragment(NotificationsFragment.newInstance());
                break;

            case R.id.tv_address:
                copyText(tv_address.getText().toString());
                break;
            case R.id.tv_main_check:
                showPopWidnow();
                break;
            case R.id.iv_scan_code:
                startBrotherFragment(WaitTransferFragment.newInstance(new Bundle()));
                break;
            default:
                break;
        }
    }

    private void showPopWidnow() {
        //从上侧弹出
        if (mPopupLayout != null) {
            mPopupLayout.show(PopupLayout.POSITION_TOP);
            refresh_choose_search.autoRefresh();
            updatePopData();
        }
    }

    /**
     * PopupLayout 数据塞入
     */
    private void popupInitData() {
        Context context = getContext();
        if (context == null) {
            return;
        }
        view = View.inflate(context, R.layout.choose_popup_chain, null);
        refresh_choose_search = view.findViewById(R.id.refresh_choose_search);
        rv_choose_search = view.findViewById(R.id.rv_choose_search);
        choose_et_search = view.findViewById(R.id.choose_et_search);
        mPopupChainTitleTv = view.findViewById(R.id.iv_popup_chain_title);
        mOnchainMoneyTv = view.findViewById(R.id.tx_onchain_money);
        mOnchainCompanyTv = view.findViewById(R.id.tx_onchain_company);
        refresh_choose_search.setRefreshFooter(new ClassicsFooter(_mActivity));
        refresh_choose_search.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mAssetsPresenter.getConcurrentAddress(mBoolAssets);
            }
        });

        rv_choose_search.setLayoutManager(new LinearLayoutManager(_mActivity));
        mChooseChainAdapter = new ChooseChainAdapter(mPopList);
        rv_choose_search.setAdapter(mChooseChainAdapter);
        choose_et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchStr = choose_et_search.getText().toString();
                    if (!TextUtils.isEmpty(searchStr)) {
                        hideKeyboard();
                        search(choose_et_search);
                    }
                    return true;
                }
                return false;
            }
        });

        choose_et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(choose_et_search);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mPopupLayout = PopupLayout.init(getActivity(), view);
        mPopupChainTitleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupLayout.dismiss();
            }
        });
        mChooseChainAdapter.setOnItemClick(o -> {
            hideSoftInput();
            if (o instanceof ChainAddressBean) {
                ChainAddressBean data = (ChainAddressBean) o;
                //存储链信息
                String wallAddress = CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS);
                String coinAddress = Constant.DEFAULT_PREFIX + wallAddress + "_" + data.getChain_id();
                CacheUtil.getInstance().setProperty(Constant.Sp.WALLET_COIN_ADDRESS, coinAddress);
                CacheUtil.getInstance().setProperty(Constant.Sp.By_Chain_Data, new Gson().toJson(data));
                CacheUtil.getInstance().setProperty(Constant.Sp.CURRENT_CHAIN_ID, data.getChain_id());
                mPopupLayout.dismiss();
                chainAddressBean = data;
                mAssetsPresenter.getAssetsList(coinAddress);
                if (mLoadDialog == null) {
                    mLoadDialog = new LoadDialog(context, false);
                }
                mLoadDialog.show();
            }
        });
    }

    private void updatePopData() {
        if (chainAddressBean == null) {
            return;
        }

        boolean isMode = CacheUtil.getInstance().getProperty(Constant.Sp.PRIVATE_MODE, false);
        if (isMode) {
            mOnchainMoneyTv.setText("****");
            mOnchainCompanyTv.setText("****");
        } else {
            mOnchainMoneyTv.setText(chainAddressBean.getBalance());
            double money = Double.parseDouble(chainAddressBean.getBalance()) * Double.parseDouble(chainAddressBean.getRate().getPrice());
            String currency = CacheUtil.getInstance().getProperty(Constant.Sp.PRICING_CURRENCY_ID_DEFAULT, "CNY");
            String moneyString = StringUtil.formatDataNoZero(8, money);
            mOnchainCompanyTv.setText(moneyString + " " + currency);
        }
        mPopupChainTitleTv.setText(getString(R.string.current_chain, chainAddressBean.getChain_id()));
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        refresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh.autoRefresh();
            }
        }, 200);
    }

    /**
     * 展示UI Data
     */
    private void showUiData() {
        boolean isMode = CacheUtil.getInstance().getProperty(Constant.Sp.PRIVATE_MODE, false);
        if (isMode) {
            tvAssets.setText("****");
        } else {
            double total = 0;
            for (ChainAddressBean bean : mDataList) {
                if (bean.getItemType() != BaseAdapterModel.ItemType.EMPTY) {
                    total += Double.parseDouble(bean.getBalance()) * Double.parseDouble(bean.getRate().getPrice());
                }
            }
            String pre = StringUtil.formatDataNoZero(2, total) + " ";
            SpannableString price = new SpannableString(pre + CacheUtil.getInstance().getProperty(Constant.Sp.PRICING_CURRENCY_ID_DEFAULT));
            RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(0.5f);
            price.setSpan(relativeSizeSpan, pre.length(), price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvAssets.setText(price);
        }

        String unReadCount = CacheUtil.getInstance().getProperty(Constant.Sp.UNREAD_COUNT, "0");
        int unRead = Integer.parseInt(TextUtils.isEmpty(unReadCount) ? "0" : unReadCount);
        if (unRead <= 0) {
            tvUnRead.setVisibility(View.GONE);
        } else {
            tvUnRead.setVisibility(View.VISIBLE);
            tvUnRead.setText(String.valueOf(unRead));
        }
    }

    @Override
    protected boolean needTransparentStatus() {
        return true;
    }


    private void initNotice(List<PublicMessageBean.ListBean> list) {
        ArrayList<String> titleList = new ArrayList<>();
        for (PublicMessageBean.ListBean bean : list) {
            titleList.add(bean.getDesc());
        }
        noticeTextView.setTextList(titleList);
        noticeTextView.setText(14, 5, Color.GRAY);//设置属性
        noticeTextView.setTextStillTime(3000);//设置停留时长间隔
        noticeTextView.setAnimTime(300);//设置进入和退出的时间间隔
        noticeTextView.setOnItemClickListener(new NoticeTextView.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
        noticeTextView.startAutoScroll();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        noticeTextView.remove();
    }

    @Override
    protected boolean enableSwipeBack() {
        return false;
    }


    private void initAdapter() {
        mAdapter = new AssetsAdapter(mDataList, new HandleCallback() {
            @Override
            public void onHandle(Object o) {
                if (o == null) {
                    refresh.autoRefresh();
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Object o) {
                if (o instanceof ChainAddressBean) {
                    Bundle bundle = new Bundle();
                    bundle.putString("bean", new Gson().toJson(o));
                    if (mBoolAssets) {
                        startBrotherFragment(ChooseChainFragment.newInstance(bundle));
                    } else {
                        startBrotherFragment(TransferReceiveFragment.newInstance(bundle));
                    }
                }
            }
        });
    }

    @Override
    public void onAssetsSuccess(ResultBean<AssetsListBean> resultBean) {
        refresh.finishRefresh();
        if (mLoadDialog != null && mLoadDialog.isShowing()) {
            mLoadDialog.dismiss();
        }

        if (200 == resultBean.getStatus() && resultBean.getData() != null) {
            AssetsListBean assetsListBean = resultBean.getData();
            List<ChainAddressBean> list = assetsListBean.getList();
            for (ChainAddressBean bean : list) {
                if ("main".equals(bean.getType())) {
                    chainAddressBean = bean;
                    break;
                }
            }
            updateAssets(list);
        } else {
            updateNoneData();
        }
    }

    private void updateNoneData() {
        mDataList.clear();
        mAdapter.update(mDataList);
        tv_address.setText(CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_COIN_ADDRESS));
        String pre = "0.00";
        SpannableString price = new SpannableString(pre + CacheUtil.getInstance().getProperty(Constant.Sp.PRICING_CURRENCY_ID_DEFAULT));
        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(0.5f);
        price.setSpan(relativeSizeSpan, pre.length(), price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvAssets.setText(price);
    }

    private void updateAssets(List<ChainAddressBean> list) {
        mDataList.clear();
        mDataList.addAll(list);
        Comparator<ChainAddressBean> mAssetComparator = (o1, o2) -> {
            if (Double.parseDouble(o1.getBalance()) - Double.parseDouble(o2.getBalance()) > 0) {
                return -1;
            } else if (Double.parseDouble(o1.getBalance()) - Double.parseDouble(o2.getBalance()) < 0) {
                return 1;
            }
            return 0;
        };
        Collections.sort(mDataList, mAssetComparator);
        List<String> symbelList = new ArrayList<>();
        for (ChainAddressBean bean : mDataList) {
            symbelList.add(bean.getSymbol().toLowerCase());
        }
        List<MarketCoindb> coindbList = MarketCoinDao.queryData(symbelList);
        if (coindbList.isEmpty()) {
            mAdapter.update(mDataList);
            referUi();
        } else {
            StringBuilder builder = new StringBuilder();
            for (MarketCoindb db : coindbList) {
                builder.append(db.getId());
                builder.append(",");
            }
            builder.deleteCharAt(builder.length() - 1);
            getMarketData(builder.toString(), "");
        }
    }

    private void getMarketData(String name, String type) {
        MarketParam param = new MarketParam();
        param.currency = CacheUtil.getInstance().getProperty(Constant.Sp.PRICING_CURRENCY_ID_DEFAULT, Constant.DEFAULT_CURRENCY);
        param.time = "1";
        param.p = "1";
        //关键字搜索
        param.coinName = name;
        mAssetsPresenter.getCoinList(param, type);
    }

    @Override
    public void onAssetsError(int code, String msg) {
        if (mLoadDialog != null && mLoadDialog.isShowing()) {
            mLoadDialog.dismiss();
        }
        refresh.finishRefresh();
        boolean isMode = CacheUtil.getInstance().getProperty(Constant.Sp.PRIVATE_MODE, false);
        if (isMode) {
            tvAssets.setText("****");
        } else {

        }
    }


    @Override
    public void onCurrencySuccess(ResultBean<CurrenciesBean> resultBean) {
        if (200 == resultBean.getStatus()) {
            CacheUtil.getInstance().setProperty(Constant.Sp.PRICING_CURRENCY, new Gson().toJson(resultBean.getData()));

            setCurrencyDefault(resultBean.getData().list);
        }
    }

    private void setCurrencyDefault(List<CurrenciesBean.ListBean> beanList) {
        if (null == beanList || beanList.isEmpty()) {
            return;
        }
        String json = CacheUtil.getInstance().getProperty(Constant.Sp.PRICING_CURRENCY_DEFAULT);
        if (TextUtils.isEmpty(json)) {
            beanList.get(0).isSelected = true;
            CacheUtil.getInstance().setProperty(Constant.Sp.PRICING_CURRENCY_DEFAULT, new Gson().toJson(beanList.get(0)));
            CacheUtil.getInstance().setProperty(Constant.Sp.PRICING_CURRENCY_SYMBOL_DEFAULT, beanList.get(0).symbol);
            CacheUtil.getInstance().setProperty(Constant.Sp.PRICING_CURRENCY_ID_DEFAULT, beanList.get(0).id);
            return;
        }

        CurrenciesBean.ListBean listBean = new Gson().fromJson(json, CurrenciesBean.ListBean.class);
        if (null != listBean) {
            for (CurrenciesBean.ListBean bean : beanList) {

                if (TextUtils.equals(listBean.id, bean.id)) {
                    bean.isSelected = true;
                    CacheUtil.getInstance().setProperty(Constant.Sp.PRICING_CURRENCY_DEFAULT, new Gson().toJson(bean));
                } else {
                    bean.isSelected = false;
                }
            }
        }


    }

    @Override
    public void onCurrencyError(int code, String msg) {

    }

    @Override
    public void onUnreadSuccess(ResultBean<UnreadBean> resultBean) {
        if (200 == resultBean.getStatus()) {
            if (null == resultBean.getData().unread_count || "0".equals(resultBean.getData().unread_count)) {
                tvUnRead.setVisibility(View.GONE);
                CacheUtil.getInstance().setProperty(Constant.Sp.UNREAD_COUNT, "0");
            } else {
                tvUnRead.setVisibility(View.VISIBLE);
                tvUnRead.setText(resultBean.getData().unread_count);
                CacheUtil.getInstance().setProperty(Constant.Sp.UNREAD_COUNT, resultBean.getData().unread_count);
            }

        }
    }

    @Override
    public void onUnreadError(int code, String msg) {

    }

    @Override
    public void onPublicMessageSuccess(ResultBean<PublicMessageBean> resultBean) {
        if (resultBean != null && resultBean.getData() != null) {
            initNotice(resultBean.getData().getList());
        }
    }

    @Override
    public void onPublicMessageError(int code, String msg) {

    }

    @Override
    public void onConcurrentSuccess(ResultBean<List<ChainAddressBean>> resultBean) {
        if (mBoolAssets) {
            refresh.finishRefresh();
        } else {
            refresh_choose_search.finishRefresh();
        }
        if (resultBean.getStatus() == 200) {
            List<ChainAddressBean> list = resultBean.getData();
            if (mBoolAssets) {
                updateAssets(list);
            } else {
                mPopList.clear();
                for (ChainAddressBean bean : list) {
                    if ("main".equals(bean.getType())) {
                        mPopList.add(bean);
                    }
                }
                mChooseChainAdapter.refreshView(mPopList);
            }

        }
    }

    @Override
    public void onConcurrentError(int code, String msg) {
        if (mBoolAssets) {
            refresh.finishRefresh();
        } else {
            refresh_choose_search.finishRefresh();
        }
    }

    @Override
    public void onWaitCrossTransSuccess(ResultBean<WaitTransactionBean> resultBean) {
        if (resultBean.getStatus() == 200) {
            WaitTransactionBean bean = resultBean.getData();
            if (bean.list != null && !bean.list.isEmpty()) {
                DialogUtils.showDialog(ConfirmDialog.class, getFragmentManager())
                        .setDialogTitle(String.format(getString(R.string.confirm_title), bean.list.size()))
                        .setConfirmText(getString(R.string.confirm))
                        .setHandleCallback(new HandleCallback() {
                            @Override
                            public void onHandle(Object o) {
                                if ((boolean) o) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("bean", new Gson().toJson(bean));
                                    startBrotherFragment(WaitTransferFragment.newInstance(bundle));
                                }
                            }
                        });
            }
        }
    }

    @Override
    public void onWaitCrossTransError(int i, String message) {

    }

    @Override
    public void onCoinListSuccess(List<MarketDataBean> marketListBeanResultBean, String type) {
        for (MarketDataBean bean : marketListBeanResultBean) {
            for (ChainAddressBean data : mDataList) {
                if (bean.getSymbol().equalsIgnoreCase(data.getSymbol())) {
                    ChainAddressBean.Rate rate = data.getRate();
                    rate.setPrice(bean.getCurrentPrice());
                    data.setRate(rate);
                }
            }
        }
        mAdapter.update(mDataList);
        referUi();
    }

    @Override
    public void onCoinListError(int i, String message, String type) {

    }


    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);

        switch (event.getMessage()) {
            case Constant.Event.READ_MESSAGE:
                int unRead = Integer.parseInt(CacheUtil.getInstance().getProperty(Constant.Sp.UNREAD_COUNT, "0"));
                if (unRead <= 0) {
                    tvUnRead.setVisibility(View.GONE);
                } else {
                    tvUnRead.setVisibility(View.VISIBLE);
                    tvUnRead.setText(String.valueOf(unRead));
                }
                break;
            case Constant.Event.SET_LANGUAGE:

                if (null == mAssetsPresenter) {
                    mAssetsPresenter = new AssetsPresenter(this);
                }

                mAssetsPresenter.getCurrencies(new BaseParam());
                if (null != refresh) {
                    refresh.autoRefresh();
                }
                break;

            case Constant.Event.PRICING_CURRENCY:

                if (null == mAssetsPresenter) {
                    mAssetsPresenter = new AssetsPresenter(this);
                }

                mAssetsPresenter.getCurrencies(new BaseParam());
                if (null != refresh) {
                    refresh.autoRefresh();
                }

                break;
            case Constant.Event.UNBIND_ASSETS:
            case Constant.Event.BIND_ASSETS:
                refresh.autoRefresh();
                break;
            case Constant.Event.ASSETS_DISPLAY:
                mBoolAssets = CacheUtil.getInstance().getProperty(Constant.Sp.ASSETS_DISPLAY_INT, 0) == 0;
                if (mBoolAssets) {
                    String chainId = CacheUtil.getInstance().getProperty(Constant.Sp.COIN_CHAIN_ID);
                    if (TextUtils.isEmpty(chainId)) {
                        chainId = getChainId();
                    }
                    String address = CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS);
                    CacheUtil.getInstance().setProperty(Constant.Sp.WALLET_COIN_ADDRESS, Constant.DEFAULT_PREFIX + address + "_" + chainId);
                } else {
                    CacheUtil.getInstance().setProperty(Constant.Sp.CHAIN_ID, getChainId());
                    String address = CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS);
                    CacheUtil.getInstance().setProperty(Constant.Sp.WALLET_COIN_ADDRESS, Constant.DEFAULT_PREFIX + address + "_" + getChainId());
                }
                refreshAsset();
                referUi();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true) //在ui线程执行
    public void onDataSynEvent(MessageEvent event) {

        if (event != null) {
            if (event.getMessage() == Constant.Event.NOTIFICATION) {
                if (event.getData() != null) {
                    Bundle bundle = event.getData();
                    if (bundle.getInt(Constant.BundleKey.NOTIFICATION_TYPE) == 0) {
                        startBrotherFragment(TransactionRecordFragment.newInstance(event.getData()));
                    } else {
                        startBrotherFragment(NotificationsFragment.newInstance());
                    }
                }
            }
        }
    }

    public void hideKeyboard() {

        try {
            /*隐藏软键盘*/
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive() && getActivity().getCurrentFocus() != null && getActivity().getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }

        } catch (Exception e) {

        }

    }

    /**
     * 搜索
     */
    public void search(EditText choose_et_search) {
        if (mChooseChainAdapter == null) {
            return;
        }
        if (mPopList != null && mPopList.size() > 0) {
            serachData.clear();
            String result = choose_et_search.getText().toString().trim();
            if (!TextUtils.isEmpty(result)) {
                for (ChainAddressBean chooseChainsBean : mPopList) {
                    if ((chooseChainsBean.getChain_id().toLowerCase()).contains(result.toLowerCase())
                            || (chooseChainsBean.getChain_id().toLowerCase()).contains(result.toLowerCase())) {
                        serachData.add(chooseChainsBean);
                    }
                }
                if (serachData != null && serachData.size() > 0) {
                    mChooseChainAdapter.refreshView(serachData);
                }
            }
            if (choose_et_search.getText().length() == 0) {
                mChooseChainAdapter.refreshView(mPopList);
            }
        }
    }

    /**
     * 获取当前chainID
     *
     * @return
     */
    private String getChainId() {
        return ServiceGenerator.getChainId();
    }
}
