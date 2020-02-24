package com.legendwd.hyperpay.aelf.model.bean;

public class TransferBean {

    private String privateKey;
    private String toAddress;
    private String amount;
    private String symbol;
    private String memo;
    private String fromNode;
    private String toNode;
    private String nodeUrl;
    private String contractAt;
    private String mainChainId;
    private String issueChainId;
    private String sendNode;
    private String txid;
    private String fromChain;
    private String toChain;
    private String fromTokenContractAddres;
    private String fromChainContractAddres;
    private String toTokenContractAddres;
    private String toChainContractAddres;

    public String getFromChain() {
        return fromChain;
    }

    public void setFromChain(String fromChain) {
        this.fromChain = fromChain;
    }

    public String getToChain() {
        return toChain;
    }

    public void setToChain(String toChain) {
        this.toChain = toChain;
    }

    public String getFromTokenContractAddres() {
        return fromTokenContractAddres;
    }

    public void setFromTokenContractAddres(String fromTokenContractAddres) {
        this.fromTokenContractAddres = fromTokenContractAddres;
    }

    public String getFromChainContractAddres() {
        return fromChainContractAddres;
    }

    public void setFromChainContractAddres(String fromChainContractAddres) {
        this.fromChainContractAddres = fromChainContractAddres;
    }

    public String getToTokenContractAddres() {
        return toTokenContractAddres;
    }

    public void setToTokenContractAddres(String toTokenContractAddres) {
        this.toTokenContractAddres = toTokenContractAddres;
    }

    public String getToChainContractAddres() {
        return toChainContractAddres;
    }

    public void setToChainContractAddres(String toChainContractAddres) {
        this.toChainContractAddres = toChainContractAddres;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getSendNode() {
        return sendNode;
    }

    public void setSendNode(String sendNode) {
        this.sendNode = sendNode;
    }

    public String getSymbol() {
        return symbol;
    }


    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNodeUrl() {
        return nodeUrl;
    }

    public void setNodeUrl(String nodeUrl) {
        this.nodeUrl = nodeUrl;
    }

    public String getContractAt() {
        return contractAt;
    }

    public void setContractAt(String contractAt) {
        this.contractAt = contractAt;
    }

    public String getMemo() {
        return memo;
    }

    public String getFromNode() {
        return fromNode;
    }

    public void setFromNode(String fromNode) {
        this.fromNode = fromNode;
    }

    public String getToNode() {
        return toNode;
    }

    public void setToNode(String toNode) {
        this.toNode = toNode;
    }

    public String getMainChainId() {
        return mainChainId;
    }

    public void setMainChainId(String mainChainId) {
        this.mainChainId = mainChainId;
    }

    public String getIssueChainId() {
        return issueChainId;
    }

    public void setIssueChainId(String issueChainId) {
        this.issueChainId = issueChainId;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
