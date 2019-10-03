package com.example.odm.garbagesorthelper.model.entity;

import java.util.List;

/**
 * description: 语音识别实体类
 * author: ODM
 * date: 2019/10/3
 */
public class VoiceRecognizedData {


    /**
     * sn : 6
     * ls : false
     * bg : 0
     * ed : 0
     * pgs : rpl
     * rg : [1,5]
     * ws : [{"bg":19,"cw":[{"sc":0,"w":"今天"}]},{"bg":79,"cw":[{"sc":0,"w":"我"}]},{"bg":103,"cw":[{"sc":0,"w":"想"}]},{"bg":123,"cw":[{"sc":0,"w":"去"}]},{"bg":159,"cw":[{"sc":0,"w":"吃饭"}]}]
     */

    private int sn;
    private boolean ls;
    private int bg;
    private int ed;
    private String pgs;
    private List<Integer> rg;
    private List<WsBean> ws;

    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

    public boolean isLs() {
        return ls;
    }

    public void setLs(boolean ls) {
        this.ls = ls;
    }

    public int getBg() {
        return bg;
    }

    public void setBg(int bg) {
        this.bg = bg;
    }

    public int getEd() {
        return ed;
    }

    public void setEd(int ed) {
        this.ed = ed;
    }

    public String getPgs() {
        return pgs;
    }

    public void setPgs(String pgs) {
        this.pgs = pgs;
    }

    public List<Integer> getRg() {
        return rg;
    }

    public void setRg(List<Integer> rg) {
        this.rg = rg;
    }

    public List<WsBean> getWs() {
        return ws;
    }

    public void setWs(List<WsBean> ws) {
        this.ws = ws;
    }

    public static class WsBean {
        /**
         * bg : 19
         * cw : [{"sc":0,"w":"今天"}]
         */

        private int bg;
        private List<CwBean> cw;

        public int getBg() {
            return bg;
        }

        public void setBg(int bg) {
            this.bg = bg;
        }

        public List<CwBean> getCw() {
            return cw;
        }

        public void setCw(List<CwBean> cw) {
            this.cw = cw;
        }

        public static class CwBean {
            /**
             * sc : 0.0
             * w : 今天
             */

            private double sc;
            private String w;

            public double getSc() {
                return sc;
            }

            public void setSc(double sc) {
                this.sc = sc;
            }

            public String getW() {
                return w;
            }

            public void setW(String w) {
                this.w = w;
            }
        }
    }
}
