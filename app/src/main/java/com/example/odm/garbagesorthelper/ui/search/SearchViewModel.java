package com.example.odm.garbagesorthelper.ui.search;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.odm.garbagesorthelper.R;
import com.example.odm.garbagesorthelper.core.Constants;
import com.example.odm.garbagesorthelper.core.net.ApiService;
import com.example.odm.garbagesorthelper.model.SearchDataRepository;
import com.example.odm.garbagesorthelper.model.entity.BannerData;
import com.example.odm.garbagesorthelper.model.entity.GarbageData;
import com.example.odm.garbagesorthelper.core.net.HttpThrowable;
import com.example.odm.garbagesorthelper.core.net.ObserverManager;
import com.example.odm.garbagesorthelper.core.net.RetrofitManager;
import com.example.odm.garbagesorthelper.model.entity.ImageClassifyBean;
import com.example.odm.garbagesorthelper.model.entity.VoiceRecognizedData;
import com.example.odm.garbagesorthelper.utils.Base64Util;
import com.example.odm.garbagesorthelper.utils.FileUtil;
import com.example.odm.garbagesorthelper.utils.GsonUtils;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
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
public class SearchViewModel extends ViewModel {


//    public SearchViewModel(Application application) {
//        super(application);
//    }
    private SearchDataRepository repository;

    public SearchViewModel(SearchDataRepository repository) {
        this.repository = repository;
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
     * 开启麦克风的控制变量
     */
    public MutableLiveData<Boolean> isOpenRecorder = new MutableLiveData<>(false);

    /**
     * 用户搜索结果--分类垃圾列表
     */
    public MutableLiveData<List<GarbageData.DataBean>> sortedList = new MutableLiveData<>();

    /*
     * 垃圾分类识别实体类对象
     */
    public MutableLiveData<ImageClassifyBean.ResultBean> imageClassfyGarbage = new MutableLiveData<>();

    /**
     * 语音识别结果的垃圾名
     */
    public MutableLiveData<String> voiceGarbageName = new MutableLiveData<>("");

    public boolean searching = false;

    public long liveEventTime ;
    /**
     * 初始化语音听写监听器。
     */
    public InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                Logger.d("初始化失败，错误码：" + code+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            }
        }
    };
    /**
     * 初始化听写UI监听器 讯飞，处理数据
     */
    public RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {

        /**
         * 接收语音听写回调信息
         * @param recognizerResult 回调结果
         * @param b 是否翻译
         */
        @Override
        public void onResult(com.iflytek.cloud.RecognizerResult recognizerResult, boolean b) {
                Logger.d(recognizerResult.getResultString().toString());
                VoiceRecognizedData data = GsonUtils.GsonToBean(recognizerResult.getResultString() ,VoiceRecognizedData.class);
                if("。".equals(data.getWs().get(0).getCw().get(0).getW())) {
                    Logger.d("接收到结果为  。 ,则排除掉它");
                } else {
                    searching = true;
                    voiceGarbageName.setValue(getGarbageFromVoiceRecdata(data));
                }
        }
        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            if(error.getErrorCode() == 14002) {
                Logger.d( error.getPlainDescription(true)+"\n请确认是否已开通翻译功能" );
            } else {
                Logger.d(error.getPlainDescription(true));
            }
        }

    };

    /**
     * 调用垃圾分类查询接口，通过垃圾名 获取垃圾所属分类
     *
     * @param garbageName the garbage name
     */
    public void onSearch(String garbageName) {
          repository.getGarbageDataResult(garbageName)
                .subscribe(new ObserverManager<GarbageData>() {
                    @Override
                    public void onError(HttpThrowable httpThrowable) {
                        Logger.d("异常原因:  "+httpThrowable.message);
                    }

                    @Override
                    public void onNext(GarbageData garbageData) {
                        if (garbageData != null) {
                                //将成功查询到的 列表加入 垃圾分类列表中
//                                Logger.d("返回搜索结果   " + garbageData.getData().get(0).getName() );
                                sortedList.setValue(garbageData.getData());
                        }
                    }
                });
    }

    public void clearResultList() {
        sortedList.setValue(new ArrayList<>());
        Logger.d("搜索结果列表的大小  " + sortedList.getValue().size());
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
    public void imageClassifyFromBaiDu(String imageName)  {
        repository.getImageClassifyResult(imageName)
                        .subscribe(new ObserverManager<ImageClassifyBean>() {
                            @Override
                            public void onError(HttpThrowable httpThrowable) {
                                Logger.d(httpThrowable.message);
                            }

                            @Override
                            public void onNext(ImageClassifyBean imageClassifyBean) {
                                if(imageClassifyBean.getResult() != null){
//                                    Logger.d("获取到图像识别结果");
                                    imageClassfyGarbage.postValue(imageClassifyBean.getResult().get(0));
                                    searching = true;
                                }else {
                                    Logger.d("图像识别结果为空");
                                }
                            }
                        });


    }

    private String  getGarbageFromVoiceRecdata(VoiceRecognizedData recognizedData) {
            return recognizedData.getWs().get(0).getCw().get(0).getW();
    }

    public void initRecorderDialog(RecognizerDialog mIatDialog) {
        mIatDialog.setParameter(SpeechConstant.RESULT_TYPE, "json");
        //设置语音输入语言，zh_cn为简体中文
        mIatDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        //设置结果返回语言
        mIatDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 设置语音前端点:静音超时时间，单位ms，即用户多长时间不说话则当做超时处理
        //取值范围{1000～10000}
        mIatDialog.setParameter(SpeechConstant.VAD_BOS, "4500");
        //设置语音后端点:后端点静音检测时间，单位ms，即用户停止说话多长时间内即认为不再输入，
        //自动停止录音，范围{0~10000}
        mIatDialog.setParameter(SpeechConstant.VAD_EOS, "1500");
        //高阶动态修正-->会导致接收多条结果，适合实时显示说话内容，暂不启用此功能
//            mIatDialog.setParameter("dwa", "wpgs");
        //开始识别并设置监听器
        mIatDialog.setListener(mRecognizerDialogListener);

    }

    /*
     * 初始化页面轮播图数据
     */
    public List<BannerData> getBannerDataList() {
        List<BannerData> dataList = new ArrayList<>();
        BannerData a = new BannerData("https://raw.githubusercontent.com/DMingOu/Markdown-Picture-repository/master/img/20191003225959.png");
        BannerData b = new BannerData("https://raw.githubusercontent.com/DMingOu/Markdown-Picture-repository/master/img/20191003230434.png");
        BannerData c = new BannerData("https://raw.githubusercontent.com/DMingOu/Markdown-Picture-repository/master/img/20191003230859.png");
        BannerData d = new BannerData("https://raw.githubusercontent.com/DMingOu/Markdown-Picture-repository/master/img/20191003232017.png");
        dataList.add(a);
        dataList.add(b);
        dataList.add(c);
        dataList.add(d);
        return  dataList;
    }



}
