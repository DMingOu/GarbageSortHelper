package com.example.odm.garbagesorthelper.model.source.local

import android.content.Context
import android.os.Environment
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.odm.garbagesorthelper.model.entity.GarbageSearchHistory
import java.io.File

/**
 * @description: 垃圾搜索历史数据库持有者
 * @author: ODM
 * @date: 2019/11/10
 */
@Database(entities = [GarbageSearchHistory::class], version = 1, exportSchema = false)
abstract class GarbageHistoryDatabase : RoomDatabase() {

    init {
        val file = File(Environment.getExternalStorageDirectory().toString() + "/GarbageSortHelper/DataBase/")
        file.mkdirs()
    }
    /**
     * 获得 DataBase 对象
     * @return
     */
    abstract fun garbageHistoryDao(): GarbageHistoryDao?

    companion object {
        private const val DB_NAME = "/storage/emulated/0/GarbageSortHelper/DataBase/garbage_history.db"


//        val MIGRATION_1_2  : Migration =  object : Migration(1,2){
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("");
//             }
//            }

        //     private static final String DB_NAME = "garbage_history.db";
        /**
         * 单例模式
         * volatile 确保线程安全
         * 线程安全意味着改对象会被许多线程使用
         * 可以被看作是一种 “程度较轻的 synchronized”
         */
//        @Volatile
//        private var INSTANCE: GarbageHistoryDatabase? = null
//
//        @JvmStatic
//        fun getInstance(context: Context): GarbageHistoryDatabase? { // 若为空则进行实例化
//            // 否则直接返回
//            if (INSTANCE == null) {
//                synchronized(GarbageHistoryDatabase::class.java) {
//                    if (INSTANCE == null) {
//                        val file = File(Environment.getExternalStorageDirectory().toString() + "/GarbageSortHelper/DataBase/")
//                        file.mkdirs()
//                        INSTANCE = Room.databaseBuilder(context.applicationContext,
//                                GarbageHistoryDatabase::class.java, DB_NAME)
//                                .build()
//                    }
//                }
//            }
//            return INSTANCE
//        }

        @Volatile private var INSTANCE: GarbageHistoryDatabase? = null
        fun getInstance(context: Context) =
                INSTANCE ?: synchronized(this) {
                    //Warning：必须先创建好目录，才能让数据库db文件创建在指定目录，不可以将目录直接传入Room的database构造器
                    INSTANCE ?:  Room.databaseBuilder(context.applicationContext, GarbageHistoryDatabase::class.java, DB_NAME)
//                                        .addMigrations(MIGRATION_1_2)
                                        .build()
                                        .apply { INSTANCE = this }
                }

    }

}