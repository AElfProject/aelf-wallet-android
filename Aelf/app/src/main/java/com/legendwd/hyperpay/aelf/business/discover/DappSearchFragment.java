package com.legendwd.hyperpay.aelf.business.discover;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.discover.adapter.DappSearchAdapter;
import com.legendwd.hyperpay.aelf.business.discover.dapp.Dapp;
import com.legendwd.hyperpay.aelf.business.discover.dapp.GameListBean;
import com.legendwd.hyperpay.aelf.business.discover.dapp.SearchType;
import com.legendwd.hyperpay.aelf.model.bean.ChooseChainsBean;
import com.legendwd.hyperpay.aelf.model.bean.DiscoveryBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.presenters.impl.DiscoveryPresenter;
import com.legendwd.hyperpay.aelf.views.IDiscoveryView;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author Colin
 * @date 2019/10/11.
 * description：
 */
public class DappSearchFragment extends BaseFragment implements IDiscoveryView {
    @BindView(R.id.iv_dapp_cancle)
    TextView iv_dapp_cancle;
    @BindView(R.id.smt_recyclerview_dapp)
    RecyclerView recyclerView;
    @BindView(R.id.smt_refresh_dapp)
    SmartRefreshLayout refresh;
    @BindView(R.id.et_game_search)
    EditText et_search;
    private DappSearchAdapter mAdapter;
    private DiscoveryPresenter mDiscoveryPresenter;
    private List<Dapp> mDapps;

    public static DappSearchFragment newInstance() {
        Bundle args = new Bundle();
        DappSearchFragment dappSearchFragment = new DappSearchFragment();
        dappSearchFragment.setArguments(args);
        return dappSearchFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_dapp_search;
    }

    @Override
    public void process() {
        mDiscoveryPresenter = new DiscoveryPresenter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refresh.setEnableScrollContentWhenLoaded(false);
        refresh.setDisableContentWhenLoading(true);
        refresh.setRefreshHeader(new ClassicsHeader(getContext()));
        refresh.setEnableLoadMore(false);
        refresh.setEnableRefresh(true);
        mDiscoveryPresenter.getGameList(new JsonObject(),"TYPE_PULL_DOWN");
        initClick();
        cacheSearchData();
    }

    /**
     * 初始化监听事件
     */
    private void initClick() {
        iv_dapp_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                hideSoftInput();
                cacheSearchData();
                return true;

            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                cacheSearchData();
            }
        });
    }

    /**
     * 检索cache data
     */
    private void cacheSearchData() {
        String input = et_search.getText().toString();
        GameListBean gameList = new GameListBean();
        if (TextUtils.isEmpty(input)) {
            gameList.searchType = SearchType.DAPP_POPULAR;
        } else {
            if (input.startsWith("http")) {
                gameList.searchType = SearchType.DAPP_URL;
            } else {
                gameList.searchType = SearchType.DAPP_NAME;
            }
        }
        ArrayList<Dapp> dappList = getDappDataFromCache(input);
        if (null == dappList || dappList.size() <= 0) {
            gameList.searchType = SearchType.DAPP_NONE;
        }
        gameList.dapps = dappList;
        setAdapter(gameList);
    }

    private void setAdapter(GameListBean dapps) {
        if (null == dapps) return;
        if (null == mAdapter) {
            mAdapter = new DappSearchAdapter(getActivity(), dapps);
            recyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.refreshData(dapps);
        }
    }

    @Override
    public void onGameListSuccess(GameListBean gameListBean,String refreshType) {
        if (gameListBean == null) return;
        CacheUtil.getInstance().setProperty(Constant.Sp.Dapp_List_Cache, new Gson().toJson(gameListBean));
        setAdapter(gameListBean);
    }

    @Override
    public void onGameListError(int code, String msg) {

    }

    @Override
    public void onApiSuccessForDapp(String id, String json) {

    }

    @Override
    public void onApiErrorForDapp(String id, int code, String msg) {

    }



    @Override
    public void onChainsSuccess(ResultBean<List<ChooseChainsBean>> resultBean) {

    }

    @Override
    public void onChainsError(int code, String msg) {

    }

    @Override
    public void onDappSuccess(DiscoveryBean discoveryBean) {

    }

    @Override
    public void onDappError(int code, String msg) {

    }

    /**
     * 从cache获取比对数据
     *
     * @param input
     * @return
     */
    public static ArrayList<Dapp> getDappDataFromCache(String input) {
        ArrayList<Dapp> dapps = new ArrayList<>();
        String json = CacheUtil.getInstance().getProperty(Constant.Sp.Dapp_List_Cache);
        GameListBean gameListBean = new Gson().fromJson(json, GameListBean.class);
        if (TextUtils.isEmpty(input)) {
            if (gameListBean!=null){
                return gameListBean.dapps;
            }
            return null;
        } else {
            if (gameListBean == null || gameListBean.dapps == null) return null;
            for (Dapp dapp : gameListBean.dapps) {
                if (input.startsWith("http") && dapp.url.contains(input)) {
                    dapps.add(dapp);
                } else if (dapp.name.contains(input)) {
                    dapps.add(dapp);
                }
            }
            return dapps;
        }
    }
}
