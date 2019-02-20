package com.wythe.mall.beans;

/**
 * 订单
 * 订单ID.商家图标，商家名，订单创建时间，订单状态
 * 订单内容，订单价格
 */
public class OrdersBean {

    private String orderId;
    private String image;
    private String shopName;
    private String createTime;
    private String status;
    private String content;
    private String price;
    private String name ;
    private String tradeNumber;
    private String orderDetailKey;
    private String refundStatus;

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getOrderDetailKey() {
        return orderDetailKey;
    }

    public void setOrderDetailKey(String orderDetailKey) {
        this.orderDetailKey = orderDetailKey;
    }

    public String getTradeNumber() {
        return tradeNumber;
    }

    public void setTradeNumber(String tradeNumber) {
        this.tradeNumber = tradeNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //    private String orderMainKey;

//    public String getOrderMainKey() {
//        return orderMainKey;
//    }
//
//    public void setOrderMainKey(String orderMainKey) {
//        this.orderMainKey = orderMainKey;
//    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

//    "tradeNumber":"null",
//        "customerKey":"admin",
//        "finishTime":"null",
//        "address":"重庆石柱县龙沙镇vbb",
//        "addressKey":"357eea3d9fd84a8c8e53c2d05bad32b5",
//        "payTime":"2019-01-22 04:29:44.0",
//        "shopLogo":"http://103.46.128.41:16019/null",
//        "totalMoney":"55.00",
//        "shopName":"XX建材",
//        "discountMoney":"0.00",
//        "orderMainKey":"002c9660190741cca28a0a31ec28e9e2",
//        "sendTime":"null",
//        "goodsCount":"100",
//        "freightMoney":"15.00",
//        "payType":"true",
//        "phone":"11111111111",
//        "shopKey":"2",
//        "orderName":"",
//        "status":"1"
    private String shopLogo;
    private String goodsCount;
    private String payTime ;

    private String totalMoney;
    private String orderMainKey ;

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public String getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(String goodsCount) {
        this.goodsCount = goodsCount;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getOrderMainKey() {
        return orderMainKey;
    }

    public void setOrderMainKey(String orderMainKey) {
        this.orderMainKey = orderMainKey;
    }

    private String orderNumber ;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
}
