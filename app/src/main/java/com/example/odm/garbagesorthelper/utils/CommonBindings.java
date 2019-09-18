package com.example.odm.garbagesorthelper.utils;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.example.odm.garbagesorthelper.application.GarbageSortApplication;
import com.orhanobut.logger.Logger;

import static com.example.odm.garbagesorthelper.application.GarbageSortApplication.getContext;

/**
 * description: DataBinding自定义属性与事件
 * author: ODM
 * date: 2019/9/18
 */
public class CommonBindings {

    @BindingAdapter("searchAction")
    public static void bindSearch(EditText editText , String value) {

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //收起软键盘
                    InputMethodManager manager = ((InputMethodManager) GarbageSortApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
                    if (manager != null) {
                        manager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        Logger.d("点击了搜索键   " + value);
                    }
                }
                return true;
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus) {
                    InputMethodManager manager = ((InputMethodManager) GarbageSortApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
                    if (manager != null) {
                        manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }
        });
    }
}
