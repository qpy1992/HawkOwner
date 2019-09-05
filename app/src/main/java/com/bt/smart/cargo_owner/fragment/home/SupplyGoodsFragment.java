package com.bt.smart.cargo_owner.fragment.home;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bt.smart.cargo_owner.MyApplication;
import com.bt.smart.cargo_owner.NetConfig;
import com.bt.smart.cargo_owner.R;
import com.bt.smart.cargo_owner.activity.samedayAct.CarTypeActivity;
import com.bt.smart.cargo_owner.adapter.LvAddGoodsAdapter;
import com.bt.smart.cargo_owner.adapter.RecyPlaceAdapter;
import com.bt.smart.cargo_owner.adapter.SpCarTypeAdapter;
import com.bt.smart.cargo_owner.adapter.SpTypeAdapter;
import com.bt.smart.cargo_owner.adapter.TsTypeAdapter;
import com.bt.smart.cargo_owner.fragment.user.SelectModelLengthFragment;
import com.bt.smart.cargo_owner.messageInfo.AddGoodsBean;
import com.bt.smart.cargo_owner.messageInfo.ApplyOrderResultInfo;
import com.bt.smart.cargo_owner.messageInfo.CarTypeListInfo;
import com.bt.smart.cargo_owner.messageInfo.CarrierInfo;
import com.bt.smart.cargo_owner.messageInfo.ChioceAdapterContentInfo;
import com.bt.smart.cargo_owner.messageInfo.DriverOrderInfo;
import com.bt.smart.cargo_owner.messageInfo.OrderDetailInfo;
import com.bt.smart.cargo_owner.messageInfo.ShengDataInfo;
import com.bt.smart.cargo_owner.messageInfo.TsTypeInfo;
import com.bt.smart.cargo_owner.messageInfo.TypeInfo;
import com.bt.smart.cargo_owner.utils.CommonUtil;
import com.bt.smart.cargo_owner.utils.HttpOkhUtils;
import com.bt.smart.cargo_owner.utils.MyFragmentManagerUtil;
import com.bt.smart.cargo_owner.utils.PopupOpenHelper;
import com.bt.smart.cargo_owner.utils.ProgressDialogUtil;
import com.bt.smart.cargo_owner.utils.RequestParamsFM;
import com.bt.smart.cargo_owner.utils.MyTextUtils;
import com.bt.smart.cargo_owner.utils.SoftKeyboardUtils;
import com.bt.smart.cargo_owner.utils.ToastUtils;
import com.bt.smart.cargo_owner.viewmodel.CustomDatePicker;
import com.bt.smart.cargo_owner.viewmodel.MyListView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Request;

public class SupplyGoodsFragment extends Fragment implements View.OnClickListener {
    private static String TAG = "SupplyGoodsFragment";
    private View mRootView;
    private ImageView img_back,iv_assign,img_add;
    private TextView tv_zh,tv_zhadd,tv_xh,tv_xhadd,tv_goods,tv_goodstype,
                     tv_cartype,tv_zhdate,tv_xhdate,tv_paytype,tv_typelength,
                     tv_goodstype_sel,tv_title,tv_xhtime,tv_assign,tv_fh_area,
                     tv_sh_area,tv_time,tv_submit;
    private LinearLayout ll_ftype,ll_ssq,ll_shssq;
    private EditText et_fh_area,et_fh_name,et_fh_phone,et_zhidan,et_sh_area,et_sh_name,
                     et_sh_phone,et_oilPrice,et_price;
    private MyListView mlv_goods;// 货物信息list
    private LinearLayout line_time,xh_time,line_sel_zpry,line_ffee,line_oilPrice;//选择发货时间
    private CheckBox ck_xj,ck_yj,ck_pc,ck_use,ck_nouse;//现结
    private String province,city,assignType,assignId;

    //省市区数据
    private List<ChioceAdapterContentInfo> mDataPopEd;
    private List<ShengDataInfo.DataBean> mSHEData,mSHIData,mQUData;
    private int stCityLevel;
    //选择省市区弹窗
    private PopupOpenHelper openHelper;

    private List<AddGoodsBean> goodsList;
    private LvAddGoodsAdapter goodsAdapter;
    private boolean bolGoodsDetail = true;

    private String carLeng="",carModel="",goodsTypeID = "";
    private List<TypeInfo.DataBean> goodsTypeList;
    private List<TsTypeInfo.DataBean> periodList;

