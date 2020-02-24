package com.legendwd.hyperpay.aelf.model.bean;

import java.util.List;

/**
 * @author lovelyzxing
 * @date 2019/6/11
 * @Description
 */
public class WaitTransactionBean {
    public List<WaitListBean> list;

    public class WaitListBean {

        public String txid;
        public String from_chain;
        public String to_chain;
        public String from_address;
        public String to_address;
        public String time;
        public String amount;
        public String symbol;
        public String memo;
        public String from_node;
        public String to_node;
    }


}
