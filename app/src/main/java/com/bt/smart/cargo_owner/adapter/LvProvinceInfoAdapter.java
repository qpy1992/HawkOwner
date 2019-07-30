package com.bt.smart.cargo_owner.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.bt.smart.cargo_owner.R;
import com.bt.smart.cargo_owner.messageInfo.ShengDataInfo;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/11/9 9:42
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class LvProvinceInfoAdapter extends BaseAdapter {
    private List<ShengDataInfo.DataBean> mList;
    private Context                      mContext;

    public LvProvinceInfoAdapter(Context context, List<ShengDataInfo.DataBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        MyViewholder viewholder;
        if (null == view) {
            viewholder = new MyViewholder();
            view = View.inflate(mContext, R.layout.spiner_task_kind, null);
            viewholder.tv_cont = view.findViewById(R.id.tv_cont);
            view.setTag(viewholder);
        } else {
            viewholder = (MyViewholder) view.getTag();
        }
        viewholder.tv_cont.setText(mList.get(i).getFname());

        return view;
    }

    private class MyViewholder {
        TextView tv_cont;
    }
}
