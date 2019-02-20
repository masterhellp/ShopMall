package com.wythe.mall.beans;

import java.io.Serializable;
import java.util.List;

public class NewAddressBean implements Serializable {

    /**
     * code : 200
     * results : {"ret":100,"msg":"成功","totalCount":1,"list":[{"customerKey":"null","isDefault":"0","address":"经十路16199","areaId":"1361","phone":"17685416937","name":"郭迎辉","customerAddressKey":"00237ae4016d4cddae2836431b364ea0","cityId":"1360","provinceId":"1359"}]}
     */

            /**
             * customerKey : null
             * isDefault : 0
             * address : 经十路16199
             * areaId : 1361
             * phone : 17685416937
             * name : 郭迎辉
             * customerAddressKey : 00237ae4016d4cddae2836431b364ea0
             * cityId : 1360
             * provinceId : 1359
             */

            private String customerKey;
            private String isDefault;
            private String address;
            private String areaId;
            private String phone;
            private String name;
            private String customerAddressKey;
            private String cityId;
            private String provinceId;

            public String getCustomerKey() {
                return customerKey;
            }

            public void setCustomerKey(String customerKey) {
                this.customerKey = customerKey;
            }

            public String getIsDefault() {
                return isDefault;
            }

            public void setIsDefault(String isDefault) {
                this.isDefault = isDefault;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getAreaId() {
                return areaId;
            }

            public void setAreaId(String areaId) {
                this.areaId = areaId;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCustomerAddressKey() {
                return customerAddressKey;
            }

            public void setCustomerAddressKey(String customerAddressKey) {
                this.customerAddressKey = customerAddressKey;
            }

            public String getCityId() {
                return cityId;
            }

            public void setCityId(String cityId) {
                this.cityId = cityId;
            }

            public String getProvinceId() {
                return provinceId;
            }

            public void setProvinceId(String provinceId) {
                this.provinceId = provinceId;
            }
        

}
