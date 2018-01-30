package com.inwecrypto.wallet.common.http;

import com.inwecrypto.wallet.App;

/**
 * Created by Administrator on 2017/8/1.
 * 功能描述：
 * 版本：@version
 */

public class Url {

    private static final String MAIN_1_URL = "https://china.inwecrypto.com:4430/api/";

    public static final String MAIN_2_URL="https://china.inwecrypto.com:4431/v1";

    private static final String TEST_1_URL = "https://dev.inwecrypto.com:4430/api/";

    public static final String TEST_2_URL = "https://dev.inwecrypto.com:4431/v1";

    public static final String WEB_ROOT="http://inwecrypto.com/";

    public static final String UPDATE="http://whalewallet.oss-cn-hongkong.aliyuncs.com/update.json";

    public static final String APP_ADDRESS="http://whalewallet.oss-cn-hongkong.aliyuncs.com/inwecrypto.apk";

    private static String BASE = App.isMain ? MAIN_1_URL : TEST_1_URL;

    private static String BASE_2 = App.isMain ? MAIN_2_URL : TEST_2_URL;

    public static String ORDER_ULR = "https://etherscan.io/tx/";

    public static String ORDER_TEST_ULR = "https://ropsten.etherscan.io/tx/";

    public static String NEO_ORDER_ULR = "https://neoscan.io/transaction/";

    public static String NEO_ORDER_TEST_ULR = "https://neoscan-testnet.io/transaction/";

    public static String WEB_ULR = "http://inwecrypto.com";

    public static String WEB_TEST_ULR = "http://testnet.inwecrypto.com";

    public static String EULA = BASE.replace("/api", "") + "EULA.html";

    public static String TERMOFUEE = BASE.replace("/api", "") + "TermOfUse.html";

    public static String OPENSOURCE = BASE.replace("/api", "") + "OpenSource.html";

    public static String TEAM = BASE.replace("/api", "") + "Team.html";

    public static String AUTH = BASE + "auth";

    public static String FIND = BASE + "find";

    public static String ARTICLE = BASE + "article";

    public static String CONTACT = BASE + "contact";

    public static String ICO_ORDER = BASE + "ico-order";

    public static String MONETARY_UNIT = BASE + "monetary-unit";

    public static String ICO = BASE + "ico";

    public static String MARKET_CATEGORY = BASE + "market-category";

    public static String WALLET = BASE + "wallet";

    public static String WALLET_CATEGORY = BASE + "wallet-category";

    public static String USER_GNT = BASE + "user-gnt";

    public static String GNT_CATEGORY = BASE + "gnt-category";

    public static String WALLET_ORDER = BASE + "wallet-order";

    public static String USER = BASE + "user";

    public static String MARKET_NOTIFICATION = BASE + "market-notification";

    public static String STS = BASE + "sts";

    public static String ETH_GASPRICE = BASE + "extend/getGasPrice";

    public static String ETH_TRANSACTION_COUNT = BASE + "extend/getTransactionCount";

    public static String ETH_SEND_RAW_TRANSACTION = BASE + "extend/sendRawTransaction";

    public static String ETH_TRANSACTION = BASE + "extend/getTransaction";

    public static String ETH_BALANCE = BASE + "extend/getBalance";

    public static String CONVERSION = BASE + "conversion";

    public static String MESSAGE = BASE + "message";

    public static String TRANSFERABI = BASE + "extend/transferABI";

    public static String BALANCEOF = BASE + "extend/balanceOf";

    public static String GAS = BASE + "gas";

    public static String USER_INFO = BASE + "user/create";

    public static String MIN_BLOCK = BASE + "min-block";

    public static String EXTEND_BLOCKNUMBER = BASE + "extend/blockNumber";

    public static String BLOCK_PER_SECOND = BASE + "extend/blockPerSecond";

    public static String GET_NEO_UTXO=BASE+"extend/getNeoUtxo?address=";

    public static String GET_CLAIM_UTXO=BASE+"extend/getNeoClaimUtxo?address=";

    public static String GET_NEO_GNT_INFO=BASE+"extend/getNeoGntInfo?address=";

    public static String GET_NEO_ICO_GAS=BASE+"extend/getIcoGasCost?treaty_address=";

    public static String GET_NEO_NEP5_GAS=BASE+"extend/getNeoGasCost";

