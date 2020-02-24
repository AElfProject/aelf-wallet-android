package com.legendwd.hyperpay.aelf.model.bean;

import com.legendwd.hyperpay.aelf.base.BaseAdapterModel;

public class AssetsBean extends BaseAdapterModel {
    /**
     * contract_address : WnV9Gv3gioSh3Vgaw8SSB96nV8fWUNxuVozCf6Y14e7RXyGaM
     * symbol : ELF
     * chain_id : AELF
     * block_hash : inner
     * tx_id : inner
     * name : elf token
     * total_supply : 1000000000
     * decimals : 2
     * logo : http://hyperpay.oss-ap-southeast-1.aliyuncs.com/onchain.default.png
     * in : 1
     */

    private String contract_address;
    private String symbol;
    private String chain_id;
    private String block_hash;
    private String tx_id;
    private String name;
    private Long total_supply;
    private int decimals;
    private String logo;
    private int in;
    private String sortKey;
    private String balance;

    public String getBalance() {
        return balance;
    }

    public String getContractAddress() {
        return contract_address;
    }

    public void setContractAddress(String contract_address) {
        this.contract_address = contract_address;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getChainId() {
        return chain_id;
    }

    public void setChainId(String chain_id) {
        this.chain_id = chain_id;
    }

    public String getBlockHash() {
        return block_hash;
    }

    public void setBlockHash(String block_hash) {
        this.block_hash = block_hash;
    }

    public String getTxId() {
        return tx_id;
    }

    public void setTxId(String tx_id) {
        this.tx_id = tx_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTotal_supply() {
        return total_supply;
    }

    public void setTotal_supply(Long total_supply) {
        this.total_supply = total_supply;
    }

    public int getDecimals() {
        return decimals;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getIn() {
        return in;
    }

    public void setIn(int in) {
        this.in = in;
    }
}
