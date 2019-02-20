package com.wythe.mall.beans;

import java.io.Serializable;

public class Goods implements Serializable {
    //商品名称
    private String title;
    //现价
    private String nowPprice;
    // 图片
    private String img;
    // 产地
    private String place;
    // 原价
    private String oldPrice;

    private String goodsId;
    private String name;
    private String images;
    private String price;
    private String mprice;
    private String url;
    private String isSeckill;
    /**
     * shopKey : a356a08f3e1c4fd8918ecd6cecac58f0
     * price : 98
     * mprice : 0
     * postages : 15
     * isFreePostage : 1
     * postageInitAmount : 1
     * postageIncr : 0
     * postageIncrAmount : 1
     * purchase : 0
     * saleVolume : 177
     * stocks : 41
     * state : 0
     * seokeyword :
     * ishasSpec : 1
     * collectionKey : -1
     * goodsForm : 0
     * validStatus :
     * validBeginTime :
     * validEndTime :
     * auditingState :
     * goodsType : 0
     * ratioKey :
     * shopRatio :
     * businessKey :
     * shopName : 中国名优广电直营
     * businessName :
     * logo : default
     */

    private String shopKey;
    private String postages;
    private String isFreePostage;
    private String postageInitAmount;
    private String postageIncr;
    private String postageIncrAmount;
    private String purchase;
    private String saleVolume;
    private String stocks;
    private String state;
    private String seokeyword;
    private String ishasSpec;
    private String collectionKey;
    private String goodsForm;
    private String validStatus;
    private String validBeginTime;
    private String validEndTime;
    private String auditingState;
    private String goodsType;
    private String ratioKey;
    private String shopRatio;
    private String businessKey;
    private String shopName;
    private String businessName;
    private String logo;
    private String shopLogo;

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    /**
     * normId : 1c032d4b6d024089991783563e73a07e
     * normName : 250g
     * price : 298
     * stock : 52
     * mprice : 348
     */

    private String normId;
    private String normName;
    private String normKey;

    private String ishas_norms;

    public String getIshas_norms() {
        return ishas_norms;
    }

    public void setIshas_norms(String ishas_norms) {
        this.ishas_norms = ishas_norms;
    }

    /**
     * stocks : 1
     * validStatus : false
     * price : 1
     * shopkey : 2
     */

    private String shopkey;

    public String getNormKey() {
        return normKey;
    }

    public void setNormKey(String normKey) {
        this.normKey = normKey;
    }

    public String getNowPprice() {
        return nowPprice;
    }

    public void setNowPprice(String nowPprice) {
        this.nowPprice = nowPprice;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIsSeckill() {
        return isSeckill;
    }

    public void setIsSeckill(String isSeckill) {
        this.isSeckill = isSeckill;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMprice() {
        return mprice;
    }

    public void setMprice(String mprice) {
        this.mprice = mprice;
    }
    private String normPrice;

    public String getNormPrice() {
        return normPrice;
    }

    public void setNormPrice(String normPrice) {
        this.normPrice = normPrice;
    }

//    public String getShopKey() {
//        return shopKey;
//    }
//
//    public void setShopKey(String shopKey) {
//        this.shopKey = shopKey;
//    }

    public String getPostages() {
        return postages;
    }

    public void setPostages(String postages) {
        this.postages = postages;
    }

    public String getSeokeyword() {
        return seokeyword;
    }

    public void setSeokeyword(String seokeyword) {
        this.seokeyword = seokeyword;
    }


    public String getCollectionKey() {
        return collectionKey;
    }

    public void setCollectionKey(String collectionKey) {
        this.collectionKey = collectionKey;
    }


    public String getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(String validStatus) {
        this.validStatus = validStatus;
    }

    public String getValidBeginTime() {
        return validBeginTime;
    }

    public void setValidBeginTime(String validBeginTime) {
        this.validBeginTime = validBeginTime;
    }

    public String getValidEndTime() {
        return validEndTime;
    }

    public void setValidEndTime(String validEndTime) {
        this.validEndTime = validEndTime;
    }

    public String getAuditingState() {
        return auditingState;
    }

    public void setAuditingState(String auditingState) {
        this.auditingState = auditingState;
    }

    public String getRatioKey() {
        return ratioKey;
    }

    public void setRatioKey(String ratioKey) {
        this.ratioKey = ratioKey;
    }

    public String getShopRatio() {
        return shopRatio;
    }

    public void setShopRatio(String shopRatio) {
        this.shopRatio = shopRatio;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }


    public String getNormId() {
        return normId;
    }

    public void setNormId(String normId) {
        this.normId = normId;
    }

    public String getNormName() {
        return normName;
    }

    public void setNormName(String normName) {
        this.normName = normName;
    }

    //商品数量
    private String goods_num;
    private boolean isSelect;

    public String getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(String goods_num) {
        this.goods_num = goods_num;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    //选择规格
    private String selectSpec;

    public String getSelectSpec() {
        return selectSpec;
    }

    public void setSelectSpec(String selectSpec) {
        this.selectSpec = selectSpec;
    }


    public String getIsFreePostage() {
        return isFreePostage;
    }

    public void setIsFreePostage(String isFreePostage) {
        this.isFreePostage = isFreePostage;
    }

    public String getPostageInitAmount() {
        return postageInitAmount;
    }

    public void setPostageInitAmount(String postageInitAmount) {
        this.postageInitAmount = postageInitAmount;
    }

    public String getPostageIncr() {
        return postageIncr;
    }

    public void setPostageIncr(String postageIncr) {
        this.postageIncr = postageIncr;
    }

    public String getPostageIncrAmount() {
        return postageIncrAmount;
    }

    public void setPostageIncrAmount(String postageIncrAmount) {
        this.postageIncrAmount = postageIncrAmount;
    }

    public String getPurchase() {
        return purchase;
    }

    public void setPurchase(String purchase) {
        this.purchase = purchase;
    }

    public String getSaleVolume() {
        return saleVolume;
    }

    public void setSaleVolume(String saleVolume) {
        this.saleVolume = saleVolume;
    }

    public String getStocks() {
        return stocks;
    }

    public void setStocks(String stocks) {
        this.stocks = stocks;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setIshasSpec(String ishasSpec) {
        this.ishasSpec = ishasSpec;
    }

    public void setGoodsForm(String goodsForm) {
        this.goodsForm = goodsForm;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getIshasSpec() {
        return ishasSpec;
    }

    public String getGoodsForm() {
        return goodsForm;
    }

    public String getGoodsType() {
        return goodsType;
    }

    private String customerKey ;
    private int amount;
    private String goodsKey ;
    private String goodImage ;
    private String cartKey ;

    private String goodsName ;

    public String getCustomerKey() {
        return customerKey;
    }

    public void setCustomerKey(String customerKey) {
        this.customerKey = customerKey;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getGoodsKey() {
        return goodsKey;
    }

    public void setGoodsKey(String goodsKey) {
        this.goodsKey = goodsKey;
    }

    public String getGoodImage() {
        return goodImage;
    }

    public void setGoodImage(String goodImage) {
        this.goodImage = goodImage;
    }

    public String getCartKey() {
        return cartKey;
    }

    public void setCartKey(String cartKey) {
        this.cartKey = cartKey;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getShopkey() {
        return shopkey;
    }

    public void setShopkey(String shopkey) {
        this.shopkey = shopkey;
    }
}
