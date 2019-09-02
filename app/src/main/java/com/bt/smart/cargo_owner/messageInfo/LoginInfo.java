package com.bt.smart.cargo_owner.messageInfo;

/**
 * @创建者 AndyYan
 * @创建时间 2018/8/28 8:58
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class LoginInfo {

    /**
     * message : 成功
     * size : 0
     * data : {"zRegister":{"id":"2c9084dd6b5a2fac016b63cb9a460002","faccountEncryption":"ca666a5b480868f07591d02994a3bbc6","companyName":"上海柏田信息科技有限公司","fmobile":"15901643801","fdepsta":"1","fidentityBack":null,"fidentity":null,"companyLxr":"宋金华","fdepcode":"1","checkStatus":"1","faddress":"上海市","ftype":"2","checkReason":"","fpassword":"86f8bb6a67d509911dc2c9822fb927b7","taxNumber":"123123612366126767","taiTou":"上海柏田信息科技有限公司","usercodeCount":0,"fbank":"招商银行","companyPhone":"15901643801","recommd":"","mainUsercode":null,"frefundtime":null,"fsettle":null,"fdeptime":null,"fidentityFront":null,"companyContract":"null","companyLicence":"upload/files/zhizhao/1560747787603.png","createTime":null,"fbankNo":"6222021001081006238","usercode":"yingsu0053","faccount":1.89},"token":"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxNTkwMTY0MzgwMSIsInN1YiI6IjE1OTAxNjQzODAxIiwiaWF0IjoxNTYxMTk3NTk4fQ.Xf-4OoxPASx4vKfbiRP4Ft84Mt-nOBe_J_oOgtd-iRw"}
     * respCode : 0
     * ok : true
     */

    private String message;
    private int size;
    private DataBean data;
    private String respCode;
    private boolean ok;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
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

    public static class DataBean {
        /**
         * zRegister : {"id":"2c9084dd6b5a2fac016b63cb9a460002","faccountEncryption":"ca666a5b480868f07591d02994a3bbc6","companyName":"上海柏田信息科技有限公司","fmobile":"15901643801","fdepsta":"1","fidentityBack":null,"fidentity":null,"companyLxr":"宋金华","fdepcode":"1","checkStatus":"1","faddress":"上海市","ftype":"2","checkReason":"","fpassword":"86f8bb6a67d509911dc2c9822fb927b7","taxNumber":"123123612366126767","taiTou":"上海柏田信息科技有限公司","usercodeCount":0,"fbank":"招商银行","companyPhone":"15901643801","recommd":"","mainUsercode":null,"frefundtime":null,"fsettle":null,"fdeptime":null,"fidentityFront":null,"companyContract":"null","companyLicence":"upload/files/zhizhao/1560747787603.png","createTime":null,"fbankNo":"6222021001081006238","usercode":"yingsu0053","faccount":1.89}
         * token : eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxNTkwMTY0MzgwMSIsInN1YiI6IjE1OTAxNjQzODAxIiwiaWF0IjoxNTYxMTk3NTk4fQ.Xf-4OoxPASx4vKfbiRP4Ft84Mt-nOBe_J_oOgtd-iRw
         */

        private ZRegisterBean zRegister;
        private String token;

        public ZRegisterBean getZRegister() {
            return zRegister;
        }

        public void setZRegister(ZRegisterBean zRegister) {
            this.zRegister = zRegister;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public static class ZRegisterBean {
            /**
             * id : 2c9084dd6b5a2fac016b63cb9a460002
             * faccountEncryption : ca666a5b480868f07591d02994a3bbc6
             * companyName : 上海柏田信息科技有限公司
             * fmobile : 15901643801
             * fdepsta : 1
             * fidentityBack : null
             * fidentity : null
             * companyLxr : 宋金华
             * fdepcode : 1
             * checkStatus : 1
             * faddress : 上海市
             * ftype : 2
             * checkReason :
             * fpassword : 86f8bb6a67d509911dc2c9822fb927b7
             * taxNumber : 123123612366126767
             * taiTou : 上海柏田信息科技有限公司
             * usercodeCount : 0
             * fbank : 招商银行
             * companyPhone : 15901643801
             * recommd :
             * mainUsercode : null
             * frefundtime : null
             * fsettle : null
             * fdeptime : null
             * fidentityFront : null
             * companyContract : null
             * companyLicence : upload/files/zhizhao/1560747787603.png
             * createTime : null
             * fbankNo : 6222021001081006238
             * usercode : yingsu0053
             * faccount : 1.89
             */

            private String id;
            private String faccountEncryption;
            private String companyName;
            private String fmobile;
            private String fdepsta;
            private Object fidentityBack;
            private Object fidentity;
            private String companyLxr;
            private String fdepcode;
            private String checkStatus;
            private String faddress;
            private String ftype;
            private String checkReason;
            private String fpassword;
            private String taxNumber;
            private String taiTou;
            private int usercodeCount;
            private String fbank;
            private String companyPhone;
            private String recommd;
            private Object mainUsercode;
            private Object frefundtime;
            private Object fsettle;
            private Object fdeptime;
            private Object fidentityFront;
            private String companyContract;
            private String companyLicence;
            private Object createTime;
            private String fbankNo;
            private String usercode;
            private double faccount;
            private String paccountid;
            /**
             * faccountid :
             */

            private String faccountid;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getFaccountEncryption() {
                return faccountEncryption;
            }

            public void setFaccountEncryption(String faccountEncryption) {
                this.faccountEncryption = faccountEncryption;
            }

            public String getCompanyName() {
                return companyName;
            }

            public void setCompanyName(String companyName) {
                this.companyName = companyName;
            }

            public String getFmobile() {
                return fmobile;
            }

            public void setFmobile(String fmobile) {
                this.fmobile = fmobile;
            }

            public String getFdepsta() {
                return fdepsta;
            }

            public void setFdepsta(String fdepsta) {
                this.fdepsta = fdepsta;
            }

            public Object getFidentityBack() {
                return fidentityBack;
            }

            public void setFidentityBack(Object fidentityBack) {
                this.fidentityBack = fidentityBack;
            }

            public Object getFidentity() {
                return fidentity;
            }

            public void setFidentity(Object fidentity) {
                this.fidentity = fidentity;
            }

            public String getCompanyLxr() {
                return companyLxr;
            }

            public void setCompanyLxr(String companyLxr) {
                this.companyLxr = companyLxr;
            }

            public String getFdepcode() {
                return fdepcode;
            }

            public void setFdepcode(String fdepcode) {
                this.fdepcode = fdepcode;
            }

            public String getCheckStatus() {
                return checkStatus;
            }

            public void setCheckStatus(String checkStatus) {
                this.checkStatus = checkStatus;
            }

            public String getFaddress() {
                return faddress;
            }

            public void setFaddress(String faddress) {
                this.faddress = faddress;
            }

            public String getFtype() {
                return ftype;
            }

            public void setFtype(String ftype) {
                this.ftype = ftype;
            }

            public String getCheckReason() {
                return checkReason;
            }

            public void setCheckReason(String checkReason) {
                this.checkReason = checkReason;
            }

            public String getFpassword() {
                return fpassword;
            }

            public void setFpassword(String fpassword) {
                this.fpassword = fpassword;
            }

            public String getTaxNumber() {
                return taxNumber;
            }

            public void setTaxNumber(String taxNumber) {
                this.taxNumber = taxNumber;
            }

            public String getTaiTou() {
                return taiTou;
            }

            public void setTaiTou(String taiTou) {
                this.taiTou = taiTou;
            }

            public int getUsercodeCount() {
                return usercodeCount;
            }

            public void setUsercodeCount(int usercodeCount) {
                this.usercodeCount = usercodeCount;
            }

            public String getFbank() {
                return fbank;
            }

            public void setFbank(String fbank) {
                this.fbank = fbank;
            }

            public String getCompanyPhone() {
                return companyPhone;
            }

            public void setCompanyPhone(String companyPhone) {
                this.companyPhone = companyPhone;
            }

            public String getRecommd() {
                return recommd;
            }

            public void setRecommd(String recommd) {
                this.recommd = recommd;
            }

            public Object getMainUsercode() {
                return mainUsercode;
            }

            public void setMainUsercode(Object mainUsercode) {
                this.mainUsercode = mainUsercode;
            }

            public Object getFrefundtime() {
                return frefundtime;
            }

            public void setFrefundtime(Object frefundtime) {
                this.frefundtime = frefundtime;
            }

            public Object getFsettle() {
                return fsettle;
            }

            public void setFsettle(Object fsettle) {
                this.fsettle = fsettle;
            }

            public Object getFdeptime() {
                return fdeptime;
            }

            public void setFdeptime(Object fdeptime) {
                this.fdeptime = fdeptime;
            }

            public Object getFidentityFront() {
                return fidentityFront;
            }

            public void setFidentityFront(Object fidentityFront) {
                this.fidentityFront = fidentityFront;
            }

            public String getCompanyContract() {
                return companyContract;
            }

            public void setCompanyContract(String companyContract) {
                this.companyContract = companyContract;
            }

            public String getCompanyLicence() {
                return companyLicence;
            }

            public void setCompanyLicence(String companyLicence) {
                this.companyLicence = companyLicence;
            }

            public Object getCreateTime() {
                return createTime;
            }

            public void setCreateTime(Object createTime) {
                this.createTime = createTime;
            }

            public String getFbankNo() {
                return fbankNo;
            }

            public void setFbankNo(String fbankNo) {
                this.fbankNo = fbankNo;
            }

            public String getUsercode() {
                return usercode;
            }

            public void setUsercode(String usercode) {
                this.usercode = usercode;
            }

            public double getFaccount() {
                return faccount;
            }

            public void setFaccount(double faccount) {
                this.faccount = faccount;
            }

            public String getFaccountid() {
                return faccountid;
            }

            public void setFaccountid(String faccountid) {
                this.faccountid = faccountid;
            }

            public String getPaccountid() {
                return paccountid;
            }

            public void setPaccountid(String paccountid) {
                this.paccountid = paccountid;
            }
        }
    }
}