    public static void changeNet(boolean isMain) {
        if (isMain) {
            BASE = MAIN_1_URL;
            BASE_2 = MAIN_2_URL;
        } else {
            BASE = TEST_1_URL;
            BASE_2 = TEST_2_URL;
        }

        ORDER_ULR = App.isMain ? " https://etherscan.io/tx/" : "https://ropsten.etherscan.io/tx/";

        EULA = BASE.replace("/api", "") + "EULA.html";

        TERMOFUEE = BASE.replace("/api", "") + "TermOfUse.html";

        OPENSOURCE = BASE.replace("/api", "") + "OpenSource.html";

        TEAM = BASE.replace("/api", "") + "Team.html";

        AUTH = BASE + "auth";

        FIND = BASE + "find";

        ARTICLE = BASE + "article";

        CONTACT = BASE + "contact";

        ICO_ORDER = BASE + "ico-order";

        MONETARY_UNIT = BASE + "monetary-unit";

        ICO = BASE + "ico";

        MARKET_CATEGORY = BASE + "market-category";

        WALLET = BASE + "wallet";

        WALLET_CATEGORY = BASE + "wallet-category";

        USER_GNT = BASE + "user-gnt";

        GNT_CATEGORY = BASE + "gnt-category";

        WALLET_ORDER = BASE + "wallet-order";

        USER = BASE + "user";

        MARKET_NOTIFICATION = BASE + "market-notification";

        STS = BASE + "sts";

        ETH_GASPRICE = BASE + "extend/getGasPrice";

        ETH_TRANSACTION_COUNT = BASE + "extend/getTransactionCount";

        ETH_SEND_RAW_TRANSACTION = BASE + "extend/sendRawTransaction";

        ETH_TRANSACTION = BASE + "extend/getTransaction";

        ETH_BALANCE = BASE + "extend/getBalance";

        CONVERSION = BASE + "conversion";

        MESSAGE = BASE + "message";

        TRANSFERABI = BASE + "extend/transferABI";

        BALANCEOF = BASE + "extend/balanceOf";

        GAS = BASE + "gas";

        USER_INFO = BASE + "user/create";

        MIN_BLOCK = BASE + "min-block";

        EXTEND_BLOCKNUMBER = BASE + "extend/blockNumber";

        BLOCK_PER_SECOND = BASE + "extend/blockPerSecond";

        GET_NEO_UTXO=BASE+"extend/getNeoUtxo?address=";

        GET_CLAIM_UTXO=BASE+"extend/getNeoClaimUtxo?address=";

        GET_NEO_GNT_INFO=BASE+"extend/getNeoGntInfo?address=";

        GET_NEO_ICO_GAS=BASE+"extend/getIcoGasCost?treaty_address=";

        GET_NEO_NEP5_GAS=BASE+"extend/getNeoGasCost";

        changeNet2();
    }

    public static String HOME_AD = BASE_2+"/home/ad";

    public static String HOME_NEWS = BASE_2+"/home/news";

    public static String ARTICLE_ALL = BASE_2+"/article/";

    public static String ARTICLE_ICO = BASE_2+"/article/ico";

    public static String HOME_PROJECT = BASE_2+"/home/project/is_mobile";

    public static String CATEGORY = BASE_2+"/category/";

    public static String TEAM_INFO = BASE_2+"/article/";

    public static String SEARCH = BASE_2+"/search/";

    public static String USER_CONTACT=BASE_2+"/user/contact?category_id=";

    public static String USER_CONTACT_ADD=BASE_2+"/user/contact";

    //用户行情列表(添加行情和编辑行情 put)
    public static String USER_TICKER = BASE_2+"/user/ticker";

    //所有行情列表(用户已添加行情被标记)
    public static String USER_TICKER_OPTIONS = BASE_2+"/user/ticker/options";

    public static String K_LINE = BASE_2+"/ico/currencies/";

    public static String CURRENT_PRICE = BASE_2+"/ico/time_price/";

    private static void changeNet2() {
        HOME_AD = BASE_2+"/home/ad";

        HOME_NEWS = BASE_2+"/home/news";

        ARTICLE_ALL = BASE_2+"/article/";

        ARTICLE_ICO = BASE_2+"/article/ico";

        HOME_PROJECT = BASE_2+"/home/project/is_mobile";

        CATEGORY = BASE_2+"/category/";

        TEAM_INFO = BASE_2+"/article/";

        SEARCH = BASE_2+"/search/";

        USER_CONTACT=BASE_2+"/user/contact?"+(App.isMain ?"ico_id=":"category_id=");

        USER_CONTACT_ADD=BASE_2+"/user/contact";

        //用户行情列表(添加行情和编辑行情 put)
        USER_TICKER = BASE_2+"/user/ticker";

        //所有行情列表(用户已添加行情被标记)
        USER_TICKER_OPTIONS = BASE_2+"/user/ticker/options";

        K_LINE = BASE_2+"/ico/currencies/";

        CURRENT_PRICE = BASE_2+"/ico/time_price/";
    }

}
