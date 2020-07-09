package com.legendwd.hyperpay.aelf.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author lovelyzxing
 * @date 2019/6/11
 * @Description
 */
public class MarketLineBean {

    private List<String[]> prices;
    @SerializedName("market_caps")
    private List<String[]> marketCaps;
    @SerializedName("total_volumes")
    private List<String[]> totalVolumes;

    public List<String[]> getPrices() {
        return prices;
    }

    public void setPrices(List<String[]> prices) {
        this.prices = prices;
    }

    public List<String[]> getMarketCaps() {
        return marketCaps;
    }

    public void setMarketCaps(List<String[]> marketCaps) {
        this.marketCaps = marketCaps;
    }

    public List<String[]> getTotalVolumes() {
        return totalVolumes;
    }

    public void setTotalVolumes(List<String[]> totalVolumes) {
        this.totalVolumes = totalVolumes;
    }
}
