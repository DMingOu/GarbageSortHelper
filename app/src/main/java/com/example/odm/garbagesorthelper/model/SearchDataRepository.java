package com.example.odm.garbagesorthelper.model;

import com.example.odm.garbagesorthelper.core.Constants;
import com.example.odm.garbagesorthelper.core.net.ApiService;
import com.example.odm.garbagesorthelper.core.net.RetrofitManager;
import com.example.odm.garbagesorthelper.model.entity.GarbageData;
import com.example.odm.garbagesorthelper.model.entity.ImageClassifyBean;
import com.example.odm.garbagesorthelper.utils.Base64Util;
import com.example.odm.garbagesorthelper.utils.FileUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SearchDataRepository {

    private static volatile SearchDataRepository singleton;

    private SearchDataRepository() {
    }

    public static SearchDataRepository getInstance() {
        if (singleton == null) {
            synchronized (SearchDataRepository.class) {
                if (singleton == null) {
                    singleton = new SearchDataRepository();
                }
            }
        }
        return singleton;
    }

    /**
     * 联网获取垃圾分类搜索结果
     * @param garbageName 垃圾名称
     * @return Observable对象
     */
    public Observable<GarbageData> getGarbageDataResult(String garbageName) {
        return  RetrofitManager.getInstance()
                .getApiService()
                .getGarbageData(garbageName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 联网获取图片识别结果
     * @param imageName 图片名
     * @return Observable对象
     */
    public Observable<ImageClassifyBean> getImageClassifyResult(String imageName) {
        String filePath = "/storage/emulated/0/"+imageName;
        byte[] imgData = new byte[0];
        try {
            imgData = FileUtil.readFileByBytes(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String imgStr_Base64 = Base64Util.encode(imgData);
        String imgParam = null;
        try {
            imgParam = URLEncoder.encode(imgStr_Base64, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String param = "image=" + imgParam;
        RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"),param);
        //请求数据
        return   RetrofitManager.getInstance()
                .getApiService()
                .getImageClassifyData(ApiService.Base_Url_Image_Classify , Constants.accessToken_baidu , body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}