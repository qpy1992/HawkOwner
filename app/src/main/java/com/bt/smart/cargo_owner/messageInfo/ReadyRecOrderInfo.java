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
         * goods_name : 4d2881f66850132a01685013f0100001
         * fh_name : 张三
         * fh_telephone : 13776931992
         * fh_address : 城银路555弄
         * sh_name : 宋金华
         * sh_telephone : 15901643801
         * sh_address : 龙信广场
         * car_type : 2c90b4e368db82ef0168db8d8e9a0004
         * is_fapiao : null
         * ffee : 1.0
         * zh_time : 2019-06-17
         * origin : 上海市上海市宝山区
         * destination : 江苏省南通市海门市
         * origin_province_id : 10
         * origin_city_id : 35
         * origin_area_id : 1170
         * destination_province_id : 11
         * destination_city_id : 114
         * destination_area_id : 1242
         * driver_id : 2c9084dd6ad2ea16016ad2ebf2940000
         */

        private int tempRowNumber;
        private int tempColumn;
        private String id;
        private String order_no;
        private String fstatus;
        private String goods_name;
        private String fh_name;
        private String fh_telephone;
        private String fh_address;
        private String sh_name;
        private String sh_telephone;
        private String sh_address;
        private String car_type;
        private Object is_fapiao;
        private double ffee;
        private String zh_time;
        private String origin;
        private String destination;
        private String origin_province_id;
        private String origin_city_id;
        private String origin_area_id;
        private String destination_province_id;
        private String destination_city_id;
        private String destination_area_id;
        private String driver_id;

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

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getFh_name() {
            return fh_name;
        }

        public void setFh_name(String fh_name) {
            this.fh_name = fh_name;
        }

        public String getFh_telephone() {
            return fh_telephone;
        }

        public void setFh_telephone(String fh_telephone) {
            this.fh_telephone = fh_telephone;
        }

        public String getFh_address() {
            return fh_address;
        }

        public void setFh_address(String fh_address) {
            this.fh_address = fh_address;
        }

        public String getSh_name() {
            return sh_name;
        }

        public void setSh_name(String sh_name) {
            this.sh_name = sh_name;
        }

        public String getSh_telephone() {
            return sh_telephone;
        }

        public void setSh_telephone(String sh_telephone) {
            this.sh_telephone = sh_telephone;
        }

        public String getSh_address() {
            return sh_address;
        }

        public void setSh_address(String sh_address) {
            this.sh_address = sh_address;
        }

        public String getCar_type() {
            return car_type;
        }

        public void setCar_type(String car_type) {
            this.car_type = car_type;
        }

        public Object getIs_fapiao() {
            return is_fapiao;
        }

        public void setIs_fapiao(Object is_fapiao) {
            this.is_fapiao = is_fapiao;
        }

        public double getFfee() {
            return ffee;
        }

        public void setFfee(double ffee) {
            this.ffee = ffee;
        }

        public String getZh_time() {
            return zh_time;
        }

        public void setZh_time(String zh_time) {
            this.zh_time = zh_time;
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

        public String getOrigin_province_id() {
            return origin_province_id;
        }

        public void setOrigin_province_id(String origin_province_id) {
            this.origin_province_id = origin_province_id;
        }

        public String getOrigin_city_id() {
            return origin_city_id;
        }

        public void setOrigin_city_id(String origin_city_id) {
            this.origin_city_id = origin_city_id;
        }

        public String getOrigin_area_id() {
            return origin_area_id;
        }

        public void setOrigin_area_id(String origin_area_id) {
            this.origin_area_id = origin_area_id;
        }

        public String getDestination_province_id() {
            return destination_province_id;
        }

        public void setDestination_province_id(String destination_province_id) {
            this.destination_province_id = destination_province_id;
        }

        public String getDestination_city_id() {
            return destination_city_id;
        }

        public void setDestination_city_id(String destination_city_id) {
            this.destination_city_id = destination_city_id;
        }

        public String getDestination_area_id() {
            return destination_area_id;
        }

        public void setDestination_area_id(String destination_area_id) {
            this.destination_area_id = destination_area_id;
        }

        public String getDriver_id() {
            return driver_id;
        }

        public void setDriver_id(String driver_id) {
            this.driver_id = driver_id;
        }
    }
}
