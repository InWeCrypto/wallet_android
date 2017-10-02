package capital.fbg.wallet.ui.me.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import capital.fbg.wallet.AppApplication;
import capital.fbg.wallet.R;
import capital.fbg.wallet.base.BaseFragment;
import capital.fbg.wallet.bean.CommonListBean;
import capital.fbg.wallet.bean.MailBean;
import capital.fbg.wallet.bean.MailIconBean;
import capital.fbg.wallet.common.Constant;
import capital.fbg.wallet.common.http.LzyResponse;
import capital.fbg.wallet.common.http.api.MeApi;
import capital.fbg.wallet.common.http.callback.JsonCallback;
import capital.fbg.wallet.common.util.AppUtil;
import capital.fbg.wallet.common.util.GsonUtils;
import capital.fbg.wallet.common.util.ToastUtil;
import capital.fbg.wallet.event.BaseEventBusBean;
import capital.fbg.wallet.event.KeyEvent;
import capital.fbg.wallet.ui.me.activity.MailDetaileActivity;
import capital.fbg.wallet.ui.me.adapter.MailListAdapter;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class MailListFragment extends BaseFragment {

    @BindView(R.id.mail_list)
    RecyclerView mailList;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private ArrayList<MailBean> mails=new ArrayList<>();
    private MailListAdapter adapter;

    private boolean isEth;
    private boolean address;

    @Override
    protected int setLayoutID() {
        return R.layout.me_fragment_mail_list;
    }

    @Override
    protected void initView() {

        Bundle bundle=getArguments();
        if (null!=bundle){
            isEth=bundle.getBoolean("isEth");
            address=bundle.getBoolean("address");
        }
        isOpenEventBus=true;
        adapter=new MailListAdapter(mContext,R.layout.me_item_mail_list,mails);
        mailList.setLayoutManager(new LinearLayoutManager(mContext));
        mailList.setAdapter(adapter);

        mailList.setOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipeRefresh.setEnabled(topRowVerticalPosition >= 0);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (address) {
                    EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_KEY,new KeyEvent(mails.get(position).getAddress())));
                    mActivity.finish();
                }else {
                    Intent intent=new Intent(mActivity,MailDetaileActivity.class);
                    intent.putExtra("mail",mails.get(position));
                    keepTogo(intent);
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
        if (isEth){
            swipeRefresh.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefresh.setRefreshing(true);
                }
            });
            MeApi.contact(mFragment,new JsonCallback<LzyResponse<CommonListBean<MailBean>>>() {
                @Override
                public void onSuccess(Response<LzyResponse<CommonListBean<MailBean>>> response) {
                    LoadSuccess(response);
                }

                @Override
                public void onCacheSuccess(Response<LzyResponse<CommonListBean<MailBean>>> response) {
                    super.onCacheSuccess(response);
                    onSuccess(response);
                }

                @Override
                public void onError(Response<LzyResponse<CommonListBean<MailBean>>> response) {
                    super.onError(response);
                    ToastUtil.show(getString(R.string.load_error));
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (null!=swipeRefresh){
                        swipeRefresh.setRefreshing(false);
                    }
                }
            });
        }else {
            ToastUtil.show(getString(R.string.jingqingqidai));
            swipeRefresh.setRefreshing(false);
        }
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode()== Constant.EVENT_REFRESH&&isEth){
            loadData();
        }
    }

    private void LoadSuccess(Response<LzyResponse<CommonListBean<MailBean>>> response) {
        boolean isSave=false;
        String mailIco=AppApplication.get().getSp().getString(Constant.MAIL_ICO,"[]");
        ArrayList<MailIconBean> mailId = GsonUtils.jsonToArrayList(mailIco, MailIconBean.class);
        HashMap<Integer,Integer> mailHash=new HashMap<>();
        for (int i=0;i<mailId.size();i++){
            mailHash.put(mailId.get(i).getId(),mailId.get(i).getIcon());
        }
        mails.clear();
        if (null!=response.body().data.getList()){
            ArrayList<MailBean> mailsBean = response.body().data.getList();
            for (int i=0;i<mailsBean.size();i++){
                if (null==mailHash.get(mailsBean.get(i).getId())){
                    int icon= AppUtil.getRoundmIcon();
                    mailHash.put(mailsBean.get(i).getId(),icon);
                    mailsBean.get(i).setHeadImg(AppUtil.getRoundmHeadIcon(icon));
                    isSave=true;
                }else {
                    mailsBean.get(i).setHeadImg(AppUtil.getRoundmHeadIcon(mailHash.get(mailsBean.get(i).getId())));
                }
            }
            mails.addAll(mailsBean);
        }
        adapter.notifyDataSetChanged();

        if (isSave){
            mailId.clear();
            Iterator iter = mailHash.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Integer key = (Integer) entry.getKey();
                Integer val = (Integer) entry.getValue();
                mailId.add(new MailIconBean(key,val));
            }
            AppApplication.get().getSp().putString(Constant.MAIL_ICO,GsonUtils.objToJson(mailId));
        }
    }
}
