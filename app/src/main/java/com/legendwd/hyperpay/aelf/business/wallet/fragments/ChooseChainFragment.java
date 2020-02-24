package com.legendwd.hyperpay.aelf.business.wallet.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.wallet.adapters.ChooseChainAdapter;
import com.legendwd.hyperpay.aelf.model.bean.ChainAddressBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.param.ChooseChainParam;
import com.legendwd.hyperpay.aelf.presenters.impl.ChooseChainPresenter;
import com.legendwd.hyperpay.aelf.views.IChainView;
import com.legendwd.hyperpay.aelf.widget.ClassicsFooter;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author Colin
 * @date 2019/9/24.
 * description：
 */
public class ChooseChainFragment extends BaseFragment implements IChainView {
    @BindView(R.id.layoutContainer_choose)
    FrameLayout layoutContainer_choose;
    @BindView(R.id.choose_et_search)
    EditText choose_et_search;
    @BindView(R.id.choose_iv_cleartext)
    ImageView choose_iv_cleartext;
    @BindView(R.id.refresh_choose_search)
    SmartRefreshLayout refresh_choose_search;
    @BindView(R.id.rv_choose_search)
    RecyclerView rv_choose_search;
    @BindView(R.id.tx_fo_tx)
    TextView txFoTx;

    private ChooseChainAdapter mChooseChainAdapter;
    private List<ChainAddressBean> mBeanList = new ArrayList<>();
    private ChooseChainPresenter mChooseChainPresenter;
    private List<ChainAddressBean> serachData = new ArrayList<>();
    private ChainAddressBean mDataBean;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initToolbarNav(view.findViewById(R.id.toolbar), "", true);
        super.onViewCreated(view, savedInstanceState);
    }

    public static ChooseChainFragment newInstance(Bundle bundle) {
        ChooseChainFragment chooseChainFragment = new ChooseChainFragment();
        chooseChainFragment.setArguments(bundle);
        return chooseChainFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_choose_chain;
    }

    @Override
    public void process() {

        mDataBean = new Gson().fromJson(getArguments().getString("bean"), ChainAddressBean.class);

        if(!TextUtils.isEmpty(mDataBean.getSymbol())){
            txFoTx.setText(String.format(getString(R.string.elf_on_chain),mDataBean.getSymbol()));
        }

        refresh_choose_search.setEnableLoadMore(true);
        refresh_choose_search.setRefreshFooter(new ClassicsFooter(_mActivity));

        refresh_choose_search.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                ChooseChainParam chooseChainParam = new ChooseChainParam();
                chooseChainParam.address = CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS);
                chooseChainParam.type = "0";
                mChooseChainPresenter.getCrossChains(chooseChainParam);
            }
        });

        if (mChooseChainAdapter == null) {
            rv_choose_search.setLayoutManager(new LinearLayoutManager(_mActivity));
            mChooseChainAdapter = new ChooseChainAdapter(mBeanList);
            rv_choose_search.setAdapter(mChooseChainAdapter);
            mChooseChainAdapter.setOnItemClick(o -> {
                hideSoftInput();
                if(o != null) {
                    Bundle bundle = new Bundle();
                    if (o instanceof ChainAddressBean) {
                        ChainAddressBean data = (ChainAddressBean) o;
                        bundle.putString("bean", new Gson().toJson(data));
                        String wallAddress = CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS);
                        String coinAddress = Constant.DEFAULT_PREFIX + wallAddress + "_" + data.getChain_id();
                        CacheUtil.getInstance().setProperty(Constant.Sp.WALLET_COIN_ADDRESS, coinAddress);
                        CacheUtil.getInstance().setProperty(Constant.Sp.CHAIN_ID, data.getChain_id());
                        ((BaseFragment) getPreFragment()).startBrotherFragment(TransferReceiveFragment.newInstance(bundle));
                    }
                }
            });
        } else {
            mChooseChainAdapter.refreshView(mBeanList);
        }
        mChooseChainPresenter = new ChooseChainPresenter(this);
        refresh_choose_search.autoRefresh();
        initListener();
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        choose_et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchStr = choose_et_search.getText().toString();
                    if (!TextUtils.isEmpty(searchStr)) {
                        hideKeyboard();
                        search();
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
                search();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 搜索
     */
    public void search() {
        if (mBeanList != null && mBeanList.size() > 0) {
            serachData.clear();
            String result = choose_et_search.getText().toString().trim();
            if (!TextUtils.isEmpty(result)) {
                for (ChainAddressBean chooseChainsBean : mBeanList) {
                    if ((chooseChainsBean.getChain_id().toLowerCase()).contains(result.toLowerCase())
                            || (chooseChainsBean.getChain_id().toLowerCase()).contains(result.toLowerCase())) {
                        serachData.add(chooseChainsBean);
                    }
                }
                mChooseChainAdapter.refreshView(serachData);
            }
            if (choose_et_search.getText().length() == 0) {
                mChooseChainAdapter.refreshView(mBeanList);
            }
        }
    }

    @Override
    public void onChainsSuccess(ResultBean<List<ChainAddressBean>> resultBean) {
        refresh_choose_search.finishRefresh();
        if(resultBean.getStatus() == 200) {
            List<ChainAddressBean> data = resultBean.getData();
            mBeanList.clear();
            for(ChainAddressBean bean : data) {
                if(mDataBean.getSymbol().equals(bean.getSymbol())) {
                    mBeanList.add(bean);
                }
            }
            mChooseChainAdapter.refreshView(mBeanList);
        }
    }

    @Override
    public void onChainsError(int code, String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
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
}
