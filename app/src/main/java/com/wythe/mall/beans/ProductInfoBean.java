package com.wythe.mall.beans;

import java.util.List;

public class ProductInfoBean {

    /**
     * code : 200
     * results : {"ret":100,"msg":"成功","totalCount":3,"list":[{"imageKey":"508bb9547f9640aab60c5ab49c72f019","url":"http://4k134lmwer.51tcp.cc:63153/kaptcha/1.jpg"},{"imageKey":"ae8a653e3c884b3d9d6fffb7a91b3670","url":"http://4k134lmwer.51tcp.cc:63153/kaptcha/2.jpg"},{"imageKey":"1212212121212121212121212121","url":"http://4k134lmwer.51tcp.cc:63153/kaptcha/3.jpg"}]}
     */

    private String code;
    private ResultsBean results;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ResultsBean getResults() {
        return results;
    }

    public void setResults(ResultsBean results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * ret : 100
         * msg : 成功
         * totalCount : 3
         * list : [{"imageKey":"508bb9547f9640aab60c5ab49c72f019","url":"http://4k134lmwer.51tcp.cc:63153/kaptcha/1.jpg"},{"imageKey":"ae8a653e3c884b3d9d6fffb7a91b3670","url":"http://4k134lmwer.51tcp.cc:63153/kaptcha/2.jpg"},{"imageKey":"1212212121212121212121212121","url":"http://4k134lmwer.51tcp.cc:63153/kaptcha/3.jpg"}]
         */

        private int ret;
        private String msg;
        private int totalCount;
        private List<ListBean> list;

        public int getRet() {
            return ret;
        }

        public void setRet(int ret) {
            this.ret = ret;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * imageKey : 508bb9547f9640aab60c5ab49c72f019
             * url : http://4k134lmwer.51tcp.cc:63153/kaptcha/1.jpg
             */

            private String imageKey;
            private String url;

            public String getImageKey() {
                return imageKey;
            }

            public void setImageKey(String imageKey) {
                this.imageKey = imageKey;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
