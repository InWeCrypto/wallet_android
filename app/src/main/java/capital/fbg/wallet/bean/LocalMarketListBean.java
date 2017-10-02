package capital.fbg.wallet.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class LocalMarketListBean  implements Serializable {

    private ArrayList<MarkeListBean> list;

    public ArrayList<MarkeListBean> getList() {
        return list;
    }

    public void setList(ArrayList<MarkeListBean> list) {
        this.list = list;
    }
}
