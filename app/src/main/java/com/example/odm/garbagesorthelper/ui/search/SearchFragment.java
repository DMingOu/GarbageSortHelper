package com.example.odm.garbagesorthelper.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.odm.garbagesorthelper.BR;
import com.example.odm.garbagesorthelper.R;
import com.example.odm.garbagesorthelper.SearchViewModelFactory;
import com.example.odm.garbagesorthelper.application.GarbageSortApplication;
import com.example.odm.garbagesorthelper.base.BaseFragment;
import com.example.odm.garbagesorthelper.databinding.FragmentSearchBinding;
import com.example.odm.garbagesorthelper.model.entity.Garbage;
import com.xuexiang.xui.widget.popupwindow.bar.CookieBar;

import java.util.List;

/**
 * description: 搜索页面View层
 * author: ODM
 * date: 2019/9/18
 */
public class SearchFragment extends BaseFragment {

    private FragmentSearchBinding mBinding;
    private SearchViewModel  searchViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initViewDataBinding(inflater ,container);
        initEditText();
        initDataObservable();
//        首先通过DataBindingUtil.inflate初始化binding对象，然后通过.getRoot()获取操作视图，并且在onCreateView中返回该视图。否则会导致binding不生效。
        return mBinding.getRoot();
    }


    @Override
    public void initViewDataBinding( LayoutInflater inflater , @Nullable ViewGroup container) {
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        mBinding =  DataBindingUtil.inflate(inflater,getLayoutId() ,container,false);
        mBinding.setViewModel(searchViewModel);
        mBinding.setVariable(BR.viewModel,searchViewModel);
        mBinding.setLifecycleOwner(this);
    }

    private void initEditText() {
        mBinding.etSearch.setOnEditorActionListener(( v,actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_SEARCH && !"".equals(searchViewModel.garbageName.toString())) {
                //搜索内容非空且点击了搜索键后收起软键盘
                InputMethodManager manager = ((InputMethodManager) GarbageSortApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
                if (manager != null) {
                    manager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //触发软键盘的点击事件
                    searchViewModel.onSearch();
                    //点击键盘的搜索键后，清空内容，放弃焦点
                    mBinding.etSearch.clearFocus();
                    mBinding.etSearch.setText("");
                }
            }
            return true;
        });
    }

    private void initDataObservable() {
        searchViewModel.sortedList.observe(this, dataBeans -> {
            if(dataBeans != null && dataBeans.size() > 0) {
                showGarbageResultBar();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search;
    }


        /**
     * 弹出Bar提示--用户搜索结果
     */
    private void showGarbageResultBar() {
        CookieBar.builder(getActivity())
                .setTitle(searchViewModel.sortedList.getValue().get(0).getGname())
                .setIcon(searchViewModel.getGarbageIcon(searchViewModel.sortedList.getValue().get(0).getGtype()))
                .setMessage(searchViewModel.sortedList.getValue().get(0).getGtype())
                .setLayoutGravity(Gravity.BOTTOM)
                .setAction(R.string.known, null)
                .setDuration(3000)
                .show();
    }
}
