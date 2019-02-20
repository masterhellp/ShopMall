package com.wythe.mall.beans;

import java.io.Serializable;
import java.util.List;

public class OrderDetailBean implements Serializable {

            /**
             * image : http://103.46.128.41:16019/http://103.46.128.41:16019/images/589909-e339eb2763fa172c.jpg
             * amount : 1
             * orderNumber : 20190121202944154807378408508
             * goodsKey : 00822599efa8445bb42230dcd98659cc
             * normName : 测试用规格
             * freight : 0.00
             * discount : 0.00
             * orderDetailKey : c3dceaee8a2947feb33aa9aba9764923
             * normKey : 00822599efa8445bb42230dcd98659cc
             * createTime : 2019-01-22 04:29:44.0
             * price : 55.00
             * subtotal : 55.00
             * name : 1
             */

            private String image;
            private String amount;
            private String orderNumber;
            private String goodsKey;
            private String normName;
            private String freight;
            private String discount;
            private String orderDetailKey;
            private String normKey;
            private String createTime;
            private String price;
            private String subtotal;
            private String name;

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getOrderNumber() {
                return orderNumber;
            }

            public void setOrderNumber(String orderNumber) {
                this.orderNumber = orderNumber;
            }

            public String getGoodsKey() {
                return goodsKey;
            }

            public void setGoodsKey(String goodsKey) {
                this.goodsKey = goodsKey;
            }

            public String getNormName() {
                return normName;
            }

            public void setNormName(String normName) {
                this.normName = normName;
            }

            public String getFreight() {
                return freight;
            }

            public void setFreight(String freight) {
                this.freight = freight;
            }

            public String getDiscount() {
                return discount;
            }

            public void setDiscount(String discount) {
                this.discount = discount;
            }

            public String getOrderDetailKey() {
                return orderDetailKey;
            }

            public void setOrderDetailKey(String orderDetailKey) {
                this.orderDetailKey = orderDetailKey;
            }

            public String getNormKey() {
                return normKey;
            }

            public void setNormKey(String normKey) {
                this.normKey = normKey;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getSubtotal() {
                return subtotal;
            }

            public void setSubtotal(String subtotal) {
                this.subtotal = subtotal;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
}
