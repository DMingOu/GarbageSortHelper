package com.example.odm.garbagesorthelper.ui.about;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.lifecycle.MutableLiveData;

import com.example.odm.garbagesorthelper.base.BaseViewModel;

/**
 * description: 关于页面ViewModel
 * author: ODM
 * date: 2019/9/19
 */
public class AboutViewModel extends BaseViewModel {


    public MutableLiveData<String> versionName = new MutableLiveData<>();


    public AboutViewModel(Application application) {
        super(application);
    }


    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = getApplication().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getApplication().getPackageName(), 0);
            String version = info.versionName;
            return "version  ".concat(version);
        } catch (Exception e) {
            e.printStackTrace();
            return "无法得到当前版本";
        }
    }


}

