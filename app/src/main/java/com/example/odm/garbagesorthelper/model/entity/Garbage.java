package com.example.odm.garbagesorthelper.model.entity;

import java.util.List;

/**
 * description: 垃圾实体类
 * author: ODM
 * date: 2019/9/20
 */
public class Garbage {

    /**
     * data : [{"gname":"小龙虾,奶茶","gtype":"湿垃圾"},{"gname":"[小龙虾,奶茶]","gtype":"湿垃圾"},{"gname":"喝过的奶茶杯子","gtype":"干垃圾"},{"gname":"奶茶杯","gtype":"干垃圾"},{"gname":"奶茶盖","gtype":"干垃圾"},{"gname":"奶茶中的珍珠","gtype":"湿垃圾"},{"gname":"珍珠奶茶杯","gtype":"干垃圾"},{"gname":"奶茶纸杯","gtype":"干垃圾"},{"gname":"装有奶茶的不锈钢杯","gtype":"可回收"},{"gname":"奶茶里的珍珠","gtype":"湿垃圾"},{"gname":"奶茶","gtype":"湿垃圾"}]
     * msg : success
     * code : 200
     */

    private String msg;
    private int code;
    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * gname : 小龙虾,奶茶
         * gtype : 湿垃圾
         */

        public DataBean(String gname,String gtype) {
            this.gname = gname;
            this.gtype = gtype;
        }

        private String gname;
        private String gtype;

        public String getGname() {
            return gname;
        }

        public void setGname(String gname) {
            this.gname = gname;
        }

        public String getGtype() {
            return gtype;
        }

        public void setGtype(String gtype) {
            this.gtype = gtype;
        }
    }
}
