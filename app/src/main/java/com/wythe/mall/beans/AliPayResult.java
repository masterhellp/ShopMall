package com.wythe.mall.beans;

public class AliPayResult {


    /**
     * alipay_trade_app_pay_response : {"code":"10000","msg":"Success","app_id":"2019011462945987","auth_app_id":"2019011462945987","charset":"utf-8","timestamp":"2019-01-26 17:42:14","out_trade_no":"20190126174152154849571281302","total_amount":"0.01","trade_no":"2019012622001410001024879285","seller_id":"2088431185459264"}
     * sign : arH2WrvNDY3YfGhLorWBIfxyVKCfwwoeC6uReeEIIdARXYjEcJFCN6pxN7a2gJcmCNEWWcwBCvj6a2MD6pudQVPcdcQVQL4sGthARAj+hV7bR3JxLQ0JyvbaRQ8nlxe+Q0CEctA34oHBM91rIb3vVadgiAwJofjw33mFuhbc252Gbl724xTCHIVyW7SDl0484cRhWZj+LMJtpaRpGSDQGTdgYfPojydm95hMhKh8v29qp19Q/oqXrMJ5ZSNfKBpW8IeLR+ttFXrBj93UCH06+dOTJJonjmcMjYEo7TB36NhL+pFaKUUmV+D0qBOj9bIcdAykLfe2ybJN7tBxEsamAw==
     * sign_type : RSA2
     */

    private AlipayTradeAppPayResponseBean alipay_trade_app_pay_response;
    private String sign;
    private String sign_type;

    public AlipayTradeAppPayResponseBean getAlipay_trade_app_pay_response() {
        return alipay_trade_app_pay_response;
    }

    public void setAlipay_trade_app_pay_response(AlipayTradeAppPayResponseBean alipay_trade_app_pay_response) {
        this.alipay_trade_app_pay_response = alipay_trade_app_pay_response;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public static class AlipayTradeAppPayResponseBean {
        /**
         * code : 10000
         * msg : Success
         * app_id : 2019011462945987
         * auth_app_id : 2019011462945987
         * charset : utf-8
         * timestamp : 2019-01-26 17:42:14
         * out_trade_no : 20190126174152154849571281302
         * total_amount : 0.01
         * trade_no : 2019012622001410001024879285
         * seller_id : 2088431185459264
         */

        private String code;
        private String msg;
        private String app_id;
        private String auth_app_id;
        private String charset;
        private String timestamp;
        private String out_trade_no;
        private String total_amount;
        private String trade_no;
        private String seller_id;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getApp_id() {
            return app_id;
        }

        public void setApp_id(String app_id) {
            this.app_id = app_id;
        }

        public String getAuth_app_id() {
            return auth_app_id;
        }

        public void setAuth_app_id(String auth_app_id) {
            this.auth_app_id = auth_app_id;
        }

        public String getCharset() {
            return charset;
        }

        public void setCharset(String charset) {
            this.charset = charset;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public String getTrade_no() {
            return trade_no;
        }

        public void setTrade_no(String trade_no) {
            this.trade_no = trade_no;
        }

        public String getSeller_id() {
            return seller_id;
        }

        public void setSeller_id(String seller_id) {
            this.seller_id = seller_id;
        }
    }
}
