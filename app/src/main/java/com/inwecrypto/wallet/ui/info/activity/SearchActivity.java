package com.inwecrypto.wallet.ui.info.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.IcoBean;
import com.inwecrypto.wallet.bean.NewsBean;
import com.inwecrypto.wallet.bean.ProjectBean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.api.InfoApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.info.adapter.SearchAdapter;
import com.inwecrypto.wallet.ui.me.activity.CommonWebActivity;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2017/11/14 17:50
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class SearchActivity extends BaseActivity {

    @BindView(R.id.back)
    FrameLayout back;
    @BindView(R.id.serch)
    EditText serch;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.title)
    TextView title;

    private ArrayList<ProjectBean> projectBeans = new ArrayList<>();
    private ArrayList<NewsBean> infoBeans = new ArrayList<>();
    private ArrayList<IcoBean> icoBeans = new ArrayList<>();

    private SearchAdapter adapter;
    private ArrayList<String> data=new ArrayList<>();

    private int type;

    @Override
    protected void getBundleExtras(Bundle extras) {
        type = extras.getInt("type", 0);
    }

    @Override
    protected int setLayoutID() {
        return R.layout.info_activity_serch_layout;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        serch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    initData();
                }
            }
        });

        serch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 当按了搜索之后关闭软键盘
                    ((InputMethodManager) serch.getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            SearchActivity.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    initData();
                    return true;
                }
                return false;
            }
        });

        switch (type) {
            case 0:
                title.setText( R.string.sousuoxiangmu);
                break;
            case 1:
                title.setText( R.string.sousuozixu);
                break;
            case 2:
                title.setText( R.string.sousuoicopingce);
                break;
        }

        adapter=new SearchAdapter(this,R.layout.info_item_serch,data);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                switch (type) {
                    case 0:
                        ProjectBean projectBean = projectBeans.get(position);
                        Intent intent = null;
                        if (projectBean.getType() == 5 || projectBean.getType() == 6) {
                            intent = new Intent(mActivity, ProjectOnlineActivity.class);
                        } else {
                            intent = new Intent(mActivity, ProjectUnderlineActivity.class);
                        }
                        intent.putExtra("project", projectBean);
                        keepTogo(intent);
                        break;
                    case 1:
                        if (null!=infoBeans.get(position).getUrl()){
                            Intent intent2=new Intent(mActivity,CommonWebActivity.class);
                            intent2.putExtra("title",infoBeans.get(position).getTitle());
                            intent2.putExtra("url", infoBeans.get(position).getUrl().startsWith("http")?infoBeans.get(position).getUrl(): Url.WEB_ROOT+infoBeans.get(position).getUrl().replace("../",""));
                            keepTogo(intent2);
                        }
                        break;
                    case 2:
                        if (null!=icoBeans.get(position).getUrl()){
                            Intent intent3=new Intent(mActivity,CommonWebActivity.class);
                            intent3.putExtra("title",icoBeans.get(position).getTitle());
                            intent3.putExtra("url", icoBeans.get(position).getUrl().startsWith("http")?icoBeans.get(position).getUrl(): Url.WEB_ROOT+icoBeans.get(position).getUrl().replace("../",""));
                            keepTogo(intent3);
                        }
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
    protected void initData() {
        if (serch.getText().toString().trim().length()==0){
            return;
        }
        switch (type) {
            case 0:
                InfoApi.searchProject(this, serch.getText().toString().trim(), new JsonCallback<LzyResponse<ArrayList<ProjectBean>>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<ArrayList<ProjectBean>>> response) {
                        if (null!=response){
                            if (null!=response.body().data){
                                projectBeans.clear();
                                projectBeans.addAll(response.body().data);
                                data.clear();
                                int size=projectBeans.size();
                                for (int i=0;i<size;i++){
                                    data.add(projectBeans.get(i).getName());
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
                break;
            case 1:
                InfoApi.searchArticles(this, serch.getText().toString().trim(), new JsonCallback<LzyResponse<ArrayList<NewsBean>>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<ArrayList<NewsBean>>> response) {
                        if (null!=response){
                            if (null!=response.body().data){
                                infoBeans.clear();
                                infoBeans.addAll(response.body().data);
                                data.clear();
                                int size=infoBeans.size();
                                for (int i=0;i<size;i++){
                                    data.add(infoBeans.get(i).getTitle());
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
                break;
            case 2:
                InfoApi.searchIcoAssess(this, serch.getText().toString().trim(), new JsonCallback<LzyResponse<ArrayList<IcoBean>>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<ArrayList<IcoBean>>> response) {
                        if (null!=response){
                            if (null!=response.body().data){
                                icoBeans.clear();
                                icoBeans.addAll(response.body().data);
                                data.clear();
                                int size=icoBeans.size();
                                for (int i=0;i<size;i++){
                                    data.add(icoBeans.get(i).getTitle());
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
                break;
        }
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
