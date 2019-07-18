package com.bt.smart.cargo_owner.fragment.sameDay;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bt.smart.cargo_owner.MyApplication;
import com.bt.smart.cargo_owner.NetConfig;
import com.bt.smart.cargo_owner.R;
import com.bt.smart.cargo_owner.adapter.DriverOfferAdapter;
import com.bt.smart.cargo_owner.messageInfo.OfferListInfo;
import com.bt.smart.cargo_owner.messageInfo.OrderDetailInfo;
import com.bt.smart.cargo_owner.messageInfo.SignInfo;
import com.bt.smart.cargo_owner.utils.CommonUtil;
import com.bt.smart.cargo_owner.utils.GlideLoaderUtil;
import com.bt.smart.cargo_owner.utils.HttpOkhUtils;
import com.bt.smart.cargo_owner.utils.MyFragmentManagerUtil;
import com.bt.smart.cargo_owner.utils.PopupOpenHelper;
import com.bt.smart.cargo_owner.utils.ProgressDialogUtil;
import com.bt.smart.cargo_owner.utils.RequestParamsFM;
import com.bt.smart.cargo_owner.utils.ShowCallUtil;
import com.bt.smart.cargo_owner.utils.ToastUtils;
import com.bt.smart.cargo_owner.viewmodel.MyListView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * @创建者 AndyYan
 * @创建时间 2019/1/8 21:35
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class OrderDetailFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {
    private View mRootView;
    private ImageView img_back, img_empty, iv_l1, iv_l2, iv_l3, iv_rece;
    private LinearLayout ll_load, ll_rece;
    private TextView tv_title, tv_orderNum, tv_place, tv_goodsname, tv_carType, tv_name, tv_fhPlace, tv_phone, tv_cont, tv_sign, tv_inter;
    private String orderID;//订单id
    private int orderType = -1;//订单类别
    private String mOrder_no;//订单号
    private Button moveBtn;
    private OrderDetailInfo orderDetailInfo;//订单详情
    private List<OfferListInfo.DataBean> mOfferListInfo;//司机报价列表

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = LayoutInflater.from(getActivity()).inflate(R.layout.frame_order_detail, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        img_back = mRootView.findViewById(R.id.img_back);
        img_empty = mRootView.findViewById(R.id.img_empty);
        tv_title = mRootView.findViewById(R.id.tv_title);
        tv_orderNum = mRootView.findViewById(R.id.tv_orderNum);
        tv_place = mRootView.findViewById(R.id.tv_place);
        tv_goodsname = mRootView.findViewById(R.id.tv_goodsname);
        tv_carType = mRootView.findViewById(R.id.tv_carType);
        tv_name = mRootView.findViewById(R.id.tv_name);
        tv_fhPlace = mRootView.findViewById(R.id.tv_fhPlace);
        tv_phone = mRootView.findViewById(R.id.tv_phone);
        tv_cont = mRootView.findViewById(R.id.tv_cont);
        tv_sign = mRootView.findViewById(R.id.tv_sign);
        tv_inter = mRootView.findViewById(R.id.tv_inter);
        iv_l1 = mRootView.findViewById(R.id.iv_l1);
        iv_l2 = mRootView.findViewById(R.id.iv_l2);
        iv_l3 = mRootView.findViewById(R.id.iv_l3);
        iv_rece = mRootView.findViewById(R.id.iv_rece);
        ll_load = mRootView.findViewById(R.id.ll_load);
        ll_rece = mRootView.findViewById(R.id.ll_rece);
        moveBtn = mRootView.findViewById(R.id.moveBtn);
    }

    private void initData() {
        tv_title.setText("货源详情");
        img_back.setVisibility(View.VISIBLE);
        orderID = getActivity().getIntent().getStringExtra("orderID");
        orderType = getActivity().getIntent().getIntExtra("orderType", -1);
        if (0 == orderType) {
            //显示报价列表按钮
            moveBtn.setVisibility(View.VISIBLE);
            setMoveListener();
            moveBtn.setOnClickListener(this);
        }
        if (orderType == 1) {
            tv_sign.setText("签署协议");
            tv_sign.setVisibility(View.VISIBLE);
        } else if (orderType == 5) {
            tv_sign.setVisibility(View.VISIBLE);
            tv_sign.setText("回单确认");
        } else if (orderType == 6) {
            tv_sign.setVisibility(View.VISIBLE);
            tv_sign.setText("取消确认");
        } else if (orderType == 8) {
            tv_sign.setVisibility(View.VISIBLE);
            tv_sign.setText("确认支付");
        }
        //获取货源详情
        getOrderDetail();
        img_back.setOnClickListener(this);
        iv_l1.setOnClickListener(this);
        iv_l2.setOnClickListener(this);
        iv_l3.setOnClickListener(this);
        iv_rece.setOnClickListener(this);
        tv_cont.setOnClickListener(this);
        tv_sign.setOnClickListener(this);
        img_empty.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_empty:
                //获取货源详情
                getOrderDetail();
                break;
            case R.id.img_back:
                MyFragmentManagerUtil.closeFragmentOnAct(this);
                break;
            case R.id.tv_cont://联系货主
                ShowCallUtil.showCallDialog(getContext(), orderDetailInfo.getData().getFh_telephone());
                break;
            case R.id.moveBtn:
                //显示司机报价类表
                showDriverPrice();
                break;
            case R.id.iv_l1:
                expandImg(iv_l1);
                break;
            case R.id.iv_l2:
                expandImg(iv_l2);
                break;
            case R.id.iv_l3:
                expandImg(iv_l3);
                break;
            case R.id.iv_rece:
                expandImg(iv_rece);
                break;
            case R.id.tv_sign:
                if (orderType == 1) {
                    //签署协议
                    signOrder();
                } else if (orderType == 5) {
                    //回单确认
                    sureReceipt();
                } else if (orderType == 6) {
                    //取消确认

                } else if (orderType == 8) {
                    //确认支付
                    surePay();
                }
                break;
            default:
                break;
        }
    }

    private void surePay() {
        RequestParamsFM headParams = new RequestParamsFM();
        headParams.put("X-AUTH-TOKEN", MyApplication.userToken);
        RequestParamsFM params = new RequestParamsFM();
        params.put("id", orderID);
        ProgressDialogUtil.startShow(getContext(), "正在确认支付...");
        HttpOkhUtils.getInstance().doPostWithHeader(NetConfig.PAY, headParams, params, new HttpOkhUtils.HttpCallBack() {
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
                SignInfo signInfo = gson.fromJson(resbody, SignInfo.class);
                ToastUtils.showToast(getContext(), signInfo.getMessage());
                if (signInfo.isOk()) {

                }
            }
        });
    }

    private void sureReceipt() {
        RequestParamsFM headParams = new RequestParamsFM();
        headParams.put("X-AUTH-TOKEN", MyApplication.userToken);
        ProgressDialogUtil.startShow(getContext(), "正在确认回单...");
        HttpOkhUtils.getInstance().doGetWithOnlyHeader(NetConfig.HUIDAN+"/"+orderID, headParams, new HttpOkhUtils.HttpCallBack() {
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
                SignInfo signInfo = gson.fromJson(resbody, SignInfo.class);
                ToastUtils.showToast(getContext(), signInfo.getMessage());
                if (signInfo.isOk()) {

                }
            }
        });
    }

    private void signOrder() {
        RequestParamsFM headParams = new RequestParamsFM();
        headParams.put("X-AUTH-TOKEN", MyApplication.userToken);
        RequestParamsFM params = new RequestParamsFM();
        params.put("id", MyApplication.userID);
        params.put("orderid", orderID);
        HttpOkhUtils.getInstance().doPostByUrl(NetConfig.HUOZHU, headParams, "jsonstr", params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ProgressDialogUtil.hideDialog();
                ToastUtils.showToast(getContext(), "网络连接错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                ProgressDialogUtil.hideDialog();
                if (200 != code) {
                    ToastUtils.showToast(getContext(), "网络连接错误");
                }
                Gson gson = new Gson();
                SignInfo signInfo = gson.fromJson(resbody, SignInfo.class);
                ToastUtils.showToast(getContext(), signInfo.getMessage());
                if (signInfo.isOk()) {
                    getActivity().finish();
                }
            }
        });
    }

    private void setMoveListener() {
        //通过Resources获取屏幕宽高
        DisplayMetrics dm = getResources().getDisplayMetrics();
        winHeigth = dm.heightPixels;
        winWidh = dm.widthPixels;
        //获取状态栏高度
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        winHeigth = winHeigth - result;
        moveBtn.setOnTouchListener(this);
    }

    private void showDriverPrice() {
        ProgressDialogUtil.startShow(getContext(), "正在获取报价列表...");
        RequestParamsFM headParams = new RequestParamsFM();
        headParams.put("X-AUTH-TOKEN", MyApplication.userToken);
        RequestParamsFM params = new RequestParamsFM();
        params.put("order_id", orderID);
        HttpOkhUtils.getInstance().doGetWithHeadParams(NetConfig.DRIVERPRICELIST, headParams, params, new HttpOkhUtils.HttpCallBack() {
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
                OfferListInfo offerListInfo = gson.fromJson(resbody, OfferListInfo.class);
                if (offerListInfo.isOk()) {
                    if (offerListInfo.getData().size() > 0) {
                        if (null == mOfferListInfo) {
                            mOfferListInfo = new ArrayList();
                        } else {
                            mOfferListInfo.clear();
                        }
                        mOfferListInfo.addAll(offerListInfo.getData());
                        //弹出pop显示报价列表
                        showPopPrice();
                    }
                }
            }
        });
    }

    private void showPopPrice() {
        PopupOpenHelper openHelper = new PopupOpenHelper(getContext(), moveBtn, R.layout.pop_driver_price);
        openHelper.openPopupWindow(true, Gravity.BOTTOM);
        openHelper.setOnPopupViewClick(new PopupOpenHelper.ViewClickListener() {
            @Override
            public void onViewListener(PopupWindow popupWindow, View inflateView) {
                MyListView mlv_offer = inflateView.findViewById(R.id.mlv_offer);
                DriverOfferAdapter offerAdapter = new DriverOfferAdapter(getContext(), mOfferListInfo, orderDetailInfo);
                mlv_offer.setAdapter(offerAdapter);
            }
        });
    }

    private void getOrderDetail() {
        ProgressDialogUtil.startShow(getContext(), "正在获取详情...");
        RequestParamsFM headParams = new RequestParamsFM();
        headParams.put("X-AUTH-TOKEN", MyApplication.userToken);
        HttpOkhUtils.getInstance().doGetWithOnlyHeader(NetConfig.ALL_ORDER_DETAIL + "/" + orderID, headParams, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ProgressDialogUtil.hideDialog();
                ToastUtils.showToast(getContext(), "网络连接错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                ProgressDialogUtil.hideDialog();
                img_empty.setVisibility(View.VISIBLE);
                if (code != 200) {
                    ToastUtils.showToast(getContext(), "网络错误" + code);
                    return;
                }
                Gson gson = new Gson();
                orderDetailInfo = gson.fromJson(resbody, OrderDetailInfo.class);
                ToastUtils.showToast(getContext(), orderDetailInfo.getMessage());
                if (orderDetailInfo.isOk()) {
                    img_empty.setVisibility(View.GONE);
                    mOrder_no = orderDetailInfo.getData().getOrder_no();
                    tv_orderNum.setText(mOrder_no);
                    tv_place.setText(orderDetailInfo.getData().getOrigin() + "  →  " + orderDetailInfo.getData().getDestination());
                    tv_goodsname.setText(orderDetailInfo.getData().getGoodsname() + " " + orderDetailInfo.getData().getCartype() + " " + orderDetailInfo.getData().getSh_address());
                    tv_carType.setText(orderDetailInfo.getData().getFh_address());
                    tv_name.setText(orderDetailInfo.getData().getFh_name());
                    tv_fhPlace.setText(orderDetailInfo.getData().getFh_address());
                    tv_phone.setText(orderDetailInfo.getData().getFh_telephone());
                    tv_inter.setText(orderDetailInfo.getData().getTime_interval());
                    String getloadUrl = orderDetailInfo.getData().getFloadpics();
                    String getreceUrl = orderDetailInfo.getData().getFrecepics();
                    if (CommonUtil.isNotEmpty(getloadUrl)) {
                        ll_load.setVisibility(View.VISIBLE);
                        String[] s = getloadUrl.split(",");
                        switch (s.length) {
                            case 1:
                                iv_l1.setVisibility(View.VISIBLE);
                                GlideLoaderUtil.showImgWithIcon(getContext(), NetConfig.IMG_HEAD + s[0], R.drawable.iman, R.drawable.iman, iv_l1);
                                break;
                            case 2:
                                iv_l1.setVisibility(View.VISIBLE);
                                iv_l2.setVisibility(View.VISIBLE);
                                GlideLoaderUtil.showImgWithIcon(getContext(), NetConfig.IMG_HEAD + s[0], R.drawable.iman, R.drawable.iman, iv_l1);
                                GlideLoaderUtil.showImgWithIcon(getContext(), NetConfig.IMG_HEAD + s[1], R.drawable.iman, R.drawable.iman, iv_l2);
                                break;
                            case 3:
                                iv_l1.setVisibility(View.VISIBLE);
                                iv_l2.setVisibility(View.VISIBLE);
                                iv_l3.setVisibility(View.VISIBLE);
                                GlideLoaderUtil.showImgWithIcon(getContext(), NetConfig.IMG_HEAD + s[0], R.drawable.iman, R.drawable.iman, iv_l1);
                                GlideLoaderUtil.showImgWithIcon(getContext(), NetConfig.IMG_HEAD + s[1], R.drawable.iman, R.drawable.iman, iv_l2);
                                GlideLoaderUtil.showImgWithIcon(getContext(), NetConfig.IMG_HEAD + s[2], R.drawable.iman, R.drawable.iman, iv_l3);
                                break;
                        }
                    }
                    if (CommonUtil.isNotEmpty(getreceUrl)) {
                        ll_rece.setVisibility(View.VISIBLE);
                        iv_rece.setVisibility(View.VISIBLE);
                        GlideLoaderUtil.showImgWithIcon(getContext(), NetConfig.IMG_HEAD + getreceUrl, R.drawable.iman, R.drawable.iman, iv_rece);
                    }
                }
            }
        });
    }

    protected void expandImg(ImageView iv) {
        iv.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(iv.getDrawingCache());
        iv.setDrawingCacheEnabled(false);
        ImageView iv1 = new ImageView(getActivity());
        iv1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        iv1.setImageBitmap(bitmap);
        final AlertDialog builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen).create();
        builder.show();
        Window window = builder.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setContentView(iv1);
        iv1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                builder.cancel();
            }
        });
    }


    private int downLeft;
    private int downTop;
    private int moveLeft;
    private int moveTop;
    private int viewLeft;
    private int viewTop;

    private int finalLeft;
    private int finalTop;
    private int finalRight;
    private int finalBottom;

    private int winWidh;
    private int winHeigth;
    private long touchTime;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.moveBtn:
                // 获取Action
                int ea = event.getAction();
                switch (ea) {
                    case MotionEvent.ACTION_DOWN: // 按下
                        touchTime = System.currentTimeMillis();

                        viewLeft = v.getLeft();//获取按钮的左坐标(在屏幕上的位置)
                        viewTop = v.getTop();//获取按钮的上坐标

                        downLeft = (int) event.getRawX();//获取点击位置(距离屏幕原点横坐标)
                        downTop = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE: // 移动
                        // 移动中动态设置位置
                        moveLeft = (int) event.getRawX();
                        moveTop = (int) event.getRawY();

                        //view最终左坐标(移动后)
                        finalLeft = viewLeft + (moveLeft - downLeft);
                        //view最终上坐标
                        finalTop = viewTop + (moveTop - downTop);

                        //判断，不使按钮移出屏幕外
                        if (finalLeft <= 0) {
                            finalLeft = 0;
                        }
                        if (finalTop <= 0) {
                            finalTop = 0;
                        }
                        finalRight = finalLeft + v.getWidth();
                        finalBottom = finalTop + v.getHeight();
                        if (finalRight >= winWidh) {
                            finalRight = winWidh;
                            finalLeft = finalRight - v.getWidth();
                        }
                        if (finalBottom >= winHeigth) {
                            finalBottom = winHeigth;
                            finalTop = finalBottom - v.getHeight();
                        }

                        //重置按钮的位置
                        v.layout(finalLeft, finalTop, finalRight, finalBottom);
                        //重新设置初始移动位置
                        downLeft = moveLeft;
                        downTop = moveTop;
                        //设置移动后，获取view的左坐标
                        viewLeft = v.getLeft();
                        viewTop = v.getTop();
                        break;
                    case MotionEvent.ACTION_UP: //抬起
                        long upTime = System.currentTimeMillis() - touchTime;
                        if (upTime < 100) {
                            return false;
                        } else {
                            return true;
                        }
                }
                break;
        }
        return false;
    }
}
