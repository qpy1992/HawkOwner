package com.bt.smart.cargo_owner.messageInfo;

import java.util.List;

public class OfferListInfo {

    /**
     * message : 成功
     * size : 0
     * data : [{"fname":"钱培元","fmobile":"13776931992","ffee":1,"driver_id":"2c9084dd6ad2ea16016ad2ebf2940000","order_id":"2c9084dd6b649386016b64a31ee20001","fnote":""}]
     * ok : true
     * respCode : 0
     */

    private String message;
    private int size;
    private boolean ok;
    private String respCode;
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

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * fname : 钱培元
         * fmobile : 13776931992
         * ffee : 1.0
         * driver_id : 2c9084dd6ad2ea16016ad2ebf2940000
         * order_id : 2c9084dd6b649386016b64a31ee20001
         * fnote :
         */

        private String fname;
        private String fmobile;
        private double ffee;
        private String driver_id;
        private String order_id;
        private String fnote;

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getFmobile() {
            return fmobile;
        }

        public void setFmobile(String fmobile) {
            this.fmobile = fmobile;
        }

        public double getFfee() {
            return ffee;
        }

        public void setFfee(double ffee) {
            this.ffee = ffee;
        }

        public String getDriver_id() {
            return driver_id;
        }

        public void setDriver_id(String driver_id) {
            this.driver_id = driver_id;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getFnote() {
            return fnote;
        }

        public void setFnote(String fnote) {
            this.fnote = fnote;
        }
    }
}
