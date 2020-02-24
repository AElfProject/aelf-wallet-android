package com.legendwd.hyperpay.aelf.business.my.fragments;

import android.view.View;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class HelpFragment extends BaseFragment {

    @BindView(R.id.tv_title_right)
    TextView tv_title_right;
    @BindView(R.id.tv_how_wallet)
    TextView tv_how_wallet;
    @BindView(R.id.tv_how_wallet_detail)
    TextView tv_how_wallet_detail;
    @BindView(R.id.tv_search_tokens)
    TextView tv_search_tokens;
    @BindView(R.id.tv_search_tokens_detail)
    TextView tv_search_tokens_detail;
    @BindView(R.id.tv_forget_password)
    TextView tv_forget_password;
    @BindView(R.id.tv_forget_password_detail)
    TextView tv_forget_password_detail;
    @BindView(R.id.tv_how_use_wallet)
    TextView tv_how_use_wallet;
    @BindView(R.id.tv_how_use_wallet_detail)
    TextView tv_how_use_wallet_detail;
    @BindView(R.id.tv_how_backup_mnemonic)
    TextView tv_how_backup_mnemonic;
    @BindView(R.id.tv_how_backup_mnemonic_detail)
    TextView tv_how_backup_mnemonic_detail;


    @BindView(R.id.tv_transferfragment_tip03)
    TextView tv_transferfragment_tip03;
    @BindView(R.id.tv_transferfragment_tip02)
    TextView tv_transferfragment_tip02;

    public static HelpFragment newInstance() {
        return new HelpFragment();
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_help;
    }

    @Override
    public void process() {
        initToolbarNav(mToolbar, R.string.help, true);

        tv_title_right.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.email, 0);

        setView(tv_how_wallet, tv_how_wallet_detail);
    }

    private void setView(TextView textView, TextView textViewDetail) {
        tv_how_wallet.setSelected(false);
        tv_search_tokens.setSelected(false);
        tv_forget_password.setSelected(false);
        tv_how_use_wallet.setSelected(false);
        tv_how_backup_mnemonic.setSelected(false);
        tv_transferfragment_tip03.setSelected(false);

        tv_how_wallet_detail.setVisibility(View.GONE);
        tv_search_tokens_detail.setVisibility(View.GONE);
        tv_forget_password_detail.setVisibility(View.GONE);
        tv_how_use_wallet_detail.setVisibility(View.GONE);
        tv_how_backup_mnemonic_detail.setVisibility(View.GONE);
        tv_transferfragment_tip02.setVisibility(View.GONE);

        textView.setSelected(true);
        textViewDetail.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.tv_how_wallet, R.id.tv_search_tokens, R.id.tv_forget_password, R.id.tv_how_use_wallet, R.id.tv_how_backup_mnemonic,R.id.tv_transferfragment_tip03,
            R.id.tv_title_right})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.tv_how_wallet:
                setView(tv_how_wallet, tv_how_wallet_detail);
                break;
            case R.id.tv_search_tokens:
                setView(tv_search_tokens, tv_search_tokens_detail);
                break;
            case R.id.tv_forget_password:
                setView(tv_forget_password, tv_forget_password_detail);
                break;
            case R.id.tv_how_use_wallet:
                setView(tv_how_use_wallet, tv_how_use_wallet_detail);
                break;
            case R.id.tv_how_backup_mnemonic:
                setView(tv_how_backup_mnemonic, tv_how_backup_mnemonic_detail);
                break;

            case R.id.tv_transferfragment_tip03:
                setView(tv_transferfragment_tip03, tv_transferfragment_tip02);
                break;

            case R.id.tv_title_right:
                start(FeedbackFragment.newInstance());
                break;
        }
    }

}
