package com.inwecrypto.wallet.common.http;

import com.inwecrypto.wallet.App;

/**
 * Created by Administrator on 2017/8/1.
 * 功能描述：
 * 版本：@version
 */

public class Url {

    private static final String MAIN_1_URL = "https://china.inwecrypto.com:4431/v2/api/";

    public static final String MAIN_2_URL="https://china.inwecrypto.com:4431/v2";

    private static final String TEST_1_URL = "https://dev.inwecrypto.com:4431/v2/api/";

    public static final String TEST_2_URL = "https://dev.inwecrypto.com:4431/v2";

    public static final String WEB_ROOT="http://inwecrypto.com/";

    public static final String UPDATE="http://inwecrypto-china.oss-cn-shanghai.aliyuncs.com/update.json";

    public static final String APP_ADDRESS="http://inwecrypto-china.oss-cn-shanghai.aliyuncs.com/inwecrypto.apk";

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

    public static String LOGIN = BASE_2 + "/login";

    public static String SEND_CODE= BASE_2 + "/send_code/";

    public static String REGISTER= BASE_2 + "/register";

    public static String FROGOT_PASSWORD= BASE_2 + "/forgot_password";

    public static String USER = BASE_2 + "/user";

    public static String RESET_PASSWORD = BASE_2 + "/user/reset_password";

    public static String USER_FAVORITE = BASE_2 + "/article?user_favorite&page=";

    public static String BASE_CATEGORY = BASE_2 + "/category/";

    public static String MARKET_TIP = BASE_2 + "/category?user_follow&page=";

    public static String PROJECT_FAVORITE = BASE_2 + "/category?user_favorite&page=";

    public static String PROJECT = BASE_2 + "/category?type=";

    public static String INWE_HOT = BASE_2 + "/article?is_not_category&type=[1,2,3,6,16]&page=";

    public static String TRADING_VIEW = BASE_2 + "/article?type=[4,7]&page=";

    public static String EXCHANGE_NOTICE = BASE_2 + "/exchange_notice?page=";

    public static String CANDY_BOW = BASE_2 + "/candy_bow?page=";

    public static String PROJECT_NEWS = BASE_2 + "/article?cid=";

    public static String PROJECT_MARKETS = BASE_2 + "/ico/markets/";

    public static String PROJECT_DEDTAILE = BASE_2 + "/category/";

    public static String SEARCH_PROJECT = BASE_2 + "/category?keyword=";

    public static String SEARCH_NEWS = BASE_2 + "/search/all?k=";

    public static String ARTICLE_TAGS = BASE_2 + "/article/tags?per_page=100";

    public static String ICO_RANKS = BASE_2 + "/ico/ranks";

    public static String SET_TOP = BASE_2 + "/category/";

    public static String NEWS_COLLECT= BASE_2 +"/article/";

    public static String GET_YAOQIN=BASE_2+"/user/ont_candy_bow";

    public static String CANCLE_DOT=BASE_2+"/category/";

    public static String INWE_HOT_HISTORY = BASE_2 + "/article?page=";

    public static String FIX_PROJECT = BASE_2 + "/category?units=";

    public static String FEED_BACK = BASE_2 + "/user/feedbackc";

    public static String ALL_PROJECT = BASE_2 + "/category?";

    public static String ARTICLE_SCROLL = BASE_2 + "/article?is_scroll&page=1&per_page=5";

    public static String INWE_VIEW = BASE_2 + "/article?type=[12,13,14,15]&page=";

    public static String GNT_SORT = BASE_2 + "/api/user-gnt-sort-all";

    public static String USER_LANG = BASE_2 + "/user/lang";

    public static String MARKET_CAP = BASE_2 + "/token_rank/market_cap";

    public static String MARKET_CAP_DATE = BASE_2 + "/token_rank/market_cap/";

    public static String EXCHANGES = BASE_2 + "/token_rank/exchanges";

    public static String DAPP = BASE_2 + "/token_rank/dapp";

