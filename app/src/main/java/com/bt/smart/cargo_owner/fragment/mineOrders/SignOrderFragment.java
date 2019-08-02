package com.bt.smart.cargo_owner.fragment.mineOrders;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bt.smart.cargo_owner.R;
import com.bt.smart.cargo_owner.messageInfo.OfferListInfo;
import com.bt.smart.cargo_owner.messageInfo.OrderDetailInfo;
import com.bt.smart.cargo_owner.utils.MyFragmentManagerUtil;

public class SignOrderFragment extends Fragment implements View.OnClickListener {
    private View mRootView;
    private ImageView img_back, img_sign, img_hzHead, img_sjHead;
    private LinearLayout ll_load, ll_rece;
    private TextView tv_title, tv_fhTime, tv_orderNum, tv_place, tv_goodsname, tv_carType, tv_hzName, tv_fhPlace, tv_company, tv_hzPhone, tv_sign, tv_sjName, tv_sjPhone;
    private String orderID;//订单id
    private OrderDetailInfo orderDetailInfo;
    private OfferListInfo.DataBean driverInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_sign_order, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        img_back = mRootView.findViewById(R.id.img_back);
        tv_title = mRootView.findViewById(R.id.tv_title);
        tv_fhTime = mRootView.findViewById(R.id.tv_fhTime);
        tv_orderNum = mRootView.findViewById(R.id.tv_orderNum);
        tv_place = mRootView.findViewById(R.id.tv_place);
        tv_goodsname = mRootView.findViewById(R.id.tv_goodsname);
        tv_carType = mRootView.findViewById(R.id.tv_carType);
        img_hzHead = mRootView.findViewById(R.id.img_hzHead);
        tv_hzName = mRootView.findViewById(R.id.tv_hzName);
        tv_company = mRootView.findViewById(R.id.tv_company);
        tv_fhPlace = mRootView.findViewById(R.id.tv_fhPlace);
        tv_hzPhone = mRootView.findViewById(R.id.tv_hzPhone);
        img_sjHead = mRootView.findViewById(R.id.img_sjHead);
        tv_sjName = mRootView.findViewById(R.id.tv_sjName);
        tv_sjPhone = mRootView.findViewById(R.id.tv_sjPhone);
        img_sign = mRootView.findViewById(R.id.img_sign);
        tv_sign = mRootView.findViewById(R.id.tv_sign);

    }

    private void initData() {
        tv_title.setText("货物运输交易协议");
        img_back.setVisibility(View.VISIBLE);
        tv_sign.setVisibility(View.VISIBLE);
        img_back.setOnClickListener(this);
        tv_sign.setOnClickListener(this);
        //填充数据
        setData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                MyFragmentManagerUtil.closeTopFragment(this);
                break;
            case R.id.tv_sign:
                //货主发起签署协议

                break;
            default:
                break;
        }
    }

    private void setData() {
        tv_orderNum.setText(orderDetailInfo.getData().getOrder_no());
        tv_place.setText(orderDetailInfo.getData().getOrigin() + "  →  " + orderDetailInfo.getData().getDestination());
        tv_goodsname.setText(orderDetailInfo.getData().getGoodsname() + " " + orderDetailInfo.getData().getCar_type() + " " + orderDetailInfo.getData().getSh_address());
        tv_fhTime.setText("装货时间：" + orderDetailInfo.getData().getZh_time());
        tv_carType.setText(orderDetailInfo.getData().getFh_address());
        tv_hzName.setText("货主：" + orderDetailInfo.getData().getFh_name());
        tv_company.setText(orderDetailInfo.getData().getCompanyname());
        tv_fhPlace.setText(orderDetailInfo.getData().getFh_address());
        tv_hzPhone.setText(orderDetailInfo.getData().getFh_telephone());
        if (null != driverInfo) {
            tv_sjName.setText(driverInfo.getFname());
            tv_sjPhone.setText(driverInfo.getFmobile());
        }
    }

    public void setDataList(OrderDetailInfo dataBean, OfferListInfo.DataBean bean) {
        orderDetailInfo = dataBean;
        driverInfo = bean;
    }
}
