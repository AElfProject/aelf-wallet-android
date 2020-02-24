package com.legendwd.hyperpay.aelf.model.bean;

import com.legendwd.hyperpay.aelf.model.CountrySortToken;

import java.util.List;

/**
 * @author lovelyzxing
 * @date 2019/6/11
 * @Description
 */
public class AddressBookBean {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * id : 8
         * name : 阿曼达
         * fc : A
         * address : xw6U3FRE5H8rU3z8vAgF9ivnWSkxULK5cibdZzMC9UWf7rPJf
         * note :
         * create_time : 1559715609
         */

        public String id;
        public String name;
        public String fc;
        public String address;
        public String note;
        public String create_time;
        public String showAddress;

        public String remark;

        public CountrySortToken sortToken = new CountrySortToken();

        public String countrySortKey;

        public String sortLetters;

    }
}
