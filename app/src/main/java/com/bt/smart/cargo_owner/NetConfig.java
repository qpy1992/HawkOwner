package com.bt.smart.cargo_owner;

/**
 * @创建者 AndyYan
 * @创建时间 2018/8/28 8:48
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class NetConfig {
    //服务器总地址
    public static String ROOT = "http://120.27.3.205/rest/";
    //    public static String ROOT = "http://172.16.52.63/rest/";
    //图片地址
    public static String IMG_HEAD = "http://120.27.3.205/";


    //file上传图片
    public static String PHOTO = ROOT + "registerDriverController/photo";

    public static String FACE = ROOT + "orderController/face";
    public static String PHOTO1 = ROOT + "orderController/photo";


    //注册用户
    public static String REGISTERDRIVER = ROOT + "tokens/register";
    //获取验证码
    public static String CHECKMESSAGE = ROOT + "tokens/SMScode";
    //修改密码(忘记原密码)
    public static String BACKFPASSWORDBYMOBILE = ROOT + "tokens/registerChangePsw";
    //用户登录
    public static String LOGINURL = ROOT + "tokens/registerLogin";
    //验证码登录
    public static String CodeLOGINURL = ROOT + "tokens/registerdriver/code";

    //个人身份认证
    public static String PERSONAUTH = ROOT + "eSignController/personAuth";
    //企业认证
    public static String ORGANIZEAUTH = ROOT + "eSignController/organizeAuth";
    //货主更新详细信息
    public static String DOUPDATEHZ = ROOT + "zRegisterController/doUpdateHZ";
    //个人货主信息补充
    public static String DOUPDATELOGISTICS = ROOT + "zRegisterController/doUpdateLogistics";
    //平台合同内容
    public static String CONTENT = ROOT + "tokens/content";
    //用户与平台签署合同
    public static String SIGNWITHPLATFORM = ROOT + "eSignController/signWithPlatform";


    //车辆类型
    public static String CARTYPE = ROOT + "zCarTypeController/list";
    //货主发布货源
    public static String ORDERCONTROLLER = ROOT + "orderController";

    //根据ID货主订单表信息
    public static String ORDERLIST = ROOT + "orderController/orderList";
    //获取当天货源条目详情
    public static String ALL_ORDER_DETAIL = ROOT + "orderController";
    //司机报价列表
    public static String DRIVERPRICELIST = ROOT + "orderController/driverPriceList";
    //货主发起协议签署
    public static String HUOZHU = ROOT + "eSignController/huozhu";
    //货主确认司机接单
    public static String CONFIRMORDER = ROOT + "orderController/confirmOrder";
    //回单
    public static String HUIDAN = ROOT + "orderController/huidan";
    //订单支付
    public static String PAY = ROOT + "orderController/pay";


    //*********************************
    //提交司机认证信息
    public static String DRIVERGDCONTROLLER = ROOT + "driverGdController";

    //获取当天货源信息列表
    public static String ALL_ORDER_LIST = ROOT + "orderController/list1";

    //司机接单
    public static String DRIVERORDERCONTROLLER = ROOT + "driverOrderController";

    public static String DRIVERORDERCONTROLLER_ADDRECORD = ROOT + "driverOrderController/addRecord";


    //根据司机id获取个人线路
    public static String DRIVERJOURNEYCONTROLLER = ROOT + "driverJourneyController";

    //获取省市区
    public static String REGIONSELECT = ROOT + "registerDriverController/regionSelect";

    //查询绑定的银行卡
    public static String BANKCARD = ROOT + "yqzlController/bindlist";

    //提现到银行卡
    public static String WITHDRAW = ROOT + "yqzlController/withdraw";

    //银行卡三要素校验
    public static String B_C_CHECK = ROOT + "yqzlController/three";

    //银行卡删除
    public static String BCDEL = ROOT + "yqzlController/cancelBind";

    //微信支付统一下单
    public static String WX = ROOT + "wxController/wxOrderHz";

    public static String WX_APPID = "wx614298998f514ca3";

    //支付宝参数拼接
    public static String ALIPAY = ROOT + "alipayController/alipayOrder";

    public static String PAYACCOUNTDRIVER_LIST = ROOT + "pADriverController/listbyid";

    public static String TYPE = ROOT + "typeController/list";

    public static String TSTYPE = ROOT + "typeController/tstype";
}
