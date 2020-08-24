package com.legendwd.hyperpay.lib;

/**
 * @author lovelyzxing
 * @date 2019/6/5
 * @Description
 */
public class Constant {

    public final static String sharePath = "/images";
    public final static String DEFAULT_CURRENCY = "USD";
    public static final String DB_NAME = "aelf.db";
    public static String lang = "";
    public static int sScreenH = 0;
    public final static Integer DEFAULT_DECIMALS = 8;
    public final static String DEFAULT_CHAIN_ID = "AELF";
    public final static String DEFAULT_PREFIX = "ELF_";

    public final static String MARKET_PRICE = "PRICE_";

    public final static String IP_MATCHER = "(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d+)";
    public final static String URL_MATCHER = "^(https?|http)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    public interface Sp {
        // common
        String UUID_PRE = "uuid_pre";
        String UDID = "udid";
        String SET_LANGUAGE = "set_language";
        String UNREAD_COUNT = "unread_count";
        String TOUCH_ID = "touch_id";
        String TOUCH_ID_FAILED = "touch_id_failed";
        String ACCOUNT_PORTRAIT = "account_portrait";
        String ACCOUNT_NAME = "account_name";
        String PRIVATE_MODE = "private_mode";
        String DEVICE_TOKEN = "deviceToken";
        String TOUCH_PASSWORD = "touch_password";


        // token
        String PRICING_CURRENCY_DEFAULT = "pricing_currency_default";  // select currency
        String PRICING_CURRENCY_SYMBOL_DEFAULT = "pricing_currency_symbol_default"; // currency symbol
        String PRICING_CURRENCY_ID_DEFAULT = "pricing_currency_id_default";  // currency id
        String PRICING_CURRENCY = "pricing_currency"; // currency list
        //0是按资产 1是按链
        String ASSETS_DISPLAY_INT = "assets_display_int";

        // wallet
        String CHAIN_ID = "chain_id";
        String COIN_CHAIN_ID = "coin_cross_chain_id";
        String CURRENT_CHAIN = "current_chain";
        String WALLET_ADDRESS = "wallet_address";
        String WALLET_COIN_ADDRESS = "wallet_coin_address";
        String WALLET_BACKUP = "wallet_backup";
        String WALLET_SIGNED_ADDRESS = "wallet_signed_address";
        String WALLET_PUBLIC_KEY = "wallet_public_key";
        String WALLET_PUBLIC_KEY_DAPP = "wallet_public_key_dapp";
        String WALLET_HINT = "Wallet_hint";
        String IS_WALLET_EXISTS = "is_wallet_exists";

        //market Star
        String MARKET_STAR = "market_star";

        //是否是导入keyStore 进入的
        String is_KeyStore = "is_key_store";

        String By_Chain_Data = "by_chain_data"; //按chain data
        String CURRENT_CHAIN_ID = "current_chain_id"; //按chain data

        String Dapp_List_Cache = "dapp_list_cache"; //按chain data

        String NETWORK_SELECT_KEY = "network_select_key";

        String NETWORK_BASE_URL = "network_base_url";

        String NETWORK_LAST_URL = "network_last_url";

    }

    public interface RequestCode {
        int CODE_ADD_ADDRESS = 1;
        int CODE_SCAN_ZXING = 2;
        int CODE_CHOOSE_CONTACT = 3;
        int CODE_NOTIFICATION = 4;
    }

    public interface Event {
        int SET_LANGUAGE = 0;
        int ASSETS_DISPLAY = 1;
        int PRICING_CURRENCY = 2;
        int BIND_ASSETS = 3;
        int UNBIND_ASSETS = 4;
        int READ_MESSAGE = 5;
        int REFRSH_TRANSATION = 6;
        int NOTIFICATION = 7;
        int REFRSH_STAR = 8;
        int HOME_SEARCH = 9;
    }

    public interface BundleKey {
        String LANG = "lang";
        String TXID = "txid";
        String ADDRESS = "address";
        String SET_LANGUAGE = "set_language";
        String NOTIFICATION_TYPE = "notification_type";
        String BACKUP = "backup";
        String ADDRESS_BOOK = "address_book";
        String KEY_WEBVIEW_URl = "key_webview_url";
        String KEY_WEBVIEW_TITLE = "key_webview_title";
        String KEY_WEBVIEW_DATA = "key_webview_data";
        String CHAIN_ID = "chain_id";

        String TransData = "trans_data";
    }

    public interface BundleValue {
        String WALLET_PAGE = "wallet_page";
        String SETTING_PAGE = "setting_page";
        String CREATE_IMPORT_PAGE = "create_import_page";
        String BACKUP_MAIN_PAGE = "main_page";
        String BACKUP_TRANSFER_PAGE = "transfer_page";
        String BACKUP_MY_ACCOUNT_PAGE = "my_account_page";
        String TRANSFER_ADDRESS_BOOK = "transfer_address_book";
    }

    public interface IntentKey {
        String Scan_Zxing = "scan_zxing";
    }

    public interface IntentValue {
        String SCAN_IMPORT_MNEMONIC = "scan_mnemonic";
        String SCAN_TRANSFER = "scan_transfer";
        String SCAN_DISCOVERY = "scan_discovery";
        String SCAN_ADD_ADDRESS = "scan_add_address";
        String SCAN_KEY_MNEMONIC = "scan_key_mnemonic";
    }

}
