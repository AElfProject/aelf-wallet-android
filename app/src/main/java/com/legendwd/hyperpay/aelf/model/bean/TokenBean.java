package com.legendwd.hyperpay.aelf.model.bean;

/**
 * @author lovelyzxing
 * @date 2019/6/5
 * @Description
 */
public class TokenBean {

    public String coinAddress;

    public String coin;

    public String address;

    public String logo;

    public boolean isAdded;

    public String balance;

    @Override
    public String toString() {
        return "TokenBean{" +
                "coinAddress='" + coinAddress + '\'' +
                ", coin='" + coin + '\'' +
                ", address='" + address + '\'' +
                ", logo='" + logo + '\'' +
                ", isAdded=" + isAdded +
                ", balance='" + balance + '\'' +
                '}';
    }
}
