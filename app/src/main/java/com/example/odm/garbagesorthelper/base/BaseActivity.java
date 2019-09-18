package com.example.odm.garbagesorthelper.base;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

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

/**
 * description: Activity基类
 * @author: ODM
 * @date: 2019/9/17
 */

public abstract class BaseActivity<V extends ViewDataBinding ,VM extends  BaseViewModel>  extends RxAppCompatActivity implements IBaseView{

    private int layoutId;

    protected V binding;

    protected VM viewModel;
    private int viewModelId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewDataBinding(savedInstanceState);
        initViewObservable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(binding != null) {
            binding.unbind();
        }
    }

    private void initViewDataBinding(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this ,getLayoutId());
        viewModelId = initVariableId();
        if (viewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
                Log.e("modelClass", "initViewDataBinding: "+"属于");
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel.class;
                Log.e("modelClass", "initViewDataBinding: "+"不属于");
            }
            viewModel = (VM) ViewModelProviders.of(this).get(modelClass);
//            viewModel = (VM) createViewModel(this, modelClass);
        }

        //关联ViewModel
        binding.setVariable(viewModelId, viewModel);
        //让ViewModel拥有View的生命周期感应
        getLifecycle().addObserver(viewModel);
        //当前Activity为 lifecycle owner
        binding.setLifecycleOwner(this);
    }

    @Override
    public abstract int getLayoutId();

    public abstract void initViewObservable();



    /**
     * 创建ViewModel
     *
     * @param cls 类
     * @return viewModel
     */
    public <T extends  ViewModel>  T createViewModel(FragmentActivity activity, Class<T> cls) {
        return ViewModelProviders.of(activity).get(cls);
    }

    /**
     * 初始化ViewModel的id,BR相关
     *
     * @return BR的id
     */
    public abstract int initVariableId();
}
