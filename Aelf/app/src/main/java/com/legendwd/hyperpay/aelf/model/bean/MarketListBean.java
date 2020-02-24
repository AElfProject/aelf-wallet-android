package com.legendwd.hyperpay.aelf.model.bean;

import com.legendwd.hyperpay.aelf.base.BaseAdapterModel;

import java.io.Serializable;
import java.util.List;

public class MarketListBean {


    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean extends BaseAdapterModel implements Serializable {

        /**
         * name : HPY
         * name_en : Hyperpay
         * name_cn : hyperpay
         * isAdd : 0
         * arrow : 1
         * increase : 0.01880
         * history_increase : -0.83757
         * vol : 2580884740
         * vol_trans : 25.81亿
         * amount : 2012363.8362342
         * amount_trans : 201.24万
         * market_value : 1653006
         * market_value_trans : 165.30万
         * last_update : 刚刚更新
         * last_price : 0.00077972
         * usd_price : 0.00077972
         * min_price : 0.00077972
         * max_price : 0.00077972
         * high_price : 0.005
         * logo : http://hpymarket.oss-ap-southeast-1.aliyuncs.com/images/coins/HPY.png
         * type : 1
         * date : 1575891557
         */

        private String name;
        private String name_en;
        private String name_cn;
        private String isAdd;
        private String arrow;
        private String increase;
        private String history_increase;
        private String vol;
        private String vol_trans;
        private String amount;
        private String amount_trans;
        private String market_value;
        private String market_value_trans;
        private String last_update;
        private String last_price;
        private String usd_price;
        private String min_price;
        private String max_price;
        private String high_price;
        private String logo;
        private String type;
        private String date;
        private boolean bStar;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName_en() {
            return name_en;
        }

        public void setName_en(String name_en) {
            this.name_en = name_en;
        }

        public String getName_cn() {
            return name_cn;
        }

        public void setName_cn(String name_cn) {
            this.name_cn = name_cn;
        }

        public String getIsAdd() {
            return isAdd;
        }

        public void setIsAdd(String isAdd) {
            this.isAdd = isAdd;
        }

        public String getArrow() {
            return arrow;
        }

        public void setArrow(String arrow) {
            this.arrow = arrow;
        }

        public String getIncrease() {
            return increase;
        }

        public void setIncrease(String increase) {
            this.increase = increase;
        }

        public String getHistory_increase() {
            return history_increase;
        }

        public void setHistory_increase(String history_increase) {
            this.history_increase = history_increase;
        }

        public String getVol() {
            return vol;
        }

        public void setVol(String vol) {
            this.vol = vol;
        }

        public String getVol_trans() {
            return vol_trans;
        }

        public void setVol_trans(String vol_trans) {
            this.vol_trans = vol_trans;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getAmount_trans() {
            return amount_trans;
        }

        public void setAmount_trans(String amount_trans) {
            this.amount_trans = amount_trans;
        }

        public String getMarket_value() {
            return market_value;
        }

        public void setMarket_value(String market_value) {
            this.market_value = market_value;
        }

        public String getMarket_value_trans() {
            return market_value_trans;
        }

        public void setMarket_value_trans(String market_value_trans) {
            this.market_value_trans = market_value_trans;
        }

        public String getLast_update() {
            return last_update;
        }

        public void setLast_update(String last_update) {
            this.last_update = last_update;
        }

        public String getLast_price() {
            return last_price;
        }

        public void setLast_price(String last_price) {
            this.last_price = last_price;
        }

        public String getUsd_price() {
            return usd_price;
        }

        public void setUsd_price(String usd_price) {
            this.usd_price = usd_price;
        }

        public String getMin_price() {
            return min_price;
        }

        public void setMin_price(String min_price) {
            this.min_price = min_price;
        }

        public String getMax_price() {
            return max_price;
        }

        public void setMax_price(String max_price) {
            this.max_price = max_price;
        }

        public String getHigh_price() {
            return high_price;
        }

        public void setHigh_price(String high_price) {
            this.high_price = high_price;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public boolean isStar() {
            return bStar;
        }

        public void setStar(boolean bStar) {
            this.bStar = bStar;
        }
    }

}
