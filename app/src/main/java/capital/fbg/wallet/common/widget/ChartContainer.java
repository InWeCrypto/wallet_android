package capital.fbg.wallet.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/9/3.
 * 功能描述：
 * 版本：@version
 */

public class ChartContainer extends RelativeLayout {
    private SwipeRefreshLayoutCompat scrollView;
    public ChartContainer(Context context) {
        super(context);
    }

    public ChartContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSwipeView(SwipeRefreshLayoutCompat scrollView) {
        this.scrollView = scrollView;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (null!=scrollView){
            if (ev.getAction() == MotionEvent.ACTION_UP) {
                scrollView.requestDisallowInterceptTouchEvent(false);
            } else {
                scrollView.requestDisallowInterceptTouchEvent(true);
            }
        }
        return true;
    }
}