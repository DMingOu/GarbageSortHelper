package com.example.odm.garbagesorthelper.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * description: Fragment基类
 * author: ODM
 * date: 2019/9/19
 */
public abstract class BaseFragment extends Fragment implements IBaseView{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

    }


    /**
     * 初始化页面的ViewModel和DataBinding
     */
    public abstract void initViewDataBinding( LayoutInflater inflater, @Nullable ViewGroup container) ;

    /**
     * 返回页面布局的ID
     *
     * @return the layout id
     */
    public abstract int getLayoutId();
}
