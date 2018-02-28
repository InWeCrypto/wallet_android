package com.inwecrypto.wallet.common.widget;

/**
 * 作者：xiaoji06 on 2018/2/8 14:03
 * github：https://github.com/xiaoji06
 * 功能：
 */
import android.graphics.Rect;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class OverFlyingLayoutManager extends RecyclerView.LayoutManager {

    private static final String TAG = "OverFlying";

    private @FloatRange(from = 0.01, to = 1.0)
    float edgePercent = 0.8f;//触发边缘动画距离百分比

    private @FloatRange(from = 1)
    float slowTimes = 3;//到达此距离后放慢倍数

    private int orientation = OrientationHelper.VERTICAL;
    private boolean offsetUseful = false;
    private int overFlyingDist;
    private int totalHeight = 0;
    private int totalWidth = 0;
    private int verticalScrollOffset;
    private int horizontalScrollOffset;
    //头部是否也要层叠，默认需要
    private boolean topOverFlying;
    private int viewWidth, viewHeight;

    public OverFlyingLayoutManager() {
        this(OrientationHelper.VERTICAL);
    }

    public OverFlyingLayoutManager(int orientation) {
        this(orientation, true);
    }

    public OverFlyingLayoutManager(int orientation, boolean topOverFlying) {
        this.orientation = orientation;
        this.topOverFlying = topOverFlying;
    }


    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        // 先把所有的View先从RecyclerView中detach掉，然后标记为"Scrap"状态，表示这些View处于可被重用状态(非显示中)。
        // 实际就是把View放到了Recycler中的一个集合中。
        if (getItemCount() == 0) {//没有Item
            detachAndScrapAttachedViews(recycler);
            return;
        }
        if (getChildCount() == 0 && state.isPreLayout()) {
            return;
        }
        reset();
        detachAndScrapAttachedViews(recycler);
        calculateChildrenSite(recycler, state);
    }

    private void reset() {
        totalHeight = 0;
        totalWidth = 0;
        if (!offsetUseful) {
            verticalScrollOffset = 0;
            horizontalScrollOffset = 0;
        }
        offsetUseful = false;
    }

    private void calculateChildrenSite(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (orientation == OrientationHelper.VERTICAL) {
            calculateChildrenSiteVertical(recycler, state);
            addAndLayoutViewVertical(recycler, state, verticalScrollOffset);
        } else {
            calculateChildrenSiteHorizontal(recycler, state);
            addAndLayoutViewHorizontal(recycler, state, horizontalScrollOffset);
        }

    }

    private void calculateChildrenSiteVertical(RecyclerView.Recycler recycler, RecyclerView.State state) {
        View view = recycler.getViewForPosition(0);//暂时这么解决，不能layout出所有的子View
        measureChildWithMargins(view, 0, 0);
        calculateItemDecorationsForChild(view, new Rect());

        viewHeight = getDecoratedMeasuredHeight(view);
        overFlyingDist = (int) (slowTimes * viewHeight);
        totalHeight = getItemCount() * viewHeight;
        Log.d(TAG, "childCountI = " + getChildCount() + "  itemCount= " + recycler.getScrapList().size());

    }


    private void calculateChildrenSiteHorizontal(RecyclerView.Recycler recycler, RecyclerView.State state) {
        View view = recycler.getViewForPosition(0);//暂时这么解决，不能layout出所有的子View
        measureChildWithMargins(view, 0, 0);
        calculateItemDecorationsForChild(view, new Rect());
        viewWidth = getDecoratedMeasuredWidth(view);
        overFlyingDist = (int) (slowTimes * viewWidth);
        totalWidth = getItemCount() * viewWidth;
        Log.d(TAG, "childCountI = " + getChildCount() + "  itemCount= " + recycler.getScrapList().size());
    }

    @Override
    public boolean canScrollHorizontally() {
        // 返回true表示可以横向滑动
        return orientation == OrientationHelper.HORIZONTAL;
    }

    @Override
    public boolean canScrollVertically() {
        // 返回true表示可以纵向滑动
        return orientation == OrientationHelper.VERTICAL;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //列表向下滚动dy为正，列表向上滚动dy为负，这点与Android坐标系保持一致。
        int tempDy = dy;
        if (verticalScrollOffset <= totalHeight - getVerticalSpace()) {
            verticalScrollOffset += dy;
            //将竖直方向的偏移量+travel
        }
        if (verticalScrollOffset > totalHeight - getVerticalSpace()) {
            verticalScrollOffset = totalHeight - getVerticalSpace();
            tempDy = 0;
        } else if (verticalScrollOffset < 0) {
            verticalScrollOffset = 0;
            tempDy = 0;
        }
        detachAndScrapAttachedViews(recycler);
        addAndLayoutViewVertical(recycler, state, verticalScrollOffset); //从新布局位置、显示View
        return tempDy;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {

        int tempDx = dx;
        if (horizontalScrollOffset <= totalWidth - getHorizontalSpace()) {
            horizontalScrollOffset += dx;
            //将竖直方向的偏移量+travel
        }
        if (horizontalScrollOffset > totalWidth - getHorizontalSpace()) {
            horizontalScrollOffset = totalWidth - getHorizontalSpace();
            tempDx = 0;
        } else if (horizontalScrollOffset < 0) {
            horizontalScrollOffset = 0;
            tempDx = 0;
        }
        detachAndScrapAttachedViews(recycler);
        addAndLayoutViewHorizontal(recycler, state, horizontalScrollOffset); //从新布局位置、显示View
        return tempDx;

    }

    private void addAndLayoutViewVertical(RecyclerView.Recycler recycler, RecyclerView.State state, int offset) {
        int itemCount = getItemCount();
        if (itemCount <= 0 || state.isPreLayout()) {
            return;
        }
        int displayHeight = getVerticalSpace();
        for (int i = itemCount - 1; i >= 0; i--) {

            // 遍历Recycler中保存的View取出来
            int bottomOffset = (i + 1) * viewHeight - offset;
            int topOffset = i * viewHeight - offset;
            boolean needAdd = true;
            if (bottomOffset - displayHeight >= overFlyingDist) {
                needAdd = false;
            }
            if (topOffset < -overFlyingDist && i != 0 && topOverFlying
                    || topOffset < -overFlyingDist && !topOverFlying) {
                needAdd = false;
            }
            if (needAdd) {
                View view = recycler.getViewForPosition(i);
                addView(view); // 因为刚刚进行了detach操作，所以现在可以重新添加
                measureChildWithMargins(view, 0, 0); // 通知测量view的margin值
                int width = getDecoratedMeasuredWidth(view); // 计算view实际大小，包括了ItemDecorator中设置的偏移量。
                int height = getDecoratedMeasuredHeight(view);
                //调用这个方法能够调整ItemView的大小，以除去ItemDecorator。
                calculateItemDecorationsForChild(view, new Rect());
                int realBottomOffset = bottomOffset;
                if (topOverFlying) {
                    if (i != 0) {//除第一个外的顶部黏性动画
                        if (topOffset <= height * edgePercent) {//到达顶部边界了
                            int edgeDist = (int) (height * edgePercent);//边界触发距离
                            int top = (int) (edgeDist - (edgeDist - topOffset) / slowTimes);//到达边界后速度放慢到原来5分之一

                            top = Math.max(top, 0);
                            realBottomOffset = top + height;
                        }
                    } else {
                        realBottomOffset = height;
                    }
                }
                if (i != itemCount - 1) {//除最后一个外的底部慢速动画
                    if (displayHeight - bottomOffset <= height * edgePercent) {
                        int edgeDist = (int) (displayHeight - height * edgePercent);
                        int bottom = (int) (edgeDist + (bottomOffset - edgeDist) / slowTimes);
                        bottom = Math.min(bottom, displayHeight);
                        realBottomOffset = bottom;

                    }
                } else {
                    realBottomOffset = totalHeight > displayHeight ? displayHeight : totalHeight;
                }
                layoutDecoratedWithMargins(view, 0, realBottomOffset - height, width, realBottomOffset);
            }
        }

        Log.d(TAG, "childCount = " + getChildCount() + "  itemCount= " + itemCount);
    }

    private void addAndLayoutViewHorizontal(RecyclerView.Recycler recycler, RecyclerView.State state, int offset) {
        int itemCount = getItemCount();
        if (itemCount <= 0 || state.isPreLayout()) {
            return;
        }
        int displayWidth = getHorizontalSpace();

        for (int i = itemCount - 1; i >= 0; i--) {
            int rightOffset = (i + 1) * viewWidth - offset;
            int leftOffset = i * viewWidth - offset;
            boolean needAdd = true;
            if (rightOffset - displayWidth >= overFlyingDist) {
                needAdd = false;
            }
            if (leftOffset < -overFlyingDist && i != 0
                    || leftOffset < -overFlyingDist && !topOverFlying) {
                needAdd = false;
            }
            if (needAdd) {
                // 遍历Recycler中保存的View取出来
                View view = recycler.getViewForPosition(i);
                addView(view); // 因为刚刚进行了detach操作，所以现在可以重新添加
                measureChildWithMargins(view, 0, 0); // 通知测量view的margin值
                int width = getDecoratedMeasuredWidth(view); // 计算view实际大小，包括了ItemDecorator中设置的偏移量。
                int height = getDecoratedMeasuredHeight(view);


                //调用这个方法能够调整ItemView的大小，以除去ItemDecorator。
                calculateItemDecorationsForChild(view, new Rect());

                int realRightOffset = rightOffset;
                if (topOverFlying) {//除第一个外的左边缘慢速动画
                    if (i != 0) {
                        if (leftOffset <= width * edgePercent) {//到达边界了
                            int edgeDist = (int) (width * edgePercent);//边界触发距离
                            int left = (int) (edgeDist - (edgeDist - leftOffset) / slowTimes);///到达边界后速度放慢到原来5分之一
                            left = Math.max(0, left);
                            if (left < 0) {
                                left = 0;
                            }
                            realRightOffset = left + width;
                        }
                    } else {
                        realRightOffset = width;
                    }
                }
                if (i != itemCount - 1) {//除最后一个外的右边缘慢速动画
                    if (displayWidth - rightOffset <= width * edgePercent) {
                        int edgeDist = (int) (displayWidth - width * edgePercent);
                        int right = (int) (edgeDist + (rightOffset - edgeDist) / slowTimes);
                        if (right >= displayWidth) {
                            right = displayWidth;
                        }
                        realRightOffset = right;
                    }
                } else {
                    realRightOffset = totalWidth > displayWidth ? displayWidth : totalWidth;
                }
                layoutDecoratedWithMargins(view, realRightOffset - width, 0, realRightOffset, height);
            }
        }

        Log.d(TAG, "childCount = " + getChildCount() + "  itemCount= " +itemCount);
    }

    private int getVerticalSpace() {
        // 计算RecyclerView的可用高度，除去上下Padding值
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }


    private int getHorizontalSpace() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }


    public void setEdgePercent(@FloatRange(from = 0.01, to = 1.0) float edgePercent) {
        this.edgePercent = edgePercent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View findViewByPosition(int position) {
        final int childCount = getChildCount();
        if (childCount == 0) {
            return null;
        }
        final int firstChild = getPosition(getChildAt(0));
        final int viewPosition = position - firstChild;
        if (viewPosition >= 0 && viewPosition < childCount) {
            final View child = getChildAt(viewPosition);
            if (getPosition(child) == position) {
                return child; // in pre-layout, this may not match
            }
        }
        return super.findViewByPosition(position);
    }

    @Override
    public void scrollToPosition(int position) {

        offsetUseful = true;
        if (orientation == OrientationHelper.VERTICAL && viewHeight != 0) {
            verticalScrollOffset = position * viewHeight;
        } else if (orientation == OrientationHelper.HORIZONTAL && viewWidth != 0) {
            horizontalScrollOffset = position * viewWidth;
        }
        requestLayout();
    }

    public void setSlowTimes(@IntRange(from = 1) int slowTimes) {
        this.slowTimes = slowTimes;
    }


}