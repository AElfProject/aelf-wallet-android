package com.legendwd.hyperpay.aelf.dialogfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseDialogFragment;
import com.legendwd.hyperpay.aelf.business.wallet.adapters.ChooseChainDialogAdapter;
import com.legendwd.hyperpay.aelf.model.bean.ChooseChainsBean;
import com.legendwd.hyperpay.aelf.widget.ClassicsFooter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;

/**
 * @author Colin
 * @date 2019/10/9.
 * description： 选择链的dialog
 */
public class ChainDialog extends BaseDialogFragment {
    @BindView(R.id.smt_chain_dialog)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.rv_choose_check)
    RecyclerView rv_choose_check;
    @BindView(R.id.check_chain_close)
    ImageView check_chain_close;
    private ChooseChainDialogAdapter mChooseChainAdapter; //popup adapter
    private List<ChooseChainsBean> mChainAddressBeans;
    private ChainClick mChainClick;


    @Override
    public int getLayoutId() {
        return R.layout.dialog_check_chain;
    }

    public void setClock(ChainClick clock) {
        this.mChainClick = clock;

    }

    public void setChainData(List<ChooseChainsBean> data) {
        this.mChainAddressBeans = data;
        if (mChooseChainAdapter != null) {
            mChooseChainAdapter.refreshView(mChainAddressBeans);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSmartRefreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        rv_choose_check.setLayoutManager(new LinearLayoutManager(getContext()));
        mChooseChainAdapter = new ChooseChainDialogAdapter(mChainAddressBeans);
        rv_choose_check.setAdapter(mChooseChainAdapter);
        mChooseChainAdapter.setOnItemClickListener(o -> {
            if (o == null || mChainAddressBeans == null || mChainAddressBeans.size() == 0) return;
            ChooseChainsBean data = mChainAddressBeans.get((Integer) o);
            if (data == null) return;
            mChainClick.clickItem(data);
            dismiss();
        });
        check_chain_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public interface ChainClick {
        void clickItem(ChooseChainsBean itemData);
    }

}
