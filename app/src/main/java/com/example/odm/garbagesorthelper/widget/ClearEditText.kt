package com.example.odm.garbagesorthelper.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.View.OnTouchListener
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.odm.garbagesorthelper.R

/**
 * description: 自定义输入框--左侧带有搜索图标，右侧有文本清空按钮
 * author: ODM
 * date: 2019/9/20
 */
class ClearEditText : AppCompatEditText, OnTouchListener, OnFocusChangeListener, TextWatcher {
    private var mClearIcon: Drawable? = null
    private var mSearchIcon: Drawable? = null
    private var mOnTouchListener: OnTouchListener? = null
    private var mOnFocusChangeListener: OnFocusChangeListener? = null

    constructor(context: Context?) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize(context)
    }

    private fun initialize(context: Context) { //清除图标
//Wrap the drawable so that it can be tinted pre Lollipop
//DrawableCompat.setTint(wrappedDrawable, getCurrentHintTextColor());
        mClearIcon = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.module_search_edittext_delete_all)!!)
        mClearIcon?.setBounds(0, 0, mClearIcon?.getIntrinsicWidth() ?: 0, mClearIcon?.getIntrinsicHeight()?:0)
        mSearchIcon = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.module_search_edittext_left_icon)!!)
        mSearchIcon?.setBounds(0, 0, mSearchIcon?.getIntrinsicWidth() ?: 0, mSearchIcon?.getIntrinsicHeight()?:0)
        setClearIconVisible(false)
        super.setOnTouchListener(this)
        super.setOnFocusChangeListener(this)
        super.addTextChangedListener(this)
        //会影响到背景Drawable的设置
//        ViewCompat.setBackgroundTintList(this, ColorStateList.valueOf(0x99000000));
    }

    override fun setBackgroundResource(resId: Int) {
        super.setBackgroundResource(resId)
    }

    private fun setClearIconVisible(visible: Boolean) {
        if (mClearIcon!!.isVisible == visible) {
            return
        }
        mClearIcon!!.setVisible(visible, false)
        val compoundDrawables = compoundDrawables
        //设置输入框相关的icon，分别是left,top,right,bottom
        setCompoundDrawables(
                mSearchIcon,
                compoundDrawables[1],
                if (visible) mClearIcon else null,
                compoundDrawables[3])
    }

    override fun setOnFocusChangeListener(onFocusChangeListener: OnFocusChangeListener) {
        mOnFocusChangeListener = onFocusChangeListener
    }

    override fun setOnTouchListener(onTouchListener: OnTouchListener) {
        mOnTouchListener = onTouchListener
    }

    /**
     * [View.OnFocusChangeListener]
     */
    override fun onFocusChange(view: View, hasFocus: Boolean) {
        if (hasFocus && text != null) {
            setClearIconVisible(text!!.length > 0)
        } else {
            setClearIconVisible(false)
        }
        if (mOnFocusChangeListener != null) {
            mOnFocusChangeListener!!.onFocusChange(view, hasFocus)
        }
    }

    /**
     * [View.OnTouchListener]
     */
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        val x = motionEvent.x.toInt()
        if (mClearIcon!!.isVisible && x > width - paddingRight - mClearIcon!!.intrinsicWidth) {
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                setText("")
            }
            return true
        }
        return mOnTouchListener != null && mOnTouchListener!!.onTouch(view, motionEvent)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (isFocused) {
            setClearIconVisible(s.length > 0)
        }
    }

    override fun afterTextChanged(s: Editable) {}
}