    public static String CATEGORY_RANKING = BASE_2 + "/token_rank/category";

    public static String ARTICLE_RANKING = BASE_2 + "/article?sort_desc=click_rate&per_page=50";

    public static String COMMENT = BASE_2 + "/user/category/comment";

    public static String COMMENT_INFO = BASE_2 + "/category/";

    public static String RANK_MARKET_CAP = BASE_2 + "/token_rank/market_cap/";

    public static String TOTLE_MARKET = BASE_2 + "/ico/total_market_value";

    public static String GET_GNT_CATEGORY = BASE_2 + "/offline_wallet/extend/getGntCategory/";

    public static String DRAW_RECORD = BASE_2 + "/redbag/draw_record";

    public static String SEND_RECORD = BASE_2 + "/redbag/send_record/";

    public static String SEND_RECORD_LIST = BASE_2 + "/redbag/send_record";

    public static String REDBAG_AUTH = BASE_2 + "/redbag/auth";

    public static String REDBAG_FEE = BASE_2 + "/redbag/fee/";

    public static String REDBAG_SEND = BASE_2 + "/redbag/send/";

    public static String REDBAG_SEND_COUNT = BASE_2 + "/redbag/send_count";

    public static String REDBAG_DRAW = BASE_2 + "/redbag/draw/";

    public static String REDBAG_SEND_RECORD = BASE_2 + "/redbag/send_record/";

    public static String RPC = BASE_2 + "/offline_wallet/rpc";

    public static String REDBAG_ID = BASE_2 + "/offline_wallet/extend/getRedbagId";

    public static String MIN_GAS = BASE_2 + "/config/get_key/REDBAG_MAX_GAS";



    public static String MAIN_NEWS = "http://inwecrypto.com/newsdetail2?art_id=";

    public static String TEST_NEWS = "http://testnet.inwecrypto.com/newsdetail2?art_id=";

    public static String MAIN_YAOQIN = "http://inwecrypto.com/share2app?code=";

    public static String TEST_YAOQIN = "http://testnet.inwecrypto.com/share2app?code=";

    public static String MAIN_HELP = "http://inwecrypto.com/";

    public static String TEST_HELP = "http://testnet.inwecrypto.com/";

    public static String MAIN_HONGBAO = "http://inwecrypto.com/redbag/";

    public static String TEST_HONGBAO = "http://testnet.inwecrypto.com/redbag/";

    public static String MAIN_HONGBAOXIEYI_EN="http://inwecrypto.com/rpAgreementEn.html";

    public static String MAIN_HONGBAOXIEYI="http://inwecrypto.com/rpAgreement.html";



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

    //public static String USER = BASE + "user";

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

        LOGIN = BASE + "auth";

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

    public static String USER_CONTACT=BASE_2+"/api/contact?category_id=";

    public static String USER_CONTACT_ADD=BASE_2+"/api/contact";

    //用户行情列表(添加行情和编辑行情 put)
    public static String USER_TICKER = BASE_2+"/api/ticker";

    //所有行情列表(用户已添加行情被标记)
    public static String USER_TICKER_OPTIONS = BASE_2+"/api/ticker/options";

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

        USER_CONTACT=BASE_2+"/api/contact?category_id=";

        USER_CONTACT_ADD=BASE_2+"/api/contact";

        //用户行情列表(添加行情和编辑行情 put)
        USER_TICKER = BASE_2+"/api/ticker";

        //所有行情列表(用户已添加行情被标记)
        USER_TICKER_OPTIONS = BASE_2+"/api/ticker/options";

        K_LINE = BASE_2+"/ico/currencies/";

        CURRENT_PRICE = BASE_2+"/ico/time_price/";

        LOGIN = BASE_2 + "/login";

        SEND_CODE= BASE_2 + "/send_code/";

        REGISTER= BASE_2 + "/register";

        FROGOT_PASSWORD= BASE_2 + "/forgot_password";

        USER = BASE_2 + "/user";

        RESET_PASSWORD = BASE_2 + "/user/reset_password";

