package com.inwecrypto.wallet.common.util;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;

/**
 * 作者：xiaoji06 on 2017/11/8 17:00
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class AnimUtil {

    public static void startShowAnimation(View view) {
        if (null==view){
            return;
        }
        //清除动画
        view.clearAnimation();
        /**
         * @param fromAlpha 开始的透明度，取值是0.0f~1.0f，0.0f表示完全透明， 1.0f表示和原来一样
         * @param toAlpha 结束的透明度，同上
         */
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        //设置动画持续时长
        alphaAnimation.setDuration(300);
        //设置动画结束之后的状态是否是动画的最终状态，true，表示是保持动画结束时的最终状态
        alphaAnimation.setFillAfter(true);
        //开始动画
        view.startAnimation(alphaAnimation);
    }

    public static void startHideAnimation(View view) {
        if (null==view){
            return;
        }
        //清除动画
        view.clearAnimation();
        /**
         * @param fromAlpha 开始的透明度，取值是0.0f~1.0f，0.0f表示完全透明， 1.0f表示和原来一样
         * @param toAlpha 结束的透明度，同上
         */
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        //设置动画持续时长
        alphaAnimation.setDuration(300);
        //设置动画结束之后的状态是否是动画的最终状态，true，表示是保持动画结束时的最终状态
        alphaAnimation.setFillAfter(true);
        //开始动画
        view.startAnimation(alphaAnimation);
    }

    public static void startMoveRightAnimation(View view,int distance) {
        if (null==view){
            return;
        }
        //清除动画
        view.clearAnimation();
        /**
         * @param fromAlpha 开始的透明度，取值是0.0f~1.0f，0.0f表示完全透明， 1.0f表示和原来一样
         * @param toAlpha 结束的透明度，同上
         */
        TranslateAnimation animation = new TranslateAnimation(-distance,0, 0, 0);
        animation.setDuration(800);
        animation.setRepeatCount(0);//动画的重复次数
        //设置动画结束之后的状态是否是动画的最终状态，true，表示是保持动画结束时的最终状态
        animation.setFillAfter(true);
        //开始动画
        view.startAnimation(animation);
    }

    public static void startMoveLeftAnimation(View view,int distance) {
        if (null==view){
            return;
        }
        //清除动画
        view.clearAnimation();
        /**
         * @param fromAlpha 开始的透明度，取值是0.0f~1.0f，0.0f表示完全透明， 1.0f表示和原来一样
         * @param toAlpha 结束的透明度，同上
         */
        TranslateAnimation animation = new TranslateAnimation(0, -distance, 0, 0);
        animation.setDuration(800);
        animation.setRepeatCount(0);//动画的重复次数
        //设置动画结束之后的状态是否是动画的最终状态，true，表示是保持动画结束时的最终状态
        animation.setFillAfter(true);
        //开始动画
        view.startAnimation(animation);
    }

    private static ValueAnimator colorAnimator;

    public static void startShowColorAnimation(final View view) {
        if (null==view){
            return;
        }
        //清除动画
        view.clearAnimation();

        if (null!=colorAnimator){
            colorAnimator.cancel();
        }
        colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), Color.parseColor("#00ffffff"), Color.parseColor("#ffffffff"));
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int color = (int) animation.getAnimatedValue();//之后就可以得到动画的颜色了
                view.setBackgroundColor(color);//设置一下, 就可以看到效果.
            }
        });
        colorAnimator.setDuration(700);
        colorAnimator.start();

    }

    public static void startHideColorAnimation(final View view) {
        if (null==view){
            return;
        }
        //清除动画
        view.clearAnimation();

        if (null!=colorAnimator){
            colorAnimator.cancel();
        }
        colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), Color.parseColor("#ffffffff"), Color.parseColor("#00ffffff"));
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int color = (int) animation.getAnimatedValue();//之后就可以得到动画的颜色了
                view.setBackgroundColor(color);//设置一下, 就可以看到效果.
            }
        });
        colorAnimator.setDuration(700);
        colorAnimator.start();

    }


}
