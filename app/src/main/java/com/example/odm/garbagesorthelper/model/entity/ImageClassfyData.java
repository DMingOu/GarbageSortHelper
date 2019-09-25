package com.example.odm.garbagesorthelper.model.entity;

import java.util.List;

/**
 * description: 图像识别实体类
 * author: ODM
 * date: 2019/9/25
 */
public class ImageClassfyData {


    /**
     * nameValuePairs : {"log_id":903460849137212409,"result_num":5,"result":{"values":[{"nameValuePairs":{"score":0.851578,"root":"商品-电脑办公","baike_info":{"nameValuePairs":{"baike_url":"http://baike.baidu.com/item/%E9%BC%A0%E6%A0%87/122323","image_url":"http://imgsrc.baidu.com/baike/pic/item/bd3eb13533fa828b1d3f4fb2f31f4134970a5a47.jpg","description":"鼠标，计算机的一种输入设备，也是计算机显示系统纵横坐标定位的指示器，因形似老鼠而得名(港台作滑鼠)。其标准称呼应该是\u201c鼠标器\u201d，英文名\u201cMouse\u201d，鼠标的使用是为了使计算机的操作更加简便快捷，来代替键盘那繁琐的指令。鼠标是1964年由加州大学伯克利分校博士道格拉斯·恩格尔巴特(Douglas Engelbart)发明的，当时道格拉斯·恩格尔巴特在斯坦福研究所(SRI)工作，该研究所是斯坦福大学赞助的一个机构，Douglas Engelbart很早就在考虑如何使电脑的操作更加简便，用什么手段来取代由键盘输入的繁琐指令，申请专利时的名字为显示系统X-Y位置指示器。"}},"keyword":"鼠标"}},{"nameValuePairs":{"score":0.533435,"root":"商品-穿戴","keyword":"海盗帽"}},{"nameValuePairs":{"score":0.356667,"root":"商品-工艺品","keyword":"工艺品"}},{"nameValuePairs":{"score":0.179183,"root":"商品-工艺品","keyword":"鸡蛋壳"}},{"nameValuePairs":{"score":0.002694,"root":"商品-体育用品","keyword":"足球"}}]}}
     */

    private NameValuePairsBeanXX nameValuePairs;

    public NameValuePairsBeanXX getNameValuePairs() {
        return nameValuePairs;
    }

    public void setNameValuePairs(NameValuePairsBeanXX nameValuePairs) {
        this.nameValuePairs = nameValuePairs;
    }

    public static class NameValuePairsBeanXX {
        /**
         * log_id : 903460849137212409
         * result_num : 5
         * result : {"values":[{"nameValuePairs":{"score":0.851578,"root":"商品-电脑办公","baike_info":{"nameValuePairs":{"baike_url":"http://baike.baidu.com/item/%E9%BC%A0%E6%A0%87/122323","image_url":"http://imgsrc.baidu.com/baike/pic/item/bd3eb13533fa828b1d3f4fb2f31f4134970a5a47.jpg","description":"鼠标，计算机的一种输入设备，也是计算机显示系统纵横坐标定位的指示器，因形似老鼠而得名(港台作滑鼠)。其标准称呼应该是\u201c鼠标器\u201d，英文名\u201cMouse\u201d，鼠标的使用是为了使计算机的操作更加简便快捷，来代替键盘那繁琐的指令。鼠标是1964年由加州大学伯克利分校博士道格拉斯·恩格尔巴特(Douglas Engelbart)发明的，当时道格拉斯·恩格尔巴特在斯坦福研究所(SRI)工作，该研究所是斯坦福大学赞助的一个机构，Douglas Engelbart很早就在考虑如何使电脑的操作更加简便，用什么手段来取代由键盘输入的繁琐指令，申请专利时的名字为显示系统X-Y位置指示器。"}},"keyword":"鼠标"}},{"nameValuePairs":{"score":0.533435,"root":"商品-穿戴","keyword":"海盗帽"}},{"nameValuePairs":{"score":0.356667,"root":"商品-工艺品","keyword":"工艺品"}},{"nameValuePairs":{"score":0.179183,"root":"商品-工艺品","keyword":"鸡蛋壳"}},{"nameValuePairs":{"score":0.002694,"root":"商品-体育用品","keyword":"足球"}}]}
         */

        private long log_id;
        private int result_num;
        private ResultBean result;

        public long getLog_id() {
            return log_id;
        }

        public void setLog_id(long log_id) {
            this.log_id = log_id;
        }

        public int getResult_num() {
            return result_num;
        }

