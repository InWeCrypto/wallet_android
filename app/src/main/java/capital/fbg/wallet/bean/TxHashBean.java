package capital.fbg.wallet.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/13.
 * 功能描述：
 * 版本：@version
 */

public class TxHashBean implements Serializable {
    private String txHash;

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }
}
