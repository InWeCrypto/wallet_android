package capital.fbg.wallet.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/27.
 * 功能描述：
 * 版本：@version
 */

public class MinBlockBean implements Serializable {
    private String min_block_num;

    public String getMin_block_num() {
        return min_block_num;
    }

    public void setMin_block_num(String min_block_num) {
        this.min_block_num = min_block_num;
    }
}
