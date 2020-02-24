package com.legendwd.hyperpay.aelf.business.assets.fragments;

import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseAdapterModel;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.assets.adapters.EditAssetsAdapter;
import com.legendwd.hyperpay.aelf.business.my.GetCountryNameSort;
import com.legendwd.hyperpay.aelf.dialogfragments.BottomSortDialog;
import com.legendwd.hyperpay.aelf.dialogfragments.ToastDialog;
import com.legendwd.hyperpay.aelf.listeners.HandleCallback;
import com.legendwd.hyperpay.aelf.model.MessageEvent;
import com.legendwd.hyperpay.aelf.model.SortModel;
import com.legendwd.hyperpay.aelf.model.bean.AssetsBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.presenters.impl.AssetsManagePresenter;
import com.legendwd.hyperpay.aelf.util.CommonItemTouchHelper;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.aelf.views.IAssetsManageView;
import com.legendwd.hyperpay.httputil.ServiceGenerator;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class EditAssetsChildFragment extends BaseFragment implements IAssetsManageView {

    public boolean isNumberAsc = true;
    @BindView(R.id.edt_search)
    EditText mEdtSearch;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.tv_sort_type)
    TextView tv_sort_type;
    CommonItemTouchHelper mItemTouchHelper;
    EditAssetsAdapter mEditAssetsAdapter;
    ToastDialog mToastDialog;

    private GetCountryNameSort mNameSort = new GetCountryNameSort();
    private boolean isChinaAsc = true;
    private Comparator<AssetsBean> mChinaComparator = (o1, o2) -> {
        if (o1.getSortKey().equals("@") || o2.getSortKey().equals("#")) {
            return -1;
        } else if (o1.getSortKey().equals("#") || o2.getSortKey().equals("@")) {
            return 1;
        } else {
            return isChinaAsc ? o1.getSortKey().compareTo(o2.getSortKey()) : o2.getSortKey().compareTo(o1.getSortKey());
        }
    };

    private Comparator<AssetsBean> mNumberComparator = (o1, o2) ->
            isNumberAsc ? (int) (Float.parseFloat(o1.getBalance()) - Float.parseFloat(o2.getBalance())) : (int) (Float.parseFloat(o2.getBalance()) - Float.parseFloat(o1.getBalance()));
    private List<AssetsBean> mBeans = new ArrayList<>();

    private AssetsManagePresenter mAssetsPresenter;
    private int mDefaultPosition = 4;

    public static EditAssetsChildFragment newInstance() {
        return new EditAssetsChildFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_child_edit_assets;
    }

    @Override
    public void process() {

        mAssetsPresenter = new AssetsManagePresenter(this);
        initAdapter();
        // 这个就不多解释了，就这么attach
//        mItemTouchHelper.attachToRecyclerView(recyclerView);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mAssetsPresenter.getAddAssetsList();
            }
        });
        smartRefreshLayout.autoRefresh();
        tv_sort_type.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.sort_up, 0);
    }

    @OnClick(R.id.rl_customised)
    void onClickCustomised() {
        DialogUtils.showDialog(BottomSortDialog.class, getFragmentManager(), new HandleCallback() {
            @Override
            public void onHandle(Object o) {
                if (mEditAssetsAdapter == null) {
                    return;
                }

                if (o instanceof SortModel) {
                    int index = ((SortModel) o).position;
                    tv_sort_type.setText(((SortModel) o).text);

                    mDefaultPosition = index;

                    switch (index) {
                        case 1:
                            mItemTouchHelper.attachToRecyclerView(recyclerView);
                            tv_sort_type.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.sort_default, 0);
                            break;
                        case 2:
                            isNumberAsc = true;
                            Collections.sort(mBeans, mNumberComparator);
                            tv_sort_type.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.sort_up, 0);
                            break;
                        case 3:
                            isNumberAsc = false;
                            Collections.sort(mBeans, mNumberComparator);
                            tv_sort_type.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.sort_down, 0);
                            break;
                        case 4:
                            isChinaAsc = true;
                            Collections.sort(mBeans, mChinaComparator);
                            tv_sort_type.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.sort_up, 0);
                            break;
                        case 5:
                            isChinaAsc = false;
                            Collections.sort(mBeans, mChinaComparator);
                            tv_sort_type.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.sort_down, 0);
                            break;

                    }
                    if (index == 1) {
                        mEditAssetsAdapter.updateDragAble(true);
                        mItemTouchHelper.attachToRecyclerView(recyclerView);
                    } else {
                        mEditAssetsAdapter.updateDragAble(false);
                        mItemTouchHelper.attachToRecyclerView(null);
                    }
                }

            }
        });
    }

    void initAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        final DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);

        mItemTouchHelper = new CommonItemTouchHelper.Builder()
                .canDrag(true)
                .canSwipe(false)
                .isLongPressDragEnable(false)
                .selectedBackgroundColor(Color.WHITE)
                .unSelectedBackgroundColor(Color.TRANSPARENT)
                .itemTouchCallbackListener(new CommonItemTouchHelper.OnItemTouchCallbackListener() {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        // 获取原来的位置
                        int fromPosition = viewHolder.getAdapterPosition();
                        // 得到目标的位置
                        int targetPosition = target.getAdapterPosition();
                        if (fromPosition > targetPosition) {
                            for (int i = fromPosition; i < targetPosition; i++) {
                                Collections.swap(mBeans, i, i + 1);// 改变实际的数据集
                            }
                        } else {
                            for (int i = fromPosition; i > targetPosition; i--) {
                                Collections.swap(mBeans, i, i - 1);// 改变实际的数据集
                            }
                        }
                        mEditAssetsAdapter.notifyItemMoved(fromPosition, targetPosition);
                        return true;
                    }

                    @Override
                    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction) {

                    }

                    @Override
                    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                        smartRefreshLayout.setEnabled(true);
                    }

                    @Override
                    public void onSwapped(int fromIndex, int toIndex) {

                    }

                    @Override
                    public void onRemoved(int index) {

                    }


                }).build();
    }

    @Override
    protected boolean enableSwipeBack() {
        return false;
    }

    private List<AssetsBean> mRealBeans = new ArrayList<>();

    void setAdapter() {
        mBeans = filterInput(mEdtSearch.getText().toString());
        if (mBeans.size() == 0) {
            AssetsBean aelfBean = new AssetsBean();
            aelfBean.setItemType(BaseAdapterModel.ItemType.EMPTY);
            mBeans.add(aelfBean);
        }

        if (mEditAssetsAdapter == null) {
            mEditAssetsAdapter = new EditAssetsAdapter(mBeans, mItemTouchHelper, smartRefreshLayout, new HandleCallback() {
                @Override
                public void onHandle(Object o) {
                    if (o == null) {
                        smartRefreshLayout.autoRefresh();
                        return;
                    }
                    int i = (int) o;
                    AssetsBean aelfBean = mBeans.get(i);
                    mBeans.remove(i);
                    mEditAssetsAdapter.update(mBeans);

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("address", CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS));
                    jsonObject.addProperty("contract_address", aelfBean.getContractAddress());
                    jsonObject.addProperty("flag", "2");
                    jsonObject.addProperty("symbol", aelfBean.getSymbol());
                    jsonObject.addProperty("signed_address", CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_SIGNED_ADDRESS));
                    jsonObject.addProperty("public_key", CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_PUBLIC_KEY));
                    mAssetsPresenter.bind(jsonObject, aelfBean, i);
                }
            });


            recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
            recyclerView.setAdapter(mEditAssetsAdapter);

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
                        mEditAssetsAdapter.update(mBeans);
                    } else {
                        mEditAssetsAdapter.update(filterInput(s.toString()));
                    }
                }
            });
        } else {
            mEditAssetsAdapter.update(mBeans);
        }

        mBeans = mEditAssetsAdapter.getDatas();
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
        isChinaAsc = true;
        Collections.sort(mBeans, mChinaComparator);

        mRealBeans = mBeans;
        setAdapter();

        if (mDefaultPosition == 1) {
            mEditAssetsAdapter.updateDragAble(true);
            mItemTouchHelper.attachToRecyclerView(recyclerView);
        } else {
            mEditAssetsAdapter.updateDragAble(false);
            mItemTouchHelper.attachToRecyclerView(null);
        }
    }

    private void addData(Map<String, List<AssetsBean>> data) {
        boolean bAsset = CacheUtil.getInstance().getProperty(Constant.Sp.ASSETS_DISPLAY_INT, 0) == 0;
        if(bAsset) {
            for(String key : data.keySet()) {
                addData(data.get(key));
            }
        }else {
            List<AssetsBean> list = data.get(ServiceGenerator.getChainId());
            addData(list);
        }
    }

    private void addData(List<AssetsBean> list) {
        if (list != null && !list.isEmpty()) {
            for (AssetsBean aelfBean : list) {
                if (aelfBean.getIn() != 0) {
                    mBeans.add(aelfBean);
                }
                aelfBean.setSortKey(mNameSort.getSortLetterBySortKey(aelfBean.getSymbol()));
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
        if (resultBean.getStatus() != 200) {
            mBeans.add(i, aelfBean);
            mEditAssetsAdapter.update(mBeans);
        } else {
            EventBus.getDefault().post(new MessageEvent(Constant.Event.UNBIND_ASSETS));
        }
    }

    @Override
    public void onBindError(int code, String msg, AssetsBean aelfBean, int i) {
        mBeans.add(i, aelfBean);
        mEditAssetsAdapter.update(mBeans);
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
        if (event.getMessage() == Constant.Event.BIND_ASSETS) {
//            smartRefreshLayout.autoRefresh();
        }
    }
}
