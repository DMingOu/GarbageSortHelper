package com.example.odm.garbagesorthelper.model.source.local;

import android.content.Context;
import android.os.Environment;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.odm.garbagesorthelper.model.entity.GarbageSearchHistory;
import com.orhanobut.logger.Logger;

import java.io.File;

/**
 * @description: 垃圾搜索历史数据库持有者
 * @author: ODM
 * @date: 2019/11/10
 */

@Database(entities = {GarbageSearchHistory.class}, version = 1, exportSchema = false)
public abstract class GarbageHistoryDatabase extends RoomDatabase {

    private static final String DB_NAME = "/storage/emulated/0/GarbageSortHelper/DataBase/garbage_history.db";
//     private static final String DB_NAME = "garbage_history.db";
    /**
     * 单例模式
     * volatile 确保线程安全
     * 线程安全意味着改对象会被许多线程使用
     * 可以被看作是一种 “程度较轻的 synchronized”
     */
    private static volatile GarbageHistoryDatabase INSTANCE;

    /**
     * 获得 DataBase 对象
     * @return
     */
    public abstract GarbageHistoryDao garbageHistoryDao();

    public static GarbageHistoryDatabase getInstance(Context context) {
        // 若为空则进行实例化
        // 否则直接返回
        if (INSTANCE == null) {
            synchronized (GarbageHistoryDatabase.class) {
                if (INSTANCE == null){
                    //Warning：必须先创建好目录，才能让数据库db文件创建在指定目录，不可以将目录直接传入Room的database构造器
                    File file = new File(Environment.getExternalStorageDirectory() + "/GarbageSortHelper/DataBase/");
                    file.mkdirs();
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GarbageHistoryDatabase.class, DB_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

