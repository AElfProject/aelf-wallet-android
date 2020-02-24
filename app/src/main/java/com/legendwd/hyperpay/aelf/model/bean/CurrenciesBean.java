package com.legendwd.hyperpay.aelf.model.bean;

import java.util.List;

/**
 * @author lovelyzxing
 * @date 2019/6/10
 * @Description
 */
public class CurrenciesBean {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * id : USD
         * name : 美元
         */

        public String id;
        public String name;
        public String symbol;
        public boolean isSelected;
    }
}
