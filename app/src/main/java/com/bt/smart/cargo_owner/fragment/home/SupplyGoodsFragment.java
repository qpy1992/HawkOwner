package com.bt.smart.cargo_owner.fragment.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.bt.smart.cargo_owner.MyApplication;
import com.bt.smart.cargo_owner.NetConfig;
import com.bt.smart.cargo_owner.R;
import com.bt.smart.cargo_owner.adapter.LvAddGoodsAdapter;
import com.bt.smart.cargo_owner.adapter.RecyPlaceAdapter;
import com.bt.smart.cargo_owner.adapter.SpCarTypeAdapter;
import com.bt.smart.cargo_owner.messageInfo.AddGoodsBean;
import com.bt.smart.cargo_owner.messageInfo.ApplyOrderResultInfo;
import com.bt.smart.cargo_owner.messageInfo.CarTypeListInfo;
import com.bt.smart.cargo_owner.messageInfo.ChioceAdapterContentInfo;
import com.bt.smart.cargo_owner.messageInfo.ShengDataInfo;
import com.bt.smart.cargo_owner.utils.HttpOkhUtils;
import com.bt.smart.cargo_owner.utils.MyFragmentManagerUtil;
import com.bt.smart.cargo_owner.utils.PopupOpenHelper;
import com.bt.smart.cargo_owner.utils.ProgressDialogUtil;
import com.bt.smart.cargo_owner.utils.RequestParamsFM;
import com.bt.smart.cargo_owner.utils.MyTextUtils;
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
    private View mRootView;
    private ImageView img_back;
    private TextView tv_title;
    private TextView tv_fh_area;//发货省市区
    private ImageView img_sel_fh;//选择发货省市区
    private EditText et_fh_area;//发货详细地址
    private EditText et_fh_name;//发货人姓名
    private EditText et_fh_phone;//发货人电话
    private TextView tv_sh_area;//收货省市区
    private ImageView img_sel_sh;//选择收货省市区
    private EditText et_sh_area;//收货详细地址
    private EditText et_sh_name;//收货人名字
    private EditText et_sh_phone;//收货人电话
    private ImageView img_add;//添加货物信息
    private MyListView mlv_goods;// 货物信息list
    private Spinner sp_goodstype;//选择订单类别
    private Spinner sp_cartype;//选择运输车辆类别
    private LinearLayout line_time;//选择发货时间
    private TextView tv_time;//发货时间
    private LinearLayout line_sel_zpry;//选择指派人
    private CheckBox ck_xj;//现结
    private CheckBox ck_yj;//月结
    private CheckBox ck_acept;//接受拼箱
    private CheckBox ck_unacept;//不接受
    private LinearLayout line_ffee;//预付费模块
    private CheckBox ck_use;//使用油卡
    private CheckBox ck_nouse;//不使用
    private LinearLayout line_oilPrice;//油卡模块
    private EditText et_oilPrice;//油卡模块
    private EditText et_price;//预算费用
    private TextView tv_submit;//发布

    //省市区数据
    private List<ChioceAdapterContentInfo> mDataPopEd;
    private List<ShengDataInfo.DataBean> mSHEData;
    private List<ShengDataInfo.DataBean> mSHIData;
    private List<ShengDataInfo.DataBean> mQUData;
    private int stCityLevel;
    //选择省市区弹窗
    private PopupOpenHelper openHelper;

    private List<AddGoodsBean> goodsList;
    private LvAddGoodsAdapter goodsAdapter;
    private boolean bolGoodsDetail = true;

    private List<CarTypeListInfo.DataBean> carTypeList;
    private SpCarTypeAdapter carTypeAdapter;
    private String carTypeID = "";
    private List goodsTypeList;
    private String goodsTypeID = "1";//普货

    private String fh_id;
    private String sh_id;
    private String isBoxed = "0";
    private double ap_price;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = LayoutInflater.from(getActivity()).inflate(R.layout.frg_supply_goods, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        img_back = mRootView.findViewById(R.id.img_back);
        tv_title = mRootView.findViewById(R.id.tv_title);
        et_fh_area = mRootView.findViewById(R.id.et_fh_area);
        et_fh_name = mRootView.findViewById(R.id.et_fh_name);
        et_fh_name = mRootView.findViewById(R.id.et_fh_name);
        et_fh_phone = mRootView.findViewById(R.id.et_fh_phone);
        tv_fh_area = mRootView.findViewById(R.id.tv_fh_area);
        img_sel_fh = mRootView.findViewById(R.id.img_sel_fh);
        tv_sh_area = mRootView.findViewById(R.id.tv_sh_area);
        img_sel_sh = mRootView.findViewById(R.id.img_sel_sh);
        et_sh_area = mRootView.findViewById(R.id.et_sh_area);
        et_sh_name = mRootView.findViewById(R.id.et_sh_name);
        et_sh_phone = mRootView.findViewById(R.id.et_sh_phone);
        img_add = mRootView.findViewById(R.id.img_add);
        mlv_goods = mRootView.findViewById(R.id.mlv_goods);
        sp_goodstype = mRootView.findViewById(R.id.sp_goodstype);
        sp_cartype = mRootView.findViewById(R.id.sp_cartype);
        line_time = mRootView.findViewById(R.id.line_time);
        tv_time = mRootView.findViewById(R.id.tv_time);
        line_sel_zpry = mRootView.findViewById(R.id.line_sel_zpry);
        ck_xj = mRootView.findViewById(R.id.ck_xj);
        ck_yj = mRootView.findViewById(R.id.ck_yj);
        ck_acept = mRootView.findViewById(R.id.ck_acept);
        ck_unacept = mRootView.findViewById(R.id.ck_unacept);
        ck_use = mRootView.findViewById(R.id.ck_use);
        ck_nouse = mRootView.findViewById(R.id.ck_nouse);
        line_oilPrice = mRootView.findViewById(R.id.line_oilPrice);
        et_oilPrice = mRootView.findViewById(R.id.et_oilPrice);
        line_ffee = mRootView.findViewById(R.id.line_ffee);
        et_price = mRootView.findViewById(R.id.et_price);
        tv_submit = mRootView.findViewById(R.id.tv_submit);
    }

    private void initData() {
        img_back.setVisibility(View.VISIBLE);
        img_back.setOnClickListener(this);
        tv_title.setText("发布货源");
        img_add.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
        img_sel_fh.setOnClickListener(this);
        img_sel_sh.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
        line_time.setOnClickListener(this);

        //初始化起点线路
        initStartPlace();

        //初始化添加的货物信息列表
        initGoodsList();
        //设置货物类型
        setGoodsType();
        //设置车辆类型
        setCarTypeSpinner();

        //设置选择按钮事件
        setCheckListener();

        //获取车辆信息列表
        getAllCarType();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                MyFragmentManagerUtil.closeTopFragment(this);
                break;
            case R.id.img_sel_fh://选择发货地
                selectStartPlace(0);
                break;
            case R.id.img_sel_sh://收货地
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
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String data = simpleDateFormat.format(new Date());
                //打开时间选择器
                CustomDatePicker dpk1 = new CustomDatePicker(getContext(), new CustomDatePicker.ResultHandler() {
                    @Override
                    public void handle(String time) { // 回调接口，获得选中的时间
                        tv_time.setText(time.substring(0, 10));
                    }
                }, data, "2090-12-31 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
                dpk1.showSpecificTime(false); // 显示时和分
                dpk1.setIsLoop(true); // 允许循环滚动
                dpk1.show(data);
                break;
            case R.id.tv_submit:
                //提交前先做数据认证，是否填写有效信息
                checkWriteInfo();
                break;
        }
    }

    private void setCheckListener() {
        ck_xj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ck_xj.setChecked(true);
            }
        });
        ck_yj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ck_yj.setChecked(true);
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

        ck_acept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ck_acept.setChecked(true);
            }
        });
        ck_unacept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ck_unacept.setChecked(true);
            }
        });
        ck_acept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ck_unacept.setChecked(!b);
                //接受拼箱
                isBoxed = "0";
            }
        });
        ck_unacept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ck_acept.setChecked(!b);
                //不接受拼箱
                isBoxed = "1";
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

    private void sendApplyInfo() {
        RequestParamsFM headparam = new RequestParamsFM();
        headparam.put("X-AUTH-TOKEN", MyApplication.userToken);

        RequestParamsFM params = new RequestParamsFM();
        params.put("fmainId", MyApplication.userCode);//发货地id
        params.put("fh", fh_id);//发货地id
        params.put("fhAddress", MyTextUtils.getEditTextContent(et_fh_area));//发货地地址
        params.put("fhName", MyTextUtils.getEditTextContent(et_fh_name));//发货人名字
        params.put("fhTelephone", MyTextUtils.getEditTextContent(et_fh_phone));//发货人手机号
        params.put("sh", sh_id);//收货地id
        params.put("shAddress", MyTextUtils.getEditTextContent(et_sh_area));//收货地址
        params.put("shName", MyTextUtils.getEditTextContent(et_sh_name));//收货人名字
        params.put("shTelephone", MyTextUtils.getEditTextContent(et_sh_phone));//收货人电话
        params.put("goodsName", "4d2881f66850132a01685013f0100001");//货物种类
        params.put("isAppoint", "0");//

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < goodsList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("goodsName", goodsList.get(i).getName());
                jsonObject.put("goodsSpace", Double.parseDouble(goodsList.get(i).getArea()));
                jsonObject.put("goodsWeight", Double.parseDouble(goodsList.get(i).getWeight()));
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        params.put("orderGoodsList", jsonArray);//货物列表

        params.put("carType", carTypeID);//车型
        params.put("zhTime", MyTextUtils.getTvTextContent(tv_time));//发货时间
        params.put("appointId", "");//指定人
        params.put("isBox", isBoxed);//是否拼箱
        if (ck_xj.isChecked()) {
            params.put("ftype", 0);//现结
            params.put("ffee", ap_price);//预算费用
            if (ck_use.isChecked()) {
                params.put("foilCard", Double.parseDouble(MyTextUtils.getEditTextContent(et_oilPrice)));//是否使用油卡
            } else {
                params.put("foilCard", 0);//是否使用油卡
            }
        }else {
            params.put("ftype", 1);//月结
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
                    getActivity().finish();
                }
            }
        });
    }

    private void checkWriteInfo() {
        if (null == fh_id || "".equals(fh_id)) {
            ToastUtils.showToast(getContext(), "请选择发货地区");
            return;
        }
        String fh_area = MyTextUtils.getEditTextContent(et_fh_area);
        String fh_name = MyTextUtils.getEditTextContent(et_fh_name);
        String fh_phone = MyTextUtils.getEditTextContent(et_fh_phone);
        if ("".equals(fh_area) || "请输入发货地街道信息".equals(fh_area)) {
            ToastUtils.showToast(getContext(), "请输入发货地街道信息");
            return;
        }
        if ("".equals(fh_name) || "请输入发货人姓名".equals(fh_name)) {
            ToastUtils.showToast(getContext(), "请输入发货人姓名");
            return;
        }
        if ("".equals(fh_phone) || "请输入发货人手机号".equals(fh_phone)) {
            ToastUtils.showToast(getContext(), "请输入发货人手机号");
            return;
        }

        if (null == sh_id || "".equals(sh_id)) {
            ToastUtils.showToast(getContext(), "请选择收货地区");
            return;
        }
        String sh_area = MyTextUtils.getEditTextContent(et_sh_area);
        String sh_name = MyTextUtils.getEditTextContent(et_sh_name);
        String sh_phone = MyTextUtils.getEditTextContent(et_sh_phone);

        if ("".equals(sh_area) || "请输入收货地街道信息".equals(sh_area)) {
            ToastUtils.showToast(getContext(), "请输入收货地街道信息");
            return;
        }
        if ("".equals(sh_name) || "请输入收货人姓名".equals(sh_name)) {
            ToastUtils.showToast(getContext(), "请输入收货人姓名");
            return;
        }
        if ("".equals(sh_phone) || "请输入发货人手机号".equals(sh_phone)) {
            ToastUtils.showToast(getContext(), "请输入收货人手机号");
            return;
        }

        if (null == goodsList || goodsList.size() == 0) {
            ToastUtils.showToast(getContext(), "请输入添加货物信息");
            return;
        }
        bolGoodsDetail = true;
        for (int i = 0; i < goodsList.size(); i++) {
            String name = goodsList.get(i).getName();
            String space = goodsList.get(i).getArea();
            String weight = goodsList.get(i).getWeight();
            if (null == name || "".equals(name) || "0".equals(space) || "0".equals(weight)) {
                bolGoodsDetail = false;
            }
        }
        if (!bolGoodsDetail) {
            ToastUtils.showToast(getContext(), "货物具体信息不能为空");
            return;
        }
        if (null == carTypeID || "".equals(carTypeID)) {
            ToastUtils.showToast(getContext(), "请输选择车型");
            return;
        }
        String tv_sh_time = MyTextUtils.getTvTextContent(tv_time);
        if ("".equals(tv_sh_time) || "选择装货日期".equals(tv_sh_time)) {
            ToastUtils.showToast(getContext(), "请选择装货日期");
            return;
        }

        if (ck_xj.isChecked()) {
            String etffee = MyTextUtils.getEditTextContent(et_price);
            if ("".equals(etffee) || "请输入您的报价".equals(etffee)) {
                ToastUtils.showToast(getContext(), "请输入您的报价");
                return;
            }
            //预算费用
            ap_price = Double.parseDouble(MyTextUtils.getEditTextContent(et_price));
        }
        //提交货源
        sendApplyInfo();
    }

    private void initStartPlace() {
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
        params.put("pid", "1");
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
                        contentInfo.setCont(bean.getName());
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
                        if (stCityLevel == 0) {
                            //获取省份对应城市
                            getCityBySheng(id, tv_back, recyPlaceAdapter);
                            stCityLevel++;
                        } else if (stCityLevel == 1) {
                            //获取城市对应的区
                            getTownByCity(id, tv_back, recyPlaceAdapter);
                            stCityLevel++;
                        } else {
                            if (kind == 0) {
                                //将选择的起点填写
                                tv_fh_area.setText(mDataPopEd.get(position).getCont());
                                fh_id = mDataPopEd.get(position).getId();
                                openHelper.dismiss();
                            } else {
                                //将选择的目的地填写
                                tv_sh_area.setText(mDataPopEd.get(position).getCont());
                                sh_id = mDataPopEd.get(position).getId();
                                openHelper.dismiss();
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
                            mDataPopEd.clear();
                            //添加上一级省数据
                            for (ShengDataInfo.DataBean bean : mSHEData) {
                                ChioceAdapterContentInfo contentInfo = new ChioceAdapterContentInfo();
                                contentInfo.setCont(bean.getName());
                                contentInfo.setId(bean.getId());
                                mDataPopEd.add(contentInfo);
                            }
                            recyPlaceAdapter.notifyDataSetChanged();
                        } else if (stCityLevel == 1) {
                            mDataPopEd.clear();
                            //添加上一级城市数据
                            for (ShengDataInfo.DataBean bean : mSHIData) {
                                ChioceAdapterContentInfo contentInfo = new ChioceAdapterContentInfo();
                                contentInfo.setCont(bean.getName());
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

    private void getCityBySheng(String id, final TextView tv_back, final RecyPlaceAdapter recyPlaceAdapter) {
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
                    mSHIData.clear();
                    mSHIData.addAll(shengDataInfo.getData());
                    if (null == mDataPopEd) {
                        mDataPopEd = new ArrayList<>();
                    } else {
                        mDataPopEd.clear();
                    }
                    for (ShengDataInfo.DataBean bean : mSHIData) {
                        ChioceAdapterContentInfo contentInfo = new ChioceAdapterContentInfo();
                        contentInfo.setCont(bean.getName());
                        contentInfo.setId(bean.getId());
                        mDataPopEd.add(contentInfo);
                    }
                    recyPlaceAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void getTownByCity(String id, final TextView tv_back, final RecyPlaceAdapter recyPlaceAdapter) {
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
                    mQUData.clear();
                    mQUData.addAll(shengDataInfo.getData());
                    if (null == mDataPopEd) {
                        mDataPopEd = new ArrayList<>();
                    } else {
                        mDataPopEd.clear();
                    }
                    for (ShengDataInfo.DataBean bean : mQUData) {
                        ChioceAdapterContentInfo contentInfo = new ChioceAdapterContentInfo();
                        contentInfo.setCont(bean.getName());
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
        CarTypeListInfo.DataBean dataBean = new CarTypeListInfo.DataBean();
        dataBean.setTypeName("普货");
        goodsTypeList.add(dataBean);
        SpCarTypeAdapter goodsTypeAdapter = new SpCarTypeAdapter(getContext(), goodsTypeList);
        sp_goodstype.setAdapter(goodsTypeAdapter);
        sp_goodstype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setCarTypeSpinner() {
        carTypeList = new ArrayList();
        carTypeAdapter = new SpCarTypeAdapter(getContext(), carTypeList);
        sp_cartype.setAdapter(carTypeAdapter);
        sp_cartype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                carTypeID = carTypeList.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getAllCarType() {
        RequestParamsFM headParams = new RequestParamsFM();
        headParams.put("X-AUTH-TOKEN", MyApplication.userToken);
        HttpOkhUtils.getInstance().doGetWithOnlyHeader(NetConfig.CARTYPE, headParams, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {

            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code == 200) {
                    Gson gson = new Gson();
                    CarTypeListInfo carTypeListInfo = gson.fromJson(resbody, CarTypeListInfo.class);
                    if (carTypeListInfo.isOk()) {
                        carTypeList.clear();
                        carTypeList.addAll(carTypeListInfo.getData());
                        carTypeAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
