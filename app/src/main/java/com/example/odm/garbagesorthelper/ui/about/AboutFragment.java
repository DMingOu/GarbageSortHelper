package com.example.odm.garbagesorthelper.ui.about;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.odm.garbagesorthelper.BR;
import com.example.odm.garbagesorthelper.R;
import com.example.odm.garbagesorthelper.base.BaseFragment;
import com.example.odm.garbagesorthelper.databinding.FragmentAboutBinding;
import com.example.odm.garbagesorthelper.databinding.FragmentAboutBindingImpl;

/**
 * description: 我的页面View层
 * author: ODM
 * date: 2019/9/18
 */
public class AboutFragment extends BaseFragment {

    FragmentAboutBinding mBinding;
    AboutViewModel aboutViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initViewDataBinding(inflater , container);
        return mBinding.getRoot();
    }

    @Override
    public void initViewDataBinding(LayoutInflater inflater, @Nullable ViewGroup container) {
        aboutViewModel = ViewModelProviders.of(this).get(AboutViewModel.class);
        mBinding = DataBindingUtil.inflate(inflater ,getLayoutId() ,container ,false);
        mBinding.setViewModel(aboutViewModel);
        mBinding.setVariable(com.example.odm.garbagesorthelper.BR.viewModel,aboutViewModel);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_about;
    }
}
