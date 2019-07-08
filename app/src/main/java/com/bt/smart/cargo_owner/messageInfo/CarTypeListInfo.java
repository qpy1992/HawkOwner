package com.bt.smart.cargo_owner.messageInfo;

import java.util.List;

public class CarTypeListInfo {

    /**
     * message : 成功
     * size : 0
     * data : [{"typeName":"30吨车型","id":"2c90b4e368db82ef0168db8f6494000e","fstatus":"0","fprice":null,"fweight":"30","forder":5,"typeValue":""},{"typeName":"20吨车型","id":"2c90b4e368db82ef0168db8efba8000c","fstatus":"0","fprice":null,"fweight":"20","forder":4,"typeValue":""},{"typeName":"15吨车型","id":"2c90b4e368db82ef0168db8ead72000a","fstatus":"0","fprice":null,"fweight":"15","forder":3,"typeValue":""},{"typeName":"10吨车型","id":"2c90b4e368db82ef0168db8e6bdf0008","fstatus":"0","fprice":null,"fweight":"10","forder":2,"typeValue":""},{"typeName":"5吨车型","id":"2c90b4e368db82ef0168db8e252e0006","fstatus":"0","fprice":null,"fweight":"5","forder":1,"typeValue":""},{"typeName":"2吨车型","id":"2c90b4e368db82ef0168db8d8e9a0004","fstatus":"0","fprice":null,"fweight":"2","forder":0,"typeValue":""}]
     * respCode : 0
     * ok : true
     */

    private String message;
    private int size;
    private String respCode;
    private boolean ok;
    private List<DataBean> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * typeName : 30吨车型
         * id : 2c90b4e368db82ef0168db8f6494000e
         * fstatus : 0
         * fprice : null
         * fweight : 30
         * forder : 5
         * typeValue :
         */

        private String typeName;
        private String id;
        private String fstatus;
        private Object fprice;
        private String fweight;
        private int forder;
        private String typeValue;

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFstatus() {
            return fstatus;
        }

        public void setFstatus(String fstatus) {
            this.fstatus = fstatus;
        }

        public Object getFprice() {
            return fprice;
        }

        public void setFprice(Object fprice) {
            this.fprice = fprice;
        }

        public String getFweight() {
            return fweight;
        }

        public void setFweight(String fweight) {
            this.fweight = fweight;
        }

        public int getForder() {
            return forder;
        }

        public void setForder(int forder) {
            this.forder = forder;
        }

        public String getTypeValue() {
            return typeValue;
        }

        public void setTypeValue(String typeValue) {
            this.typeValue = typeValue;
        }
    }
}
