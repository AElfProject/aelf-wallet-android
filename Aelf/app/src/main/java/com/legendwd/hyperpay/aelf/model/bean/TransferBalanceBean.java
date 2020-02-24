package com.legendwd.hyperpay.aelf.model.bean;

import com.legendwd.hyperpay.aelf.util.StringUtil;
import com.legendwd.hyperpay.lib.Constant;

import java.util.List;

/**
 * @author lovelyzxing
 * @date 2019/6/18
 * @Description
 */
public class TransferBalanceBean {

    /**
     * TransactionId : d6a3b9261d2e0a38a8c42d8c3aed7c53545a406d70d25b1d56cc46d627def66f
     * Status : Pending
     * balance : {"balance":0}
     * fee : [{"id":"15","fee":"0.00050000","coin":"elf"}]
     * rate : {"price":"0.206","increace":"0","increace2":"-0.01227"}
     */

    public String TransactionId;
    public String Status;
    public BalanceBean balance;
    public RateBean rate;
    public RateBean usd_rate;
    public List<FeeBean> fee;

    public static class BalanceBean {
        /**
         * balance : 0
         */

        private String balance;
        public String symbol;
        public String owner;

        public String getBalance() {
            return StringUtil.formatDataNoZero(Constant.DEFAULT_DECIMALS, balance);
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }
    }

    public static class RateBean {
        /**
         * price : 0.206
         * increace : 0
         * increace2 : -0.01227
         */

        public String price;
        public String increace;
        public String increace2;
    }

    public static class FeeBean {
        /**
         * id : 15
         * fee : 0.00050000
         * coin : elf
         */

        public String id;
        public String fee;
        public String coin;
    }
}
