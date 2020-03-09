package com.legendwd.hyperpay.aelf.model.bean;

import com.legendwd.hyperpay.aelf.base.BaseAdapterModel;

/**
 * @author Colin
 * @date 2019/9/26.
 * description：
 */
public class ChooseChainsBean extends BaseAdapterModel {
    String type;
    String name;
    String contract_address;
    String node;
    String symbol;
    String logo;
    String color;
    String issueid;
    String explorer;
    String crossChainContractAddress;
    String transferCoins;

    public String getCrossChainContractAddress() {
        return crossChainContractAddress;
    }

    public void setCrossChainContractAddress(String crossChainContractAddress) {
        this.crossChainContractAddress = crossChainContractAddress;
    }

    public String getExplorer() {
        return explorer;
    }

    public void setExplorer(String explorer) {
        this.explorer = explorer;
    }

    public String getIssueid() {
        return issueid;
    }

    public void setIssueid(String issueid) {
        this.issueid = issueid;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContract_address() {
        return contract_address;
    }

    public void setContract_address(String contract_address) {
        this.contract_address = contract_address;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getTransferCoins() {
        return transferCoins;
    }

    public void setTransferCoins(String transferCoins) {
        this.transferCoins = transferCoins;
    }

    @Override
    public String toString() {
        return "ChooseChainsBean{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", contract_address='" + contract_address + '\'' +
                ", node='" + node + '\'' +
                '}';
    }

}
