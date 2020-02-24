package com.legendwd.hyperpay.aelf.business.my.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.assets.fragments.NotificationsFragment;
import com.legendwd.hyperpay.aelf.model.MessageEvent;
import com.legendwd.hyperpay.aelf.model.bean.IdentityBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.UnreadBean;
import com.legendwd.hyperpay.aelf.model.param.AddressParam;
import com.legendwd.hyperpay.aelf.presenters.IMyPresenter;
import com.legendwd.hyperpay.aelf.presenters.impl.MyPresenter;
import com.legendwd.hyperpay.aelf.views.IMyView;
import com.legendwd.hyperpay.aelf.widget.RoundImageView;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class MyFragment extends BaseFragment implements IMyView {
    @BindView(R.id.rel_my_account)
    RelativeLayout rel_my_account;
    @BindView(R.id.rel_address_book)
    RelativeLayout rel_address_book;
    @BindView(R.id.rel_notifications)
    RelativeLayout rel_notifications;
    @BindView(R.id.rel_settings)
    RelativeLayout rel_settings;
    @BindView(R.id.rel_help)
    RelativeLayout rel_help;
    @BindView(R.id.rel_user_agreement)
    RelativeLayout rel_user_agreement;
    @BindView(R.id.rel_about_us)
    RelativeLayout rel_about_us;
    @BindView(R.id.tv_un_read)
    TextView tv_un_read;

    @BindView(R.id.iv_cover)
    RoundImageView mIvCover;
    @BindView(R.id.tv_name)
    TextView tv_name;

    private IMyPresenter presenter;

    public static MyFragment newInstance() {
        Bundle args = new Bundle();
        MyFragment tabFourFragment = new MyFragment();
        tabFourFragment.setArguments(args);
        return tabFourFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initToolbarNav(mToolbar, R.string.tab_profile, false);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        //         return super.onCreateFragmentAnimation();
        return new FragmentAnimator();
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    public void process() {
        presenter = new MyPresenter(this);
        String unReadCount = CacheUtil.getInstance().getProperty(Constant.Sp.UNREAD_COUNT);

        if (TextUtils.isEmpty(unReadCount) || "0".equals(unReadCount)) {
            tv_un_read.setVisibility(View.GONE);
        } else {
            tv_un_read.setVisibility(View.VISIBLE);
            tv_un_read.setText(unReadCount);
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("address", CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS));
        presenter.getIdentity(jsonObject);
    }


    @Override
    protected boolean enableSwipeBack() {
        return false;
    }

    @OnClick({R.id.rel_my_account, R.id.rel_address_book, R.id.rel_notifications,
            R.id.rel_settings, R.id.rel_help, R.id.rel_user_agreement, R.id.rel_about_us})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.rel_my_account:
                startBrotherFragment(MyAccountFragment.newInstance());
                break;
            case R.id.rel_settings:
                startBrotherFragment(SettingFragment.newInstance());
                break;
            case R.id.rel_about_us: {
                startBrotherFragment(AboutUsFragment.newInstance());
                break;
            }
            case R.id.rel_notifications:
                startBrotherFragment(NotificationsFragment.newInstance());
                break;
            case R.id.rel_address_book:
                startBrotherFragment(AddressBookFragment.newInstance(new Bundle()));
                break;
            case R.id.rel_help:
                startBrotherFragment(HelpFragment.newInstance());
                break;

            case R.id.rel_user_agreement:
                ((BaseFragment) getParentFragment()).start(UserAgreementFragment.newInstance());
                break;

        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if(presenter == null) {
            return;
        }
        AddressParam param = new AddressParam();
        param.address = CacheUtil.getInstance().getProperty(Constant.Sp.WALLET_ADDRESS);
        presenter.getUnreadCount(param);

        String path = CacheUtil.getInstance().getProperty(Constant.Sp.ACCOUNT_PORTRAIT);

        Glide.with(_mActivity).load(path).placeholder(R.mipmap.user_profile).into(mIvCover);

        String name = CacheUtil.getInstance().getProperty(Constant.Sp.ACCOUNT_NAME);
        if (!TextUtils.isEmpty(name)) {
            tv_name.setText(name);
        }
    }

    @Override
    public void onUnreadSuccess(ResultBean<UnreadBean> resultBean) {
        if (200 == resultBean.getStatus()) {
            if (null == resultBean.getData().unread_count || "0".equals(resultBean.getData().unread_count)) {
                tv_un_read.setVisibility(View.GONE);
                CacheUtil.getInstance().setProperty(Constant.Sp.UNREAD_COUNT, "0");
            } else {
                tv_un_read.setVisibility(View.VISIBLE);
                tv_un_read.setText(resultBean.getData().unread_count);
                CacheUtil.getInstance().setProperty(Constant.Sp.UNREAD_COUNT, resultBean.getData().unread_count);
            }

        }
    }

    @Override
    public void onUnreadError(int code, String msg) {

    }

    @Override
    public void onIdentitySuccess(ResultBean<IdentityBean> resultBean) {
        if (200 == resultBean.getStatus()) {
            CacheUtil.getInstance().setProperty(Constant.Sp.ACCOUNT_PORTRAIT, resultBean.getData().getImg());
            CacheUtil.getInstance().setProperty(Constant.Sp.ACCOUNT_NAME, resultBean.getData().getName());
            Glide.with(_mActivity).load(resultBean.getData().getImg()).placeholder(R.mipmap.user_profile).into(mIvCover);
        }
    }

    @Override
    public void onIdentityError(int code, String msg) {

    }

    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        switch (event.getMessage()) {
            case Constant.Event.READ_MESSAGE:
                int unRead = Integer.parseInt(CacheUtil.getInstance().getProperty(Constant.Sp.UNREAD_COUNT, "0"));
                if (unRead <= 0) {
                    tv_un_read.setVisibility(View.GONE);
                } else {
                    tv_un_read.setVisibility(View.VISIBLE);
                    tv_un_read.setText(String.valueOf(unRead));
                }
                break;
        }
    }
}
