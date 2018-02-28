package com.inwecrypto.wallet.common;

import java.math.BigDecimal;


public class Constant {

    public static final String CRASH_ID="383376bf9a";

    public static final String MAIN_WEB="http://inwecrypto.com";

    public static final String BLOCKCHAIN="https://blockchain.info";

    public static final String ETHERSCAN="https://ethscan.io";

    public static final String NEO_TRACKER="https://neotracker.io";

    public static final String NEO_ASSETS="0xc56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b";

    public static final String GAS_ASSETS="0x602c79718b16e442de58778e148d0b1084e3b2dffd5de6b7b16cee7969282de7";

    public static final String ETH_ORDER_ASSET_ID="0x0000000000000000000000000000000000000000";

    public static final long GAS_LIMIT=90000L;

    public static final int OPEN_CLOSE_MENU = 10001;

    public static final BigDecimal pEther = new BigDecimal("1000000000000000000");

    public static final BigDecimal p100 = new BigDecimal(100);

    public static final BigDecimal low=new BigDecimal("25200000000000").multiply(new BigDecimal(Constant.GAS_LIMIT)).divide(new BigDecimal(21000),0,BigDecimal.ROUND_HALF_UP);

    public static final BigDecimal high=new BigDecimal("2520120000000000").multiply(new BigDecimal(Constant.GAS_LIMIT)).divide(new BigDecimal(21000),0,BigDecimal.ROUND_HALF_UP);

    public static final String NET="net";

    public static final String SP_NAME="cryptobox";

    public static final String WALLETS="wallets";

    public static final String BALANCEOF="balanceof";

    public static final String BALANCE="balance";

    public static final String WALLET_CATEGORY="wallet_category";

    public static final String CONVERSION="conversions";

    public static final String WALLETS_BEIFEN="wallets_beifen";

    public static final String WALLETS_ZJC_BEIFEN="wallets_zjc_beifen";

    public static final String WALLETS_CLOD_BEIFEN="wallets_clod_beifen";

    public static final String TOKEN="token";

    public static final String TEST_TOKEN="test_token";

    public static final String USER_INFO="user";

    public static final String UNIT_TYPE="unit";

    public static final String UNIT_CHANGE="unit_change";

    public static final String MARKET="market";

    public static final String WALLET_ORDER="wallet_order";

    public static final String DISCOVER_BANNER="discover_banner";

    public static final String MAIL_LIST="mail_list";

    public static final String UNIT="unit";

    public static final String MAIL_ICO="mail_ico";

    public static final String WALLET_ICO="wallet_ico";

    public static final String ETHER_PRICE="etherprice";

    public static final String WORD_PRICE="wordprice";

    public static final String TOTOLE_GNT="totle_gnt";

    public static final String TOTOLE_GNT_TEST="totle_gnt_test";

    public static final String NEED_RESTART="need_restart";

    public static final String OPEN_ID="open_id";

    public static final String TEST_OPEN_ID="test_open_id";

    public static final String ETH_LIST="eth_list";

    public static final String ETH_TEST_LIST="eth_test_list";

    public static final String NEO_LIST="neo_list";

    public static final String NEO_TEST_LIST="neo_test_list";

    public static final String TOTAL_PRICE="total_price";

    public static final String TOTAL_TEST_PRICE="total_test_price";

    public static final String MIN_BLOCK="min_block";

    public static final String CURRENT_BLOCK="current_block";

    public static final String MAIN_SEE="main_block";

    public static final String TOUCH_ID="touch_id";

    public static final String PROJECT_JSON_MAIN="project_json_main";

    public static final String PROJECT_JSON_TEST="project_json_test";

    public static final String LANGUE_CHANGE="ischange";

    public static final String IS_CHINESE="ischinese";

    public static final String BASE_PROJECT_JSON="[\n" +
            " {\n" +
            "   \"id\":0,\n" +
            "   \"type\":\"sys_msg_inwehot\",\n" +
            "   \"isShow\":true\n" +
            " },\n" +
            " {\n" +
            "   \"id\":1,\n" +
            "   \"type\":\"sys_msg_trading\",\n" +
            "   \"isShow\":true\n" +
            " },\n" +
            " {\n" +
            "   \"id\":2,\n" +
            "   \"type\":\"sys_msg_exchangenotice\",\n" +
            "   \"isShow\":true\n" +
            " },\n" +
            " {\n" +
            "   \"id\":3,\n" +
            "   \"type\":\"sys_msg_candybow\",\n" +
            "   \"isShow\":false\n" +
            " },\n" +
            " {\n" +
            "   \"id\":4,\n" +
            "   \"type\":\"sys_msg_order\",\n" +
            "   \"isShow\":true\n" +
            " },\n" +
            " {\n" +
            "   \"id\":5,\n" +
            "   \"type\":\"sys_msg\",\n" +
            "   \"isShow\":true\n" +
            " }\n" +
            "]";

    public static final String BASE_SORT="[{\"p\":0},{\"p\":1},{\"p\":2},{\"p\":3},{\"p\":4},{\"p\":5}]";

    public static final int EVENT_REFRESH=0;

    public static final int EVENT_KEY=1;

    public static final int EVENT_WALLET=2;

    public static final int EVENT_USERINFO=0;

    public static final int EVENT_MAIN_PRICE = 3;

    public static final int EVENT_TOTLE_GNT = 4;

    public static final int EVENT_PRICE = 5;

    public static final int EVENT_MARKET = 6;

    public static final int EVENT_TOTLE_PRICE = 7;

    public static final int EVENT_MAIN_REFERSH_COMP = 8;

    public static final int EVENT_UNIT_CHANGE = 9;

    public static final int EVENT_TOTLE_PRICE_SERVICE = 10;

    public static final int EVENT_CLOD_NET = 11;

    public static final int EVENT_CLOD_UNNET = 12;

    public static final int EVENT_TIP_SUCCESS = 13;

    public static final int EVENT_WATCH_TRANSFER = 14;

    public static final int EVENT_MESSAGE = 15;

    public static final int EVENT_DRAWLAYOUT = 16;

    public static final int EVENT_PROJECT = 17;

    public static final int EVENT_POPUP_MARKET = 18;

    public static final int EVENT_WALLET_INFO = 19;

    public static final int EVENT_CARD_OPEN = 20;

    public static final int EVENT_START_REFRESH = 21;

    public static final int EVENT_JIEDONG_DIALOG = 22;

    public static final int EVENT_WALLET_DAIBI = 23;

    public static final int EVENT_PASS_SEE = 24;

    public static final int EVENT_COMMON_PROJECT = 25;

    public static final int EVENT_WALLET_SELECT = 26;

    public static final int EVENT_LANGUE = 27;

    public static final int EVENT_REFERSH = 28;

    public static final int EVENT_REFERSH_SUC = 29;

    public static final int EVENT_FIX = 30;

    public static final int EVENT_ZIXUN_MESSAGE = 31;

    public static final int EVENT_SHOUCANG = 32;

    public static final int EVENT_DINGZHI = 33;

    public static final int EVENT_PINGLUN = 34;

    public static final int EVENT_TIP = 35;

    public static final int EVENT_DOWNLOAD_UPDATE = 36;

    public static final int EVENT_NOTIFY = 37;

    public static final int EVENT_TOKEN = 4009;

    public static final int PAGE_SIZE=20;

    public static final String ZHENGCHANG="0";

    public static final String BEIFEN="1";

    public static final String GUANCHA="2";

    public static final String CLOD="3";
}
