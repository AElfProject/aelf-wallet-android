package com.legendwd.hyperpay.aelf.business.discover.dapp;

/**
 * @author lovelyzxing
 * @date 2019/5/25
 * @Description
 */
public class LoginBean {

    /**
     * action : login
     * version : 1.0.0
     * params : {"type":"account","dappName":"My dapp","dappIcon":"HyperDragons Go!","message":"22ce653091979901","expired":1558751775438,"callback":""}
     * needTimeout : false
     * id : msglz8tb
     */

    public String action;
    public String version;
    public ParamsBean params;
    public boolean needTimeout;
    public String id;

    public static class ParamsBean {
        /**
         * type : account
         * dappName : My dapp
         * dappIcon : HyperDragons Go!
         * message : 22ce653091979901
         * expired : 1558751775438
         * callback :
         */

        public String type;
        public String dappName;
        public String dappIcon;
        public String message;
        public long expired;
        public String callback;
    }
}
