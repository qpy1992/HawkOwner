package com.bt.smart.cargo_owner.messageInfo;

import java.sql.DataTruncation;
import java.util.List;

public class CarInfo {

    private String respCode;
    private int                size;
    private String             message;
    private List<CarBean> data;
    Boolean ok;

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

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public List<CarBean> getData() {
        return data;
    }

    public void setData(List<CarBean> data) {
        this.data = data;
    }

    public class CarBean{
        String car_long;
        String car_type;
        String add_date;
        String fname;
        String fphoto;
        String fmobile;
        String origin1;
        String origin2;
        String origin3;
        String destination1;
        String destination2;
        String destination3;
        int    ftype;
        String fweight;
        String fvolume;

        public String getCar_long() {
            return car_long;
        }

        public void setCar_long(String car_long) {
            this.car_long = car_long;
        }

        public String getCar_type() {
            return car_type;
        }

        public void setCar_type(String car_type) {
            this.car_type = car_type;
        }

        public String getAdd_date() {
            return add_date;
        }

        public void setAdd_date(String add_date) {
            this.add_date = add_date;
        }

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getFphoto() {
            return fphoto;
        }

        public void setFphoto(String fphoto) {
            this.fphoto = fphoto;
        }

        public String getFmobile() {
            return fmobile;
        }

        public void setFmobile(String fmobile) {
            this.fmobile = fmobile;
        }

        public String getOrigin1() {
            return origin1;
        }

        public void setOrigin1(String origin1) {
            this.origin1 = origin1;
        }

        public String getOrigin2() {
            return origin2;
        }

        public void setOrigin2(String origin2) {
            this.origin2 = origin2;
        }

        public String getOrigin3() {
            return origin3;
        }

        public void setOrigin3(String origin3) {
            this.origin3 = origin3;
        }

        public String getDestination1() {
            return destination1;
        }

        public void setDestination1(String destination1) {
            this.destination1 = destination1;
        }

        public String getDestination2() {
            return destination2;
        }

        public void setDestination2(String destination2) {
            this.destination2 = destination2;
        }

        public String getDestination3() {
            return destination3;
        }

        public void setDestination3(String destination3) {
            this.destination3 = destination3;
        }

        public int getFtype() {
            return ftype;
        }

        public void setFtype(int ftype) {
            this.ftype = ftype;
        }

        public String getFweight() {
            return fweight;
        }

        public void setFweight(String fweight) {
            this.fweight = fweight;
        }

        public String getFvolume() {
            return fvolume;
        }

        public void setFvolume(String fvolume) {
            this.fvolume = fvolume;
        }
    }
}
