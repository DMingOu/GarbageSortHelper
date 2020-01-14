package com.example.odm.garbagesorthelper.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import com.example.odm.garbagesorthelper.R
import com.orhanobut.logger.Logger

/**
 * description: 对焦圆框自定义View
 * author: ODM
 * date: 2019/10/13
 */
class FocusCircleView : View {
    private var paint: Paint? = null
    var scaleAnimation: ScaleAnimation? = null
    //默认启动时在中心对焦，自定义View在屏幕中心画出
    private var mX = 0f
    private var mY = 0f

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    /**
     * 初始化：画笔 ,缩放效果
     */
    private fun init() {
        paint = Paint()
        //设画笔置白色
        paint?.color = resources.getColor(R.color.xui_config_color_white)
        //空心圆
        paint?.style = Paint.Style.STROKE
        paint?.strokeWidth = 5f
//        scaleAnimation = ScaleAnimation(1.015f, 1f, 1.015f, 1f,
//                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f)
        scaleAnimation = ScaleAnimation(1.0f, 1f, 1.0f, 1f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f)
        scaleAnimation?.duration = 1000
        scaleAnimation?.fillAfter = false
        scaleAnimation?.repeatCount = 0


    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (paint != null && scaleAnimation != null) {
            if (mX > 0 && mY > 0) {
                canvas.drawCircle(mX, mY, 20f, paint!!)
                canvas.drawCircle(mX, mY, 90f, paint!!)
            }
        }
    }

    fun setFocusPoint(x: Float, y: Float) {
        mX = x
        mY = y
    }

    /**
     * 聚焦成功，显示绿色对焦圆圈，Todo：可能导致性能问题，有待改进
     */
    fun focusCompleted() {
        postDelayed({
            paint!!.color = resources.getColor(R.color.xui_btn_green_normal_color)
            invalidate()
        }, 500)
        //让自定义View显示绿色状态后，延迟1000ms再消失
        postDelayed({
            //重新初始化属性，准备下一次对焦
            setFocusPoint(0f, 0f)
            invalidate()
            paint?.color = resources.getColor(R.color.xui_config_color_white)
        }, 1000)
    }

    fun focusStart(focusCircleView: FocusCircleView, x: Float, y: Float) {
        setFocusPoint(x, y)
        focusCircleView.startAnimation(scaleAnimation)
        invalidate()
    }
}