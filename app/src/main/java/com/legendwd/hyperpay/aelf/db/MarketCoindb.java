package com.legendwd.hyperpay.aelf.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @author myth_li
 * @date 2020/6/29
 * description:
 */
@Entity(nameInDb = "MarketCoin")
public class MarketCoindb {

    /**
     * id : 01coin
     * symbol : zoc
     * name : 01coin
     */
    @Id
    private String id;
    private String symbol;
    private String name;

    @Generated(hash = 1462436118)
    public MarketCoindb(String id, String symbol, String name) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
    }

    @Generated(hash = 2092169345)
    public MarketCoindb() {
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
}
