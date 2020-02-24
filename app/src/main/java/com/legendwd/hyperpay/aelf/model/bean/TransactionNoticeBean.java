package com.legendwd.hyperpay.aelf.model.bean;

import java.util.List;

public class TransactionNoticeBean {

    /**
     * count : 1
     * unread_count : 0
     */

    private int count;
    private int unread_count;
    private List<NoticeBean> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getUnreadCount() {
        return unread_count;
    }

    public void setUnreadCount(int unread_count) {
        this.unread_count = unread_count;
    }

    public List<NoticeBean> getList() {
        return list;
    }

    public void setList(List<NoticeBean> list) {
        this.list = list;
    }

    public static class NoticeBean {
        /**
         * chain : onchain
         * category : send
         * symbol :
         * txid : 7de7d1660af64316eae9655a0780773bc094532a1d3a42d35c285597223888f2
         * amount : 11.00000000
         * block :
         * time : 1556237109
         * nonce :
         * fee :
         * gasLimit :
         * gasPrice :
         * gasUsed :
         * status : 1
         * to : xw6U3FRE5H8rU3z8vAgF9ivnWSkxULK5cibdZzMC9UWf7rPJf
         * statusText : 转账成功
         * addressList : []
         * timeOffset : 1560389473
         * confirmations : 0
         * completed : 6
         * is_read : 1
         */
        private String id;
        private String chain;
        private String category;
        private String symbol;
        private String txid;
        private String amount;
        private String block;
        private String time;
        private String nonce;
        private String fee;
        private String gasLimit;
        private String gasPrice;
        private String gasUsed;
        private String status;
        private String from;
        private String to;
        private String statusText;
        private String timeOffset;
        private String confirmations;
        private String completed;
        private String is_read;
        private List<?> addressList;
        private String from_chainid;
        private String to_chainid;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getChain() {
            return chain;
        }

        public void setChain(String chain) {
            this.chain = chain;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getTxid() {
            return txid;
        }

        public void setTxid(String txid) {
            this.txid = txid;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getBlock() {
            return block;
        }

        public void setBlock(String block) {
            this.block = block;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getNonce() {
            return nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public String getGasLimit() {
            return gasLimit;
        }

        public void setGasLimit(String gasLimit) {
            this.gasLimit = gasLimit;
        }

        public String getGasPrice() {
            return gasPrice;
        }

        public void setGasPrice(String gasPrice) {
            this.gasPrice = gasPrice;
        }

        public String getGasUsed() {
            return gasUsed;
        }

        public void setGasUsed(String gasUsed) {
            this.gasUsed = gasUsed;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getStatusText() {
            return statusText;
        }

        public void setStatusText(String statusText) {
            this.statusText = statusText;
        }

        public String getTimeOffset() {
            return timeOffset;
        }

        public void setTimeOffset(String timeOffset) {
            this.timeOffset = timeOffset;
        }

        public String getConfirmations() {
            return confirmations;
        }

        public void setConfirmations(String confirmations) {
            this.confirmations = confirmations;
        }

        public String getCompleted() {
            return completed;
        }

        public void setCompleted(String completed) {
            this.completed = completed;
        }

        public String getIsRead() {
            return is_read;
        }

        public void setIsRead(String is_read) {
            this.is_read = is_read;
        }

        public List<?> getAddressList() {
            return addressList;
        }

        public void setAddressList(List<?> addressList) {
            this.addressList = addressList;
        }

        public String getFrom_chainid() {
            return from_chainid;
        }

        public void setFrom_chainid(String from_chainid) {
            this.from_chainid = from_chainid;
        }

        public String getTo_chainid() {
            return to_chainid;
        }

        public void setTo_chainid(String to_chainid) {
            this.to_chainid = to_chainid;
        }
    }
}
