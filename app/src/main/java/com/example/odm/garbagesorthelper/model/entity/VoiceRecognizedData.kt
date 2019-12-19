package com.example.odm.garbagesorthelper.model.entity

/**
 * description: 语音识别实体类
 * author: ODM
 * date: 2019/10/3
 */
data class VoiceRecognizedData (val sn : Int,
                                val isLs : Boolean,
                                val bq : Int ,
                                val ed : Int ,
                                val pgs: String ,
                                val rq : List<Int> ,
                                val ws : List<WsBean>) {
    /**
     * sn : 6
     * ls : false
     * bg : 0
     * ed : 0
     * pgs : rpl
     * rg : [1,5]
     * ws : [{"bg":19,"cw":[{"sc":0,"w":"今天"}]},{"bg":79,"cw":[{"sc":0,"w":"我"}]},{"bg":103,"cw":[{"sc":0,"w":"想"}]},{"bg":123,"cw":[{"sc":0,"w":"去"}]},{"bg":159,"cw":[{"sc":0,"w":"吃饭"}]}]
     */
    class WsBean(val bg: Int ,val cw: List<CwBean>) {
        /**
         * bg : 19
         * cw : [{"sc":0,"w":"今天"}]
         */
        class CwBean(val sc : Double ,val w: String)
            /**
             * sc : 0.0
             * w : 今天
             */

    }
}