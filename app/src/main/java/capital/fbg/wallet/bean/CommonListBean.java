package capital.fbg.wallet.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class CommonListBean<T>  implements Serializable {

    private ArrayList<T> list;

    public ArrayList<T> getList() {
        return list;
    }

    public void setList(ArrayList<T> list) {
        this.list = list;
    }
}
