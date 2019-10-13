package com.example.odm.garbagesorthelper.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import androidx.annotation.Nullable;

import com.example.odm.garbagesorthelper.R;
import com.orhanobut.logger.Logger;

/**
 * description: 对焦圆框自定义View
 * author: ODM
 * date: 2019/10/13
 */
public class FocusCircleView extends View {

    private Paint paint;
    ScaleAnimation scaleAnimation;
    //默认启动时在中心对焦，自定义View在屏幕中心画出
    private float mX ;
    private float mY ;


    public FocusCircleView(Context context) {
        super(context);
        init();

    }


    public FocusCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    /**
     * 初始化：画笔 ,缩放效果
     */
    private void init() {
        paint = new Paint();
        scaleAnimation = new ScaleAnimation(1.015f, 1f, 1.015f, 1f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
        //设画笔置白色
        paint.setColor(getResources().getColor(R.color.xui_config_color_white));
        //空心圆
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);

        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(false);
        scaleAnimation.setRepeatCount(0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(paint != null && scaleAnimation != null) {
            if(mX > 0 && mY >0) {
                Logger.d("绘制对焦⚪");
                canvas.drawCircle(mX , mY , 20 ,paint);
                canvas.drawCircle(mX ,mY , 90 ,paint);
            }
        }

    }




    public void setFocusPoint(float x , float y) {
        this.mX = x;
        this.mY = y;

    }


    /**
     * 聚焦成功，显示绿色对焦圆圈，Todo：可能导致性能问题，有待改进
     */
    public void focusCompleted() {
        postDelayed(()-> {
            paint.setColor(getResources().getColor(R.color.xui_btn_green_normal_color));
            invalidate();
        }, 500);

        //让自定义View显示绿色状态后，延迟1000ms再消失
        postDelayed(()-> {
            //重新初始化属性，准备下一次对焦
            setFocusPoint(0 , 0);
            invalidate();
            paint.setColor(getResources().getColor(R.color.xui_config_color_white));
        }, 1000);
    }


     public void focusStart (FocusCircleView focusCircleView , float x , float y){
             setFocusPoint(x ,y);
             focusCircleView.startAnimation(scaleAnimation);
             invalidate();
     }




}
