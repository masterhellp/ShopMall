package com.wythe.mall.beans;

import java.io.Serializable;
import java.util.List;

/**
 * 购物车数据的bean类
 */

public class ShoppingCarDataBean {

    private int code;
    private List<DatasBean> datas;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean implements Serializable {

        private String shopKey;
        private String shopName;
        private boolean isSelect_shop;      //店铺是否在购物车中被选中
        private List<Goods> goods;
        //图片地址
        private String shopImg;

        public String getShopImg() {
            return shopImg;
        }

        public void setShopImg(String shopImg) {
            this.shopImg = shopImg;
        }

        public boolean getIsSelect_shop() {
            return isSelect_shop;
        }

        public void setIsSelect_shop(boolean select_shop) {
            isSelect_shop = select_shop;
        }

        public String getShopKey() {
            return shopKey;
        }

        public void setShopKey(String shopKey) {
            this.shopKey = shopKey;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public List<Goods> getGoods() {
            return goods;
        }

        public void setGoods(List<Goods> goods) {
            this.goods = goods;
        }

    }
}
