package com.legendwd.hyperpay.aelf.model.bean;

import java.util.List;

public class AssetsListBean {


    /**
     * list : {"AELF":[{"amount":"0","balance":"0.00","income":"0","expenditure":"0","type":"main","chain_id":"AELF","name":"elf token","symbol":"ELF","decimals":"2","contractAddress":"WnV9Gv3gioSh3Vgaw8SSB96nV8fWUNxuVozCf6Y14e7RXyGaM","logo":"http://hyperpay.oss-ap-southeast-1.aliyuncs.com/onchain.default.png","rate":{"price":"0.000","increace":"0","increace2":""}}]}
     * fee : [{"id":"15","fee":"0.00050000","coin":"elf"}]
     * chain : ["AELF"]
     */

    private List<ChainAddressBean> list;
    private List<FeeBean> fee;
    private List<String> chain;

    public List<ChainAddressBean> getList() {
        return list;
    }

    public void setList(List<ChainAddressBean> list) {
        this.list = list;
    }

    public List<FeeBean> getFee() {
        return fee;
    }

    public void setFee(List<FeeBean> fee) {
        this.fee = fee;
    }

    public List<String> getChain() {
        return chain;
    }

    public void setChain(List<String> chain) {
        this.chain = chain;
    }


    public static class FeeBean {
        /**
         * id : 15
         * fee : 0.00050000
         * coin : elf
         */

        private String id;
        private String fee;
        private String coin;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }
    }
}
