package com.legendwd.hyperpay.aelf.business.discover.dapp;

import java.util.List;

/**
 * @author lovelyzxing
 * @date 2019/5/25
 * @Description
 */
public class InvokeTransactionBean {

    /**
     * action : invoke
     * version : 1.0.0
     * params : {"login":true,"url":"","message":"invoke smart contract test","invokeConfig":{"contractHash":"cae215265a5e348bfd603b8db22893aa74b42417","functions":[{"operation":"userOp","args":[{"value":"ByteArray:a873a135a098330f65550c0aa7368ab0b73d84f6"},{"value":20},{"value":1}]}],"payer":"AX8ZgpWYxxYZpNkaAHk4LbQS8mm88SEQ7y","gasPrice":500,"gasLimit":100000}}
     * id : 4r1l067h
     */

    public String action;
    public String version;
    public ParamsBean params;
    public String id;

    public static class ParamsBean {
        /**
         * login : true
         * url :
         * message : invoke smart contract test
         * invokeConfig : {"contractHash":"cae215265a5e348bfd603b8db22893aa74b42417","functions":[{"operation":"userOp","args":[{"value":"ByteArray:a873a135a098330f65550c0aa7368ab0b73d84f6"},{"value":20},{"value":1}]}],"payer":"AX8ZgpWYxxYZpNkaAHk4LbQS8mm88SEQ7y","gasPrice":500,"gasLimit":100000}
         */

        public boolean login;
        public String url;
        public String message;
        public InvokeConfigBean invokeConfig;

        public static class InvokeConfigBean {
            /**
             * contractHash : cae215265a5e348bfd603b8db22893aa74b42417
             * functions : [{"operation":"userOp","args":[{"value":"ByteArray:a873a135a098330f65550c0aa7368ab0b73d84f6"},{"value":20},{"value":1}]}]
             * payer : AX8ZgpWYxxYZpNkaAHk4LbQS8mm88SEQ7y
             * gasPrice : 500
             * gasLimit : 100000
             */

            public String contractHash;
            public String payer;
            public int gasPrice;
            public int gasLimit;
            public List<FunctionsBean> functions;

            public static class FunctionsBean {
                /**
                 * operation : userOp
                 * args : [{"value":"ByteArray:a873a135a098330f65550c0aa7368ab0b73d84f6"},{"value":20},{"value":1}]
                 */

                public String operation;
                public List<ArgsBean> args;

                public static class ArgsBean {
                    /**
                     * value : ByteArray:a873a135a098330f65550c0aa7368ab0b73d84f6
                     */

                    public String value;
                }
            }
        }
    }
}
