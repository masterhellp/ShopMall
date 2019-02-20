package com.wythe.mall.beans;

import java.io.Serializable;
import java.util.List;

public class OneTypeBean implements Serializable {


            /**
             * qualification :
             * parentKey : 0
             * grade : 1
             * classifyKey : 7db448faad24437097097b01c18eabe3
             * ordernum : -2
             * classifyName : 个护化妆
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
