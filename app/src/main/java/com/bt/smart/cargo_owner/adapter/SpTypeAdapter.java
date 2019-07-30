package com.bt.smart.cargo_owner.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bt.smart.cargo_owner.R;
import com.bt.smart.cargo_owner.messageInfo.CarTypeListInfo;
import com.bt.smart.cargo_owner.messageInfo.TypeInfo;

import java.util.List;

public class SpTypeAdapter extends BaseAdapter {
    private Context mContext;
    private List<TypeInfo.DataBean> mList;

    public SpTypeAdapter(Context context, List<TypeInfo.DataBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final MyViewHolder viewholder;
        if (null == view) {
            viewholder = new MyViewHolder();
            view = View.inflate(mContext, R.layout.adapter_sp_cartype, null);
            viewholder.tv_title = view.findViewById(R.id.tv_title);
            view.setTag(viewholder);
        } else {
            viewholder = (MyViewHolder) view.getTag();
        }
        viewholder.tv_title.setText(mList.get(i).getName());
        return view;
    }


    private class MyViewHolder {
        TextView tv_title;
    }
}
