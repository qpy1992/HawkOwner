package com.bt.smart.cargo_owner.messageInfo;

import java.math.BigDecimal;

public class DriverOrderInfo {

    /**
     * data : {}
     * message : string
     * ok : true
     * respCode : string
     * size : 0
     */

    private DriverOrderBean data;
    private String message;
    private boolean ok;
    private String respCode;
    private int size;

    public void setData(DriverOrderBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public static class DriverOrderBean {
        /**主键*/
        private String id;
        /**接单日期*/
        private String createString;
        /**司机id*/
        private String driverId;
        /**订单id*/
        private String orderId;
        /**单据状态*/
        private String orderStatus;
        /**取消原因*/
        private String cancelReason;
        /**取消时间*/
        private String cancelTime;
        /**同意时间*/
        private String agreeTime;
        /**审核状态*/
        private String fcheck;
        /**审核意见*/
        private String fchcekOpinion;
        /**审核人*/
        private String fchecker;
        /**审核时间*/
        private String fcheckTime;
        /**费用*/
        private BigDecimal ffee;
        /**备注*/
        private  String fnote;
        /**承运商id*/
        private String registerId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreateString() {
            return createString;
        }

        public void setCreateString(String createString) {
            this.createString = createString;
        }

        public String getDriverId() {
            return driverId;
        }

        public void setDriverId(String driverId) {
            this.driverId = driverId;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getCancelReason() {
            return cancelReason;
        }

        public void setCancelReason(String cancelReason) {
            this.cancelReason = cancelReason;
        }

        public String getCancelTime() {
            return cancelTime;
        }

        public void setCancelTime(String cancelTime) {
            this.cancelTime = cancelTime;
        }

        public String getAgreeTime() {
            return agreeTime;
        }

        public void setAgreeTime(String agreeTime) {
            this.agreeTime = agreeTime;
        }

        public String getFcheck() {
            return fcheck;
        }

        public void setFcheck(String fcheck) {
            this.fcheck = fcheck;
        }

        public String getFchcekOpinion() {
            return fchcekOpinion;
        }

        public void setFchcekOpinion(String fchcekOpinion) {
            this.fchcekOpinion = fchcekOpinion;
        }

        public String getFchecker() {
            return fchecker;
        }

        public void setFchecker(String fchecker) {
            this.fchecker = fchecker;
        }

        public String getFcheckTime() {
            return fcheckTime;
        }

        public void setFcheckTime(String fcheckTime) {
            this.fcheckTime = fcheckTime;
        }

        public BigDecimal getFfee() {
            return ffee;
        }

        public void setFfee(BigDecimal ffee) {
            this.ffee = ffee;
        }

        public String getFnote() {
            return fnote;
        }

        public void setFnote(String fnote) {
            this.fnote = fnote;
        }

        public String getRegisterId() {
            return registerId;
        }

        public void setRegisterId(String registerId) {
            this.registerId = registerId;
        }
    }
}
