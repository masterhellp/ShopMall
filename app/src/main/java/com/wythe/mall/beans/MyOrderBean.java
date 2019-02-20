package com.wythe.mall.beans;

import java.util.List;

public class MyOrderBean {

    /**
     * code : 200
     * results : {"ret":100,"msg":"成功","totalCount":2,"list":[{"tradeNumber":"null","customerKey":"null","finishTime":"null","address":"{address","addressKey":"null","payTime":"null","shopLogo":"null","shopName":"null","orderMainKey":"null","sendTime":"null","goodsCount":"null","phone":"1","shopKey":"null","orderName":"null"},{"tradeNumber":"null","customerKey":"null","finishTime":"null","address":"11","addressKey":"null","payTime":"null","shopLogo":"null","shopName":"null","orderMainKey":"null","sendTime":"null","goodsCount":"null","phone":"11","shopKey":"null","orderName":"null"}]}
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
         * totalCount : 2
         * list : [{"tradeNumber":"null","customerKey":"null","finishTime":"null","address":"{address","addressKey":"null","payTime":"null","shopLogo":"null","shopName":"null","orderMainKey":"null","sendTime":"null","goodsCount":"null","phone":"1","shopKey":"null","orderName":"null"},{"tradeNumber":"null","customerKey":"null","finishTime":"null","address":"11","addressKey":"null","payTime":"null","shopLogo":"null","shopName":"null","orderMainKey":"null","sendTime":"null","goodsCount":"null","phone":"11","shopKey":"null","orderName":"null"}]
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
             * tradeNumber : null
             * customerKey : null
             * finishTime : null
             * address : {address
             * addressKey : null
             * payTime : null
             * shopLogo : null
             * shopName : null
             * orderMainKey : null
             * sendTime : null
             * goodsCount : null
             * phone : 1
             * shopKey : null
             * orderName : null
             */

            private String tradeNumber;
            private String customerKey;
            private String finishTime;
            private String address;
            private String addressKey;
            private String payTime;
            private String shopLogo;
            private String shopName;
            private String orderMainKey;
            private String sendTime;
            private String goodsCount;
            private String phone;
            private String shopKey;
            private String orderName;

            public String getTradeNumber() {
                return tradeNumber;
            }

            public void setTradeNumber(String tradeNumber) {
                this.tradeNumber = tradeNumber;
            }

            public String getCustomerKey() {
                return customerKey;
            }

            public void setCustomerKey(String customerKey) {
                this.customerKey = customerKey;
            }

            public String getFinishTime() {
                return finishTime;
            }

            public void setFinishTime(String finishTime) {
                this.finishTime = finishTime;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getAddressKey() {
                return addressKey;
            }

            public void setAddressKey(String addressKey) {
                this.addressKey = addressKey;
            }

            public String getPayTime() {
                return payTime;
            }

            public void setPayTime(String payTime) {
                this.payTime = payTime;
            }

            public String getShopLogo() {
                return shopLogo;
            }

            public void setShopLogo(String shopLogo) {
                this.shopLogo = shopLogo;
            }

            public String getShopName() {
                return shopName;
            }

            public void setShopName(String shopName) {
                this.shopName = shopName;
            }

            public String getOrderMainKey() {
                return orderMainKey;
            }

            public void setOrderMainKey(String orderMainKey) {
                this.orderMainKey = orderMainKey;
            }

            public String getSendTime() {
                return sendTime;
            }

            public void setSendTime(String sendTime) {
                this.sendTime = sendTime;
            }

            public String getGoodsCount() {
                return goodsCount;
            }

            public void setGoodsCount(String goodsCount) {
                this.goodsCount = goodsCount;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getShopKey() {
                return shopKey;
            }

            public void setShopKey(String shopKey) {
                this.shopKey = shopKey;
            }

            public String getOrderName() {
                return orderName;
            }

            public void setOrderName(String orderName) {
                this.orderName = orderName;
            }
        }
    }
}