        public void setResult_num(int result_num) {
            this.result_num = result_num;
        }

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public static class ResultBean {
            private List<ValuesBean> values;

            public List<ValuesBean> getValues() {
                return values;
            }

            public void setValues(List<ValuesBean> values) {
                this.values = values;
            }

            public static class ValuesBean {
                /**
                 * nameValuePairs : {"score":0.851578,"root":"商品-电脑办公","baike_info":{"nameValuePairs":{"baike_url":"http://baike.baidu.com/item/%E9%BC%A0%E6%A0%87/122323","image_url":"http://imgsrc.baidu.com/baike/pic/item/bd3eb13533fa828b1d3f4fb2f31f4134970a5a47.jpg","description":"鼠标，计算机的一种输入设备，也是计算机显示系统纵横坐标定位的指示器，因形似老鼠而得名(港台作滑鼠)。其标准称呼应该是\u201c鼠标器\u201d，英文名\u201cMouse\u201d，鼠标的使用是为了使计算机的操作更加简便快捷，来代替键盘那繁琐的指令。鼠标是1964年由加州大学伯克利分校博士道格拉斯·恩格尔巴特(Douglas Engelbart)发明的，当时道格拉斯·恩格尔巴特在斯坦福研究所(SRI)工作，该研究所是斯坦福大学赞助的一个机构，Douglas Engelbart很早就在考虑如何使电脑的操作更加简便，用什么手段来取代由键盘输入的繁琐指令，申请专利时的名字为显示系统X-Y位置指示器。"}},"keyword":"鼠标"}
                 */

                private NameValuePairsBeanX nameValuePairs;

                public NameValuePairsBeanX getNameValuePairs() {
                    return nameValuePairs;
                }

                public void setNameValuePairs(NameValuePairsBeanX nameValuePairs) {
                    this.nameValuePairs = nameValuePairs;
                }

                public static class NameValuePairsBeanX {
                    /**
                     * score : 0.851578
                     * root : 商品-电脑办公
                     * baike_info : {"nameValuePairs":{"baike_url":"http://baike.baidu.com/item/%E9%BC%A0%E6%A0%87/122323","image_url":"http://imgsrc.baidu.com/baike/pic/item/bd3eb13533fa828b1d3f4fb2f31f4134970a5a47.jpg","description":"鼠标，计算机的一种输入设备，也是计算机显示系统纵横坐标定位的指示器，因形似老鼠而得名(港台作滑鼠)。其标准称呼应该是\u201c鼠标器\u201d，英文名\u201cMouse\u201d，鼠标的使用是为了使计算机的操作更加简便快捷，来代替键盘那繁琐的指令。鼠标是1964年由加州大学伯克利分校博士道格拉斯·恩格尔巴特(Douglas Engelbart)发明的，当时道格拉斯·恩格尔巴特在斯坦福研究所(SRI)工作，该研究所是斯坦福大学赞助的一个机构，Douglas Engelbart很早就在考虑如何使电脑的操作更加简便，用什么手段来取代由键盘输入的繁琐指令，申请专利时的名字为显示系统X-Y位置指示器。"}}
                     * keyword : 鼠标
                     */

                    private double score;
                    private String root;
                    private BaikeInfoBean baike_info;
                    private String keyword;

                    public double getScore() {
                        return score;
                    }

                    public void setScore(double score) {
                        this.score = score;
                    }

                    public String getRoot() {
                        return root;
                    }

                    public void setRoot(String root) {
                        this.root = root;
                    }

                    public BaikeInfoBean getBaike_info() {
                        return baike_info;
                    }

                    public void setBaike_info(BaikeInfoBean baike_info) {
                        this.baike_info = baike_info;
                    }

                    public String getKeyword() {
                        return keyword;
                    }

                    public void setKeyword(String keyword) {
                        this.keyword = keyword;
                    }

                    public static class BaikeInfoBean {
                        /**
                         * nameValuePairs : {"baike_url":"http://baike.baidu.com/item/%E9%BC%A0%E6%A0%87/122323","image_url":"http://imgsrc.baidu.com/baike/pic/item/bd3eb13533fa828b1d3f4fb2f31f4134970a5a47.jpg","description":"鼠标，计算机的一种输入设备，也是计算机显示系统纵横坐标定位的指示器，因形似老鼠而得名(港台作滑鼠)。其标准称呼应该是\u201c鼠标器\u201d，英文名\u201cMouse\u201d，鼠标的使用是为了使计算机的操作更加简便快捷，来代替键盘那繁琐的指令。鼠标是1964年由加州大学伯克利分校博士道格拉斯·恩格尔巴特(Douglas Engelbart)发明的，当时道格拉斯·恩格尔巴特在斯坦福研究所(SRI)工作，该研究所是斯坦福大学赞助的一个机构，Douglas Engelbart很早就在考虑如何使电脑的操作更加简便，用什么手段来取代由键盘输入的繁琐指令，申请专利时的名字为显示系统X-Y位置指示器。"}
                         */

                        private NameValuePairsBean nameValuePairs;

                        public NameValuePairsBean getNameValuePairs() {
                            return nameValuePairs;
                        }

                        public void setNameValuePairs(NameValuePairsBean nameValuePairs) {
                            this.nameValuePairs = nameValuePairs;
                        }

                        public static class NameValuePairsBean {
                            /**
                             * baike_url : http://baike.baidu.com/item/%E9%BC%A0%E6%A0%87/122323
                             * image_url : http://imgsrc.baidu.com/baike/pic/item/bd3eb13533fa828b1d3f4fb2f31f4134970a5a47.jpg
                             * description : 鼠标，计算机的一种输入设备，也是计算机显示系统纵横坐标定位的指示器，因形似老鼠而得名(港台作滑鼠)。其标准称呼应该是“鼠标器”，英文名“Mouse”，鼠标的使用是为了使计算机的操作更加简便快捷，来代替键盘那繁琐的指令。鼠标是1964年由加州大学伯克利分校博士道格拉斯·恩格尔巴特(Douglas Engelbart)发明的，当时道格拉斯·恩格尔巴特在斯坦福研究所(SRI)工作，该研究所是斯坦福大学赞助的一个机构，Douglas Engelbart很早就在考虑如何使电脑的操作更加简便，用什么手段来取代由键盘输入的繁琐指令，申请专利时的名字为显示系统X-Y位置指示器。
                             */

                            private String baike_url;
                            private String image_url;
                            private String description;

                            public String getBaike_url() {
                                return baike_url;
                            }

                            public void setBaike_url(String baike_url) {
                                this.baike_url = baike_url;
                            }

                            public String getImage_url() {
                                return image_url;
                            }

                            public void setImage_url(String image_url) {
                                this.image_url = image_url;
                            }

                            public String getDescription() {
                                return description;
                            }

                            public void setDescription(String description) {
                                this.description = description;
                            }
                        }
                    }
                }
            }
        }
    }
}
