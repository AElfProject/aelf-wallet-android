package com.legendwd.hyperpay.aelf.business.discover.dapp;

import java.util.List;

/**
 * @author Colin
 * @date 2019/10/18.
 * description：
 */
public class GameTrBean {
    public int code;
    public TransData data;
    public List error;

    public class TransData {
        public String TransactionId;
        public String Status;
        public String Bloom;
        public String BlockNumber;
        public String BlockHash;
        public String ReturnValue;
        public String ReadableReturnValue;
        public String Error;
        public List<Logs> Logs;
        public Transact Transaction;

        public class Logs {
            public String Address;
            public String Name;
            public List<String> Indexed;
            public String NonIndexed;
        }

        public class Transact {
            public String From;
            public String To;
            public int RefBlockNumber;
            public String RefBlockPrefix;
            public String MethodName;
            public String Params;
            public String Signature;
        }
    }
}
