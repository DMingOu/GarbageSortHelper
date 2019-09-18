package com.example.odm.garbagesorthelper.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * description: Activity基类
 * @author: ODM
 * @date: 2019/9/17
 */

public abstract class  BaseActivity extends RxAppCompatActivity implements IBaseView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //锁定旋转屏幕--只能竖屏使用
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 初始化页面的ViewModel和DataBinding
     */
    public abstract void initViewDataBinding() ;

    /**
     * 返回页面布局的ID
     *
     * @return the layout id
     */
    public abstract int getLayoutId();

    /**
     * 事件分发
     * 子Activity继承此方法就可以点击除输入框其他地方隐藏软键盘
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(this, Objects.requireNonNull(v).getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     */
    public static boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v instanceof EditText) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            //得到输入框在屏幕中上下左右的位置
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击位置如果是EditText的区域，忽略它，不收起键盘。
                return false;
            }
            else {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     */
    public static void hideKeyboard(Activity activity, IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(im).hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}

//public abstract class BaseActivity<V extends ViewDataBinding ,VM extends  BaseViewModel>  extends RxAppCompatActivity implements IBaseView{
//
//    private int layoutId;
//
//    protected V binding;
//
//    protected VM viewModel;
//    private int viewModelId;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        initViewDataBinding(savedInstanceState);
//        initViewObservable();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if(binding != null) {
//            binding.unbind();
//        }
//    }
//
//    private void initViewDataBinding(Bundle savedInstanceState) {
//        binding = DataBindingUtil.setContentView(this ,getLayoutId());
//        viewModelId = initVariableId();
//        if (viewModel == null) {
//            Class modelClass;
//            Type type = getClass().getGenericSuperclass();
//            if (type instanceof ParameterizedType) {
//                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
//                Log.e("modelClass", "initViewDataBinding: "+"属于");
//            } else {
//                //如果没有指定泛型参数，则默认使用BaseViewModel
//                modelClass = BaseViewModel.class;
//                Log.e("modelClass", "initViewDataBinding: "+"不属于");
//            }
//            viewModel = (VM) ViewModelProviders.of(this).get(modelClass);
////            viewModel = (VM) createViewModel(this, modelClass);
//        }
//
//        //关联ViewModel
//        binding.setVariable(viewModelId, viewModel);
//        //让ViewModel拥有View的生命周期感应
//        getLifecycle().addObserver(viewModel);
//        //当前Activity为 lifecycle owner
//        binding.setLifecycleOwner(this);
//    }
//
//    @Override
//    public abstract int getLayoutId();
//
//    public abstract void initViewObservable();
//
//
//
//    /**
//     * 创建ViewModel
//     *
//     * @param cls 类
//     * @return viewModel
//     */
//    public <T extends  ViewModel>  T createViewModel(FragmentActivity activity, Class<T> cls) {
//        return ViewModelProviders.of(activity).get(cls);
//    }
//
//    /**
//     * 初始化ViewModel的id,BR相关
//     *
//     * @return BR的id
//     */
//    public abstract int initVariableId();
//}
