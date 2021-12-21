package com.github.ont.cyano;

public class Constant {

    public static final String WALLET_FILE = "wallet file";
    public static final String DEFAULT_ADDRESS = "default address";
    public static final String DEFAULT_ONTID = "default ontId";
    public static final String DEFAULT_NET = "default net";
    public static final String ONT_ID_TRANSACTION = "ONT ID Transaction";
    public static final String TEST_PRIVATE_NETS = "test private nets";

    public static final String KEY = "key";
    public static final String ADDRESS = "address";

    public final static long ONG_TRAN = 1000000000;


    public static final String ONT = "ont";

    public static final String PAYER = "ATGJSGzm2poCB8N44BgrAccJcZ64MFf187";
    public static final int WIF_LENGTH = 52;
    public static final int KEY_LENGTH = 64;

    /**
     * 协议
     */
    public static final String PRIVACY_POLICY = "https://onto.app/privacy";
    public static final String TERMS_CONDITIONS = "https://onto.app/terms";
    public static final String WALLET_RECEIVER_URL = "http://www.baidu.com";
    public static final String CYANO_AUTH_URL = "https://auth.ont.io/#/authHome";
    public static final String CYANO_MANAGER_URL = "https://auth.ont.io/#/mgmtHome?ontid=";
    public static final String CYANO_VERSION_URL = "http://101.132.193.149:4027/cyanoversion";
    public static final String CYANO_DOWNLOAD_URL = "http://101.132.193.149/files/app-debug.apk";

    public static final String WHAT_WIF_URL_EN = "https://info.onto.app/#/detail/90";
    public static final String WHAT_WIF_URL_CN = "https://info.onto.app/#/detail/87";

    public static final String LOGIN = "login";
    public static final String INVOKE = "invoke";
    public static final String GET_ACCOUNT = "Account";
    public static final String INVOKE_READ = "invokeRead";
    public static final String INVOKE_PASSWORD_FREE = "invokePasswordFree";
    public static final String AUTHENTICATION = "authentication";
    public static final String AUTHORIZATION = "authorization";
    public static final String DECRYPT_MESSAGE = "decryptMessage";
    public static final String GET_IDENTITY = "getIdentity";
    public static final String CYANO_WEB_TAG = "aelf://aelf.io?";
    public static final String CYANO_SPLIT_TAG = "params=";
    public static final String CONNECT = "connect";//与客户端进行初始化连接交换公钥
    public static final String KEY_PAIR_UTILS = "keyPairUtils";

    //Error Code
    public static final int PARAMS_ERROR = 80001;
    public static final int METHOD_ERROR = 80002;
    public static final int INTERNAL_ERROR = 80003;
    public static final int CANCEL = 80004;
}
