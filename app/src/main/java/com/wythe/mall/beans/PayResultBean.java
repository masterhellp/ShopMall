package com.wythe.mall.beans;

public class PayResultBean {


    /**
     * out_trade_no : 20190120152511154796911151305
     * total_amount : 0.01
     * subject : 测试支付
     * privateKeys : MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC+s9HWwM8FpFaCo/99zZksa5pDQQb01s+a7DaFuAQ9P1GC1s7dBLhLqdVK2h3x9jX/wxKzPwh+D73kvkVDIL7qBvOqNJctfx9xm7+GhwzBmhJtnQkVroIwFtRe5mdWL5hVFTDcBjIhFhsfe+7KokqMCc5iPKgvWzX7e49oJbcjEWdwxCD+kJQz8oSxL6ZtpEC3neMaouGomXqxNOZ/uTo/w9wAnujz0MWRgWOONz9p6E3cUI+gmfWqgBxWURd+81uC6B8+1FCALsSicp8u6eUlcn0/3C8D+9hbqDpYRPjoyi7jvH8XBvzycv6YEfaQcdHgotspMesaxlf71XM6/NPXAgMBAAECggEATzts+rnfCawO2XaD52PtuRv32uuAlWdOmEJ+IK6k7EGLfz36cq/MVnt4iAH5oRc3tkz5TwnoYF/v+MLpCgvn5ln/EP+KtDd1/ADG4YDo1uNJtsshBCYeJxbFI6y80H0z4F3OkAvu9fo2SK14OC6XpHlKi1xCUHKoChV7Lr1Eysdu/F6FFh8ERJOWiRCCLVuwz9yAdHFmefFecws+YT8cbSTEhxdOJXMd5SYLCWeIqzm+wNVtPovLe1aTRVLiXg0UcGhzR0Bu79kZdC2lQTb5FkMC2RXH+gV118tewTLvvNb9AkKnhcYNa0uxm8OM7hPVwj1aWsvC2mXkXoLnvFzUWQKBgQDyW5j2f55Hccp8bQpZjpElA85tC+F7VcpcUL9jwYxTEODgpPTU2MG4jbIpEKX3GX9Czh+fUA12eMrRIEuZC2WLAoVxezI3JPbrPxGpBpRFcBou0KBUU2Air8fwqj3K10yrQFwBIhkWJhN7oHpbjiN7y05UZ4l/cwc3sf25Vs2WgwKBgQDJb9zJ7xlQO0L1M+YqUjzg6Hna95APZEnRLNsS4Fia/TJV7SYCkuGqV1NjEtfN4wueSAAzCf9u0KmyGsh9LGDqoj3/AedU6eh1Qc3gbAstlBSmglUM4zuFj28RH3tUZFTkBcKJEMpoffiN1M3k32F4aIs/kxI4u+Mz6l2MVl9tHQKBgALUOOPft3xEAsYTC8Th2iXhdn5lXhtvc+5B7wn+D2JE1ysoIcd+RDygQs0h6Z4JySeBenMEnzOb01o/k0JVFLlbrS0W8A4eu1chpFYhwodQ+K1FOCnj00J0ZNQVWKVOgBGbrbdBq6NLjHyr8eGxtg5S4DYyAzPept3qkPih38J7AoGAfs8d9j2/+MMl1skSR2g4DPos5AzfMuFWmwka7NowzXlBu9gsCoi+6YZJ51/QXTtmcAExpu/PTsqrWYUE29AZPvxd/PAM2mbaQR7o5TP/IXVMPrpjolZWfTEboXLpJ+p1ioqZucHJOli7JUZnS4LYkH1309DZMsANOvx6YcNqFX0CgYBOvxBV+4pJhi98U4FI5iObzUHxliKzXvIvmx3kUEavm/E9aLac5I4MTcjYgPJCpbV0xySyPA91e23lt+lLYG8bUSXarG3QMO/0qF0diQUu1d/eRR5XrZTSnZKec4SA7ZsKP1DfWY9HK0UpeEkcwNAmrarP6cycxcuqWx+ZWqFAUA==
     * timeout_express : 10
     * product_code : QUICK_MSECURITY_PAY
     * body : 测试
     */

    private String out_trade_no;
    private String total_amount;
    private String subject;
    private String privateKeys;
    private String timeout_express;
    private String product_code;
    private String body;

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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPrivateKeys() {
        return privateKeys;
    }

    public void setPrivateKeys(String privateKeys) {
        this.privateKeys = privateKeys;
    }

    public String getTimeout_express() {
        return timeout_express;
    }

    public void setTimeout_express(String timeout_express) {
        this.timeout_express = timeout_express;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
