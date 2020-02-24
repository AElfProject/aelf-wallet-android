package com.legendwd.hyperpay.aelf.business.assets.fragments;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseAdapterModel;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.assets.adapters.AddAssetsChildAdapter;
import com.legendwd.hyperpay.aelf.business.my.GetCountryNameSort;
import com.legendwd.hyperpay.aelf.dialogfragments.ToastDialog;
import com.legendwd.hyperpay.aelf.listeners.HandleCallback;
import com.legendwd.hyperpay.aelf.model.MessageEvent;
import com.legendwd.hyperpay.aelf.model.bean.AssetsBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.presenters.impl.AssetsManagePresenter;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.aelf.views.IAssetsManageView;
import com.legendwd.hyperpay.httputil.ServiceGenerator;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class AddAssetsChildFragment extends BaseFragment implements IAssetsManageView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.refresh)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.edt_search)
    EditText mEdtSearch;
    private List<AssetsBean> mRealBeans = new ArrayList<>();
    private List<AssetsBean> mBeans = new ArrayList<>();
    private AddAssetsChildAdapter mAddAssetsChildAdapter;
    private AssetsManagePresenter mAssetsPresenter;
    private GetCountryNameSort mNameSort = new GetCountryNameSort();

    ToastDialog mToastDialog;

    public static AddAssetsChildFragment newInstance() {
        return new AddAssetsChildFragment();
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_child_addassets;
    }

    @Override
    public void process() {
        mAssetsPresenter = new AssetsManagePresenter(this);

        smartRefreshLayout.setOnRefreshListener(refreshLayout ->
                mAssetsPresenter.getAddAssetsList());
        smartRefreshLayout.autoRefresh();
    }

    @Override
    protected boolean enableSwipeBack() {
        return false;
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    void setAdapter() {
        mBeans = filterInput(mEdtSearch.getText().toString());


        mAddAssetsChildAdapter = new AddAssetsChildAdapter(mBeans, mAssetsPresenter, new HandleCallback() {
            @Override
            public void onHandle(Object o) {
                smartRefreshLayout.autoRefresh();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        recyclerView.setAdapter(mAddAssetsChildAdapter);
        mEdtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mBeans = mRealBeans;
                    mAddAssetsChildAdapter.update(mBeans);
                } else {
                    mAddAssetsChildAdapter.update(filterInput(s.toString()));
                }
            }
        });

        mBeans = mAddAssetsChildAdapter.getDatas();
    }

    private List filterInput(String input) {
        if (TextUtils.isEmpty(input)) {
            return mRealBeans;
        }
        char[] chars = input.toLowerCase().toCharArray();
        List<AssetsBean> result = new ArrayList<>();
        for (AssetsBean bean : mRealBeans) {
            if (bean.getItemType() == BaseAdapterModel.ItemType.EMPTY) {
                continue;
            }
            int index = 0;
            int count = 0;
            String symbol = bean.getSymbol().toLowerCase();
            for (char c : chars) {
                if ((index = symbol.indexOf(c, index)) != -1) {
                    index = index + 1;
                    count++;
                }
            }
            if (count == chars.length) {
                result.add(bean);
            }
        }
        mBeans = result;
        return result;
    }

    @Override
    public void onAssetsSuccess(ResultBean<Map<String, List<AssetsBean>>> resultBean) {
        smartRefreshLayout.finishRefresh();
        if (resultBean == null || resultBean.getData() == null) {
            setAdapter();
            return;
        }
        mBeans.clear();
        addData(resultBean.getData());
        for (AssetsBean aelfBean : mBeans) {
            aelfBean.setSortKey(mNameSort.getSortLetterBySortKey(aelfBean.getSymbol()));
        }
        mRealBeans = mBeans;
        setAdapter();
    }

    private void addData(Map<String, List<AssetsBean>> data) {
        boolean bAsset = CacheUtil.getInstance().getProperty(Constant.Sp.ASSETS_DISPLAY_INT, 0) == 0;
        if(bAsset) {
            for(String key : data.keySet()) {
                mBeans.addAll(data.get(key));
            }
        }else {
            List<AssetsBean> list = data.get(ServiceGenerator.getChainId());
            if (list != null && !list.isEmpty()) {
                mBeans.addAll(list);
            }
        }
    }

    @Override
    public void onAssetsError(int code, String msg) {
        smartRefreshLayout.finishRefresh();
        setAdapter();
    }

    @Override
    public void onBindSuccess(ResultBean resultBean, AssetsBean aelfBean, int i) {
        EventBus.getDefault().post(new MessageEvent(Constant.Event.BIND_ASSETS));
    }

    @Override
    public void onBindError(int code, String msg, AssetsBean aelfBean, int i) {
        aelfBean.setIn(0);
        mAddAssetsChildAdapter.updateItem(i);
        DialogUtils.showDialog(ToastDialog.class, getFragmentManager()).setToast(msg);

    }

    @Override
    public void showLoading() {
        if (mToastDialog == null) {
            mToastDialog = DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                    .setToast(R.string.loading)
                    .setLoading(true)
                    .setToastCancelable(false);
        } else {
            mToastDialog.setToast(R.string.loading);
            mToastDialog.show(getFragmentManager(), "ToastDialog");
        }
    }

    @Override
    public void dismissLoading() {
        if (mToastDialog != null) {
            mToastDialog.dismiss();
        }
    }



    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        if (event.getMessage() == Constant.Event.UNBIND_ASSETS) {
//            smartRefreshLayout.autoRefresh();
        }
    }
}
