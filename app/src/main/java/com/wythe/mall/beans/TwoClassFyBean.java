package com.wythe.mall.beans;

import java.io.Serializable;
import java.util.List;

public class TwoClassFyBean implements Serializable {


            /**
             * qualification :
             * parentKey : 5fa41aba9eb545d69200e5cf3d3853ac
             * grade : 2
             * classifyKey : 1978971b201a41ab8fa86de6556c88a5
             * ordernum : 0
             * classifyName : 手机通讯
             */

            private String qualification;
            private String parentKey;
            private String grade;
            private String classifyKey;
            private String ordernum;
            private String classifyName;

            public String getQualification() {
                return qualification;
            }

            public void setQualification(String qualification) {
                this.qualification = qualification;
            }

            public String getParentKey() {
                return parentKey;
            }

            public void setParentKey(String parentKey) {
                this.parentKey = parentKey;
            }

            public String getGrade() {
                return grade;
            }

            public void setGrade(String grade) {
                this.grade = grade;
            }

            public String getClassifyKey() {
                return classifyKey;
            }

            public void setClassifyKey(String classifyKey) {
                this.classifyKey = classifyKey;
            }

            public String getOrdernum() {
                return ordernum;
            }

            public void setOrdernum(String ordernum) {
                this.ordernum = ordernum;
            }

            public String getClassifyName() {
                return classifyName;
            }

            public void setClassifyName(String classifyName) {
                this.classifyName = classifyName;
            }
}
