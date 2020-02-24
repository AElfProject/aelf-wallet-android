package com.legendwd.hyperpay.aelf.dialogfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseDialogFragment;

import butterknife.BindView;

/**
 * @author Colin
 * @date 2019/10/9.
 * description： 交易链弹框
 */
public class TransMsgDialog extends BaseDialogFragment {
    @BindView(R.id.tx_chain_from)
    TextView tx_chain_from;
    @BindView(R.id.tx_to_from)
    TextView tx_to_from;
    @BindView(R.id.dialog_chain_next)
    TextView dialog_chain_next;

    private TransClick mTransClick;
    private String mfrom;
    private String mTo;

    @Override
    public int getLayoutId() {
        return R.layout.dialog_msg_chain;
    }

    public void setClock(TransClick clock) {
        this.mTransClick = clock;

    }

    public void setChainData(String from, String to) {
        this.mfrom = from;
        this.mTo = to;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tx_chain_from.setText(mfrom);
        tx_to_from.setText(mTo);
        dialog_chain_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mTransClick.trans();
            }
        });

    }

    public interface TransClick {
        void trans();
    }

}
