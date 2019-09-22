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

    public List<String> tabTitles;
    public List<Drawable> tabIcons;

    public KnowLedgeViewModel(Application application ) {
        super(application );
    }

    public void initTabData(){
        tabTitles = new ArrayList<>();
        tabIcons = new ArrayList<>();
        tabTitles.add("可回收垃圾");
        tabTitles.add("有害垃圾");
        tabTitles.add("干垃圾");
        tabTitles.add("湿垃圾");
        tabIcons.add(getApplication().getDrawable(R.drawable.module_search_cookiebar_recycle_garbage));
        tabIcons.add(getApplication().getDrawable(R.drawable.module_search_cookiebar_harmful_garbage));
        tabIcons.add(getApplication().getDrawable(R.drawable.module_search_cookiebar_dry_garbage));
        tabIcons.add(getApplication().getDrawable(R.drawable.module_search_cookiebar_wet_garbage));
    }
}
