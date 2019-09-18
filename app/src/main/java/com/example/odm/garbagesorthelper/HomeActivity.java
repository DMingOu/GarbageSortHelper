package com.example.odm.garbagesorthelper;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.odm.garbagesorthelper.base.BaseActivity;
import com.example.odm.garbagesorthelper.base.IBaseView;
import com.example.odm.garbagesorthelper.databinding.ActivityHomeBinding;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

public class HomeActivity extends RxAppCompatActivity implements IBaseView {

    private static final String TAG = "HomeActivity";

    ActivityHomeBinding homeBinding;
    HomeViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViewDataBinding();
    }

    @Override
    public void initViewDataBinding() {
        homeBinding = DataBindingUtil.setContentView(this ,getLayoutId());
        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homeBinding.setViewModel(viewModel);
        homeBinding.setLifecycleOwner(this);
        homeBinding.setVariable(BR.viewModel,viewModel);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }


}
