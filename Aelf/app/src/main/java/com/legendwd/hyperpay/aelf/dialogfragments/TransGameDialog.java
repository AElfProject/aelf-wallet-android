package com.legendwd.hyperpay.aelf.dialogfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseDialogFragment;

import butterknife.BindView;

/**
 * @author Colin
 * @date 2019/10/9.
 * description： Dapp游戏交易框
 */
public class TransGameDialog extends BaseDialogFragment {
    @BindView(R.id.tx_game_money)
    TextView tx_game_money;
    @BindView(R.id.tx_game_pay_from)
    TextView tx_game_pay_from;
    @BindView(R.id.tx_gamepay_to)
    TextView tx_gamepay_to;
    @BindView(R.id.tx_game_taxid)
    TextView tx_game_taxid;
    @BindView(R.id.tx_game_block)
    TextView tx_game_block;
    @BindView(R.id.chb_white_name)
    CheckBox chb_white_name;
    @BindView(R.id.dialog_chain_next)
    TextView dialog_chain_next;

    private TransGameClick mTransClick;
    private String mBlance, mFrom, mTo, mTaxid, mBlock;

    private boolean mIsChecked;


    @Override
    public int getLayoutId() {
        return R.layout.dialog_game_transchain;
    }

    public void setClock(TransGameClick clock) {
        this.mTransClick = clock;
    }

    public void setChainData(String blance, String from, String to, String taxid, String block,boolean isChecked) {
        this.mBlance = blance;
        this.mFrom = from;
        this.mTo = to;
        this.mTaxid = taxid;
        this.mBlock = block;
        this.mIsChecked = isChecked;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tx_game_money.setText(mBlance);
        tx_game_pay_from.setText(mFrom);
        tx_gamepay_to.setText(mTo);
        tx_game_taxid.setText(mTaxid);
        tx_game_block.setText(mBlock);
        chb_white_name.setChecked(mIsChecked);
        dialog_chain_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTransClick!=null){
                    mTransClick.transClick(chb_white_name.isChecked());
                }
                dismiss();
            }
        });
    }

    public interface TransGameClick {
        void transClick(boolean isCheck);
    }

}
