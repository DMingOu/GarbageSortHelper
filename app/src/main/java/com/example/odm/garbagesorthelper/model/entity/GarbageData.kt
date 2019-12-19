package com.example.odm.garbagesorthelper.model.entity

/**
 * description: 垃圾分类数据实体类
 * author: ODM
 * date: 2019/9/21
 */
data class GarbageData (val code : Int ,
                        val msg : String ,
                        val data : List<DataBean>) {
    /**
     * code : 1
     * msg : 查询到1条数据
     * data : [{"id":4216,"name":"鸡心","type":2,"category":"湿垃圾","remark":"","num":8}]
     */


    class DataBean (val id: Int ,
                    val name: String ,
                    val type : Int ,
                    val category :String ,
                    val remark : String ,
                    val num : Int ) {
        /**
         * id : 4216
         * name : 鸡心
         * type : 2
         * category : 湿垃圾
         * remark :
         * num : 8
         */
    }
}