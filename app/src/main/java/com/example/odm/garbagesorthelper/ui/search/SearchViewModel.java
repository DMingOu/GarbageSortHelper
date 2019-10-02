package com.example.odm.garbagesorthelper.ui.search;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.example.odm.garbagesorthelper.R;
import com.example.odm.garbagesorthelper.base.BaseViewModel;
import com.example.odm.garbagesorthelper.core.Constants;
import com.example.odm.garbagesorthelper.core.net.ApiService;
import com.example.odm.garbagesorthelper.model.RepositoryManager;
import com.example.odm.garbagesorthelper.model.entity.GarbageData;
import com.example.odm.garbagesorthelper.core.net.HttpThrowable;
import com.example.odm.garbagesorthelper.core.net.ObserverManager;
import com.example.odm.garbagesorthelper.core.net.RetrofitManager;
import com.example.odm.garbagesorthelper.model.entity.ImageClassifyBean;
import com.example.odm.garbagesorthelper.utils.Base64Util;
import com.example.odm.garbagesorthelper.utils.FileUtil;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * description: 搜索页面ViewModel
 * author: ODM
 * date: 2019/9/19
 */
public class SearchViewModel extends BaseViewModel<RepositoryManager> {


    public SearchViewModel(Application application) {
        super(application);
    }


    /**
     * 用户搜索框搜索内容--垃圾名
     */
    public MutableLiveData<String> garbageName = new MutableLiveData<>();
    /**
     * 开启摄像头的变量
     */
    public MutableLiveData<Boolean> isOpenCamera = new MutableLiveData<>(false);
    /**
     * 用户搜索结果--分类垃圾列表
     */
    public MutableLiveData<List<GarbageData.DataBean>> sortedList = new MutableLiveData<>();

    public MutableLiveData<ImageClassifyBean.ResultBean> imageClassfyGarbage = new MutableLiveData<>();


    /**
     * 调用垃圾分类查询接口，通过垃圾名 获取垃圾所属分类
     *
     * @param garbageName the garbage name
     */
    public void onSearch(String garbageName) {


        RetrofitManager.getInstance()
                .getApiService()
                .getGarbageData(garbageName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverManager<GarbageData>() {
                    @Override
                    public void onError(HttpThrowable httpThrowable) {
                        Logger.d("异常原因:  "+httpThrowable.message);
                    }

                    @Override
                    public void onNext(GarbageData garbageData) {
                        if (garbageData != null) {
                                //将成功查询到的 列表加入 垃圾分类列表中
                                sortedList.setValue(garbageData.getData());
                        }
                    }
                });
    }


    /**
     * 不同的垃圾类别(int) 返回不同的垃圾分类图标
     *
     * @param garbageType the garbage type
     * @return the int
     */
    public int getGarbageIcon(int garbageType){
        switch (garbageType) {

            case 0:
                return R.drawable.module_search_cookiebar_fail_garbage;
            case 1 :
                return R.drawable.module_search_cookiebar_dry_garbage;

            case 2 :
                return R.drawable.module_search_cookiebar_wet_garbage;

            case 3:
                return R.drawable.module_search_cookiebar_recycle_garbage;

            case 4:
                return R.drawable.module_search_cookiebar_harmful_garbage;
            default:
                return R.drawable.module_search_cookiebar_fail_garbage;

        }
    }

    public void openCamera(){

        isOpenCamera.setValue(true);

    }

    /**
     * 调用百度的接口获取图片识别的数据
     *
     * @param imageName the image name
     */
    public void imageClassfyFromBaidu(String imageName)  {
        String filePath = "/storage/emulated/0/"+imageName;
        byte[] imgData = new byte[0];
        try {
            imgData = FileUtil.readFileByBytes(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String imgStr = Base64Util.encode(imgData);
        String imgParam = null;
        try {
            imgParam = URLEncoder.encode(imgStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String param = "image=" + imgParam;
        RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"),param);
        //请求数据
        RetrofitManager.getInstance()
                        .getApiService()
                        .getImageClassifyData(ApiService.Base_Url_Image_Classify , Constants.accessToken_baidu , body)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ObserverManager<ImageClassifyBean>() {
                            @Override
                            public void onError(HttpThrowable httpThrowable) {
                                Logger.d(httpThrowable.message);
                            }

                            @Override
                            public void onNext(ImageClassifyBean imageClassifyBean) {
                                imageClassfyGarbage.setValue(imageClassifyBean.getResult().get(0));
                            }
                        });


    }

}
