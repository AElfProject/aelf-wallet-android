package com.legendwd.hyperpay.aelf.business.wallet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.legendwd.hyperpay.aelf.MainActivity;
import com.legendwd.hyperpay.aelf.dialogfragments.ToastDialog;
import com.legendwd.hyperpay.aelf.ui.fragment.MainFragment;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.my.fragments.MyAccountFragment;
import com.legendwd.hyperpay.aelf.business.wallet.adapters.MnemonicAdapter;
import com.legendwd.hyperpay.aelf.listeners.OnItemClickListener;
import com.legendwd.hyperpay.aelf.model.bean.MnemonicWord;
import com.legendwd.hyperpay.aelf.model.bean.WalletBean;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.aelf.util.ScreenUtils;
import com.legendwd.hyperpay.aelf.widget.GridSpacingItemDecoration;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MnemonicConfirmFragment extends BaseFragment {

    @BindView(R.id.recyclerView_top)
    RecyclerView recyclerViewTop;
    @BindView(R.id.recyclerView_bottom)
    RecyclerView recyclerViewBottom;

    private MnemonicAdapter mTopAdapter;
    private MnemonicAdapter mBottomAdapter;

    private List<MnemonicWord> mTopDataList = new ArrayList<>();
    private List<MnemonicWord> mBottomDataList = new ArrayList<>();

    private List<String> topList = new ArrayList<>();
    private List<String> wordList = new ArrayList<>();

    public static MnemonicConfirmFragment newInstance(Bundle bundle) {
        MnemonicConfirmFragment fragment = new MnemonicConfirmFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbarNav(mToolbar, R.string.mnemonic_confirm_title, true);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_confirm_mnemonic;
    }

    @Override
    public void process() {

        Bundle bundle = getArguments();
        if (null != bundle) {
            WalletBean bean = new Gson().fromJson(bundle.getString("bean"), WalletBean.class);
            if (null != bean) {

                String[] words = bean.mnemonic.split("\\s");
                wordList = Arrays.asList(words);

                List<MnemonicWord> wordList = new ArrayList<>();
                for (int i = 0; i < words.length; i++) {
                    MnemonicWord word = new MnemonicWord();
                    word.word = words[i];
                    word.index = i;
                    word.isSelected = false;
                    wordList.add(word);
                }

                mTopAdapter = new MnemonicAdapter(mTopDataList, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o) {

                        if (o instanceof MnemonicWord) {
                            MnemonicWord bean = (MnemonicWord) o;
                            refreshTopView(bean);
                        }

                    }
                });

                recyclerViewTop.addItemDecoration(new GridSpacingItemDecoration(3, ScreenUtils.dip2px(_mActivity, 15), false));
                recyclerViewTop.setLayoutManager(new GridLayoutManager(_mActivity, 3));
                recyclerViewTop.setAdapter(mTopAdapter);

                mBottomDataList = wordList;
                Collections.shuffle(mBottomDataList);

                mBottomAdapter = new MnemonicAdapter(mBottomDataList, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o) {
                        if (o instanceof MnemonicWord) {
                            MnemonicWord bean = (MnemonicWord) o;
                            bean.isSelected = !bean.isSelected;
                            refreshBottomView(bean);
                        }
                    }
                });

                recyclerViewBottom.addItemDecoration(new GridSpacingItemDecoration(3, ScreenUtils.dip2px(_mActivity, 15), false));
                recyclerViewBottom.setLayoutManager(new GridLayoutManager(_mActivity, 3));
                recyclerViewBottom.setAdapter(mBottomAdapter);
            }

        }
    }

    private void refreshTopView(MnemonicWord bean) {
        mTopDataList.remove(bean);
        topList.remove(bean.word);
        mTopAdapter.refreshView(mTopDataList);

        changeWordState(bean);
        Collections.shuffle(mBottomDataList);
        mBottomAdapter.refreshView(mBottomDataList);
    }

    private void changeWordState(MnemonicWord bean) {
        for (MnemonicWord mnemonicWord : mBottomDataList) {
            if (TextUtils.equals(mnemonicWord.word, bean.word)) {
                mnemonicWord.isSelected = !mnemonicWord.isSelected;
            }
        }
    }

    private void refreshBottomView(MnemonicWord bean) {
        Collections.shuffle(mBottomDataList);
        mBottomAdapter.refreshView(mBottomDataList);

        if (bean.isSelected) {
            mTopDataList.add(mergeMnemonicWord(bean));
            topList.add(bean.word);
        } else {
            removeList(bean);
            topList.remove(bean.word);
        }

        mTopAdapter.refreshView(mTopDataList);
    }

    private void removeList(MnemonicWord bean) {
        int index = -1;
        for (int i = 0; i < mTopDataList.size(); i++) {
            if (TextUtils.equals(mTopDataList.get(i).word, bean.word)) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            mTopDataList.remove(index);
        }
    }

    private MnemonicWord mergeMnemonicWord(MnemonicWord mnemonicWord) {
        MnemonicWord bean = new MnemonicWord();
        bean.word = mnemonicWord.word;
        return bean;
    }

    @OnClick(R.id.tv_done)
    void onClickDone() {
        if (topList.size() < 12) {
            return;
        }

        boolean isEqual = true;
        for (int i = 0; i < wordList.size(); i++) {
            String word = wordList.get(i);
            String topWord = topList.get(i);
            if (TextUtils.equals(word, topWord)) {
                continue;
            } else {
                isEqual = false;
            }
        }

        if (!isEqual) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(getString(R.string.mnemonics_not_correct));
            return;
        }

        CacheUtil.getInstance().setProperty(Constant.Sp.WALLET_BACKUP, true);

        Bundle bundle = getArguments();
        String from = bundle.getString(Constant.BundleKey.BACKUP);
        if (TextUtils.equals(from, Constant.BundleValue.BACKUP_MAIN_PAGE)) {
            popTo(MainFragment.class, false);
        } else if (TextUtils.equals(from, Constant.BundleValue.BACKUP_MY_ACCOUNT_PAGE)) {
            popTo(MyAccountFragment.class, false);
        } else if (TextUtils.equals(from, Constant.BundleValue.BACKUP_TRANSFER_PAGE)) {
            popTo(TransferFragment.class, false);
        } else {
            startActivity(new Intent(_mActivity, MainActivity.class));
            _mActivity.finish();
        }

    }
}