        USER_FAVORITE = BASE_2 + "/article?user_favorite&page=";

        BASE_CATEGORY = BASE_2 + "/category/";

        MARKET_TIP = BASE_2 + "/category?user_follow&page=";

        PROJECT_FAVORITE = BASE_2 + "/category?user_favorite&page=";

        PROJECT = BASE_2 + "/category?type=";

        INWE_HOT = BASE_2 + "/article?is_not_category&type=[1,2,3,6,16]&page=";

        TRADING_VIEW = BASE_2 + "/article?type=[4,7]&page=";

        EXCHANGE_NOTICE = BASE_2 + "/exchange_notice?page=";

        CANDY_BOW = BASE_2 + "/candy_bow?page=";

        PROJECT_NEWS = BASE_2 + "/article?cid=";

        PROJECT_MARKETS = BASE_2 + "/ico/markets/";

        PROJECT_DEDTAILE = BASE_2 + "/category/";

        SEARCH_PROJECT = BASE_2 + "/category?keyword=";

        SEARCH_NEWS = BASE_2 + "/search/all?k=";

        ARTICLE_TAGS = BASE_2 + "/article/tags?per_page=100";

        ICO_RANKS = BASE_2 + "/ico/ranks";

        SET_TOP = BASE_2 + "/category/";

        NEWS_COLLECT= BASE_2 +"/article/";

        GET_YAOQIN=BASE_2+"/user/ont_candy_bow";

        CANCLE_DOT=BASE_2+"/category/";

        INWE_HOT_HISTORY = BASE_2 + "/article?page=";

        FIX_PROJECT = BASE_2 + "/category?units=";

        FEED_BACK = BASE_2 + "/user/feedbackc";

        ALL_PROJECT = BASE_2 + "/category?";

        ARTICLE_SCROLL = BASE_2 + "/article?is_scroll&page=1&per_page=5";

        INWE_VIEW = BASE_2 + "/article?type=[12,13,14,15]&page=";

        GNT_SORT = BASE_2 + "/api/user-gnt-sort-all";

        USER_LANG = BASE_2 + "/user/lang";

        MARKET_CAP = BASE_2 + "/token_rank/market_cap";

        MARKET_CAP_DATE = BASE_2 + "/token_rank/market_cap/";

        EXCHANGES = BASE_2 + "/token_rank/exchanges";

        DAPP = BASE_2 + "/token_rank/dapp";

        CATEGORY_RANKING = BASE_2 + "/token_rank/category";

        ARTICLE_RANKING = BASE_2 + "/article?sort_desc=click_rate&per_page=50";

        COMMENT = BASE_2 + "/user/category/comment";

        COMMENT_INFO = BASE_2 + "/category/";

        RANK_MARKET_CAP = BASE_2 + "/token_rank/market_cap/";

        TOTLE_MARKET = BASE_2 + "/ico/total_market_value";

        GET_GNT_CATEGORY = BASE_2 + "/offline_wallet/extend/getGntCategory/";

        DRAW_RECORD = BASE_2 + "/redbag/draw_record";

        SEND_RECORD = BASE_2 + "/redbag/send_record/";

        SEND_RECORD_LIST = BASE_2 + "/redbag/send_record";

        REDBAG_AUTH = BASE_2 + "/redbag/auth";

        REDBAG_FEE = BASE_2 + "/redbag/fee/";

        REDBAG_SEND = BASE_2 + "/redbag/send/";

        REDBAG_SEND_COUNT = BASE_2 + "/redbag/send_count";

        REDBAG_DRAW = BASE_2 + "/redbag/draw/";

        REDBAG_SEND_RECORD = BASE_2 + "/redbag/send_record/";

        RPC = BASE_2 + "/offline_wallet/rpc";

        REDBAG_ID = BASE_2 + "/offline_wallet/extend/getRedbagId";

        MIN_GAS = BASE_2 + "/config/get_key/REDBAG_MAX_GAS";
    }

}
