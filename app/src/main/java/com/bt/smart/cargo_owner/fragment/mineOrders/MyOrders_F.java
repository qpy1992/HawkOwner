package com.bt.smart.cargo_owner.fragment.mineOrders;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bt.smart.cargo_owner.R;
import com.bt.smart.cargo_owner.adapter.MyPagerAdapter;
import com.bt.smart.cargo_owner.fragment.user.OrderListFragment;
import com.bt.smart.cargo_owner.viewmodel.MyFixedViewpager;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/8/18 9:58
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class MyOrders_F extends Fragment {
    private View mRootView;
    private TextView tv_title;
    private TabLayout tablayout;
    private MyFixedViewpager view_pager;
    private List<OrderListFragment> fragmentsList;
    private MyPagerAdapter myPagerAdapter;
    private List<String> contsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = LayoutInflater.from(getActivity()).inflate(R.layout.serv_apply_f, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        tv_title = mRootView.findViewById(R.id.tv_title);
        tablayout = mRootView.findViewById(R.id.tablayout);
        view_pager = mRootView.findViewById(R.id.view_pager);
    }

    private void initData() {
        tv_title.setText("订单列表");
        contsList = new ArrayList<>();
        contsList.add("已发布");//0,6,7
        contsList.add("待运输");//1,2
        contsList.add("运输中");//3,4
        contsList.add("待支付");//5,8
        contsList.add("已结单");//9,10
        fragmentsList = new ArrayList<>();
        OrderListFragment orderListFgt0 = new OrderListFragment();
        orderListFgt0.setType(0);
        fragmentsList.add(orderListFgt0);
        OrderListFragment orderListFgt1 = new OrderListFragment();
        orderListFgt1.setType(1);
        fragmentsList.add(orderListFgt1);
        OrderListFragment orderListFgt2 = new OrderListFragment();
        orderListFgt2.setType(2);
        fragmentsList.add(orderListFgt2);
        OrderListFragment orderListFgt3 = new OrderListFragment();
        orderListFgt3.setType(3);
        fragmentsList.add(orderListFgt3);
        OrderListFragment orderListFgt4 = new OrderListFragment();
        orderListFgt4.setType(4);
        fragmentsList.add(orderListFgt4);
        // 创建ViewPager适配器
        myPagerAdapter = new MyPagerAdapter(getFragmentManager());//getChildFragmentManager()
        myPagerAdapter.setFragments((ArrayList<OrderListFragment>) fragmentsList);
        // 给ViewPager设置适配器
        view_pager.setAdapter(myPagerAdapter);
        //view_pager.setCanScroll(true);
        tablayout.setupWithViewPager(view_pager);
        for (int i = 0; i < contsList.size(); i++) {
            tablayout.getTabAt(i).setText(contsList.get(i));
        }
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                fragmentsList.get(tab.getPosition()).refreshData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                fragmentsList.get(tab.getPosition()).refreshData();
            }
        });
        tablayout.getTabAt(0).select();
        view_pager.setCurrentItem(0);
    }
}
