package com.inwecrypto.wallet.ui.me;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.AppApplication;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.LoginBean;
import com.inwecrypto.wallet.bean.MeMenuBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.imageloader.GlideCircleTransform;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.service.AutoUpdateService;
import com.inwecrypto.wallet.ui.login.LoginActivity;
import com.inwecrypto.wallet.ui.me.activity.AboutUsActivity;
import com.inwecrypto.wallet.ui.me.activity.BrowserActivity;
import com.inwecrypto.wallet.ui.me.activity.MailListActivity;
import com.inwecrypto.wallet.ui.me.activity.UnitActivity;
import com.inwecrypto.wallet.ui.me.activity.UserInfoActivity;
import com.inwecrypto.wallet.ui.wallet.adapter.MeAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import com.inwecrypto.wallet.common.widget.MaterialDialog;
/**
 * Created by Administrator on 2017/7/15.
 * 功能描述：
 * 版本：@version
 */

public class MeFragment extends BaseFragment {


    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;
    @BindView(R.id.wallet_list)
    RecyclerView walletList;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.amin_title)
    TextView mainTitle;

    private MeAdapter adapter;
    private ArrayList<MeMenuBean> data = new ArrayList<>();

    private String[] title;
    private int[] ico = {R.mipmap.btn_browser, R.mipmap.btn_cantacts, R.mipmap.btn_currency, R.mipmap.netchange, R.mipmap.aboutus};
    private int[] type = {6, 1, 3, 4, 5};

    private MaterialDialog mMaterialDialog;
    private boolean net;
    private boolean current;

    @Override
    protected int setLayoutID() {
        return R.layout.me_fragment;
    }

    @Override
    protected void initView() {
        title = new String[] {getString(R.string.liulanqi),getString(R.string.tongxunlu), getString(R.string.huobidanwei), getString(R.string.wangluoqiehuan), getString(R.string.guanyuwomen)};
        isOpenEventBus = true;
        txtLeftTitle.setVisibility(View.GONE);
        Drawable drawable = getResources().getDrawable(R.mipmap.btn_edit);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        txtRightTitle.setCompoundDrawables(drawable, null, null, null);
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keepTogo(UserInfoActivity.class);
            }
        });
        ivImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keepTogo(UserInfoActivity.class);
            }
        });


        appbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset==0){
                    mainTitle.setVisibility(View.INVISIBLE);
                }
                if (verticalOffset+appBarLayout.getTotalScrollRange()==0){
                    mainTitle.setVisibility(View.VISIBLE);
                }
            }
        });


        for (int i = 0; i < title.length; i++) {
            data.add(new MeMenuBean(title[i], ico[i], type[i]));
        }
        adapter = new MeAdapter(mActivity, R.layout.me_item_menu, data);
        walletList.setLayoutManager(new LinearLayoutManager(mActivity));
        walletList.setAdapter(adapter);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                switch (data.get(position).getType()) {
                    case 1:
                        keepTogo(MailListActivity.class);
                        break;
                    case 2:
                        //keepTogo(IcoOrderActivity.class);
                        break;
                    case 3:
                        keepTogo(UnitActivity.class);
                        break;
                    case 4:
                        netChange();
                        break;
                    case 5:
                        keepTogo(AboutUsActivity.class);
                        break;
                    case 6:
                        keepTogo(BrowserActivity.class);
                        break;
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    protected void loadData() {
        LoginBean loginBean = AppApplication.get().getLoginBean();
        if (null != loginBean) {
            if (null != loginBean.getUser().getImg() && loginBean.getUser().getImg().length() > 0) {
                Glide.with(this)
                        .load(loginBean.getUser().getImg())
                        .crossFade()
                        .transform(new GlideCircleTransform(mContext))
                        .into(ivImg);
            }else{
                Glide.with(this)
                        .load(R.mipmap.clod_icon)
                        .crossFade()
                        .transform(new GlideCircleTransform(mContext))
                        .into(ivImg);
            }
            tvName.setText(loginBean.getUser().getNickname());
        }
    }

    private void netChange() {

        View view = LayoutInflater.from(mActivity).inflate(R.layout.view_dialog_net_change, null, false);
        View test = view.findViewById(R.id.test_net);
        View main = view.findViewById(R.id.main_net);
        View ok = view.findViewById(R.id.ok);
        final ImageView testImg = (ImageView) view.findViewById(R.id.test_img);
        final ImageView mainImg = (ImageView) view.findViewById(R.id.main_img);
        if (AppApplication.isMain) {
            current = true;
            net = true;
            mainImg.setImageResource(R.mipmap.list_btn_selected);
        } else {
            current = false;
            net = false;
            testImg.setImageResource(R.mipmap.list_btn_selected);
        }
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!net) {
                    return;
                }
                net = false;
                testImg.setImageResource(R.mipmap.list_btn_selected);
                mainImg.setImageResource(R.mipmap.list_btn_default);
            }
        });
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (net) {
                    return;
                }
                net = true;
                testImg.setImageResource(R.mipmap.list_btn_default);
                mainImg.setImageResource(R.mipmap.list_btn_selected);
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current == net) {
                    mMaterialDialog.dismiss();
                    return;
                }
                AppApplication.isMain = net;
                AppApplication.get().getSp().putBoolean(Constant.NET, net);
                Url.changeNet(net);
                mActivity.stopService(new Intent(mActivity, AutoUpdateService.class));
                AppApplication.get().getSp().putBoolean(Constant.NEED_RESTART, true);
                mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
            }
        });
        mMaterialDialog = new MaterialDialog(mActivity).setView(view);
        mMaterialDialog.setBackgroundResource(R.drawable.trans_bg);
        mMaterialDialog.setCanceledOnTouchOutside(true);
        mMaterialDialog.show();
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode() == Constant.EVENT_USERINFO) {
            loadData();
        }
    }
}
