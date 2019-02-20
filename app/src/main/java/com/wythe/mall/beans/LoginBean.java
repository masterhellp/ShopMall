package com.wythe.mall.beans;

public class LoginBean {
    /**
     * result : {"credentials":"111111","host":"","password":"111111","principal":"admin","rememberMe":false,"username":"admin"}
     * code : 200
     */

    private ResultBean result;
    private String code;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class ResultBean {
        /**
         * credentials : 111111
         * host :
         * password : 111111
         * principal : admin
         * rememberMe : false
         * username : admin
         */

        private String credentials;
        private String host;
        private String password;
        private String principal;
        private boolean rememberMe;
        private String username;

        public String getCredentials() {
            return credentials;
        }

        public void setCredentials(String credentials) {
            this.credentials = credentials;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPrincipal() {
            return principal;
        }

        public void setPrincipal(String principal) {
            this.principal = principal;
        }

        public boolean isRememberMe() {
            return rememberMe;
        }

        public void setRememberMe(boolean rememberMe) {
            this.rememberMe = rememberMe;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

}