    private String fh_id,sh_id,zhtime,xhtime,zhperiod,zhperiodcode,xhperiod,xhperiodcode,orderId;
    private String isBoxed = "1";
    private int ftype = 0;
    private double ap_price;
    private String current_select;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = LayoutInflater.from(getActivity()).inflate(R.layout.frg_supply_goods, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        img_back = mRootView.findViewById(R.id.img_back_a);
        tv_title = mRootView.findViewById(R.id.tv_title);
        tv_zh = mRootView.findViewById(R.id.tv_zh);
        tv_zhadd = mRootView.findViewById(R.id.tv_zhadd);
        tv_xh = mRootView.findViewById(R.id.tv_xh);
        tv_xhadd = mRootView.findViewById(R.id.tv_xhadd);
        tv_goods = mRootView.findViewById(R.id.tv_goods);
//        tv_ystype = mRootView.findViewById(R.id.tv_ystype);
        tv_goodstype = mRootView.findViewById(R.id.tv_goodstype);
        tv_cartype = mRootView.findViewById(R.id.tv_cartype);
        tv_zhdate = mRootView.findViewById(R.id.tv_zhdate);
        tv_xhdate = mRootView.findViewById(R.id.tv_xhdate);
        tv_paytype = mRootView.findViewById(R.id.tv_paytype);
        tv_xhtime = mRootView.findViewById(R.id.tv_xhtime);
        et_fh_area = mRootView.findViewById(R.id.et_fh_area);
        et_fh_name = mRootView.findViewById(R.id.et_fh_name);
        et_fh_name.setText(MyApplication.userName);
        et_fh_phone = mRootView.findViewById(R.id.et_fh_phone);
        et_fh_phone.setText(MyApplication.userPhone);
        et_zhidan = mRootView.findViewById(R.id.et_zhidan);
        et_zhidan.setText(MyApplication.userName);
        tv_fh_area = mRootView.findViewById(R.id.tv_fh_area);
        ll_ftype = mRootView.findViewById(R.id.ll_ftype);
        if(MyApplication.userType.equals("3")){
            ll_ftype.setVisibility(View.GONE);
        }
        ll_ssq = mRootView.findViewById(R.id.ll_ssq);
        ll_shssq = mRootView.findViewById(R.id.ll_shssq);
        tv_sh_area = mRootView.findViewById(R.id.tv_sh_area);
        et_sh_area = mRootView.findViewById(R.id.et_sh_area);
        et_sh_name = mRootView.findViewById(R.id.et_sh_name);
        et_sh_phone = mRootView.findViewById(R.id.et_sh_phone);
        img_add = mRootView.findViewById(R.id.img_add);
        mlv_goods = mRootView.findViewById(R.id.mlv_goods);
        tv_goodstype_sel = mRootView.findViewById(R.id.tv_goodstype_sel);
        tv_typelength = mRootView.findViewById(R.id.tv_typelength);
        line_time = mRootView.findViewById(R.id.line_time);
        xh_time = mRootView.findViewById(R.id.xh_time);
        tv_time = mRootView.findViewById(R.id.tv_time);
        line_sel_zpry = mRootView.findViewById(R.id.line_sel_zpry);
        ck_xj = mRootView.findViewById(R.id.ck_xj);
        ck_yj = mRootView.findViewById(R.id.ck_yj);
//        ck_zc = mRootView.findViewById(R.id.ck_zc);
        ck_pc = mRootView.findViewById(R.id.ck_pc);
        ck_use = mRootView.findViewById(R.id.ck_use);
        ck_nouse = mRootView.findViewById(R.id.ck_nouse);
        line_oilPrice = mRootView.findViewById(R.id.line_oilPrice);
        et_oilPrice = mRootView.findViewById(R.id.et_oilPrice);
        line_ffee = mRootView.findViewById(R.id.line_ffee);
        et_price = mRootView.findViewById(R.id.et_price);
        tv_submit = mRootView.findViewById(R.id.tv_submit);
        tv_assign = mRootView.findViewById(R.id.tv_assign);
        iv_assign = mRootView.findViewById(R.id.iv_assign);
    }

