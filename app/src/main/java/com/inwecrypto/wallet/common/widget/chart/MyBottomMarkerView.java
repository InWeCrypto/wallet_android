package com.inwecrypto.wallet.common.widget.chart;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.inwecrypto.wallet.R;

import java.text.SimpleDateFormat;

/**
 * Created by loro on 2017/2/8.
 */
public class MyBottomMarkerView extends MarkerView {
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    private TextView markerTv;
    private String time;
    private SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public MyBottomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        markerTv = (TextView) findViewById(R.id.marker_tv);
        markerTv.setTextSize(10);
    }

    public void setData(String time){

        this.time=time;
    }
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        markerTv.setText(format.format(new Float(time)));
    }

    @Override
    public int getXOffset(float xpos) {
        return 0;
    }

    @Override
    public int getYOffset(float ypos) {
        return 0;
    }
}
