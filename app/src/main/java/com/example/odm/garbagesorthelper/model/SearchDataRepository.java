package com.example.odm.garbagesorthelper.model;

import com.example.odm.garbagesorthelper.core.Constants;
import com.example.odm.garbagesorthelper.model.source.net.ApiService;
import com.example.odm.garbagesorthelper.model.source.net.RetrofitManager;
import com.example.odm.garbagesorthelper.model.entity.GarbageData;
import com.example.odm.garbagesorthelper.model.entity.GarbageSearchHistory;
import com.example.odm.garbagesorthelper.model.entity.ImageClassifyBean;
import com.example.odm.garbagesorthelper.model.source.local.LocalGarbageHistoryDataSource;
import com.example.odm.garbagesorthelper.utils.Base64Util;
import com.example.odm.garbagesorthelper.utils.FileUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SearchDataRepository {

    private static volatile SearchDataRepository singleton;

    private final LocalGarbageHistoryDataSource historyDataSource;


    private SearchDataRepository(LocalGarbageHistoryDataSource dataSource) {
        this.historyDataSource = dataSource;
    }

    public static SearchDataRepository getInstance(LocalGarbageHistoryDataSource dataSource) {
        if (singleton == null) {
            synchronized (SearchDataRepository.class) {
                if (singleton == null) {
                    singleton = new SearchDataRepository(dataSource);
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

    /**
     * 获取所有 垃圾搜索历史
     * Flowable 背压，第一次启动后，每次Flowable内部数据有变动，都会执行一次方法
     * @return 数据库中所有垃圾搜索分类历史
     */
    public Flowable<List<GarbageSearchHistory>> getAllGarbageHistory() {
        return historyDataSource.getAllGarbageHistory()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<GarbageSearchHistory> getGarbageHistoryByName(String name) {
        return historyDataSource.getGarbageHistoryByName(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 根据垃圾名称和垃圾种类，插入数据库
     * @param garbageName
     * @param garbageType
     * @return
     */
    public Completable insertGarbageHistory(String garbageName ,int garbageType) {

        GarbageSearchHistory history = new GarbageSearchHistory(garbageName ,garbageType,(int)System.currentTimeMillis());
        return historyDataSource.insertGarbageHistory(history)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void deleteAllGarbageHistory() {
        historyDataSource.deleteAllGarbageHistory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void deleteGarbageHistory(GarbageSearchHistory... histories) {
        historyDataSource.delete(histories)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }


}