package com.example.odm.garbagesorthelper.ui.knowledge;

import android.app.Application;
import android.graphics.drawable.Drawable;

import com.example.odm.garbagesorthelper.R;
import com.example.odm.garbagesorthelper.base.BaseViewModel;
import com.example.odm.garbagesorthelper.model.RepositoryManager;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 知识页面ViewModel
 * author: ODM
 * date: 2019/9/19
 */
public class KnowLedgeViewModel extends BaseViewModel<RepositoryManager> {



    public int screenHeight;
    public int statusBarHeight;
    public int tabHeight;
    //判断是否是recyclerView主动引起的滑动，true- 是，false- 否，否则由tablayout引起的
    public boolean isRecyclerScroll;
    //记录上一次位置，防止在同一内容块里滑动 重复定位到tablayout
    public int lastPos;
    //用于recyclerView滑动到指定的位置
    public boolean canScroll;
    public int scrollToPosition;
    public String[] tabTxt = {"可回收垃圾", "有害垃圾", "干垃圾", "湿垃圾"};
    public int[] tabIcon = {R.drawable.module_search_cookiebar_recycle_garbage,R.drawable.module_search_cookiebar_harmful_garbage,
                             R.drawable.module_search_cookiebar_dry_garbage, R.drawable.module_search_cookiebar_wet_garbage};
    String[] imgUrls = {
            "https://raw.githubusercontent.com/DMingOu/Markdown-Picture-repository/master/img/20191029163449.webp",
            "https://raw.githubusercontent.com/DMingOu/Markdown-Picture-repository/master/img/20191029163500.webp",
            "https://raw.githubusercontent.com/DMingOu/Markdown-Picture-repository/master/img/20191029163513.webp",
            "https://raw.githubusercontent.com/DMingOu/Markdown-Picture-repository/master/img/20191029163520.webp"
    };
    public KnowLedgeViewModel(Application application ) {
        super(application );
    }

    public int getFinalHeight() {
        return screenHeight - statusBarHeight - tabHeight ;
    }
}
