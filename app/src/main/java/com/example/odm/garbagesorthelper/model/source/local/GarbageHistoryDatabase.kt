package com.example.odm.garbagesorthelper.model.source.local

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.odm.garbagesorthelper.model.entity.GarbageSearchHistory
import com.orhanobut.logger.Logger
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
        if(!file.exists()) {
            file.mkdirs()
        }
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