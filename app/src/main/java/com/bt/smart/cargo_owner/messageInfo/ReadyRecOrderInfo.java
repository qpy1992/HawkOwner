package com.bt.smart.cargo_owner.messageInfo;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2019/1/23 10:16
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class ReadyRecOrderInfo {

    /**
     * message : 成功
     * size : 1
     * data : [{"tempRowNumber":1,"tempColumn":0,"id":"2c9084dd6b649386016b64a31ee20001","order_no":"2019061700425124969","fstatus":"8","goods_name":"4d2881f66850132a01685013f0100001","fh_name":"张三","fh_telephone":"13776931992","fh_address":"城银路555弄","sh_name":"宋金华","sh_telephone":"15901643801","sh_address":"龙信广场","car_type":"2c90b4e368db82ef0168db8d8e9a0004","is_fapiao":null,"ffee":1,"zh_time":"2019-06-17","origin":"上海市上海市宝山区","destination":"江苏省南通市海门市","origin_province_id":"10","origin_city_id":"35","origin_area_id":"1170","destination_province_id":"11","destination_city_id":"114","destination_area_id":"1242","driver_id":"2c9084dd6ad2ea16016ad2ebf2940000"}]
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
         * tempRowNumber : 1
         * tempColumn : 0
         * id : 2c9084dd6b649386016b64a31ee20001
         * order_no : 2019061700425124969
         * fstatus : 8
         * name : 设备
         * car_type : 平板
         * car_length : 1.8米
         * zhTime : 2019-06-17上午(6:00~12:00)
         * xhTime : 2019-06-18上午(6:00~12:00)
         * fnote: 整车，一装一卸
         * origin : 上海市宝山区
         * destination : 南通市海门市
         * is_box: 1
         */

        private int tempRowNumber;
        private int tempColumn;
        private String id;
        private String order_no;
        private String fstatus;
        private String name;
        private String car_type;
        private String car_length;
        private String zhTime;
        private String xhTime;
        private String origin;
        private String destination;
        private String fnote;
        private String is_box;

        public int getTempRowNumber() {
            return tempRowNumber;
        }

        public void setTempRowNumber(int tempRowNumber) {
            this.tempRowNumber = tempRowNumber;
        }

        public int getTempColumn() {
            return tempColumn;
        }

        public void setTempColumn(int tempColumn) {
            this.tempColumn = tempColumn;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public String getFstatus() {
            return fstatus;
        }

        public void setFstatus(String fstatus) {
            this.fstatus = fstatus;
        }

        public String getCar_type() {
            return car_type;
        }

        public void setCar_type(String car_type) {
            this.car_type = car_type;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCar_length() {
            return car_length;
        }

        public void setCar_length(String car_length) {
            this.car_length = car_length;
        }

        public String getZhTime() {
            return zhTime;
        }

        public void setZhTime(String zhTime) {
            this.zhTime = zhTime;
        }

        public String getXhTime() {
            return xhTime;
        }

        public void setXhTime(String xhTime) {
            this.xhTime = xhTime;
        }

        public String getFnote() {
            return fnote;
        }

        public void setFnote(String fnote) {
            this.fnote = fnote;
        }

        public String getIs_box() {
            return is_box;
        }

        public void setIs_box(String is_box) {
            this.is_box = is_box;
        }
    }
}
