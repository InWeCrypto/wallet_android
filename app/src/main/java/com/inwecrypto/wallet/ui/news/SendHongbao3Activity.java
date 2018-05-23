package com.inwecrypto.wallet.ui.news;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.event.BaseEventBusBean;

import net.qiujuer.genius.ui.widget.Button;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/4/23 12:29
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class SendHongbao3Activity extends BaseActivity {

    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.b1)
    ImageView b1;
    @BindView(R.id.type1)
    LinearLayout type1;
    @BindView(R.id.b2)
    ImageView b2;
    @BindView(R.id.type2)
    LinearLayout type2;
    @BindView(R.id.b3)
    ImageView b3;
    @BindView(R.id.type3)
    LinearLayout type3;
    @BindView(R.id.next)
    Button next;
    @BindView(R.id.b0)
    ImageView b0;
    @BindView(R.id.type0)
    LinearLayout type0;

    private int type=0;
    private int preType=0;

    private String id;
    private String redbagAddress;
    private String gntName;

    private boolean isShare;

    @Override
    protected void getBundleExtras(Bundle extras) {
        id=extras.getString("id");
        redbagAddress=extras.getString("redbagAddress");
        gntName=extras.getString("gntName");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.send_hongbao_3_activity;
    }

    @Override
    protected void initView() {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_HONGBAO_REFERS));
                finish();
            }
        });

        type0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=0;
                setType(type);
                b0.setImageResource(R.mipmap.red_type_select);
            }
        });

        type1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=1;
                setType(type);
                b1.setImageResource(R.mipmap.red_type_select);
            }
        });

        type2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=2;
                setType(type);
                b2.setImageResource(R.mipmap.red_type_select);
            }
        });

        type3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=3;
                setType(type);
                b3.setImageResource(R.mipmap.red_type_select);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=null;
                switch (type){
                    case 0:
                        intent=new Intent(mActivity,SendHongbao44Activity.class);
                        break;
                    case 1:
                        intent=new Intent(mActivity,SendHongbao41Activity.class);
                        break;
                    case 2:
                        intent=new Intent(mActivity,SendHongbao42Activity.class);
                        break;
                    case 3:
                        intent=new Intent(mActivity,SendHongbao43Activity.class);
                        break;
                }
                intent.putExtra("id",id);
                intent.putExtra("redbagAddress",redbagAddress);
                intent.putExtra("gntName",gntName);
                intent.putExtra("isShare",isShare);
                finshTogo(intent);
            }
        });
    }

    private void setType(int type) {
        if (preType!=type){
            switch (preType){
                case 0:
                    b0.setImageResource(R.mipmap.red_type_unselect);
                    break;
                case 1:
                    b1.setImageResource(R.mipmap.red_type_unselect);
                    break;
                case 2:
                    b2.setImageResource(R.mipmap.red_type_unselect);
                    break;
                case 3:
                    b3.setImageResource(R.mipmap.red_type_unselect);
                    break;
            }
        }
        preType=type;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode()==Constant.EVENT_HONGBAO_SHARE){
            if (((String)event.getData()).equals(id)){
                isShare=true;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_HONGBAO_REFERS));
    }
}
