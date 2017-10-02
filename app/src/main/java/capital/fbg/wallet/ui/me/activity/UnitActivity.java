package capital.fbg.wallet.ui.me.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import capital.fbg.wallet.AppApplication;
import capital.fbg.wallet.R;
import capital.fbg.wallet.base.BaseActivity;
import capital.fbg.wallet.bean.UnitBean;
import capital.fbg.wallet.common.Constant;
import capital.fbg.wallet.common.http.LzyResponse;
import capital.fbg.wallet.common.http.api.MeApi;
import capital.fbg.wallet.common.http.callback.JsonCallback;
import capital.fbg.wallet.common.util.ToastUtil;
import capital.fbg.wallet.event.BaseEventBusBean;
import capital.fbg.wallet.ui.me.adapter.UnitAdapter;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class UnitActivity extends BaseActivity {
    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.select)
    ImageView select;
    @BindView(R.id.cn)
    RelativeLayout cn;
    @BindView(R.id.select2)
    ImageView select2;
    @BindView(R.id.us)
    RelativeLayout us;
//    @BindView(R.id.unit_list)
//    RecyclerView unitList;
//    @BindView(R.id.swipeRefresh)
//    SwipeRefreshLayout swipeRefresh;

    private ArrayList<UnitBean> units = new ArrayList<>();
    private UnitAdapter adapter;

    private int selectPosition = -1;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int setLayoutID() {
        return R.layout.me_activity_unit;
    }

    @Override
    protected void initView() {
        txtMainTitle.setText(R.string.unit_title);
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtRightTitle.setVisibility(View.GONE);

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectPosition == -1) {
                    finish();
                } else {
                    AppApplication.get().getSp().putInt(Constant.UNIT_TYPE,selectPosition);
                    EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_UNIT_CHANGE));
                    finish();
//                    MeApi.setUnit(mActivity, units.get(selectPosition).getId(), new JsonCallback<LzyResponse<Object>>() {
//                        @Override
//                        public void onSuccess(Response<LzyResponse<Object>> response) {
//                            ToastUtil.show(getString(R.string.mail_hit4));
//                            AppApplication.get().getSp().putInt(Constant.UNIT_TYPE, units.get(selectPosition).getId());
//                            finish();
//                        }
//
//                        @Override
//                        public void onError(Response<LzyResponse<Object>> response) {
//                            super.onError(response);
//                            ToastUtil.show(getString(R.string.fix_error));
//                        }
//                    });
                }
            }
        });

        cn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.setImageResource(R.mipmap.list_btn_selected);
                select2.setImageResource(R.mipmap.list_btn_default);
                selectPosition=1;
            }
        });

        us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.setImageResource(R.mipmap.list_btn_default);
                select2.setImageResource(R.mipmap.list_btn_selected);
                selectPosition=2;
            }
        });

//        adapter=new UnitAdapter(this,R.layout.me_item_unit,units);
//        unitList.setLayoutManager(new LinearLayoutManager(this));
//        unitList.setAdapter(adapter);
//        unitList.setOnScrollListener(new RecyclerView.OnScrollListener(){
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                int topRowVerticalPosition =
//                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
//                swipeRefresh.setEnabled(topRowVerticalPosition >= 0);
//            }
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//        });
//        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
//        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                initData();
//            }
//        });
//
//        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                if (-1!=selectPosition){
//                    units.get(selectPosition).setUser_unit_count(0);
//                }
//                units.get(position).setUser_unit_count(1);
//                selectPosition=position;
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
//                return false;
//            }
//        });
    }

    @Override
    protected void initData() {
        if (1== AppApplication.get().getUnit()){
            select.setImageResource(R.mipmap.list_btn_selected);
            select2.setImageResource(R.mipmap.list_btn_default);
            selectPosition=1;
        }else {
            select.setImageResource(R.mipmap.list_btn_default);
            select2.setImageResource(R.mipmap.list_btn_selected);
            selectPosition=2;
        }
//        swipeRefresh.post(new Runnable() {
//            @Override
//            public void run() {
//                swipeRefresh.setRefreshing(true);
//            }
//        });
//        MeApi.getUnit(mActivity,new JsonCallback<LzyResponse<CommonListBean<UnitBean>>>() {
//            @Override
//            public void onSuccess(Response<LzyResponse<CommonListBean<UnitBean>>> response) {
//                LoadSuccess(response);
//            }
//
//            @Override
//            public void onCacheSuccess(Response<LzyResponse<CommonListBean<UnitBean>>> response) {
//                super.onCacheSuccess(response);
//                onSuccess(response);
//            }
//
//            @Override
//            public void onError(Response<LzyResponse<CommonListBean<UnitBean>>> response) {
//                super.onError(response);
//                ToastUtil.show(getString(R.string.load_error));
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                swipeRefresh.setRefreshing(false);
//            }
//        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

//    private void LoadSuccess(Response<LzyResponse<CommonListBean<UnitBean>>> response) {
//        units.clear();
//        if (null!=response.body().data.getList()){
//            units.addAll(response.body().data.getList());
//            for (int i=0;i<units.size();i++){
//                if (0!=units.get(i).getUser_unit_count()){
//                    selectPosition=i;
//                    break;
//                }
//            }
//        }
//        adapter.notifyDataSetChanged();
//    }
}
