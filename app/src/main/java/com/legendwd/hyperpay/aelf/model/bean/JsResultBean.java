package com.legendwd.hyperpay.aelf.model.bean;

import com.legendwd.hyperpay.aelf.util.StringUtil;
import com.legendwd.hyperpay.lib.Constant;

import java.util.List;

/**
 * @author myth_li
 * @date 2020/4/9
 * description:
 */
public class JsResultBean {
    private String txId;
    private String err;
    private int success;
    private List<Fee> fee;

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public List<Fee> getFee() {
        return fee;
    }

    public void setFee(List<Fee> fee) {
        this.fee = fee;
    }

    public static class Fee{
        String symbol;
        int amount;
        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getAmount() {
            return StringUtil.divideDataString(Constant.DEFAULT_DECIMALS,
                    String.valueOf(amount)) +" " + symbol;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }
}
