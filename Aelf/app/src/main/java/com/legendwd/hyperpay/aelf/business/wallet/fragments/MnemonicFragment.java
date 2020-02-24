package com.legendwd.hyperpay.aelf.business.wallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.wallet.adapters.MnemonicAdapter;
import com.legendwd.hyperpay.aelf.dialogfragments.DontScreenShotDialog;
import com.legendwd.hyperpay.aelf.model.bean.MnemonicWord;
import com.legendwd.hyperpay.aelf.model.bean.WalletBean;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.aelf.util.ScreenUtils;
import com.legendwd.hyperpay.aelf.widget.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MnemonicFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private MnemonicAdapter mAdapter;
    private List<MnemonicWord> mDataList = new ArrayList<>();

    public static MnemonicFragment newInstance(Bundle bundle) {
        MnemonicFragment fragment = new MnemonicFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbarNav(mToolbar, R.string.mnemonic_title, true);

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mnemonic;
    }

    @Override
    public void process() {

        Bundle bundle = getArguments();
        if (null != bundle) {
            WalletBean bean = new Gson().fromJson(bundle.getString("bean"), WalletBean.class);
            if (null != bean) {
                String[] words = bean.mnemonic.split("\\s");

                for (String word : words) {
                    MnemonicWord mnemonicWord = new MnemonicWord();
                    mnemonicWord.word = word;
                    mDataList.add(mnemonicWord);
                }

                mAdapter = new MnemonicAdapter(mDataList, null);
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, ScreenUtils.dip2px(_mActivity, 15), false));
                recyclerView.setLayoutManager(new GridLayoutManager(_mActivity, 3));
                recyclerView.setAdapter(mAdapter);
            }

        }

        DialogUtils.showDialog(DontScreenShotDialog.class, getFragmentManager());

    }

    @OnClick(R.id.tv_next)
    void onClickNext() {
        Bundle bundle = getArguments();
        start(MnemonicConfirmFragment.newInstance(bundle));
    }
}
