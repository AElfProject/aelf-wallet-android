package com.legendwd.hyperpay.aelf.model.bean;

import com.legendwd.hyperpay.aelf.base.BaseAdapterModel;
import com.legendwd.hyperpay.aelf.util.StringUtil;
import com.legendwd.hyperpay.lib.Constant;

/**
 * @author Colin
 * @date 2019/9/26.
 * description：
 */
public class ChainAddressBean extends BaseAdapterModel {
    private String address;
    private String contractAddress;
    private String symbol;
    private String chain_id;
    private String balance;
    private String logo;
    private String type;
    private Rate rate;
    private String color;
    private int decimals = Constant.DEFAULT_DECIMALS;
    private String balance_0;
    private String issue_chain_id;

    public String getIssue_chain_id() {
        return issue_chain_id;
    }

    public void setIssue_chain_id(String issue_chain_id) {
        this.issue_chain_id = issue_chain_id;
    }

    public int getDecimals() {
        return decimals;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    public String getBalance_0() {
        return balance_0;
    }

    public void setBalance_0(String balance_0) {
        this.balance_0 = balance_0;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractaddress) {
        this.contractAddress = contractaddress;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }


    public String getBalance() {
        return StringUtil.formatDataNoZero(Constant.DEFAULT_DECIMALS, balance);
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }


    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public String getChain_id() {
        return chain_id;
    }

    public void setChain_id(String chain_id) {
        this.chain_id = chain_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "ChainAddressBean{" +
                "address='" + address + '\'' +
                ", contractaddress='" + contractAddress + '\'' +
                ", symbol='" + symbol + '\'' +
                ", chainid='" + chain_id + '\'' +
                ", balance='" + balance + '\'' +
                ", logo='" + logo + '\'' +
                ", mRate=" + rate +
                '}';
    }


    public class Rate {
        /**
         * price : 0.000
         * increace : 0
         * increace2 :
         */

        private String price;
        private String increace;
        private String increace2;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getIncreace() {
            return increace;
        }

        public void setIncreace(String increace) {
            this.increace = increace;
        }

        public String getIncreace2() {
            return increace2;
        }

        public void setIncreace2(String increace2) {
            this.increace2 = increace2;
        }

        @Override
        public String toString() {
            return "RateBean{" +
                    "price='" + price + '\'' +
                    ", increace='" + increace + '\'' +
                    ", increace2='" + increace2 + '\'' +
                    '}';
        }
    }
}
