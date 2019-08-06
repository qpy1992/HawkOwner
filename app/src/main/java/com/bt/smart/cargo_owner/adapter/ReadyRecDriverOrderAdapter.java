package com.bt.smart.cargo_owner.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bt.smart.cargo_owner.R;
import com.bt.smart.cargo_owner.activity.homeAct.SupplyGoodsActivity;
import com.bt.smart.cargo_owner.messageInfo.ReadyRecOrderInfo;
import com.bt.smart.cargo_owner.utils.MyAlertDialogHelper;
import com.bt.smart.cargo_owner.utils.ShowCallUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/11/20 14:07
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class ReadyRecDriverOrderAdapter extends BaseQuickAdapter<ReadyRecOrderInfo.DataBean, BaseViewHolder> {
    private Context mContext;
    private String  selecPhone;//选择的电话

    public ReadyRecDriverOrderAdapter(int layoutResId, Context context, List<ReadyRecOrderInfo.DataBean> data) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final ReadyRecOrderInfo.DataBean item) {
        helper.setText(R.id.tv_orderno, item.getOrder_no());
        helper.setText(R.id.tv_place, item.getOrigin().replaceAll("市"," ").replaceAll("区"," ") + "  →  " + item.getDestination().replaceAll("市"," ").replaceAll("区"," "));
        helper.setText(R.id.tv_goodsname, item.getName()+" "+item.getCar_type()+"|"+item.getCar_length());
        helper.setText(R.id.tv_loadtime, "装货时间：" + item.getZhTime());
        helper.setText(R.id.tv_xhtime, "卸货时间："+item.getXhTime());
        helper.setText(R.id.tv_mark,item.getFnote());
        ImageView iv_edit = helper.getView(R.id.iv_edit);
        ImageView iv_type = helper.getView(R.id.iv_type);
        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转修改界面
                Intent intent = new Intent(mContext, SupplyGoodsActivity.class);
                intent.putExtra("orderId",item.getId());
                intent.putExtra("fstatus",item.getFstatus());
                mContext.startActivity(intent);
            }
        });
        if(item.getIs_box().equals("0")){
            iv_type.setImageResource(R.drawable.pinche);
        }else{
            iv_type.setImageResource(R.drawable.zhengche);
        }
    }

}
