package com.bt.smart.cargo_owner.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bt.smart.cargo_owner.fragment.user.OrderListFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * @创建者 AndyYan
 * @创建时间 2018/5/26 21:31
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class MyPagerAdapter extends FragmentPagerAdapter {
    private List<OrderListFragment> mFragmentList;

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setFragments(ArrayList<OrderListFragment> fragments) {
        mFragmentList = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = mFragmentList.get(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
