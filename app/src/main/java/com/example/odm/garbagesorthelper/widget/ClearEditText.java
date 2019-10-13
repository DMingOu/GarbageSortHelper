package com.example.odm.garbagesorthelper.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;

import com.example.odm.garbagesorthelper.R;

/**
 * description: 自定义输入框--左侧带有搜索图标，右侧有文本清空按钮
 * author: ODM
 * date: 2019/9/20
 */
public class ClearEditText extends AppCompatEditText implements View.OnTouchListener, View.OnFocusChangeListener, TextWatcher {

    private Drawable mClearIcon;
    private Drawable mSearchIcon;
    private OnTouchListener mOnTouchListener;

    private OnFocusChangeListener mOnFocusChangeListener;

    public ClearEditText(Context context) {
        super(context);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(final Context context) {

        //清除图标
        //Wrap the drawable so that it can be tinted pre Lollipop
        //DrawableCompat.setTint(wrappedDrawable, getCurrentHintTextColor());
        mClearIcon = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.module_search_edittext_delete_all));
        mClearIcon.setBounds(0, 0, mClearIcon.getIntrinsicWidth(), mClearIcon.getIntrinsicHeight());
        mSearchIcon = DrawableCompat.wrap(ContextCompat.getDrawable(context ,R.drawable.module_search_edittext_left_icon));
        mSearchIcon.setBounds(0, 0, mSearchIcon.getIntrinsicWidth(), mSearchIcon.getIntrinsicHeight());
        setClearIconVisible(false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        super.addTextChangedListener(this);
        //会影响到背景Drawable的设置
//        ViewCompat.setBackgroundTintList(this, ColorStateList.valueOf(0x99000000));
    }

    @Override
    public void setBackgroundResource(int resId) {
        super.setBackgroundResource(resId);
    }

    private void setClearIconVisible(final boolean visible) {
        if (mClearIcon.isVisible() == visible) {
            return;
        }
        mClearIcon.setVisible(visible, false);
        final Drawable[] compoundDrawables = getCompoundDrawables();
        //设置输入框相关的icon，分别是left,top,right,bottom
        setCompoundDrawables(
                mSearchIcon,
                compoundDrawables[1],
                visible ? mClearIcon : null,
                compoundDrawables[3]);
    }



    @Override
    public void setOnFocusChangeListener(final OnFocusChangeListener onFocusChangeListener) {
        mOnFocusChangeListener = onFocusChangeListener;
    }

    @Override
    public void setOnTouchListener(final OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
    }

    /**
     * {@link View.OnFocusChangeListener}
     */


    @Override
    public void onFocusChange(final View view, final boolean hasFocus) {
        if (hasFocus && getText() != null) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
        if (mOnFocusChangeListener != null) {
            mOnFocusChangeListener.onFocusChange(view, hasFocus);
        }
    }

    /**
     * {@link View.OnTouchListener}
     */

    @Override
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        final int x = (int) motionEvent.getX();
        if (mClearIcon.isVisible() && x > getWidth() - getPaddingRight() - mClearIcon.getIntrinsicWidth()) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                setText("");
            }
            return true;
        }
        return mOnTouchListener != null && mOnTouchListener.onTouch(view, motionEvent);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isFocused()) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}