    private void initData() {
        tv_zh.setText(Html.fromHtml(getString(R.string.zh)));
        tv_zhadd.setText(Html.fromHtml(getString(R.string.zhadd)));
        tv_xh.setText(Html.fromHtml(getString(R.string.xh)));
        tv_xhadd.setText(Html.fromHtml(getString(R.string.xhadd)));
        tv_goods.setText(Html.fromHtml(getString(R.string.goods)));
        tv_goodstype.setText(Html.fromHtml(getString(R.string.goodstype)));
//        tv_ystype.setText(Html.fromHtml(getString(R.string.ystype)));
        tv_cartype.setText(Html.fromHtml(getString(R.string.cartype)));
        tv_zhdate.setText(Html.fromHtml(getString(R.string.zhdate)));
        tv_xhdate.setText(Html.fromHtml(getString(R.string.xhdate)));
        tv_paytype.setText(Html.fromHtml(getString(R.string.paytype)));
        img_back.setVisibility(View.VISIBLE);
        img_back.setOnClickListener(this);
        tv_title.setText("发布货源");
        img_add.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
        ll_ssq.setOnClickListener(this);
        ll_shssq.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
        line_time.setOnClickListener(this);
        tv_typelength.setOnClickListener(this);
        xh_time.setOnClickListener(this);
        tv_goodstype_sel.setOnClickListener(this);
        line_sel_zpry.setOnClickListener(this);

        //初始化起点线路
        initStartPlace();
        //初始化添加的货物信息列表
        initGoodsList();
        //设置车辆类型
//        setCarTypeSpinner();
        //设置选择按钮事件
        setCheckListener();
        //获取车辆信息列表
//        getAllCarType();
        //判断是否是更新
        orderId = getActivity().getIntent().getStringExtra("orderId");
        Log.i(TAG,"订单id为："+orderId);
        if(orderId!=null){
            showOrderDetail(orderId);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back_a:
                MyFragmentManagerUtil.closeFragmentOnAct(this);
                break;
            case R.id.ll_ssq://选择发货地
                if(SoftKeyboardUtils.isSoftShowing(getActivity())){
                    SoftKeyboardUtils.hideSystemSoftKeyboard(getActivity());
                }
                selectStartPlace(0);
                break;
            case R.id.ll_shssq://选择收货地
                if(SoftKeyboardUtils.isSoftShowing(getActivity())){
                    SoftKeyboardUtils.hideSystemSoftKeyboard(getActivity());
                }
                selectStartPlace(1);
                break;
            case R.id.img_add:
                //新增货物
                AddGoodsBean goodsBean = new AddGoodsBean();
                goodsList.add(goodsBean);
                goodsAdapter.notifyDataSetChanged();
                break;
            case R.id.line_time://选择装货时间
                //获取当前日期
                String data = simpleDateFormat.format(new Date());
                //打开时间选择器
                CustomDatePicker dpk1 = new CustomDatePicker(getContext(), new CustomDatePicker.ResultHandler() {
                    @Override
                    public void handle(String time) { // 回调接口，获得选中的时间
                        setPeriod(0,time);
                    }
                }, data, "2090-12-31 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
                dpk1.showSpecificTime(false); // 显示时和分
                dpk1.setIsLoop(true); // 允许循环滚动
                dpk1.show(data);
                break;
            case R.id.xh_time:
                //获取当前日期
                String data1 = simpleDateFormat.format(new Date());
                //打开时间选择器
                CustomDatePicker dpk = new CustomDatePicker(getContext(), new CustomDatePicker.ResultHandler() {
                    @Override
                    public void handle(String time) { // 回调接口，获得选中的时间
                        setPeriod(1,time);
                    }
                }, data1, "2090-12-31 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
                dpk.showSpecificTime(false); // 显示时和分
                dpk.setIsLoop(true); // 允许循环滚动
                dpk.show(data1);
                break;
            case R.id.tv_submit:
                //提交前先做数据认证，是否填写有效信息
                checkWriteInfo();
                break;
            case R.id.tv_typelength:
                if(SoftKeyboardUtils.isSoftShowing(getActivity())){
                    SoftKeyboardUtils.hideSystemSoftKeyboard(getActivity());
                }
                toSelectModelLength();
                break;
            case R.id.tv_goodstype_sel:
                if(SoftKeyboardUtils.isSoftShowing(getActivity())){
                    SoftKeyboardUtils.hideSystemSoftKeyboard(getActivity());
                }
                setGoodsType();
                break;
            case R.id.line_sel_zpry:
                toAssignaDriver();
                break;
        }
    }

    private void setCheckListener() {
        ck_xj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ck_xj.setChecked(true);
                ftype = 0;
            }
        });
        ck_yj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ck_yj.setChecked(true);
                ftype = 1;
            }
        });
        ck_pc.setOnClickListener(new View.OnClickListener() {
            int index = 0;
            @Override
            public void onClick(View view) {
                if(index%2==0){
                    ck_pc.setChecked(true);
                    isBoxed = "0";
                    index++;
                }else{
                    ck_pc.setChecked(false);
                    isBoxed = "1";
                    index++;
                }
            }
        });
        ck_xj.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ck_yj.setChecked(!b);
                if (b) {
                    //线上
                    line_ffee.setVisibility(View.VISIBLE);
                }
            }
        });
        ck_yj.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ck_xj.setChecked(!b);
                if (b) {
                    //线下
                    line_ffee.setVisibility(View.GONE);
                }
            }
        });

        ck_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ck_use.setChecked(true);
            }
        });
        ck_nouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ck_nouse.setChecked(true);
            }
        });
        ck_use.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ck_nouse.setChecked(!b);
                //使用油卡
                line_oilPrice.setVisibility(View.VISIBLE);
            }
        });
        ck_nouse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ck_use.setChecked(!b);
                //不使用油卡
                line_oilPrice.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 订单新增
     */
    private void sendApplyInfo() {
        RequestParamsFM headparam = new RequestParamsFM();
        headparam.put("X-AUTH-TOKEN", MyApplication.userToken);

        RequestParamsFM params = new RequestParamsFM();
        params.put("fmainId", MyApplication.userCode);//用户主账号
        params.put("fh", fh_id);//发货地id
        params.put("fhAddress", MyTextUtils.getEditTextContent(et_fh_area));//发货地地址
        params.put("fhName", MyTextUtils.getEditTextContent(et_fh_name));//发货人名字
        params.put("fhTelephone", MyTextUtils.getEditTextContent(et_fh_phone));//发货人手机号
        params.put("sh", sh_id);//收货地id
        params.put("shAddress", MyTextUtils.getEditTextContent(et_sh_area));//收货地址
        params.put("shName", MyTextUtils.getEditTextContent(et_sh_name));//收货人名字
        params.put("shTelephone", MyTextUtils.getEditTextContent(et_sh_phone));//收货人电话
        params.put("goodsName", goodsTypeID);//货物种类
        params.put("isAppoint", "0");//

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < goodsList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("goodsName", goodsList.get(i).getName());
                if(CommonUtil.isNotEmpty(goodsList.get(i).getArea())){
                    jsonObject.put("goodsSpace", Double.parseDouble(goodsList.get(i).getArea()));
                }else{
                    jsonObject.put("goodsSpace", 0);
                }
                jsonObject.put("goodsWeight", Double.parseDouble(goodsList.get(i).getWeight()));
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        params.put("orderGoodsList", jsonArray);//货物列表

        params.put("carType", carModel);//车型
        params.put("carLength", carLeng);
        params.put("zhTime", zhtime);//发货时间
        params.put("xhTime", xhtime);//卸货时间
        params.put("zhperiod",zhperiod);
        params.put("xhperiod",xhperiod);
        params.put("appointId", "");//指定人
        params.put("isBox", isBoxed);//是否拼箱
        params.put("ftype",ftype);
        if(ftype==0){
            //现结
            params.put("ffee", ap_price);//预算费用
            if (ck_use.isChecked()) {
                params.put("foilCard", Double.parseDouble(MyTextUtils.getEditTextContent(et_oilPrice)));//是否使用油卡
            } else {
                params.put("foilCard", 0);//是否使用油卡
            }
        }
        params.setUseJsonStreamer(true);
        ProgressDialogUtil.startShow(getContext(), "正在提交...");
        HttpOkhUtils.getInstance().doPostWithHeader(NetConfig.ORDERCONTROLLER, headparam, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ProgressDialogUtil.hideDialog();
                ToastUtils.showToast(getContext(), "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                ProgressDialogUtil.hideDialog();
                if (200 != code) {
                    ToastUtils.showToast(getContext(), "网络错误" + code);
                }
                Gson gson = new Gson();
                ApplyOrderResultInfo applyOrderResultInfo = gson.fromJson(resbody, ApplyOrderResultInfo.class);
                if (applyOrderResultInfo.isOk()) {
                    if(CommonUtil.isNotEmpty(assignId)){
                        //指定司机
                        String orderid = applyOrderResultInfo.getData().getId();
                        assignDriver(orderid);
                    }
                    Toast.makeText(getContext(),"发布成功",Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
            }
        });
    }

    private void checkWriteInfo() {
        if (!CommonUtil.isNotEmpty(fh_id)) {
            ToastUtils.showToast(getContext(), "请选择装货地");
            return;
        }
        String fh_area = MyTextUtils.getEditTextContent(et_fh_area);
        if ("".equals(fh_area) || "请输入发货地街道信息".equals(fh_area)) {
            ToastUtils.showToast(getContext(), "请输入发货地街道信息");
            return;
        }
        if (!CommonUtil.isNotEmpty(sh_id)) {
            ToastUtils.showToast(getContext(), "请选择卸货地");
            return;
        }
        String sh_area = MyTextUtils.getEditTextContent(et_sh_area);
        if ("".equals(sh_area) || "请输入收货地街道信息".equals(sh_area)) {
            ToastUtils.showToast(getContext(), "请输入收货地街道信息");
            return;
        }
        if (null == goodsList || goodsList.size() == 0) {
            ToastUtils.showToast(getContext(), "请添加货物信息");
            return;
        }
        bolGoodsDetail = true;
        for (int i = 0; i < goodsList.size(); i++) {
            String name = goodsList.get(i).getName();
//            String space = goodsList.get(i).getArea();
            String weight = goodsList.get(i).getWeight();
            if (null == name || "".equals(name)  || "0".equals(weight)) {
                bolGoodsDetail = false;
            }
        }
        if (!bolGoodsDetail) {
            ToastUtils.showToast(getContext(), "货物具体信息不能为空");
            return;
        }
        if (!CommonUtil.isNotEmpty(carLeng)||!CommonUtil.isNotEmpty(carModel)) {
            ToastUtils.showToast(getContext(), "请选择车型和车长");
            return;
        }
        String tv_sh_time = MyTextUtils.getTvTextContent(tv_time);
        String tv_xh_time = MyTextUtils.getTvTextContent(tv_xhtime);
        if (!CommonUtil.isNotEmpty(tv_sh_time)) {
            ToastUtils.showToast(getContext(), "请选择装货日期");
            return;
        }
        if (!CommonUtil.isNotEmpty(tv_xh_time)) {
            ToastUtils.showToast(getContext(), "请选择卸货日期");
            return;
        }
        if (ck_xj.isChecked()) {
            String etffee = MyTextUtils.getEditTextContent(et_price);
            if ("".equals(etffee) || "请输入您的报价".equals(etffee)) {
                //预算费用
                ap_price = 0;
            }else{
                ap_price = Double.parseDouble(MyTextUtils.getEditTextContent(et_price));
            }
        }
        //提交货源
        sendApplyInfo();
    }

    private void initStartPlace() {
        stCityLevel = 0;
        current_select = "选择：全国";
        //获取省的数据
        mSHEData = new ArrayList();
        mSHIData = new ArrayList();
        mQUData = new ArrayList();
        //选择窗数据
        mDataPopEd = new ArrayList<>();
        getShengFen();
    }

    private void getShengFen() {
        RequestParamsFM headParam = new RequestParamsFM();
        headParam.put("X-AUTH-TOKEN", MyApplication.userToken);
        RequestParamsFM params = new RequestParamsFM();
        params.put("pid", "100000");
        HttpOkhUtils.getInstance().doGetWithHeadParams(NetConfig.REGIONSELECT, headParam, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ProgressDialogUtil.hideDialog();
                ToastUtils.showToast(getContext(), "网络连接错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                ProgressDialogUtil.hideDialog();
                if (code != 200) {
                    ToastUtils.showToast(getContext(), "网络错误" + code);
                    return;
                }
                Gson gson = new Gson();
                ShengDataInfo shengDataInfo = gson.fromJson(resbody, ShengDataInfo.class);
                ToastUtils.showToast(getContext(), shengDataInfo.getMessage());
                if (shengDataInfo.isOk()) {
                    mSHEData.clear();
                    mSHEData.addAll(shengDataInfo.getData());
                    if (null == mDataPopEd) {
                        mDataPopEd = new ArrayList<>();
                    } else {
                        mDataPopEd.clear();
                    }
                    for (ShengDataInfo.DataBean bean : mSHEData) {
                        ChioceAdapterContentInfo contentInfo = new ChioceAdapterContentInfo();
                        contentInfo.setCont(bean.getFname());
                        contentInfo.setId(bean.getId());
                        mDataPopEd.add(contentInfo);
                    }
                }
            }
        });
    }

    private void selectStartPlace(final int kind) {
        openHelper = new PopupOpenHelper(getContext(), tv_fh_area, R.layout.popup_choice_start);
        openHelper.openPopupWindowWithView(true, 0, (int) tv_fh_area.getY() + tv_fh_area.getHeight());
        openHelper.setOnPopupViewClick(new PopupOpenHelper.ViewClickListener() {
            @Override
            public void onViewListener(PopupWindow popupWindow, View inflateView) {
                RecyclerView recy_city = inflateView.findViewById(R.id.recy_city);
                final TextView tv_current_select = inflateView.findViewById(R.id.tv_current_select);
                final TextView tv_back = inflateView.findViewById(R.id.tv_back);
                final TextView tv_cancel = inflateView.findViewById(R.id.tv_cancel);
                if (stCityLevel != 0) {
                    tv_back.setVisibility(View.VISIBLE);
                }
                recy_city.setLayoutManager(new GridLayoutManager(getContext(), 4));
                final RecyPlaceAdapter recyPlaceAdapter = new RecyPlaceAdapter(R.layout.adpter_pop_city_place, getContext(), mDataPopEd);
                recy_city.setAdapter(recyPlaceAdapter);
                recyPlaceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        String id = mDataPopEd.get(position).getId();
                        current_select = current_select + "-" + mDataPopEd.get(position).getCont();
                        if (stCityLevel == 0) {
                            //获取省份对应城市
                            getCityBySheng(id, tv_back,tv_current_select, recyPlaceAdapter);
                            province = mDataPopEd.get(position).getCont();
                            stCityLevel++;
                        } else if (stCityLevel == 1) {
                            //获取城市对应的区
                            getTownByCity(id, tv_back,tv_current_select, recyPlaceAdapter);
                            city = mDataPopEd.get(position).getCont();
                            stCityLevel++;
                        } else {
                            if (kind == 0) {
                                //将选择的起点填写
                                tv_fh_area.setText(province + city + mDataPopEd.get(position).getCont());
                                fh_id = mDataPopEd.get(position).getId();
                                openHelper.dismiss();
                                initStartPlace();
                            } else {
                                //将选择的目的地填写
                                tv_sh_area.setText(province + city + mDataPopEd.get(position).getCont());
                                sh_id = mDataPopEd.get(position).getId();
                                openHelper.dismiss();
                                initStartPlace();
                            }
                        }
                    }
                });
                //设置返回上一级事件
                tv_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        stCityLevel--;
                        if (stCityLevel == 0) {
                            tv_back.setVisibility(View.GONE);
                            current_select = "选择：全国";
                            tv_current_select.setText(current_select);
                            mDataPopEd.clear();
                            //添加上一级省数据
                            for (ShengDataInfo.DataBean bean : mSHEData) {
                                ChioceAdapterContentInfo contentInfo = new ChioceAdapterContentInfo();
                                contentInfo.setCont(bean.getFname());
                                contentInfo.setId(bean.getId());
                                mDataPopEd.add(contentInfo);
                            }
                            recyPlaceAdapter.notifyDataSetChanged();
                        } else if (stCityLevel == 1) {
                            current_select = current_select.split("-")[0]+"-"+current_select.split("-")[1];
                            tv_current_select.setText(current_select);
                            mDataPopEd.clear();
                            //添加上一级城市数据
                            for (ShengDataInfo.DataBean bean : mSHIData) {
                                ChioceAdapterContentInfo contentInfo = new ChioceAdapterContentInfo();
                                contentInfo.setCont(bean.getFname());
                                contentInfo.setId(bean.getId());
                                mDataPopEd.add(contentInfo);
                            }
                            recyPlaceAdapter.notifyDataSetChanged();
                        }
                    }
                });
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openHelper.dismiss();
                    }
                });
            }
        });
    }

    private void getCityBySheng(String id, final TextView tv_back,final TextView tv_current, final RecyPlaceAdapter recyPlaceAdapter) {
        RequestParamsFM headParam = new RequestParamsFM();
        headParam.put("X-AUTH-TOKEN", MyApplication.userToken);
        RequestParamsFM params = new RequestParamsFM();
        params.put("pid", id);
        HttpOkhUtils.getInstance().doGetWithHeadParams(NetConfig.REGIONSELECT, headParam, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ProgressDialogUtil.hideDialog();
                ToastUtils.showToast(getContext(), "网络连接错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                ProgressDialogUtil.hideDialog();
                if (code != 200) {
                    ToastUtils.showToast(getContext(), "网络错误" + code);
                    return;
                }
                Gson gson = new Gson();
                ShengDataInfo shengDataInfo = gson.fromJson(resbody, ShengDataInfo.class);
                ToastUtils.showToast(getContext(), shengDataInfo.getMessage());
                if (shengDataInfo.isOk()) {
                    tv_back.setVisibility(View.VISIBLE);
                    tv_current.setText(current_select);
                    mSHIData.clear();
                    mSHIData.addAll(shengDataInfo.getData());
                    if (null == mDataPopEd) {
                        mDataPopEd = new ArrayList<>();
                    } else {
                        mDataPopEd.clear();
                    }
                    for (ShengDataInfo.DataBean bean : mSHIData) {
                        ChioceAdapterContentInfo contentInfo = new ChioceAdapterContentInfo();
                        contentInfo.setCont(bean.getFname());
                        contentInfo.setId(bean.getId());
                        mDataPopEd.add(contentInfo);
                    }
                    recyPlaceAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void getTownByCity(String id, final TextView tv_back,final TextView tv_current, final RecyPlaceAdapter recyPlaceAdapter) {
        RequestParamsFM headParam = new RequestParamsFM();
        headParam.put("X-AUTH-TOKEN", MyApplication.userToken);
        RequestParamsFM params = new RequestParamsFM();
        params.put("pid", id);
        HttpOkhUtils.getInstance().doGetWithHeadParams(NetConfig.REGIONSELECT, headParam, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ProgressDialogUtil.hideDialog();
                ToastUtils.showToast(getContext(), "网络连接错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                ProgressDialogUtil.hideDialog();
                if (code != 200) {
                    ToastUtils.showToast(getContext(), "网络错误" + code);
                    return;
                }
                Gson gson = new Gson();
                ShengDataInfo shengDataInfo = gson.fromJson(resbody, ShengDataInfo.class);
                ToastUtils.showToast(getContext(), shengDataInfo.getMessage());
                if (shengDataInfo.isOk()) {
                    tv_back.setVisibility(View.VISIBLE);
                    tv_current.setText(current_select);
                    mQUData.clear();
                    mQUData.addAll(shengDataInfo.getData());
                    if (null == mDataPopEd) {
                        mDataPopEd = new ArrayList<>();
                    } else {
                        mDataPopEd.clear();
                    }
                    for (ShengDataInfo.DataBean bean : mQUData) {
                        ChioceAdapterContentInfo contentInfo = new ChioceAdapterContentInfo();
                        contentInfo.setCont(bean.getFname());
                        contentInfo.setId(bean.getId());
                        mDataPopEd.add(contentInfo);
                    }
                    recyPlaceAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initGoodsList() {
        goodsList = new ArrayList();
        goodsAdapter = new LvAddGoodsAdapter(getContext(), goodsList);
        mlv_goods.setAdapter(goodsAdapter);
    }


    private void setGoodsType() {
        goodsTypeList = new ArrayList();
        RequestParamsFM headparam = new RequestParamsFM();
        headparam.put("X-AUTH-TOKEN", MyApplication.userToken);
        HttpOkhUtils.getInstance().doGetWithOnlyHeader(NetConfig.TYPE, headparam, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                Log.i(TAG,"网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code != 200) {
                    Log.i(TAG, "网络错误" + code);
                    return;
                }
                Gson gson = new Gson();
                TypeInfo type = gson.fromJson(resbody,TypeInfo.class);
                List<TypeInfo.DataBean> dataBeanList = type.getData();
                goodsTypeList.addAll(dataBeanList);
                SpTypeAdapter typeAdapter = new SpTypeAdapter(getContext(), goodsTypeList);
                final ListView lv = new ListView(getContext());
                lv.setAdapter(typeAdapter);
                final AlertDialog builder = new AlertDialog.Builder(getContext()).create();
                builder.setView(lv);
                builder.setCanceledOnTouchOutside(false);
                builder.show();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        goodsTypeID = goodsTypeList.get(i).getId();
                        tv_goodstype_sel.setText(goodsTypeList.get(i).getName());
                        builder.dismiss();
                    }
                });
            }
        });
    }

    protected void setPeriod(final int t, final String time){
        periodList = new ArrayList<>();
        if(t==1&&!CommonUtil.isNotEmpty(zhtime)){
            ToastUtils.showToast(getContext(),"请先选择装货时间");
            return;
        }
        RequestParamsFM headparam = new RequestParamsFM();
        headparam.put("X-AUTH-TOKEN", MyApplication.userToken);
        HttpOkhUtils.getInstance().doGetWithOnlyHeader(NetConfig.TSTYPE + "/2c9f1a626c40745d016c4146de420003", headparam, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                Log.i(TAG,"网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code != 200) {
                    Log.i(TAG, "网络错误" + code);
                    return;
                }
                Gson gson = new Gson();
                final TsTypeInfo type = gson.fromJson(resbody,TsTypeInfo.class);
                List<TsTypeInfo.DataBean> dataBeanList = type.getData();
                periodList.addAll(dataBeanList);
                TsTypeAdapter typeAdapter = new TsTypeAdapter(getContext(), periodList);
                final ListView lv = new ListView(getContext());
                lv.setAdapter(typeAdapter);
                final AlertDialog builder = new AlertDialog.Builder(getContext()).create();
                builder.setView(lv);
                builder.setCanceledOnTouchOutside(false);
                builder.show();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        switch(t){
                            case 0:
                                zhtime = time.substring(0, 10);
                                zhperiod = periodList.get(i).getTypename();
                                zhperiodcode = periodList.get(i).getTypecode();
                                tv_time.setText(time.substring(0, 10)+periodList.get(i).getTypename().substring(0,2));
                                break;
                            case 1:
                                xhperiodcode = periodList.get(i).getTypecode();
                                if(zhtime.equals(time.substring(0, 10))){
                                    if(Integer.parseInt(zhperiodcode)>Integer.parseInt(xhperiodcode)){
                                        ToastUtils.showToast(getContext(),"卸货时间不能早于装货时间");
                                        return;
                                    }
                                    if(Integer.parseInt(zhperiodcode)==Integer.parseInt(xhperiodcode)){
                                        Toast.makeText(getContext(),"装卸货时间相近，请注意检查",Toast.LENGTH_LONG).show();
                                    }
                                }
                                xhtime = time.substring(0, 10);
                                xhperiod = periodList.get(i).getTypename();
                                tv_xhtime.setText(time.substring(0, 10)+periodList.get(i).getTypename().substring(0,2));
                                break;
                        }
                        builder.dismiss();
                    }
                });
            }
        });
    }

    public void setChioceTerm(List<ChioceAdapterContentInfo> lengthData, List<ChioceAdapterContentInfo> modelData) {
        if (null != lengthData) {
            for (int i = 0; i < lengthData.size(); i++) {
                if (lengthData.get(i).isChioce()) {
                    if ("".equals(carLeng)) {
                        carLeng = carLeng + lengthData.get(i).getCont();
                    } else {
                        carLeng = carLeng + "/" + lengthData.get(i).getCont();
                    }

                }
            }
        }

        if (null != modelData) {
            for (int i = 0; i < modelData.size(); i++) {
                if (modelData.get(i).isChioce()) {
                    if ("".equals(carModel)) {
                        carModel = carModel + modelData.get(i).getCont();
                    } else {
                        carModel = carModel + "/" + modelData.get(i).getCont();
                    }

                }
            }
        }
        tv_typelength.setText(carLeng+"|"+carModel);
    }

    public void setAssignId(CarrierInfo.DataBean item,int type){
        if(item!=null){
            assignType = type+"";
            assignId = item.getId();
            tv_assign.setText(item.getFname()+" "+item.getFmobile());
            iv_assign.setVisibility(View.GONE);
        }
    }

    private void toSelectModelLength() {
        SelectModelLengthFragment mollengthFt = new SelectModelLengthFragment();
        mollengthFt.setTopFragment(this);
        FragmentTransaction ftt = getFragmentManager().beginTransaction();
        ftt.add(R.id.frame, mollengthFt, "mollengthFt");
        ftt.addToBackStack("mollengthFt");
        ftt.commit();
    }

    private void toAssignaDriver() {
        ListView lv = new ListView(getContext());
        String[] items = new String[]{"承运商","司机"};
        ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,items);
        lv.setAdapter(adapter);
        final AlertDialog builder = new AlertDialog.Builder(getContext()).create();
        builder.setView(lv);
        builder.setCanceledOnTouchOutside(false);
        builder.show();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction ftt = getFragmentManager().beginTransaction();
                DriverFragment driverFragment = new DriverFragment();
                driverFragment.setType(position);
                driverFragment.setTopFragment(SupplyGoodsFragment.this);
                ftt.add(R.id.frame, driverFragment, "driverFragment");
                ftt.addToBackStack("driverFragment");
                ftt.commit();
                assignType = position+"";
                builder.dismiss();
            }
        });

    }

    protected void showOrderDetail(String id){
        RequestParamsFM headparam = new RequestParamsFM();
        headparam.put("X-AUTH-TOKEN", MyApplication.userToken);
        HttpOkhUtils.getInstance().doGetWithOnlyHeader(NetConfig.ALL_ORDER_DETAIL + "/" + id, headparam, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                Log.i(TAG,"网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code != 200) {
                    Log.i(TAG, "网络错误" + code);
                    return;
                }
                Gson gson = new Gson();
                OrderDetailInfo orderDetailInfo = gson.fromJson(resbody,OrderDetailInfo.class);
                if(orderDetailInfo.isOk()){
                    OrderDetailInfo.DataBean bean = orderDetailInfo.getData();
                    tv_fh_area.setText(bean.getOrigin());
                    fh_id = bean.getOrigin_area_id();
                    et_fh_area.setText(bean.getFh_address());
                    et_fh_area.setSelection(et_fh_area.getText().length());
                    et_fh_name.setText(bean.getFh_name());
                    et_fh_phone.setText(bean.getFh_telephone());
                    tv_sh_area.setText(bean.getDestination());
                    sh_id = bean.getDestination_area_id();
                    et_sh_area.setText(bean.getSh_address());
                    et_sh_name.setText(bean.getSh_name());
                    et_sh_phone.setText(bean.getSh_telephone());
                    if(bean.getIs_box().equals("0")){
                        //拼车
                        ck_pc.setChecked(true);
                        isBoxed = "0";
                    }
                    tv_goodstype_sel.setText(bean.getGoodsname());
                    goodsTypeID = bean.getGoods_name();
                    tv_typelength.setText(bean.getCar_length()+"|"+bean.getCar_type());
                    carLeng = bean.getCar_length();
                    carModel = bean.getCar_type();
                    tv_time.setText(bean.getZh_time().substring(0,10)+bean.getZhperiod().substring(0,2));
                    tv_xhtime.setText(bean.getXh_time().substring(0,10)+bean.getXhperiod().substring(0,2));
                    if(bean.getFtype()==0){
                        //现结
                        ck_xj.setChecked(true);
                        ck_yj.setChecked(false);
                        ftype = 0;
                    }else{
                        //月结
                        ck_xj.setChecked(false);
                        ck_yj.setChecked(true);
                        ftype = 1;
                    }
                    if(bean.getFoil_card()==0){
                        ck_nouse.setChecked(true);
                        ck_use.setChecked(false);
                    }else{
                        ck_use.setChecked(true);
                        ck_nouse.setChecked(false);
                        et_oilPrice.setText(bean.getFoil_card());
                    }
                    if(bean.getFfee()!=0){
                        et_price.setText(bean.getFfee()+"");
                    }
                }
            }
        });
    }

    protected void updateOrder(String id){
        RequestParamsFM headparam = new RequestParamsFM();
        headparam.put("X-AUTH-TOKEN", MyApplication.userToken);
        RequestParamsFM params = new RequestParamsFM();

        HttpOkhUtils.getInstance().doPutWithHeader(NetConfig.ALL_ORDER_DETAIL + "/" + id, headparam, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                Log.i(TAG,"网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code != 200) {
                    Log.i(TAG, "网络错误" + code);
                    return;
                }
            }
        });
    }

    protected void assignDriver(String orderid){
        RequestParamsFM headparam = new RequestParamsFM();
        headparam.put(NetConfig.TOKEN,MyApplication.userToken);
        RequestParamsFM params = new RequestParamsFM();
        params.put("orderId",orderid);
        params.put("orderStatus","1");
        if(assignType.equals("0")){
            //承运商
            params.put("registerId",assignId);
        }else{
            //个体司机
            params.put("driverId",assignId);
        }
        params.setUseJsonStreamer(true);
        HttpOkhUtils.getInstance().doPostWithHeader(NetConfig.ASSIGNDRIVER, headparam, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                Log.i(TAG,"网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if(code!=200){
                    Log.i(TAG,"网络错误");
                }
                DriverOrderInfo info = new Gson().fromJson(resbody,DriverOrderInfo.class);
                if(info.isOk()){
                    ToastUtils.showToast(getContext(),"指定司机成功");
                }
            }
        });
    }
}
