package com.wythe.mall.beans;

import java.util.List;

public class MerchantinfoBean {


    /**
     * code : 200
     * results : {"ret":100,"msg":"成功","totalCount":1,"list":[{"images":"http://103.46.128.41:16019/images/589909-e339eb2763fa172c.jpg","mprice":"12.00","goodsId":"00822599efa8445bb42230dcd98659cc","price":"1.00","name":"1","shopName":"XX建材","isSeckill":"","logo":"http://103.46.128.41:16019/images/qr_logo.jpg","shopKey":"2"}]}
     */

            /**
             * images : http://103.46.128.41:16019/images/589909-e339eb2763fa172c.jpg
             * mprice : 12.00
             * goodsId : 00822599efa8445bb42230dcd98659cc
             * price : 1.00
             * name : 1
             * shopName : XX建材
             * isSeckill :
             * logo : http://103.46.128.41:16019/images/qr_logo.jpg
             * shopKey : 2
             */

            private String images;
            private String mprice;
            private String goodsId;
            private String price;
            private String name;
            private String shopName;
            private String isSeckill;
            private String logo;
            private String shopKey;

            public String getImages() {
                return images;
            }

            public void setImages(String images) {
                this.images = images;
            }

            public String getMprice() {
                return mprice;
            }

            public void setMprice(String mprice) {
                this.mprice = mprice;
            }

            public String getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(String goodsId) {
                this.goodsId = goodsId;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getShopName() {
                return shopName;
            }

            public void setShopName(String shopName) {
                this.shopName = shopName;
            }

            public String getIsSeckill() {
                return isSeckill;
            }

            public void setIsSeckill(String isSeckill) {
                this.isSeckill = isSeckill;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public String getShopKey() {
                return shopKey;
            }

            public void setShopKey(String shopKey) {
                this.shopKey = shopKey;
            }
}
