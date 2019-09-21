package com.example.odm.garbagesorthelper.ui.knowledge;

import android.os.Bundle;
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
import com.example.odm.garbagesorthelper.databinding.FragmentKnowledgeBinding;

/**
 * description: 知识页面View层
 * author: ODM
 * date: 2019/9/18
 */
public class KnowLedgeFragment extends BaseFragment {

    private FragmentKnowledgeBinding mBinding;
    private KnowLedgeViewModel knowLedgeViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initViewDataBinding(inflater ,container);
        return mBinding.getRoot();
    }

    @Override
    public void initViewDataBinding(LayoutInflater inflater, @Nullable ViewGroup container) {
        knowLedgeViewModel = ViewModelProviders.of(this).get(KnowLedgeViewModel.class);
        mBinding = DataBindingUtil.inflate(inflater,getLayoutId(),container ,false);
        mBinding.setViewModel(knowLedgeViewModel);
        mBinding.setVariable(com.example.odm.garbagesorthelper.BR.viewModel,knowLedgeViewModel);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_knowledge;
    }
}
