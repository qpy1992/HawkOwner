package com.bt.smart.cargo_owner.messageInfo;

import java.util.List;

public class CarrierInfo {
    private String message;
    private List<DataBean> data;
    private String   respCode;
    private boolean  ok;
    private int size;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public static class DataBean{
        private String id;
        private String fname;
        private String fphoto;
        private String fmobile;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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
    }
}
