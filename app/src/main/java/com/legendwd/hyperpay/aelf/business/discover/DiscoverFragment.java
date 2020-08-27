package com.legendwd.hyperpay.aelf.business.discover;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.discover.adapter.DappAdapter;
import com.legendwd.hyperpay.aelf.business.discover.dapp.GameListBean;
import com.legendwd.hyperpay.aelf.model.bean.ChooseChainsBean;
import com.legendwd.hyperpay.aelf.model.bean.DappListBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.presenters.impl.DiscoveryPresenter;
import com.legendwd.hyperpay.aelf.views.IDiscoveryView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class DiscoverFragment extends BaseFragment implements IDiscoveryView {
    public String TAG = getClass().getSimpleName();
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.et_search)
    TextView et_search;
    private DiscoveryPresenter mDiscoveryPresenter;
    private DappAdapter mAdapter;
    private DappListBean mDappListBean;

    public static DiscoverFragment newInstance() {
        Bundle args = new Bundle();
        DiscoverFragment tabThirdFragment = new DiscoverFragment();
        tabThirdFragment.setArguments(args);
        return tabThirdFragment;
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new FragmentAnimator();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbarNav((Toolbar) view.findViewById(R.id.toolbar), R.string.discover, false);
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_discovery;
    }

    @Override
    public void process() {
        mDiscoveryPresenter = new DiscoveryPresenter(this);
        mDiscoveryPresenter.getDapp();
        refresh.setOnRefreshListener(refreshLayout -> mDiscoveryPresenter.getDapp());
        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseFragment) getPreFragment()).startBrotherFragment(DappSearchFragment.newInstance());
            }
        });
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
//        if (TextUtils.isEmpty(SPWrapper.getDefaultNet())) {
//            SPWrapper.setDefaultNet(Constants.MAIN_NET);
//        } else {
//            String[] mainNets = getResources().getStringArray(R.array.release_port);
//            SPWrapper.setDefaultNet(mainNets[0] + ":20334");
//        }
    }

    //    @OnClick(R.id.iv_scan)
//    void onClickScan() {
//        Intent intent = new Intent(_mActivity, CaptureActivity.class);
//        intent.putExtra(Constant.IntentKey.Scan_Zxing, Constant.IntentValue.SCAN_DISCOVERY);
//        startActivityForResult(intent, Constant.RequestCode.CODE_SCAN_ZXING);
//    }
    @Override
    protected boolean enableSwipeBack() {
        return false;
    }

    @Override
    public void onDappSuccess(DappListBean dappListBean) {
        refresh.finishRefresh();
        if (dappListBean != null) {
            mDappListBean = dappListBean;
            recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
            mAdapter = new DappAdapter(this, mDappListBean);
            recyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onDappError(int code, String msg) {
        refresh.finishRefresh();
    }

    @Override
    public void onGameListSuccess(GameListBean gameListBean, String refreshType) {

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

}
