package com.legendwd.hyperpay.aelf.model.bean;

import com.google.gson.annotations.SerializedName;
import com.legendwd.hyperpay.aelf.base.BaseAdapterModel;
import com.legendwd.hyperpay.aelf.util.StringUtil;
import android.text.TextUtils;

import java.io.Serializable;

/**
 * @author myth_li
 * @date 2020/6/28
 * description:
 */
public class MarketDataBean extends BaseAdapterModel implements Serializable, Comparable<MarketDataBean> {

    /**
     * id : ethereum
     * symbol : eth
     * name : Ethereum
     * image : https://assets.coingecko.com/coins/images/279/large/ethereum.png?1547034048
     * current_price : 218.97
     * market_cap : 24479912793
     * market_cap_rank : 2
     * total_volume : 6131017220
     * high_24h : 231.07
     * low_24h : 217.54
     * price_change_24h : -10.68479909
     * price_change_percentage_24h : -4.65249
     * market_cap_change_24h : -1.14526100161957E9
     * market_cap_change_percentage_24h : -4.46928
     * circulating_supply : 1.115320561865E8
     * total_supply : null
     * ath : 1448.18
     * ath_change_percentage : -84.84695
     * ath_date : 2018-01-13T00:00:00.000Z
     * atl : 0.432979
     * atl_change_percentage : 50582.27236
     * atl_date : 2015-10-20T00:00:00.000Z
     * roi : {"times":31.633945460536996,"currency":"btc","percentage":3163.3945460536993}
     * last_updated : 2020-06-28T05:56:33.371Z
     */

    private String id;
    private String symbol;
    private String name;
    private String image;
    @SerializedName("current_price")
    private String currentPrice;
    @SerializedName("market_cap")
    private String marketCap;
    @SerializedName("market_cap_rank")
    private String marketCapRank;
    @SerializedName("total_volume")
    private String totalVolume;
    @SerializedName("high_24h")
    private String high24h;
    @SerializedName("low_24h")
    private String low24h;
    @SerializedName("price_change_24h")
    private String priceChange24h;
    @SerializedName("price_change_percentage_24h")
    private String priceChangePercentage24h;
    @SerializedName("market_cap_change_24h")
    private String marketCapChange24h;
    @SerializedName("market_cap_change_percentage_24h")
    private String marketCapChangePercentage24h;
    @SerializedName("circulating_supply")
    private String circulatingSupply;
    @SerializedName("total_supply")
    private String totalSupply;
    private String ath;
    @SerializedName("ath_change_percentage")
    private String athChangePercentage;
    @SerializedName("ath_date")
    private String athDate;
    private String atl;
    @SerializedName("atl_change_percentage")
    private String atlChangePercentage;
    @SerializedName("atl_date")
    private String atlDate;
    private RoiBean roi;
    @SerializedName("last_updated")
    private String lastUpdated;
    private boolean bStar;
    public static int mSort = -1;

    @Override
    public int compareTo(MarketDataBean o) {
        switch (mSort) {
            case 0:
                return StringUtil.parseDoubleU(currentPrice)
                        .compareTo(StringUtil.parseDoubleU(o.currentPrice));
            case 1:
                return StringUtil.parseDoubleU(o.currentPrice)
                        .compareTo(StringUtil.parseDoubleU(currentPrice));
            case 2:
                return StringUtil.parseDoubleU(priceChangePercentage24h)
                        .compareTo(StringUtil.parseDoubleU(o.priceChangePercentage24h));
            case 3:
                return StringUtil.parseDoubleU(o.priceChangePercentage24h)
                        .compareTo(StringUtil.parseDoubleU(priceChangePercentage24h));
            default:
                return StringUtil.parseDoubleU(o.marketCap)
                        .compareTo(StringUtil.parseDoubleU(marketCap));
        }
    }

    public boolean isStar() {
        return bStar;
    }

    public void setStar(boolean bStar) {
        this.bStar = bStar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(String marketCap) {
        this.marketCap = marketCap;
    }

    public String getMarketCapRank() {
        return marketCapRank;
    }

    public void setMarketCapRank(String marketCapRank) {
        this.marketCapRank = marketCapRank;
    }

    public String getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(String totalVolume) {
        this.totalVolume = totalVolume;
    }

    public String getHigh24h() {
        return high24h;
    }

    public void setHigh24h(String high24h) {
        this.high24h = high24h;
    }

    public String getLow24h() {
        return low24h;
    }

    public void setLow24h(String low24h) {
        this.low24h = low24h;
    }

    public String getPriceChange24h() {
        return priceChange24h;
    }

    public void setPriceChange24h(String priceChange24h) {
        this.priceChange24h = priceChange24h;
    }

    public String getPriceChangePercentage24h() {
        return priceChangePercentage24h;
    }

    public void setPriceChangePercentage24h(String priceChangePercentage24h) {
        this.priceChangePercentage24h = priceChangePercentage24h;
    }

    public String getMarketCapChange24h() {
        return marketCapChange24h;
    }

    public void setMarketCapChange24h(String marketCapChange24h) {
        this.marketCapChange24h = marketCapChange24h;
    }

    public String getMarketCapChangePercentage24h() {
        return marketCapChangePercentage24h;
    }

    public void setMarketCapChangePercentage24h(String marketCapChangePercentage24h) {
        this.marketCapChangePercentage24h = marketCapChangePercentage24h;
    }

    public String getCirculatingSupply() {
        return circulatingSupply;
    }

    public void setCirculatingSupply(String circulatingSupply) {
        this.circulatingSupply = circulatingSupply;
    }

    public String getTotalSupply() {
        if(TextUtils.isEmpty(totalSupply)){
            return "--";
        }
        return totalSupply ;
    }

    public void setTotalSupply(String totalSupply) {
        this.totalSupply = totalSupply;
    }

    public String getAth() {
        return ath;
    }

    public void setAth(String ath) {
        this.ath = ath;
    }

    public String getAthChangePercentage() {
        return athChangePercentage;
    }

    public void setAthChangePercentage(String athChangePercentage) {
        this.athChangePercentage = athChangePercentage;
    }

    public String getAthDate() {
        return athDate;
    }

    public void setAthDate(String athDate) {
        this.athDate = athDate;
    }

    public String getAtl() {
        return atl;
    }

    public void setAtl(String atl) {
        this.atl = atl;
    }

    public String getAtlChangePercentage() {
        return atlChangePercentage;
    }

    public void setAtlChangePercentage(String atlChangePercentage) {
        this.atlChangePercentage = atlChangePercentage;
    }

    public String getAtlDate() {
        return atlDate;
    }

    public void setAtlDate(String atlDate) {
        this.atlDate = atlDate;
    }

    public RoiBean getRoi() {
        return roi;
    }

    public void setRoi(RoiBean roi) {
        this.roi = roi;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public static class RoiBean {
        /**
         * times : 31.633945460536996
         * currency : btc
         * percentage : 3163.3945460536993
         */

        private double times;
        private String currency;
        private double percentage;

        public double getTimes() {
            return times;
        }

        public void setTimes(double times) {
            this.times = times;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public double getPercentage() {
            return percentage;
        }

        public void setPercentage(double percentage) {
            this.percentage = percentage;
        }
    }
}
