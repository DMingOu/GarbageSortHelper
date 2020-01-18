package com.example.odm.garbagesorthelper.ui.knowledge

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.odm.garbagesorthelper.R
import com.example.odm.garbagesorthelper.base.BaseFragment
import com.example.odm.garbagesorthelper.utils.StatusBarUtils
import com.google.android.material.tabs.TabLayout

/**
 * description: 知识页面View层
 * author: ODM
 * date: 2019/9/18
 */
class KnowLedgeFragment : BaseFragment() {

    private var tabKnowledge : TabLayout ?= null
    private var rvKnowledge : RecyclerView ?= null
    
    private var knowLedgeViewModel: KnowLedgeViewModel? = null
    private var manager: LinearLayoutManager? = null

    override val layoutId: Int
        get() = R.layout.fragment_knowledge

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        knowLedgeViewModel = ViewModelProviders.of(this).get(KnowLedgeViewModel::class.java)

        return inflater.inflate(layoutId, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        initTabLayout()
        initRecyclerView()
    }

    override fun initViews() {
         tabKnowledge = activity?.findViewById(R.id.tab_knowledge)
         rvKnowledge = activity?.findViewById(R.id.rv_knowledge)
    }



    private fun initTabLayout() {
        for (i in knowLedgeViewModel!!.tabTxt.indices) {
            val tab = tabKnowledge?.newTab()
            tab?.let {
                it.customView = getTabView(i)
                tabKnowledge?.addTab(it)
            }
        }
        //点击标签，使recyclerView滑动，isRecyclerScroll置false
         tabKnowledge?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val pos = tab.position
                knowLedgeViewModel?.isRecyclerScroll = false
                moveRecyclerViewToPosition(manager,  rvKnowledge!!, pos)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    /**
     * 初始化滚动列表
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun initRecyclerView() {
        knowLedgeViewModel?.screenHeight = screenHeight
        knowLedgeViewModel?.statusBarHeight = StatusBarUtils.getStatusBarHeight(context)
        knowLedgeViewModel?.tabHeight = StatusBarUtils.getStatusBarHeight(context) * 3
        manager = LinearLayoutManager(context)
         rvKnowledge?.layoutManager = manager
         rvKnowledge?.adapter = AnchorRecyclerViewAdapter(requireContext(), knowLedgeViewModel!!.imgUrls )
         rvKnowledge?.setOnTouchListener { v, event ->
            //当滑动由recyclerView触发时，isRecyclerScroll 置true
            if (event.action == MotionEvent.ACTION_DOWN) {
                knowLedgeViewModel?.isRecyclerScroll = true
            }
            false
        }
         rvKnowledge?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (knowLedgeViewModel?.canScroll ?: false) {
                    knowLedgeViewModel?.canScroll = false
                    moveRecyclerViewToPosition(manager, recyclerView, knowLedgeViewModel?.scrollToPosition ?: 0)
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (knowLedgeViewModel?.isRecyclerScroll ?: false) { //第一个可见的view的位置，即tabLayout需定位的位置
                    val position = manager?.findFirstVisibleItemPosition() ?: 0
                    if (knowLedgeViewModel?.lastPos != position) {
                         tabKnowledge?.setScrollPosition(position, 0f, true)
                    }
                    knowLedgeViewModel?.lastPos = position
                }
            }
        })
    }

    private fun getTabView(position: Int): View {
        val v = LayoutInflater.from(activity).inflate(R.layout.item_tab_fragment_knowledge, null)
        val tv = v.findViewById<TextView>(R.id.tv_tab_title)
        tv.text = knowLedgeViewModel?.tabTxt?.get(position)
        val iv = v.findViewById<ImageView>(R.id.iv_tab_icon)
        iv.setImageDrawable(resources.getDrawable(knowLedgeViewModel?.tabIcon?.get(position) ?: 0))
        return v
    }

    private val screenHeight: Int
         get() = resources.displayMetrics.heightPixels

    /**
     * TabLayout 移动--> RecyclerView移动
     * @param manager LinearLayoutManager
     * @param mRecyclerView 列表
     * @param position 位置
     */
    private fun moveRecyclerViewToPosition(manager: LinearLayoutManager?, mRecyclerView: RecyclerView, position: Int) { // 第一个可见的view的位置
        val firstItem = manager?.findFirstVisibleItemPosition() ?: 0
        // 最后一个可见的view的位置
        val lastItem = manager?.findLastVisibleItemPosition() ?: 0
        if (position <= firstItem) { // 如果跳转位置firstItem 之前(滑出屏幕的情况)，就smoothScrollToPosition可以直接跳转，
            mRecyclerView.smoothScrollToPosition(position)
        } else if (position <= lastItem) { // 跳转位置在firstItem 之后，lastItem 之间（显示在当前屏幕），smoothScrollBy来滑动到指定位置
            val top = mRecyclerView.getChildAt(position - firstItem).top
            mRecyclerView.smoothScrollBy(0, top)
        } else { // 如果要跳转的位置在lastItem 之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用当前moveToPosition方法，执行上一个判断中的方法
            mRecyclerView.smoothScrollToPosition(position)
            knowLedgeViewModel?.scrollToPosition = position
            knowLedgeViewModel?.canScroll = true
        }
    }
}