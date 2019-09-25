package com.example.odm.garbagesorthelper.ui.search;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.example.odm.garbagesorthelper.R;
import com.example.odm.garbagesorthelper.base.BaseViewModel;
import com.example.odm.garbagesorthelper.model.AipImageClassifyCilent;
import com.example.odm.garbagesorthelper.model.RepositoryManager;
import com.example.odm.garbagesorthelper.model.entity.GarbageData;
import com.example.odm.garbagesorthelper.core.net.HttpThrowable;
import com.example.odm.garbagesorthelper.core.net.ObserverManager;
import com.example.odm.garbagesorthelper.core.net.RetrofitManager;
import com.example.odm.garbagesorthelper.model.entity.ImageClassfyData;
import com.example.odm.garbagesorthelper.utils.GsonUtils;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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

    public MutableLiveData<ImageClassfyData.NameValuePairsBeanXX.ResultBean.ValuesBean.NameValuePairsBeanX> imageClassfyGarbage = new MutableLiveData<>();
    /**
     * 百度图像识别的分类配置
     */
    private HashMap<String, String> classfyOptions = new HashMap<String, String>();



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

    public void imageClassfyFromBaidu(String imageName) {
        //返回信息的百科数量
        classfyOptions.put("baike_num", "1");
        Disposable subscribe = Flowable.create(new FlowableOnSubscribe<ImageClassfyData>() {
            @Override
            public void subscribe(FlowableEmitter<ImageClassfyData> emitter) throws Exception {
                // 参数为本地路径
                String imagePath = "/storage/emulated/0/"+imageName;
                JSONObject object = AipImageClassifyCilent.getInstance().advancedGeneral(imagePath, classfyOptions);
                ImageClassfyData classfyData = GsonUtils.GsonToBean(GsonUtils.GsonString(object), ImageClassfyData.class);
                emitter.onNext(classfyData);

            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ImageClassfyData>() {
                    @Override
                    public void accept(ImageClassfyData data) throws Exception {

                        imageClassfyGarbage.setValue(data.getNameValuePairs().getResult().getValues().get(0).getNameValuePairs());
                    }
                });

    }

}
