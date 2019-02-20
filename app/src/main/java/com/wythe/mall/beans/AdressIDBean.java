package com.wythe.mall.beans;

import java.util.List;

public class AdressIDBean {


    /**
     * code : 200
     * results : {"ret":100,"msg":"成功","totalCount":3239,"list":[{"code":"110000","level":"true","name":"北京市","parentid":"0"},{"code":"110100","level":"true","name":"区","parentid":"110000"},{"code":"110101","level":"true","name":"东城区","parentid":"110100"},{"code":"110102","level":"true","name":"西城区","parentid":"110100"},{"code":"110105","level":"true","name":"朝阳区","parentid":"110100"},{"code":"110106","level":"true","name":"丰台区","parentid":"110100"},{"code":"110107","level":"true","name":"石景山区","parentid":"110100"},{"code":"110108","level":"true","name":"海淀区","parentid":"110100"},{"code":"110109","level":"true","name":"门头沟区","parentid":"110100"},{"code":"110111","level":"true","name":"房山区","parentid":"110100"},{"code":"110112","level":"true","name":"通州区","parentid":"110100"},{"code":"110113","level":"true","name":"顺义区","parentid":"110100"},{"code":"110114","level":"true","name":"昌平区","parentid":"110100"},{"code":"110115","level":"true","name":"大兴区","parentid":"110100"},{"code":"110116","level":"true","name":"怀柔区","parentid":"110100"},{"code":"110117","level":"true","name":"平谷区","parentid":"110100"},{"code":"110200","level":"true","name":"县","parentid":"110000"},{"code":"110228","level":"true","name":"密云县","parentid":"110200"},{"code":"110229","level":"true","name":"延庆县","parentid":"110200"},{"code":"120000","level":"true","name":"天津市","parentid":"0"},{"code":"120100","level":"true","name":"区","parentid":"120000"},{"code":"120101","level":"true","name":"和平区","parentid":"120100"},{"code":"120102","level":"true","name":"河东区","parentid":"120100"},{"code":"120103","level":"true","name":"河西区","parentid":"120100"},{"code":"120104","level":"true","name":"南开区","parentid":"120100"},{"code":"120105","level":"true","name":"河北区","parentid":"120100"},{"code":"120106","level":"true","name":"红桥区","parentid":"120100"},{"code":"120110","level":"true","name":"东丽区","parentid":"120100"},{"code":"120111","level":"true","name":"西青区","parentid":"120100"},{"code":"120112","level":"true","name":"津南区","parentid":"120100"},{"code":"120113","level":"true","name":"北辰区","parentid":"120100"},{"code":"120114","level":"true","name":"武清区","parentid":"120100"},{"code":"120115","level":"true","name":"宝坻区","parentid":"120100"},{"code":"120116","level":"true","name":"滨海新区","parentid":"120100"},{"code":"120200","level":"true","name":"县","parentid":"120000"},{"code":"120221","level":"true","name":"宁河县","parentid":"120200"},{"code":"120223","level":"true","name":"静海县","parentid":"120200"},{"code":"120225","level":"true","name":"蓟县","parentid":"120200"},{"code":"130000","level":"true","name":"河北省","parentid":"0"}]}
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
         * totalCount : 3239
         * list : [{"code":"110000","level":"true","name":"北京市","parentid":"0"},{"code":"110100","level":"true","name":"区","parentid":"110000"},{"code":"110101","level":"true","name":"东城区","parentid":"110100"},{"code":"110102","level":"true","name":"西城区","parentid":"110100"},{"code":"110105","level":"true","name":"朝阳区","parentid":"110100"},{"code":"110106","level":"true","name":"丰台区","parentid":"110100"},{"code":"110107","level":"true","name":"石景山区","parentid":"110100"},{"code":"110108","level":"true","name":"海淀区","parentid":"110100"},{"code":"110109","level":"true","name":"门头沟区","parentid":"110100"},{"code":"110111","level":"true","name":"房山区","parentid":"110100"},{"code":"110112","level":"true","name":"通州区","parentid":"110100"},{"code":"110113","level":"true","name":"顺义区","parentid":"110100"},{"code":"110114","level":"true","name":"昌平区","parentid":"110100"},{"code":"110115","level":"true","name":"大兴区","parentid":"110100"},{"code":"110116","level":"true","name":"怀柔区","parentid":"110100"},{"code":"110117","level":"true","name":"平谷区","parentid":"110100"},{"code":"110200","level":"true","name":"县","parentid":"110000"},{"code":"110228","level":"true","name":"密云县","parentid":"110200"},{"code":"110229","level":"true","name":"延庆县","parentid":"110200"},{"code":"120000","level":"true","name":"天津市","parentid":"0"},{"code":"120100","level":"true","name":"区","parentid":"120000"},{"code":"120101","level":"true","name":"和平区","parentid":"120100"},{"code":"120102","level":"true","name":"河东区","parentid":"120100"},{"code":"120103","level":"true","name":"河西区","parentid":"120100"},{"code":"120104","level":"true","name":"南开区","parentid":"120100"},{"code":"120105","level":"true","name":"河北区","parentid":"120100"},{"code":"120106","level":"true","name":"红桥区","parentid":"120100"},{"code":"120110","level":"true","name":"东丽区","parentid":"120100"},{"code":"120111","level":"true","name":"西青区","parentid":"120100"},{"code":"120112","level":"true","name":"津南区","parentid":"120100"},{"code":"120113","level":"true","name":"北辰区","parentid":"120100"},{"code":"120114","level":"true","name":"武清区","parentid":"120100"},{"code":"120115","level":"true","name":"宝坻区","parentid":"120100"},{"code":"120116","level":"true","name":"滨海新区","parentid":"120100"},{"code":"120200","level":"true","name":"县","parentid":"120000"},{"code":"120221","level":"true","name":"宁河县","parentid":"120200"},{"code":"120223","level":"true","name":"静海县","parentid":"120200"},{"code":"120225","level":"true","name":"蓟县","parentid":"120200"},{"code":"130000","level":"true","name":"河北省","parentid":"0"}]
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
             * code : 110000
             * level : true
             * name : 北京市
             * parentid : 0
             */

            private String code;
            private String level;
            private String name;
            private String parentid;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getParentid() {
                return parentid;
            }

            public void setParentid(String parentid) {
                this.parentid = parentid;
            }
        }
    }
}
