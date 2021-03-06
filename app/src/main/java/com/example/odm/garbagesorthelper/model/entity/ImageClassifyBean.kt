package com.example.odm.garbagesorthelper.model.entity

/**
 * description: 图片识别实体类（精简版）
 * author: ODM
 * date: 2019/10/2
 */
data class ImageClassifyBean(var log_id: Long,
                            var result_num : Int,
                            var result: List<ResultBean> ) {
    /**
     * log_id : 327863200205075661
     * result_num : 5
     * result : [{"score":0.967622,"root":"公众人物","baike_info":{"baike_url":"http://baike.baidu.com/item/%E6%96%B0%E5%9E%A3%E7%BB%93%E8%A1%A3/8035884","image_url":"http://imgsrc.baidu.com/baike/pic/item/91ef76c6a7efce1b27893518a451f3deb58f6546.jpg","description":"新垣结衣(Aragaki Yui)，1988年6月11日出生于冲绳县那霸市。日本女演员、歌手、模特。毕业于日出高中。2001年，参加《nicola》模特比赛并获得最优秀奖。2005年，因出演现代剧《涩谷15》而作为演员出道。2006年，参演校园剧《我的老大，我的英雄》；同年，她还出版了个人首本写真集《水漾青春》。2007年，她从日出高校毕业后开始专注于演艺发展，并发表个人首张音乐专辑《天空》；同年，新垣结衣还主演了爱情片《恋空》，而她也凭借该片获得了多个电影新人奖项。2010年，主演爱情片《花水木》。2011年，主演都市剧《全开女孩》。2012年，相继参演现代剧《Legal High》、剧情片《剧场版新参者：麒麟之翼》。2013年，主演都市剧《飞翔情报室》。2014年，她主演了剧情片《黎明的沙耶》。2016年，主演爱情喜剧《逃避虽可耻但有用》，并凭借该剧获得了多个电视剧女主角奖项。2017年，主演爱情片《恋爱回旋》，凭借该片获得第60届蓝丝带奖最佳女主角；同年11月，她还凭借医疗剧《Code Blue 3》获得第94届日剧学院赏最佳女配角。"},"keyword":"新垣结衣"},{"score":0.716067,"root":"人物-人物特写","keyword":"头发"},{"score":0.421281,"root":"商品-穿戴","keyword":"围巾"},{"score":0.22347,"root":"商品-五金","keyword":"拉链"},{"score":0.028031,"root":"商品-穿戴","keyword":"脖套"}]
     */

    data class ResultBean (val score : Double ,
                          val root: String ,
                          val baike_info: BaikeInfoBean ,
                          val keyword: String) {
        /**
         * score : 0.967622
         * root : 公众人物
         * baike_info : {"baike_url":"http://baike.baidu.com/item/%E6%96%B0%E5%9E%A3%E7%BB%93%E8%A1%A3/8035884","image_url":"http://imgsrc.baidu.com/baike/pic/item/91ef76c6a7efce1b27893518a451f3deb58f6546.jpg","description":"新垣结衣(Aragaki Yui)，1988年6月11日出生于冲绳县那霸市。日本女演员、歌手、模特。毕业于日出高中。2001年，参加《nicola》模特比赛并获得最优秀奖。2005年，因出演现代剧《涩谷15》而作为演员出道。2006年，参演校园剧《我的老大，我的英雄》；同年，她还出版了个人首本写真集《水漾青春》。2007年，她从日出高校毕业后开始专注于演艺发展，并发表个人首张音乐专辑《天空》；同年，新垣结衣还主演了爱情片《恋空》，而她也凭借该片获得了多个电影新人奖项。2010年，主演爱情片《花水木》。2011年，主演都市剧《全开女孩》。2012年，相继参演现代剧《Legal High》、剧情片《剧场版新参者：麒麟之翼》。2013年，主演都市剧《飞翔情报室》。2014年，她主演了剧情片《黎明的沙耶》。2016年，主演爱情喜剧《逃避虽可耻但有用》，并凭借该剧获得了多个电视剧女主角奖项。2017年，主演爱情片《恋爱回旋》，凭借该片获得第60届蓝丝带奖最佳女主角；同年11月，她还凭借医疗剧《Code Blue 3》获得第94届日剧学院赏最佳女配角。"}
         * keyword : 新垣结衣
         */
        data class BaikeInfoBean(var baike_url: String ,
                                 var image_url: String ,
                                 var description: String){
            /**
             * baike_url : http://baike.baidu.com/item/%E6%96%B0%E5%9E%A3%E7%BB%93%E8%A1%A3/8035884
             * image_url : http://imgsrc.baidu.com/baike/pic/item/91ef76c6a7efce1b27893518a451f3deb58f6546.jpg
             * description : 新垣结衣(Aragaki Yui)，1988年6月11日出生于冲绳县那霸市。日本女演员、歌手、模特。毕业于日出高中。2001年，参加《nicola》模特比赛并获得最优秀奖。2005年，因出演现代剧《涩谷15》而作为演员出道。2006年，参演校园剧《我的老大，我的英雄》；同年，她还出版了个人首本写真集《水漾青春》。2007年，她从日出高校毕业后开始专注于演艺发展，并发表个人首张音乐专辑《天空》；同年，新垣结衣还主演了爱情片《恋空》，而她也凭借该片获得了多个电影新人奖项。2010年，主演爱情片《花水木》。2011年，主演都市剧《全开女孩》。2012年，相继参演现代剧《Legal High》、剧情片《剧场版新参者：麒麟之翼》。2013年，主演都市剧《飞翔情报室》。2014年，她主演了剧情片《黎明的沙耶》。2016年，主演爱情喜剧《逃避虽可耻但有用》，并凭借该剧获得了多个电视剧女主角奖项。2017年，主演爱情片《恋爱回旋》，凭借该片获得第60届蓝丝带奖最佳女主角；同年11月，她还凭借医疗剧《Code Blue 3》获得第94届日剧学院赏最佳女配角。
             */

        }
    }
}