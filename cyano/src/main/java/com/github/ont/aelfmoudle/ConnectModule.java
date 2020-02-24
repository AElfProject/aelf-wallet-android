package com.github.ont.aelfmoudle;

/**
 * @author Colin
 * @date 2019/11/20.
 * description： dapp与客户端进行初始化连接交换公钥
 */
public class ConnectModule {
    private String id; // request id, 用于区分是哪个request，每次请求都产生随机的id
    private String appId;// app id
    private String action;
    private ParamsBean params;

    public class ParamsBean {
        public String encryptAlgorithm;
        public String publicKey;
        public String random;
        public String signature;

        public String getEncryptAlgorithm() {
            return encryptAlgorithm;
        }

        public void setEncryptAlgorithm(String encryptAlgorithm) {
            this.encryptAlgorithm = encryptAlgorithm;
        }

        public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }

        public String getRandom() {
            return random;
        }

        public void setRandom(String random) {
            this.random = random;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "ConnectModule{" +
                "id='" + id + '\'' +
                ", appId='" + appId + '\'' +
                ", action='" + action + '\'' +
                ", params=" + params +
                '}';
    }
}
