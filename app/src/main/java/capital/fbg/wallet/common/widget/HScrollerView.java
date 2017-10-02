package capital.fbg.wallet.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2017/8/13.
 * 功能描述：
 * 版本：@version
 */

public class HScrollerView extends ScrollView {

    private float xDistance;
    private float yDistance;
    private float xLast;
    private float yLast;

    public HScrollerView(Context context) {
        super(context);
    }

    public HScrollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HScrollerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;

                /**
                 * X轴滑动距离大于Y轴滑动距离，也就是用户横向滑动时，返回false，ScrollView不处理这次事件，
                 * 让子控件中的TouchEvent去处理，所以横向滑动的事件交由子控件处理， ScrollView只处理纵向滑动事件
                 */
                if (xDistance > yDistance) {
                    return false;
                }
        }

        return super.onInterceptTouchEvent(ev);
    }
}
