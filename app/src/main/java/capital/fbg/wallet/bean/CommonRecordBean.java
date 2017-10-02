package capital.fbg.wallet.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class CommonRecordBean<T> implements Serializable{

    private T record;

    public T getRecord() {
        return record;
    }

    public void setRecord(T record) {
        this.record = record;
    }
}
