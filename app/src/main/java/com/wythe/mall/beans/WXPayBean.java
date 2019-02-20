package com.wythe.mall.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WXPayBean implements Serializable {


    /**
     * package : Sign=WXPay
     * appid : wx3b788a4850538962
     * sign : D3DD4B6BC45A5F17C36922C763BEBC1E
     * partnerid : 1524021131
     * prepayid : wx28214129090509477246b0c11908725671
     * noncestr : 6w1KDIVcOGLVdMlN
     * timestamp : 1548682891
     */

    @SerializedName("package")
    private String packageX;
    private String appid;
    private String sign;
    private String partnerid;
    private String prepayid;
    private String noncestr;
    private String timestamp;

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
