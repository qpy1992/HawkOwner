package com.bt.smart.cargo_owner.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bt.smart.cargo_owner.MainActivity;
import com.bt.smart.cargo_owner.MyApplication;
import com.bt.smart.cargo_owner.R;
import com.bt.smart.cargo_owner.activity.homeAct.SupplyGoodsActivity;
import com.bt.smart.cargo_owner.utils.MyAlertDialog;
import com.bt.smart.cargo_owner.utils.SoundPoolUtil;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/22 16:41
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class Home_F extends Fragment implements View.OnClickListener {
    private View mRootView;
    private TextView tv_msg;
    private TextView tv_ship;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = LayoutInflater.from(getActivity()).inflate(R.layout.home_f, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        tv_msg = mRootView.findViewById(R.id.tv_msg);
        tv_ship = mRootView.findViewById(R.id.tv_ship);
    }

    private void initData() {
        tv_ship.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        SoundPoolUtil.play(0);
        switch (view.getId()) {
            case R.id.tv_ship://发货
                //跳转发布货源界面
                toShipView();
                break;
        }
    }

    private void toShipView() {
        if ("1".equals(MyApplication.checkStatus)) {
            startActivity(new Intent(getContext(), SupplyGoodsActivity.class));
        } else {
            //提示还未通过审核
            new MyAlertDialog(getActivity(), MyAlertDialog.WARNING_TYPE_1).setTitleText("您还未通过审核，请先去个人中心查看审核状态!")
                    .setConfirmText("确定")
                    .setCancelText("取消")
                    .showCancelButton(false)
                    .setConfirmClickListener(new MyAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(MyAlertDialog sweetAlertDialog) {
                            //跳转个人中心
                            ((MainActivity) getActivity()).getLinear_mine().performClick();
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .setCancelClickListener(null)
                    .show();
        }
    }
}
