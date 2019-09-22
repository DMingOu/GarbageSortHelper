package com.example.odm.garbagesorthelper.ui.knowledge;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.odm.garbagesorthelper.BR;
import com.example.odm.garbagesorthelper.R;
import com.example.odm.garbagesorthelper.base.BaseFragment;
import com.example.odm.garbagesorthelper.databinding.FragmentKnowledgeBinding;
import com.example.odm.garbagesorthelper.utils.StatusBarUtils;
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
    private LinearLayoutManager manager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initViewDataBinding(inflater, container);
        initTabLayout();
        initRecyclerView();
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
        for (int i = 0; i < knowLedgeViewModel.tabTxt.length; i++) {
            TabLayout.Tab tab = mBinding.tabKnowledge.newTab();
            if (tab!=null){
                tab.setCustomView(getTabView(i));
            }
            mBinding.tabKnowledge.addTab(tab);
        }
        //点击标签，使recyclerView滑动，isRecyclerScroll置false
        mBinding.tabKnowledge.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int pos = tab.getPosition();
                knowLedgeViewModel.isRecyclerScroll = false;
                moveRecyclerViewToPosition(manager, mBinding.rvKnowledge, pos);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initRecyclerView() {
        knowLedgeViewModel.screenHeight = getScreenHeight();
        Logger.d(getScreenHeight());
        knowLedgeViewModel.statusBarHeight = StatusBarUtils.getStatusBarHeight(getContext());
        knowLedgeViewModel.tabHeight = StatusBarUtils.getStatusBarHeight(getContext()) * 3;
        manager = new LinearLayoutManager(getContext());
        mBinding.rvKnowledge.setLayoutManager(manager);
        mBinding.rvKnowledge.setAdapter(new AnchorRecyclerViewAdapter(getContext(),knowLedgeViewModel.garbageSortedResArray,knowLedgeViewModel.getFinalHeight()));
        mBinding.rvKnowledge.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //当滑动由recyclerView触发时，isRecyclerScroll 置true
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    knowLedgeViewModel.isRecyclerScroll = true;
//                    v.performClick();
                }
                //返回True时必须手动触发 v.performClick(); 否则导致点击事件不生效，事件分发机制导致
                return false;
            }
        });


        mBinding.rvKnowledge.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (knowLedgeViewModel.canScroll) {
                    knowLedgeViewModel.canScroll = false;
                    moveRecyclerViewToPosition(manager, recyclerView, knowLedgeViewModel.scrollToPosition);
                }
            }
            @Override
            public void onScrolled(@NonNull  RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (knowLedgeViewModel.isRecyclerScroll) {
                    //第一个可见的view的位置，即tablayou需定位的位置
                    int position = manager.findFirstVisibleItemPosition();
                    if (knowLedgeViewModel.lastPos != position) {
                        mBinding.tabKnowledge.setScrollPosition(position, 0, true);
                    }
                    knowLedgeViewModel.lastPos = position;
                }
            }
        });
    }

    private View getTabView(int position) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.item_tab_fragment_knowledge, null);
        TextView tv = v.findViewById(R.id.tv_tab_title);
        tv.setText(knowLedgeViewModel.tabTxt[position]);
        ImageView iv = v.findViewById(R.id.iv_tab_icon);
        iv.setImageDrawable(getResources().getDrawable(knowLedgeViewModel.tabIcon[position]));
        return v;
    }

    private int getScreenHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * TabLayout 移动--> RecyclerView移动
     * @param manager
     * @param mRecyclerView
     * @param position
     */
    private void moveRecyclerViewToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int position) {
        // 第一个可见的view的位置
        int firstItem = manager.findFirstVisibleItemPosition();
        // 最后一个可见的view的位置
        int lastItem = manager.findLastVisibleItemPosition();
        if (position <= firstItem) {
            // 如果跳转位置firstItem 之前(滑出屏幕的情况)，就smoothScrollToPosition可以直接跳转，
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 跳转位置在firstItem 之后，lastItem 之间（显示在当前屏幕），smoothScrollBy来滑动到指定位置
            int top = mRecyclerView.getChildAt(position - firstItem).getTop();
            mRecyclerView.smoothScrollBy(0, top);
        } else {
            // 如果要跳转的位置在lastItem 之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用当前moveToPosition方法，执行上一个判断中的方法
            mRecyclerView.smoothScrollToPosition(position);
            knowLedgeViewModel.scrollToPosition = position;
            knowLedgeViewModel.canScroll = true;
        }
    }
}
