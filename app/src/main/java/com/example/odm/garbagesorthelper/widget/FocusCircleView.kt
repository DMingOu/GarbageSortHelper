package com.example.odm.garbagesorthelper.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
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
    //圆心的x坐标
    private var mX = 0f
    //圆心的Y坐标
    private var mY = 0f
    //小圆半径
    private var rs = 0f
    //大圆半径
    private var rb = 0f
    //控制对焦同心圆View缩放属性动画
    var valueAnimator :ValueAnimator ?= null
    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    /**
     * 初始化：画笔 , 属性动画和插值器
     */
    private fun init() {
        paint = Paint()
        //设画笔置白色
        paint?.color = resources.getColor(R.color.xui_config_color_white)
        //空心圆
        paint?.style = Paint.Style.STROKE
        paint?.strokeWidth = 5f
        valueAnimator = ValueAnimator.ofFloat(20f, 18.5f)
        valueAnimator?.interpolator = LinearInterpolator()
        valueAnimator?.duration = 1000
        valueAnimator?.repeatCount = 0
        valueAnimator?.addUpdateListener { animation ->
            rs = animation.animatedValue as Float
            rb = rs * 9 / 2
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint?.let {
            if (mX > 0 && mY > 0) {
                canvas.drawCircle(mX, mY, rs, paint!!)
                canvas.drawCircle(mX, mY, rb, paint!!)
            }
        }
    }

    private fun setFocusPoint(x: Float, y: Float) {
        mX = x
        mY = y
    }

    /**
     * 聚焦成功，显示绿色对焦圆圈，Todo：可能导致性能问题，有待改进
     */
    fun focusCompleted() {
        postDelayed({
            paint?.color = resources.getColor(R.color.xui_btn_green_normal_color)
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

    fun focusStart(x: Float, y: Float) {
        setFocusPoint(x, y)
        Logger.d("开始对焦")

        valueAnimator?.start()
    }
}