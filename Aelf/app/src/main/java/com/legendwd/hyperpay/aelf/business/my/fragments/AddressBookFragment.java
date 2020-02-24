package com.legendwd.hyperpay.aelf.business.my.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.my.CharacterParserUtil;
import com.legendwd.hyperpay.aelf.business.my.CountryComparator;
import com.legendwd.hyperpay.aelf.business.my.GetCountryNameSort;
import com.legendwd.hyperpay.aelf.business.my.adapters.AddressBookAdapter;
import com.legendwd.hyperpay.aelf.dialogfragments.ToastDialog;
import com.legendwd.hyperpay.aelf.model.bean.AddressBookBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.param.AddressBookParam;
import com.legendwd.hyperpay.aelf.presenters.IAddressBookPresenter;
import com.legendwd.hyperpay.aelf.presenters.impl.AddressBookPresenter;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.aelf.util.StringUtil;
import com.legendwd.hyperpay.aelf.views.IAddressBookView;
import com.legendwd.hyperpay.aelf.widget.BtnDeleteListern;
import com.legendwd.hyperpay.aelf.widget.SideBar;
import com.legendwd.hyperpay.aelf.widget.SlideDeleteListView;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddressBookFragment extends BaseFragment implements IAddressBookView {
    @BindView(R.id.country_et_search)
    EditText country_edt_search;
    @BindView(R.id.country_lv_list)
    SlideDeleteListView country_lv_countryList;
    @BindView(R.id.country_iv_cleartext)
    ImageView country_iv_clearText;
    @BindView(R.id.country_sidebar)
    SideBar sideBar;
    @BindView(R.id.country_dialog)
    TextView dialog;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;
    @BindView(R.id.ll_empty)
    LinearLayout ll_empty;
    @BindView(R.id.ll_data)
    LinearLayout ll_data;
    //    private List<AddressSortModel> mAllCountryList;
    private List<AddressBookBean.ListBean> addressList;
    private AddressBookAdapter adapter;

    private CountryComparator pinyinComparator = new CountryComparator();

    private GetCountryNameSort countryChangeUtil = new GetCountryNameSort();

    private CharacterParserUtil characterParserUtil = new CharacterParserUtil();

    private IAddressBookPresenter mAddressBookPresenter;

    public static AddressBookFragment newInstance(Bundle args) {
        AddressBookFragment tabFourFragment = new AddressBookFragment();
        tabFourFragment.setArguments(args);
        return tabFourFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initToolbarNav(mToolbar, R.string.address_book, true);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_address_book;
    }

    @Override
    public void process() {
        mAddressBookPresenter = new AddressBookPresenter(this);

        tv_title_right.setText("");
        tv_title_right.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.add, 0);
        initView();
        setListener();

        AddressBookParam param = new AddressBookParam();
        param.address = CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS);
        mAddressBookPresenter.getAddresBook(param);
    }

    @OnClick(R.id.tv_title_right)
    public void clickView() {
        hideSoftInput();
        startForResult(AddAddressFragment.newInstance(new Bundle()), Constant.RequestCode.CODE_ADD_ADDRESS);
    }



    /**
     * 初始化界面
     */
    private void initView() {

        sideBar.setTextView(dialog);

        addressList = new ArrayList<>();

        // 将联系人进行排序，按照A~Z的顺序


        Collections.sort(addressList, pinyinComparator);
        adapter = new AddressBookAdapter(_mActivity, addressList);
        country_lv_countryList.setAdapter(adapter);

        country_lv_countryList.setEmptyView(ll_empty);

        country_lv_countryList.setBtnDelClickListener(new BtnDeleteListern() {
            @Override
            public void deleteOnCliclListern(int positon) {
                AddressBookBean.ListBean bean = addressList.get(positon);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("address", StringUtil.getRealAelfAddress(
                        CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS)));
                jsonObject.addProperty("contact_address", bean.address);
                mAddressBookPresenter.delAddress(jsonObject, bean, positon);
                addressList.remove(positon);
//                adapter.notifyDataSetChanged();
                adapter.updateListView(addressList);
            }
        });

    }

    /****
     * 添加监听
     */
    private void setListener() {
        country_iv_clearText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                country_edt_search.setText("");
                Collections.sort(addressList, pinyinComparator);
                adapter.updateListView(addressList);
            }
        });

        country_edt_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchContent = country_edt_search.getText().toString();
                if (searchContent.equals("")) {
                    country_iv_clearText.setVisibility(View.INVISIBLE);
                } else {
                    country_iv_clearText.setVisibility(View.VISIBLE);
                }

                if (searchContent.length() > 0) {
                    // 按照输入内容进行匹配
                    ArrayList<AddressBookBean.ListBean> fileterList = (ArrayList<AddressBookBean.ListBean>) countryChangeUtil
                            .search(searchContent, addressList);

                    adapter.updateListView(fileterList);
                } else {
                    adapter.updateListView(addressList);
                }
                country_lv_countryList.setSelection(0);
            }
        });

        // 右侧sideBar监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    country_lv_countryList.setSelection(position);
                }
            }
        });

        country_lv_countryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
                String searchContent = country_edt_search.getText().toString();

                AddressBookBean.ListBean bean = null;

                if (searchContent.length() > 0) {
                    ArrayList<AddressBookBean.ListBean> fileterList = (ArrayList<AddressBookBean.ListBean>) countryChangeUtil
                            .search(searchContent, addressList);
                    bean = fileterList.get(position);

                } else {
                    bean = addressList.get(position);
                }

                if (null == bean) {
                    return;
                }

                hideSoftInput();

                bean.address = StringUtil.checkAddress(bean.address);

                String from = getArguments().getString(Constant.BundleKey.ADDRESS_BOOK);

                if (TextUtils.equals(from, Constant.BundleValue.TRANSFER_ADDRESS_BOOK)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("bean", new Gson().toJson(bean));
                    setFragmentResult(RESULT_OK, bundle);
                    pop();
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putString("bean", new Gson().toJson(bean));
                    startForResult(AddAddressFragment.newInstance(bundle), Constant.RequestCode.CODE_ADD_ADDRESS);
                }

            }
        });

    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
    }

    private void refreshView() {
        for (AddressBookBean.ListBean bean : addressList) {
            bean.countrySortKey = characterParserUtil.getSelling(bean.name);
            String sortLetter = countryChangeUtil.getSortLetterBySortKey(bean.countrySortKey);
            if (sortLetter == null) {
                sortLetter = countryChangeUtil.getSortLetterBySortKey(bean.name);
            }
            bean.showAddress = bean.address;
//            bean.showAddress = Constant.DEFAULT_PREFIX + bean.address + "_" + CacheUtil.getInstance().getProperty(Constant.Sp.CHAIN_ID);
            bean.sortLetters = sortLetter;
        }

        Collections.sort(addressList, pinyinComparator);
        if (null == adapter) {
            adapter = new AddressBookAdapter(_mActivity, addressList);
        }
        adapter.updateListView(addressList);
    }

    private void setView() {
        if (null == addressList || addressList.size() <= 0) {
            ll_data.setVisibility(View.GONE);
            ll_empty.setVisibility(View.VISIBLE);
        } else {
            ll_data.setVisibility(View.VISIBLE);
            ll_empty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSuccess(ResultBean<AddressBookBean> listResultBean) {
        addressList = listResultBean.getData().list;
        setView();
        refreshView();
    }

    @Override
    public void onError(int code, String msg) {
        setView();
    }

    @Override
    public void onDelSuccess(ResultBean resultBean) {

        if (200 == resultBean.getStatus()) {
            if (!TextUtils.isEmpty(resultBean.getMsg())) {
                DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                        .setToast(resultBean.getMsg());
            }
            setView();
        }

    }


    @Override
    public void onDelError(int code, String msg, AddressBookBean.ListBean bean, int index) {
        if (!TextUtils.isEmpty(msg)) {
            DialogUtils.showDialog(ToastDialog.class, getFragmentManager())
                    .setToast(msg);
        }

        addressList.add(index, bean);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == Constant.RequestCode.CODE_ADD_ADDRESS && resultCode == RESULT_OK) {
            AddressBookParam param = new AddressBookParam();
            param.address = CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS);
            mAddressBookPresenter.getAddresBook(param);
//            AddressBookBean.ListBean listBean = new Gson().fromJson(data.getString("bean"), AddressBookBean.ListBean.class);
//            String sortLetter = countryChangeUtil.getSortLetterBySortKey(listBean.countrySortKey);
//            if (sortLetter == null) {
//                sortLetter = countryChangeUtil.getSortLetterBySortKey(listBean.name);
//            }
//
//            listBean.sortLetters = sortLetter;
//            addressList.add(listBean);
//
//            Collections.sort(addressList, pinyinComparator);
//
//            setView();
//            adapter.notifyDataSetChanged();
        }
    }
}
