package com.inwecrypto.wallet.ui.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.Rank1Bean;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.kelin.scrollablepanel.library.PanelAdapter;

import java.util.ArrayList;

/**
 * 作者：xiaoji06 on 2018/4/2 14:39
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class Ranking1Adapter extends PanelAdapter {

    private static final int TITLE_TYPE = 0;
    private static final int NAME_TYPE = 1;
    private static final int DATE_TYPE = 2;

    private Context context;

    private ArrayList<Rank1Bean> data;

    public Ranking1Adapter(Context context){
        this.context=context;
    }

    @Override
    public int getRowCount() {
        return data.size()+1;
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    public int getItemViewType(int row, int column) {
        if (row == 0) {
            return TITLE_TYPE;
        }
        if (column == 0) {
            return NAME_TYPE;
        }
        return DATE_TYPE;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int row, int column) {
        int viewType = getItemViewType(row, column);
        switch (viewType) {
            case TITLE_TYPE:
                setTitleView(column, (TitleViewHolder) holder);
                break;
            case NAME_TYPE:
                setNameView(row, (NameViewHolder) holder);
                break;
            case DATE_TYPE:
                setDateView(row, column, (DateViewHolder) holder);
                break;
            default:
                setDateView(row, column, (DateViewHolder) holder);
        }
    }

    private void setDateView(int row, int column, DateViewHolder holder) {
        switch (column){
            case 1:
                holder.num.setText(data.get(row-1).getVolume());
                break;
            case 2:
                holder.num.setText(data.get(row-1).getPrice());
                break;
            case 3:
                holder.num.setText(data.get(row-1).getChange());
                break;
            case 4:
                holder.num.setText(data.get(row-1).getMarket());
                break;
        }
    }

    private void setNameView(int row, NameViewHolder holder) {
        Glide.with(context).load(data.get(row-1).getImg()).crossFade().into(holder.img);
        holder.no.setText(data.get(row-1).getRank());
        holder.name.setText(data.get(row-1).getSymbol());
        holder.long_name.setText(data.get(row-1).getKey());
    }

    private void setTitleView(int column, TitleViewHolder holder) {
        holder.line.setVisibility(View.INVISIBLE);
        switch (column){
            case 0:
                holder.text.setText(R.string.shizhipaiming);
                holder.line.setVisibility(View.VISIBLE);
                break;
            case 1:
                holder.text.setText(R.string.jiaoyiliang24);
                break;
            case 2:
                holder.text.setText(R.string.dangqianjiage);
                break;
            case 3:
                holder.text.setText(R.string.zhangdiefu24);
                break;
            case 4:
                holder.text.setText(R.string.shizhi);
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TITLE_TYPE:
                View title_view=LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ranking_title_layout, parent, false);
                ViewGroup.LayoutParams title_params = title_view.getLayoutParams();
                title_params.width= (int) (ScreenUtils.getScreenWidth(context)/2.8f);
                title_view.setLayoutParams(title_params);
                return new TitleViewHolder(title_view);
            case NAME_TYPE:
                View name_view=LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ranking_name_layout, parent, false);
                ViewGroup.LayoutParams name_params = name_view.getLayoutParams();
                name_params.width= (int) (ScreenUtils.getScreenWidth(context)/2.8f);
                name_params.height= DensityUtil.dip2px(context,50);
                name_view.setLayoutParams(name_params);
                return new NameViewHolder(name_view);
            case DATE_TYPE:
                View data_view=LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ranking_data_layout, parent, false);
                ViewGroup.LayoutParams data_params = data_view.getLayoutParams();
                data_params.width= (int) (ScreenUtils.getScreenWidth(context)/2.8f);
                data_params.height= DensityUtil.dip2px(context,50);
                data_view.setLayoutParams(data_params);
                return new DateViewHolder(data_view);
            default:
                break;
        }
        return new DateViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ranking_data_layout, parent, false));
    }

    private static class TitleViewHolder extends RecyclerView.ViewHolder {

        public ImageView img;
        public TextView text;
        public View line;

        public TitleViewHolder(View itemView) {
            super(itemView);
            this.img = (ImageView) itemView.findViewById(R.id.img);
            this.text = (TextView) itemView.findViewById(R.id.text);
            this.line = itemView.findViewById(R.id.line);
        }

    }

    private static class NameViewHolder extends RecyclerView.ViewHolder {

        public ImageView img;
        public TextView no;
        public TextView name;
        public TextView long_name;

        public NameViewHolder(View itemView) {
            super(itemView);
            this.img = (ImageView) itemView.findViewById(R.id.img);
            this.no = (TextView) itemView.findViewById(R.id.no);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.long_name = (TextView) itemView.findViewById(R.id.long_name);
        }

    }

    private static class DateViewHolder extends RecyclerView.ViewHolder {

        public TextView num;

        public DateViewHolder(View itemView) {
            super(itemView);
            this.num = (TextView) itemView.findViewById(R.id.num);
        }

    }

    public void setData(ArrayList<Rank1Bean> data){
        this.data=data;
    }
}
