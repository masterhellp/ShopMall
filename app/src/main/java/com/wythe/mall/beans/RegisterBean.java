package com.wythe.mall.beans;

import java.util.List;

public class RegisterBean {

    /**
     * code : 200
     * results : {"ret":100,"msg":"成功","list":[]}
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
         * list : []
         */

        private int ret;
        private String msg;
        private List<?> list;

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

        public List<?> getList() {
            return list;
        }

        public void setList(List<?> list) {
            this.list = list;
        }
    }
}
