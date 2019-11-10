package com.example.odm.garbagesorthelper.model.entity;

import java.util.List;

/**
 * description: 垃圾分类数据实体类
 * author: ODM
 * date: 2019/9/21
 */
public class GarbageData {


    /**
     * code : 1
     * msg : 查询到1条数据
     * data : [{"id":4216,"name":"鸡心","type":2,"category":"湿垃圾","remark":"","num":8}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 4216
         * name : 鸡心
         * type : 2
         * category : 湿垃圾
         * remark :
         * num : 8
         */

        private int id;
        private String name;
        private int type;
        private String category;
        private String remark;
        private int num;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }
}
