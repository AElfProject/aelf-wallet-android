package com.legendwd.hyperpay.aelf.model.bean;

import com.legendwd.hyperpay.aelf.util.StringUtil;
import com.legendwd.hyperpay.lib.Constant;

import java.util.List;

/**
 * @author lovelyzxing
 * @date 2019/6/11
 * @Description
 */
public class TransactionBean {

    /**
     * count : 1
     * pageCount : 1
     * list : [{"chain":"onchain","category":"receive","symbol":"ELF","txid":"4fcb10034e5affb21319a121f46ff14d1102292884ecbf32f6fbe8681b51077b","amount":"200","block":819651,"time":1560169593,"nonce":"","fee":"","gasLimit":"","gasPrice":"","gasUsed":"","status":1,"from":"27BitpmfWdRCgpb8t6r11UrzKh1kx6KrLehLCx9CKTQWZPnMXz","to":"iuvGAJzQVbgPuuVsSMwT71BhewDKgAVHXfKmWuHkFGny6miYc","statusText":"Receive successful","addressList":[],"timeOffset":1560244606,"confirmations":0,"completed":6}]
     */

    public int count;
    public int pageCount;
    public List<ListBean> list;


    public static class ListBean {
        /**
         * chain : onchain
         * category : receive
         * symbol : ELF
         * txid : 4fcb10034e5affb21319a121f46ff14d1102292884ecbf32f6fbe8681b51077b
         * amount : 200
         * block : 819651
         * time : 1560169593
         * nonce :
         * fee :
         * gasLimit :
         * gasPrice :
         * gasUsed :
         * status : 1
         * from : 27BitpmfWdRCgpb8t6r11UrzKh1kx6KrLehLCx9CKTQWZPnMXz
         * to : iuvGAJzQVbgPuuVsSMwT71BhewDKgAVHXfKmWuHkFGny6miYc
         * statusText : Receive successful
         * addressList : []
         * timeOffset : 1560244606
         * confirmations : 0
         * completed : 6
         */

        public String chain;
        public String category;
        public String symbol;
        public String txid;
        private String amount;
        public String block;
        public String time;
        public String nonce;
        public String fee;
        public String feeSymbol;
        public String gasLimit;
        public String gasPrice;
        public String gasUsed;
        public String status;
        public String from;
        public String to;
        public String statusText;
        public String timeOffset;
        public String confirmations;
        public String completed;
        public String memo;
        public List<?> addressList;
        public String from_chainid;
        public String to_chainid;

        public RateBean rate;
        public UsdRateBean usd_rate;

        public static class RateBean {
            public String price;
            public String increace;
            public String increace2;
        }

        public static class UsdRateBean {
            public String price;
            public String increace;
            public String increace2;
        }

        public String getAmount() {
            return StringUtil.formatDataNoZero(Constant.DEFAULT_DECIMALS, amount);
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }


}
