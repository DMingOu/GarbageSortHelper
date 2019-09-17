package com.example.odm.garbagesorthelper.base;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * description: Activity基类
 * @author: ODM
 * @date: 2019/9/17
 */

public abstract class BaseActivity<T extends ViewDataBinding ,VM extends  BaseViewModel>  extends RxAppCompatActivity {

    private int layoutId;

    protected T binding;

    protected VM viewModel;
    private int viewModelId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewDataBinding(savedInstanceState);

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
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel.class;
            }
            viewModel = (VM) createViewModel(this, modelClass);
        }
        //关联ViewModel
        binding.setVariable(viewModelId, viewModel);
        //让ViewModel拥有View的生命周期感应
//        getLifecycle().addObserver(viewModel);
        //当前Activity为 lifecycle owner
        binding.setLifecycleOwner(this);
    }

    public int getLayoutId() {
        return layoutId;
    }

    public abstract void setLayoutId(int layoutId);

    /**
     * 创建ViewModel
     *
     * @param cls
     * @return
     */
    public VM createViewModel(RxAppCompatActivity activity, Class<VM> cls) {
        return ViewModelProviders.of(activity).get(cls);
    }

    /**
     * 初始化ViewModel的id,BR相关
     *
     * @return BR的id
     */
    public abstract int initVariableId();
}
