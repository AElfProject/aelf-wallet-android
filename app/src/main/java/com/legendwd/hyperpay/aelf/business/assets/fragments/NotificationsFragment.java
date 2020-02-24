package com.legendwd.hyperpay.aelf.business.assets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.assets.adapters.NotificationsAdapter;
import com.legendwd.hyperpay.aelf.model.MessageEvent;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.presenters.impl.NotificationsParentPresenter;
import com.legendwd.hyperpay.aelf.views.INotificationsParentView;
import com.legendwd.hyperpay.lib.Constant;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 通知界面
 */
public class NotificationsFragment extends BaseFragment implements INotificationsParentView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private NotificationsAdapter mAdapter;
    private NotificationsParentPresenter mNotificationsParentPresenter;

    public static NotificationsFragment newInstance() {
        return new NotificationsFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_assets_notification;
    }

    @Override
    public void process() {
        initToolbarNav(getView().findViewById(R.id.toolbar), R.string.notifications, true);

        mNotificationsParentPresenter = new NotificationsParentPresenter(this);

        tv_title.setText(R.string.notifications);
        tv_title_right.setText(R.string.clear);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(TransactionNoticeFragment.newInstance());
        fragmentList.add(SystemMessagesFragment.newInstance());

        mAdapter = new NotificationsAdapter(getChildFragmentManager(), fragmentList);
        String[] titles = new String[]{getString(R.string.trasncation_notice), getString(R.string.system_message)};
        for (int x = 0; x < 2; x++) {
            TabLayout.Tab tab = tabLayout.newTab();
            View inflate = View.inflate(_mActivity, R.layout.item_tab_notification, null);
            TextView textView = inflate.findViewById(R.id.tv_title);
            textView.setText(titles[x]);
            tab.setCustomView(inflate);
            tabLayout.addTab(tab);
        }

        viewPager.setAdapter(mAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.setScrollPosition(position, 0f, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 0) {

                }
            }
        });
    }


    public void setUnread(int index, String count) {
        TabLayout.Tab tab = tabLayout.getTabAt(index);
        TextView unread = tab.getCustomView().findViewById(R.id.tv_un_read);
        unread.setVisibility(Integer.parseInt(count) > 0 ? View.VISIBLE : View.GONE);
        unread.setText(count);
        EventBus.getDefault().post(new MessageEvent(Constant.Event.READ_MESSAGE));
    }

    @OnClick(R.id.tv_title_right)
    public void clickView() {

        int pos = viewPager.getCurrentItem();
        switch (pos) {
            case 0: {
                TransactionNoticeFragment fragment = (TransactionNoticeFragment) mAdapter.getItem(pos);
                fragment.clearItem();
                break;
            }
            case 1: {
                SystemMessagesFragment fragment = (SystemMessagesFragment) mAdapter.getItem(pos);
                fragment.clearItem();
                break;
            }

            default:
                break;
        }

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

    }


    @Override
    public void onClearSuccess(ResultBean resultBean) {

    }

    @Override
    public void onClearError(int code, String msg) {

    }
}
