package com.bt.smart.cargo_owner.messageInfo;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2019/1/9 16:19
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class AllOrderListInfo {
    /**
     * code : 1
     * pageSize : 1
     * pageList : [{"id":"2c90b4e368841e4a0168842f855a0001","fh":"南通市如皋市","sh":"重庆市江北区","faddtime":"2019-06-02 10:00:00.000","zh_time":"2019-06-02 10:00:00.000","fnote":"装货，一装一卸","goodsName":"普货","fhName":"赵三","fhTele":"15901863894","time_interval":"3天前"}]
     * message : 查询成功！
     */

    private String respCode;
    private int                size;
    private String             message;
    private List<PageListBean> data;
    Boolean ok;

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PageListBean> getData() {
        return data;
    }

    public void setData(List<PageListBean> data) {
        this.data = data;
    }

    public static class PageListBean {
        /**
         * id : 2c90b4e368841e4a0168842f855a0001
         * fh : 南通市如皋市
         * sh : 重庆市江北区
         * faddtime: 下单时间
         * zh_time: 装货时间
         * fnote: 备注
         * goodsName : 普货
         * fhName : 赵三
         * fhTele : 1590854673
         * time_interval: 1天前
         * lat : 23.17236213
         * lng : 120.8237474
         * distance : 22
         */

        private String id;
        private String fh;
        private String sh;
        private String faddtime;
        private String zh_time;
        private String fnote;
        private String goodsName;
        private String fhName;
        private String fhTele;
        private String time_interval;
        private String lat;
        private String lng;
        private String distance;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFh() {
            return fh;
        }

        public void setFh(String fh) {
            this.fh = fh;
        }

        public String getSh() {
            return sh;
        }

        public void setSh(String sh) {
            this.sh = sh;
        }

        public String getFaddtime() {
            return faddtime;
        }

        public void setFaddtime(String faddtime) {
            this.faddtime = faddtime;
        }

        public String getZh_time() {
            return zh_time;
        }

        public void setZh_time(String zh_time) {
            this.zh_time = zh_time;
        }

        public String getFnote() {
            return fnote;
        }

        public void setFnote(String fnote) {
            this.fnote = fnote;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getFhName() {
            return fhName;
        }

        public void setFhName(String fhName) {
            this.fhName = fhName;
        }

        public String getFhTele() {
            return fhTele;
        }

        public void setFhTele(String fhTele) {
            this.fhTele = fhTele;
        }

        public String getTime_interval() {
            return time_interval;
        }

        public void setTime_interval(String time_interval) {
            this.time_interval = time_interval;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }
    }
}
