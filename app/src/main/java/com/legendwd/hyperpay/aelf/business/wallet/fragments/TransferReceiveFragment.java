package com.legendwd.hyperpay.aelf.business.wallet.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.assets.fragments.ReceiveFragment;
import com.legendwd.hyperpay.aelf.business.wallet.adapters.TransferReceivePagerAdapter;
import com.legendwd.hyperpay.aelf.model.MessageEvent;
import com.legendwd.hyperpay.aelf.model.bean.ChainAddressBean;
import com.legendwd.hyperpay.aelf.model.bean.ChooseChainsBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.TransferBalanceBean;
import com.legendwd.hyperpay.aelf.model.param.TransferBalanceParam;
import com.legendwd.hyperpay.aelf.presenters.ITransferPresenter;
import com.legendwd.hyperpay.aelf.presenters.impl.TransferPresenter;
import com.legendwd.hyperpay.aelf.util.StringUtil;
import com.legendwd.hyperpay.aelf.views.ITransferView;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TransferReceiveFragment extends BaseFragment implements ITransferView {
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tb_layout)
    TabLayout tabLayout;
    @BindView(R.id.tv_copy)
    TextView tvCopy;
    @BindView(R.id.tv_balance)
    TextView mTvBalance;
    @BindView(R.id.tv_cny)
    TextView mTvCny;
    @BindView(R.id.tv_title)
    TextView tv_title;
    private String[] mTabTitles;
    private ITransferPresenter mITransferPresenter;
    private ChainAddressBean mDataBean;

    public static TransferReceiveFragment newInstance(Bundle bundle) {
        TransferReceiveFragment fragment = new TransferReceiveFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_transfer_receive;
    }

    @Override
    public void process() {

        mDataBean = new Gson().fromJson(getArguments().getString("bean"), ChainAddressBean.class);

        setCurrentBalance(mDataBean.getBalance(), mDataBean.getRate().getPrice());

        initToolbarNav(mToolbar, mDataBean.getChain_id() + "-" + mDataBean.getSymbol(), true);

        mITransferPresenter = new TransferPresenter(this);

        getTransferBalance();
        mTabTitles = new String[]{getString(R.string.all), getString(R.string.transfer), getString(R.string.receive)};
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        List<Fragment> list = new ArrayList<>();
        list.add(TransferChildFragment.newInstance(createBundle("0")));
        list.add(TransferChildFragment.newInstance(createBundle("1")));
        list.add(TransferChildFragment.newInstance(createBundle("2")));
        TransferReceivePagerAdapter transferReceivePagerAdapter = new TransferReceivePagerAdapter(getChildFragmentManager(), list, mTabTitles);
        viewPager.setAdapter(transferReceivePagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);

        ImageView imgBack = getView().findViewById(R.id.img_back);
        imgBack.setColorFilter(getResources().getColor(R.color.white));

        tvCopy.setText(CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_COIN_ADDRESS));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                ((TransferChildFragment) list.get(i)).setTag(String.valueOf(i));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    public void getTransferBalance() {
        TransferBalanceParam param = new TransferBalanceParam();
        param.address = CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS);
        param.contractAddress = null == mDataBean ? "" : mDataBean.getContractAddress();
        param.symbol = mDataBean.getSymbol();
        param.chainid = mDataBean.getChain_id();
        mITransferPresenter.getTransferBalance(param);
    }

    private Bundle createBundle(String page) {
        Bundle bundle = getArguments();
        bundle.putString("page", page);
        return bundle;
    }

    @Override
    public void initImmersionBar() {
        ImmersionBar.with(this).keyboardEnable(true)
                .navigationBarColorInt(Color.WHITE)
                .statusBarDarkFont(true, 0.2f)
                .autoDarkModeEnable(true, 0.2f).init();
    }

    @OnClick({R.id.rl_transfer, R.id.rl_receive, R.id.tv_copy})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_copy:
                copyText(CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_COIN_ADDRESS));
                break;
            case R.id.rl_transfer:
                start(TransferFragment.newInstance(getArguments()));
                break;
            case R.id.rl_receive:
                start(ReceiveFragment.newInstance(getArguments()));
                break;
        }
    }


    @Override
    public void onTransferBalanceSuccess(ResultBean<TransferBalanceBean> bean, String chainid) {
        if (bean == null || bean.getData() == null || bean.getData().balance == null) {
            return;
        }
        setCurrentBalance(bean.getData().balance.getBalance(), bean.getData().rate.price);
    }

    private void setCurrentBalance(String balance, String price) {
        boolean isMode = CacheUtil.getInstance().getProperty(Constant.Sp.PRIVATE_MODE, false);
        if (isMode) {
            mTvBalance.setText("****");
            mTvCny.setText("****");
        } else {
            mTvBalance.setText(balance);

            double value = Double.parseDouble(balance) * Double.parseDouble(price);
            String pre = "≈" + StringUtil.formatDataNoZero(2, value);
            SpannableString priceString = new SpannableString(pre + CacheUtil.getInstance().getProperty(Constant.Sp.PRICING_CURRENCY_ID_DEFAULT));
            RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(0.5f);
            priceString.setSpan(relativeSizeSpan, pre.length(), priceString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mTvCny.setText(priceString);
        }
    }

    @Override
    public void onTransferBalanceError(int code, String msg) {

    }

    @Override
    public void onConcurrentSuccess(ResultBean<List<ChainAddressBean>> resultBean) {

    }

    @Override
    public void onConcurrentError(int code, String msg) {

    }

    @Override
    public void onChainsSuccess(ResultBean<List<ChooseChainsBean>> resultBean) {

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
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        if (event.getMessage() == Constant.Event.REFRSH_TRANSATION && mDataBean != null) {
            getTransferBalance();
        }
    }
}
