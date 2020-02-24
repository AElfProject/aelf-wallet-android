package com.legendwd.hyperpay.aelf.business.discover.dapp;

/**
 * @author Colin
 * @date 2019/10/16.
 * description：
 */
public class GameData {
    public String accountName;
    public double balance;
    public String symbol;
    public String txId;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }
}
