package com.legendwd.hyperpay.aelf.model.bean;

/**
 * @author Colin
 * @date 2019/8/5.
 * description：keyStore Moudle
 */
public class KeyStoreBean {
    private String type;
    private String nickName;
    private String address;
    private crypto mCrypto;

    class crypto {
        private String version;
        private String cipher;
        private String cipherparams;
        private String mnemonicEncrypted;
        private String privateKeyEncrypted;
        private String kdf;
        private kdfparams mKdfparams;

        class kdfparams {
            private String r;
            private String n;
            private String p;
            private String dkLen;
            private String salt;

            @Override
            public String toString() {
                return "kdfparams{" +
                        "r='" + r + '\'' +
                        ", n='" + n + '\'' +
                        ", p='" + p + '\'' +
                        ", dkLen='" + dkLen + '\'' +
                        ", salt='" + salt + '\'' +
                        '}';
            }
        }

        private String mac;

        @Override
        public String toString() {
            return "crypto{" +
                    "version='" + version + '\'' +
                    ", cipher='" + cipher + '\'' +
                    ", cipherparams='" + cipherparams + '\'' +
                    ", mnemonicEncrypted='" + mnemonicEncrypted + '\'' +
                    ", privateKeyEncrypted='" + privateKeyEncrypted + '\'' +
                    ", kdf='" + kdf + '\'' +
                    ", mKdfparams=" + mKdfparams +
                    ", mac='" + mac + '\'' +
                    '}';
        }
    }

    private String password;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public crypto getCrypto() {
        return mCrypto;
    }

    public void setCrypto(crypto crypto) {
        mCrypto = crypto;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "KeyStoreBean{" +
                "type='" + type + '\'' +
                ", nickName='" + nickName + '\'' +
                ", address='" + address + '\'' +
                ", mCrypto=" + mCrypto +
                ", password='" + password + '\'' +
                '}';
    }
}
