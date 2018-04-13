package com.inwecrypto.wallet.ui.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.Rank1Bean;
import com.inwecrypto.wallet.bean.Rank2Bean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.kelin.scrollablepanel.library.PanelAdapter;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * 作者：xiaoji06 on 2018/4/2 14:39
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class Ranking2Adapter extends PanelAdapter {

    private static final int TITLE_TYPE = 0;
    private static final int NAME_TYPE = 1;
    private static final int DATE_TYPE = 2;

    private Context context;

    private ArrayList<Rank2Bean> data;

    public Ranking2Adapter(Context context){
        this.context=context;
    }

    @Override
    public int getRowCount() {
        return data.size()+1;
    }

    @Override
    public int getColumnCount() {
        return 4;
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
                String volume="0.00";
                if (new BigDecimal(data.get(row-1).getVolume()).floatValue() < 10000) {
                    volume=data.get(row-1).getVolume();
                } else if (new BigDecimal(data.get(row-1).getVolume()).floatValue() < 100000000) {
                    volume=new BigDecimal(data.get(row-1).getVolume()).divide(new BigDecimal(10000)).setScale(2, RoundingMode.HALF_UP).toPlainString()+App.get().getString(R.string.wan);
                } else {
                    volume=new BigDecimal(data.get(row-1).getVolume()).divide(new BigDecimal(100000000)).setScale(2, RoundingMode.HALF_UP).toPlainString()+App.get().getString(R.string.yi);
                }
                if (App.get().getUnit()==1){
                    holder.num.setText("¥"+volume);
                }else {
                    holder.num.setText("$"+volume);
                }
                break;
            case 2:
                holder.num.setText(""+data.get(row-1).getTrade_ratio());
                break;
            case 3:
                StringBuilder sb=new StringBuilder();
                for (String type:data.get(row-1).getType().getZh()){
                    sb.append(type+"、");
                }
                holder.num.setText(sb.toString());
                break;
        }
    }

    private void setNameView(final int row, NameViewHolder holder) {
        holder.no.setText(row+"  "+data.get(row-1).getName());
        holder.namell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PAIXU2,row-1));
            }
        });
    }

    private void setTitleView(int column, TitleViewHolder holder) {
        holder.line.setVisibility(View.INVISIBLE);
        switch (column){
            case 0:
                holder.text.setText(R.string.mincheng);
                holder.line.setVisibility(View.VISIBLE);
                break;
            case 1:
                holder.text.setText(R.string.jiaoyiliang24);
                break;
            case 2:
                holder.text.setText(R.string.jiaoyidui);
                break;
            case 3:
                holder.text.setText(R.string.jiaoyileixing);
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TITLE_TYPE:
                View title_view=LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ranking2_title_layout, parent, false);
                ViewGroup.LayoutParams title_params = title_view.getLayoutParams();
                title_params.width= (int) (ScreenUtils.getScreenWidth(context)/2.8f);
                title_view.setLayoutParams(title_params);
                return new TitleViewHolder(title_view);
            case NAME_TYPE:
                View name_view=LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ranking2_name_layout, parent, false);
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

        public TextView text;
        public View line;

        public TitleViewHolder(View itemView) {
            super(itemView);
            this.text = (TextView) itemView.findViewById(R.id.text);
            this.line = itemView.findViewById(R.id.line);
        }

    }

    private static class NameViewHolder extends RecyclerView.ViewHolder {

        public View namell;
        public TextView no;

        public NameViewHolder(View itemView) {
            super(itemView);
            this.no = (TextView) itemView.findViewById(R.id.no);
            this.namell=itemView.findViewById(R.id.namell);
        }

    }

    private static class DateViewHolder extends RecyclerView.ViewHolder {

        public TextView num;

        public DateViewHolder(View itemView) {
            super(itemView);
            this.num = (TextView) itemView.findViewById(R.id.num);
        }

    }

    public void setData(ArrayList<Rank2Bean> data){
        this.data=data;
    }
}
