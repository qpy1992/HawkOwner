package com.bt.smart.cargo_owner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.bt.smart.cargo_owner.R;
import com.bt.smart.cargo_owner.messageInfo.OrderDetailInfo;
import java.util.List;

public class OrderGoodsAdapter extends BaseAdapter {
    Context mContext;
    List<OrderDetailInfo.DataBean.OrdergoodsBean> list;

    public OrderGoodsAdapter(Context context, List<OrderDetailInfo.DataBean.OrdergoodsBean> list){
        this.mContext = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if(view==null){
            holder = new Holder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_order_goods,null);
            holder.tv_goods_name = view.findViewById(R.id.tv_goods_name);
            holder.tv_goods_wei = view.findViewById(R.id.tv_goods_wei);
            holder.tv_goods_vol = view.findViewById(R.id.tv_goods_vol);
            view.setTag(holder);
        }else{
            holder = (Holder)view.getTag();
        }
        holder.tv_goods_name.setText(list.get(i).getGoodsName());
        holder.tv_goods_wei.setText(list.get(i).getGoodsWeight()+"kg");
        holder.tv_goods_vol.setText(list.get(i).getGoodsSpace()+"m³");
        return view;
    }

    class Holder{
        TextView tv_goods_name,tv_goods_wei,tv_goods_vol;
    }
}
