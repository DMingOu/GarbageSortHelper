package com.example.odm.garbagesorthelper.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Random;

/**
 * @description: 垃圾搜索历史
 * @author: ODM
 * @date: 2019/11/10
 */
//表名为 garbage
@Entity(tableName = "garbage")
public class GarbageSearchHistory {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "garbageid")
    private int Id;

    @ColumnInfo(name = "garbagename")
    private String garbageName;

    @ColumnInfo(name = "garbagetype")
    private int garbageType;


    /**
     * 构造方法，设置了 @Ignore ，方法不会被自动传入Room中，适合临时创建
     * @param garbageName
     * @param garbageType
     */
    public GarbageSearchHistory(String garbageName , int garbageType,int Id) {
        this.garbageName = garbageName;
        this.garbageType = garbageType;
        this.Id = Id;
    }


    /**
     * 构造方法，编译时会被传入Room中，这里的处理应该是add新数据
     * @param garbageName
     * @param garbageType
     */
    @Ignore
    public GarbageSearchHistory(String garbageName , int garbageType) {
        this.garbageName = garbageName;
        this.garbageType = garbageType;
    }

    public int getId() {
        return Id;
    }

    public String getGarbageName() {
        return garbageName;
    }

    public int getGarbageType() {
        return garbageType;
    }



}
