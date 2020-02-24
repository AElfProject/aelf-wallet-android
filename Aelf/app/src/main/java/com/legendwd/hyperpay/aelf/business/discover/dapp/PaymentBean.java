package com.legendwd.hyperpay.aelf.business.discover.dapp;

import java.util.List;

/**
 * @author lovelyzxing
 * @date 2019/5/27
 * @Description
 */
public class PaymentBean {

    /**
     * Notify : [{"States":["transfer","AX8ZgpWYxxYZpNkaAHk4LbQS8mm88SEQ7y","AdqVWVdZjCFTEJwXu7i4xKFJ8d4JP2CZ1e",20000000],"ContractAddress":"0200000000000000000000000000000000000000"},{"States":["3131315f7061796f72646572","f201f3832a95fe35cea4e97f930c38c1799420e6","a873a135a098330f65550c0aa7368ab0b73d84f6","3135353839323039373732353530393538343934323238323334","002d3101"],"ContractAddress":"e6209479c1380c937fe9a4ce35fe952a83f301f2"},{"States":"7472616e73666572204f6e672073756363656564","ContractAddress":"e6209479c1380c937fe9a4ce35fe952a83f301f2"}]
     * State : 1
     * Gas : 20000
     * Result : 01
     */

    public int State;
    public Long Gas;
    public String Result;
    public List<NotifyBean> Notify;

    public static class NotifyBean {
        /**
         * States : ["transfer","AX8ZgpWYxxYZpNkaAHk4LbQS8mm88SEQ7y","AdqVWVdZjCFTEJwXu7i4xKFJ8d4JP2CZ1e",20000000]
         * ContractAddress : 0200000000000000000000000000000000000000
         */

        public String ContractAddress;
        public List<String> States;
    }
}
