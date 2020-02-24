package com.legendwd.hyperpay.aelf.model.bean;

import java.util.List;

/**
 * created by joseph at 2019/6/17
 */

public class CoinDetailBean {


    /**
     * name : BTC
     * name_en : Bitcoin
     * name_cn : 比特币
     * highest_price : 61850.464
     * logo : http://hpymarket.oss-ap-southeast-1.aliyuncs.com/images/coins/BTC.png
     * vol : 2326263
     * vol_trans : 2.33M
     * circulate : 17.73M
     * supply : 21.00M
     * turnover_rate : 0.1312
     * circulate_rate : 0.8444
     * exchange_count : 206
     * pub_time : 2008-11-01
     * market_value_order : 1
     * market_value : 975.45B
     * global_rate : 0.5575
     * profile : Bitcoin is a type of virtual money which means; in the event that you have Bitcoin (we will get to know how you acquire Bitcoin later in the section), you dont physically buy merchandise by giving notes or tokens to the vender. Bitcoin are utilized for electronic buys and exchanges. You can utilize Bitcoin to pay companions, dealers, and so forth. Each and every buy is promptly logged carefully (on PCs) on an exchange log that tracks the season of procurement and which person possesses what number of Bitcoin.
     * community : [{"logo":"http://hpymarket.oss-ap-southeast-1.aliyuncs.com/images/community/ic_facebook.png","url":"https://www.facebook.com/buy.bitcoin.news"},{"logo":"http://hpymarket.oss-ap-southeast-1.aliyuncs.com/images/community/ic_github.png","url":"https://github.com/bitcoin/bitcoin"},{"logo":"http://hpymarket.oss-ap-southeast-1.aliyuncs.com/images/community/ic_reddit.png","url":"https://www.reddit.com/r/bitcoin"},{"logo":"http://hpymarket.oss-ap-southeast-1.aliyuncs.com/images/community/ic_twitter.png","url":"https://twitter.com/bitcoin"}]
     * site : [{"name":"Official Website","url":"https://bitcoin.org/"},{"name":"Block Explorer","url":"https://www.blockchain.com/explorer"},{"name":"Whitepaper","url":"https://bitcoin.org/bitcoin.pdf"},{"name":"Forum","url":"https://bitcointalk.org/"}]
     */

    private String name;
    private String name_en;
    private String name_cn;
    private String highest_price;
    private String logo;
    private String vol;
    private String vol_trans;
    private String circulate;
    private String supply;
    private String turnover_rate;
    private String circulate_rate;
    private String exchange_count;
    private String pub_time;
    private String market_value_order;
    private String market_value;
    private String global_rate;
    private String profile;
    private List<CommunityBean> community;
    private List<SiteBean> site;
    private String usd_cny;

    public String getUsd_cny() {
        return usd_cny;
    }

    public void setUsd_cny(String usd_cny) {
        this.usd_cny = usd_cny;
    }

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

    public String getHighest_price() {
        return highest_price;
    }

    public void setHighest_price(String highest_price) {
        this.highest_price = highest_price;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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

    public String getCirculate() {
        return circulate;
    }

    public void setCirculate(String circulate) {
        this.circulate = circulate;
    }

    public String getSupply() {
        return supply;
    }

    public void setSupply(String supply) {
        this.supply = supply;
    }

    public String getTurnover_rate() {
        return turnover_rate;
    }

    public void setTurnover_rate(String turnover_rate) {
        this.turnover_rate = turnover_rate;
    }

    public String getCirculate_rate() {
        return circulate_rate;
    }

    public void setCirculate_rate(String circulate_rate) {
        this.circulate_rate = circulate_rate;
    }

    public String getExchange_count() {
        return exchange_count;
    }

    public void setExchange_count(String exchange_count) {
        this.exchange_count = exchange_count;
    }

    public String getPub_time() {
        return pub_time;
    }

    public void setPub_time(String pub_time) {
        this.pub_time = pub_time;
    }

    public String getMarket_value_order() {
        return market_value_order;
    }

    public void setMarket_value_order(String market_value_order) {
        this.market_value_order = market_value_order;
    }

    public String getMarket_value() {
        return market_value;
    }

    public void setMarket_value(String market_value) {
        this.market_value = market_value;
    }

    public String getGlobal_rate() {
        return global_rate;
    }

    public void setGlobal_rate(String global_rate) {
        this.global_rate = global_rate;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public List<CommunityBean> getCommunity() {
        return community;
    }

    public void setCommunity(List<CommunityBean> community) {
        this.community = community;
    }

    public List<SiteBean> getSite() {
        return site;
    }

    public void setSite(List<SiteBean> site) {
        this.site = site;
    }

    public static class CommunityBean {
        /**
         * logo : http://hpymarket.oss-ap-southeast-1.aliyuncs.com/images/community/ic_facebook.png
         * url : https://www.facebook.com/buy.bitcoin.news
         */

        private String logo;
        private String url;

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class SiteBean {
        /**
         * name : Official Website
         * url : https://bitcoin.org/
         */

        private String name;
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
