package com.example.odm.garbagesorthelper.model.source.http;

import com.example.odm.garbagesorthelper.core.net.ApiService;

public class HttpDataSourceImpl {

    private com.example.odm.garbagesorthelper.core.net.ApiService apiService;

    private volatile static HttpDataSourceImpl INSTANCE = null;

    public static HttpDataSourceImpl getInstance(com.example.odm.garbagesorthelper.core.net.ApiService apiService) {
        if (INSTANCE == null) {
            synchronized (HttpDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpDataSourceImpl(apiService);
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private HttpDataSourceImpl(ApiService apiService) {
        this.apiService = apiService;
    }


}