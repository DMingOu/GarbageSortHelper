package com.example.odm.garbagesorthelper.ui.knowledge;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.odm.garbagesorthelper.BR;
import com.example.odm.garbagesorthelper.R;
import com.example.odm.garbagesorthelper.base.BaseFragment;
import com.example.odm.garbagesorthelper.databinding.FragmentKnowledgeBinding;
import com.google.android.material.tabs.TabLayout;
import com.orhanobut.logger.Logger;

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
        initViewDataBinding(inflater, container);
        initTabLayout();
        return mBinding.getRoot();
    }

    @Override
    public void initViewDataBinding(LayoutInflater inflater, @Nullable ViewGroup container) {
        knowLedgeViewModel = ViewModelProviders.of(this).get(KnowLedgeViewModel.class);
        mBinding = DataBindingUtil.inflate(inflater,getLayoutId(),container ,false);
        mBinding.setViewModel(knowLedgeViewModel);
        mBinding.setVariable(com.example.odm.garbagesorthelper.BR.viewModel,knowLedgeViewModel);
        mBinding.setLifecycleOwner(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_knowledge;
    }

    private void initTabLayout(){
        knowLedgeViewModel.initTabData();
        for (int i = 0; i < knowLedgeViewModel.tabTitles.size(); i++) {
            TabLayout.Tab tab = mBinding.tabKnowledge.newTab();
            if (tab!=null){
                tab.setCustomView(getTabView(i));
            }
            mBinding.tabKnowledge.addTab(tab);

        }

    }

    public View getTabView(int position) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.item_tab_fragment_knowledge, null);
        TextView tv = v.findViewById(R.id.tv_tab_title);
        tv.setText(knowLedgeViewModel.tabTitles.get(position));
        ImageView iv = v.findViewById(R.id.iv_tab_icon);
        iv.setImageDrawable(knowLedgeViewModel.tabIcons.get(position));
        return v;
    }
}
