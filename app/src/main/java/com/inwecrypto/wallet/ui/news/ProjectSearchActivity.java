package com.inwecrypto.wallet.ui.news;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.ProjectDetaileBean;
import com.inwecrypto.wallet.bean.ProjectListBean;
import com.inwecrypto.wallet.bean.SearchBean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.EndLessOnScrollListener;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.adapter.SearchNewsAdapter;
import com.inwecrypto.wallet.ui.news.adapter.SearchProjectAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：xiaoji06 on 2018/2/12 17:29
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ProjectSearchActivity extends BaseActivity {


    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.delete)
    ImageView delete;
    @BindView(R.id.quxiao)
    TextView quxiao;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.zixun)
    TextView zixun;
    @BindView(R.id.xiangmu)
    TextView xiangmu;
    @BindView(R.id.whitell)
    LinearLayout whitell;
    private boolean isZixun=true;

    private ArrayList<ProjectDetaileBean> project = new ArrayList<>();
    private ArrayList<SearchBean.DataBean> news = new ArrayList<>();

    private SearchProjectAdapter projectAdapter;
    private SearchNewsAdapter newsAdapter;

    private int page = 1;
    private boolean isEnd;
    private boolean isShow;

    private LinearLayoutManager layoutManager;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int setLayoutID() {
        return R.layout.search_layout;
    }

    @Override
    protected void initView() {
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                OkGo.getInstance().cancelTag(mActivity);
                if (s.toString().length() > 0) {
                    whitell.setVisibility(View.GONE);
                    page=1;
                    if (isZixun) {
                        searchZixunKey();
                    } else {
                        searchXiangmuKey();
                    }
                } else {
                    whitell.setVisibility(View.VISIBLE);
                    if (isZixun){
                        email.setHint(R.string.zixun);
                    } else {
                        email.setHint(R.string.xiangmu);
                    }
                }
            }
        });

        zixun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isZixun){
                    return;
                }
                isZixun = true;
                zixun.setTextColor(Color.parseColor("#0A9234"));
                xiangmu.setTextColor(Color.parseColor("#ACACAC"));
                list.setAdapter(newsAdapter);
                email.setHint(R.string.zixun);
            }
        });

        xiangmu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isZixun){
                    return;
                }
                isZixun = false;
                zixun.setTextColor(Color.parseColor("#ACACAC"));
                xiangmu.setTextColor(Color.parseColor("#0A9234"));
                list.setAdapter(projectAdapter);
                email.setHint(R.string.xiangmu);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.setText("");
            }
        });

        projectAdapter = new SearchProjectAdapter(this, R.layout.search_xiangmu_item, project);
        newsAdapter = new SearchNewsAdapter(this, R.layout.search_zixun_item, news);

        layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        list.setAdapter(newsAdapter);

        list.addOnScrollListener(new EndLessOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                if (isEnd) {
                    if (!isShow && page != 1) {
                        ToastUtil.show(getString(R.string.zanwugengduoshuju));
                        isShow = true;
                    }
                } else {
                    page++;
                    if (isZixun){
                        searchZixunKey();
                    }else {
                        searchXiangmuKey();
                    }
                }
            }
        });


        projectAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (1==project.get(position).getType()){
                    Intent intent=new Intent(mActivity, TradingActivity.class);
                    intent.putExtra("project",project.get(position));
                    keepTogo(intent);
                }else {
                    Intent intent=new Intent(mActivity, NoTradingActivity.class);
                    intent.putExtra("project",project.get(position));
                    keepTogo(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        newsAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(mActivity,ProjectNewsWebActivity.class);
                intent.putExtra("title",news.get(position).getTitle());
                intent.putExtra("url", (App.isMain? Url.MAIN_NEWS:Url.TEST_NEWS)+news.get(position).getId());
                intent.putExtra("id",news.get(position).getId());
                keepTogo(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    private void searchZixunKey() {
        ZixunApi.getNewsKey(this, email.getText().toString().trim(),page, new JsonCallback<LzyResponse<SearchBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<SearchBean>> response) {
                if (page==1){
                    news.clear();
                    if (1>=response.body().data.getLast_page()){
                        isEnd=true;
                    }
                }else {
                    if (page>=response.body().data.getLast_page()){
                        isEnd=true;
                    }
                }
                if (null != response.body().data.getData()) {
                    news.addAll(response.body().data.getData());
                }
                newsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Response<LzyResponse<SearchBean>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.load_error));
                if (page!=1){
                    page--;
                }
            }
        });
    }

    private void searchXiangmuKey() {
        ZixunApi.getProjectKey(this, email.getText().toString().trim(),page, new JsonCallback<LzyResponse<ProjectListBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ProjectListBean>> response) {
                if (page==1){
                    project.clear();
                    if (1>=response.body().data.getLast_page()){
                        isEnd=true;
                    }
                }else {
                    if (page>=response.body().data.getLast_page()){
                        isEnd=true;
                    }
                }

                if (null != response.body().data.getData()) {
                    project.addAll(response.body().data.getData());
                }
                projectAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Response<LzyResponse<ProjectListBean>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.load_error));
                if (page!=1){
                    page--;
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

}
