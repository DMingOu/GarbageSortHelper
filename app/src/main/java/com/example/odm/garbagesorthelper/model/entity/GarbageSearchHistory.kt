package com.example.odm.garbagesorthelper.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * @description: 垃圾搜索历史
 * @author: ODM
 * @date: 2019/11/10
 */
//表名为 garbage
@Entity(tableName = "garbage")
data class GarbageSearchHistory (@ColumnInfo(name = "garbagename") var garbageName: String,
                                 @ColumnInfo(name = "garbagetype") var garbageType: Int,
                                 @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "garbageid") var Id: Int){
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "garbageid")
//    var id = 0
//        private set
//    @ColumnInfo(name = "garbagename")
//    var garbageName: String
//        private set
//    @ColumnInfo(name = "garbagetype")
//    var garbageType: Int
//        private set

//    /**
//     * 构造方法，编译时会被传入Room中，这里的处理应该是add新数据
//     * @param garbageName
//     * @param garbageType
//     */
//    public constructor(garbageName: String, garbageType: Int, Id: Int) {
//        this.garbageName = garbageName
//        this.garbageType = garbageType
//        id = Id
//    }

//    /**
//     * 构造方法，设置了 @Ignore ，方法不会被自动传入Room中，适合临时创建
//     * @param garbageName
//     * @param garbageType
//     */
//    @Ignore
//    public constructor(garbageName: String, garbageType: Int) {
//        this.garbageName = garbageName
//        this.garbageType = garbageType
//    }

}