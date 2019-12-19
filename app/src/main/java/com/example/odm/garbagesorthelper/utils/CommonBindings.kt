package com.example.odm.garbagesorthelper.utils

import android.content.Context
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.example.odm.garbagesorthelper.application.GarbageSortApplication.Companion.context
import java.util.*

/**
 * description: DataBinding自定义属性与事件
 * author: ODM
 * date: 2019/9/18
 */
object CommonBindings {
    @BindingAdapter("afterSearch")
    fun bindSearch(editText: EditText, value: String) {
        editText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH && "" != value) { //搜索内容非空且点击了搜索键后收起软键盘
                val manager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                //点击键盘的搜索键后，清空内容，放弃焦点
                editText.clearFocus()
                editText.setText("")

            }
            true
        }
    }

    @BindingAdapter("checkNewStringValue")
    fun checkNewValue(editText: EditText, newValue: String) {
        if (editText.text.toString() != newValue) {
            editText.setText(newValue)
        }
    }
}