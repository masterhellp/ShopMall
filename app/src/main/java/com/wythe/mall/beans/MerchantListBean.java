package com.wythe.mall.beans;

import java.util.List;

public class MerchantListBean {


    /**
     * code : 200
     * results : {"ret":100,"msg":"成功","totalCount":2,"list":[{"logo":"http://images/589909-e339eb2763fa172c.jpg","shopName":"水果生鲜店","shopKey":"1","imgList":[{"images":"http://111.231.116.244:63153/images/5d662fab-3c24-4a2a-9691-918b7ab9a3af.png"},{"images":"http://111.231.116.244:63153/images/048ee3d9-5e0a-4dbe-872f-3d6efd3718b9.jpg"},{"images":"http://111.231.116.244:63153/images/93f25934-ff9c-4fd4-806d-08771e443d3b.jpg"}]},{"logo":"http://images/qr_logo.jpg","shopName":"XX建材","shopKey":"2","imgList":""}]}
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
         * list : [{"logo":"http://images/589909-e339eb2763fa172c.jpg","shopName":"水果生鲜店","shopKey":"1","imgList":[{"images":"http://111.231.116.244:63153/images/5d662fab-3c24-4a2a-9691-918b7ab9a3af.png"},{"images":"http://111.231.116.244:63153/images/048ee3d9-5e0a-4dbe-872f-3d6efd3718b9.jpg"},{"images":"http://111.231.116.244:63153/images/93f25934-ff9c-4fd4-806d-08771e443d3b.jpg"}]},{"logo":"http://images/qr_logo.jpg","shopName":"XX建材","shopKey":"2","imgList":""}]
         */

        private String ret;
        private String msg;
        private String totalCount;
        private List<ListBean> list;

        public String getRet() {
            return ret;
        }

        public void setRet(String ret) {
            this.ret = ret;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(String totalCount) {
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
             * logo : http://images/589909-e339eb2763fa172c.jpg
             * shopName : 水果生鲜店
             * shopKey : 1
             * imgList : [{"images":"http://111.231.116.244:63153/images/5d662fab-3c24-4a2a-9691-918b7ab9a3af.png"},{"images":"http://111.231.116.244:63153/images/048ee3d9-5e0a-4dbe-872f-3d6efd3718b9.jpg"},{"images":"http://111.231.116.244:63153/images/93f25934-ff9c-4fd4-806d-08771e443d3b.jpg"}]
             */

            private String logo;
            private String shopName;
            private String shopKey;
            private List<ImgListBean> imgList;

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public String getShopName() {
                return shopName;
            }

            public void setShopName(String shopName) {
                this.shopName = shopName;
            }

            public String getShopKey() {
                return shopKey;
            }

            public void setShopKey(String shopKey) {
                this.shopKey = shopKey;
            }

            public List<ImgListBean> getImgList() {
                return imgList;
            }

            public void setImgList(List<ImgListBean> imgList) {
                this.imgList = imgList;
            }

            public static class ImgListBean {
                /**
                 * images : http://111.231.116.244:63153/images/5d662fab-3c24-4a2a-9691-918b7ab9a3af.png
                 */

                private String images;

                public String getImages() {
                    return images;
                }

                public void setImages(String images) {
                    this.images = images;
                }
            }
        }
    }
}
