package capital.fbg.wallet.common.http;

import capital.fbg.wallet.AppApplication;

/**
 * Created by Administrator on 2017/8/1.
 * 功能描述：
 * 版本：@version
 */

public class Url {

    private static final String MAIN_URL = "https://mainnet.unichain.io/api/";

    private static final String TEST_URL = "https://ropsten.unichain.io/api/";

    private static String BASE = AppApplication.isMain ? MAIN_URL : TEST_URL;

    public static String ORDER_ULR = AppApplication.isMain ? " https://etherscan.io/tx/" : "https://ropsten.etherscan.io/tx/";

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

    public static void changeNet(boolean isMain) {
        if (isMain) {
            BASE = MAIN_URL;
        } else {
            BASE = TEST_URL;
        }

        ORDER_ULR = AppApplication.isMain ? " https://etherscan.io/tx/" : "https://ropsten.etherscan.io/tx/";

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
    }

}
