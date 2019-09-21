package com.example.odm.garbagesorthelper.model.source.local;

public class LocalDataSourceImpl  implements LocalDataSource{

    private LocalDataSourceImpl() {
        //或许可以构建数据库，例如Room
    }

    private static class SingletonInstance {
        private static final LocalDataSourceImpl INSTANCE = new LocalDataSourceImpl();
    }

    public static LocalDataSourceImpl getInstance() {
        return SingletonInstance.INSTANCE;
    }

    @Override
    public void saveSomething(String something) {
        //save
    }
}