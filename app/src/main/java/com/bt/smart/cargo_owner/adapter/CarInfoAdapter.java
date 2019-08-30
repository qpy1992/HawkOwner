package com.bt.smart.cargo_owner.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bt.smart.cargo_owner.NetConfig;
import com.bt.smart.cargo_owner.R;
import com.bt.smart.cargo_owner.messageInfo.AllOrderListInfo;
import com.bt.smart.cargo_owner.messageInfo.CarInfo;
import com.bt.smart.cargo_owner.messageInfo.CarrierInfo;
import com.bt.smart.cargo_owner.utils.CommonUtil;
import com.bt.smart.cargo_owner.utils.GlideLoaderUtil;
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

public class CarInfoAdapter extends BaseQuickAdapter<CarInfo.CarBean, BaseViewHolder> {
    private Context mContext;

    public CarInfoAdapter(int layoutResId, Context context, List data) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final CarInfo.CarBean item) {
        helper.setText(R.id.tv_driver_name, item.getFname());
        String origin = item.getOrigin1();
        String destination = item.getDestination1();
        if(CommonUtil.isNotEmpty(item.getOrigin2())){
            origin = origin + "/" +item.getOrigin2();
        }
        if(CommonUtil.isNotEmpty(item.getOrigin3())){
            origin = origin + "/" + item.getOrigin3();
        }
        if(CommonUtil.isNotEmpty(item.getDestination2())){
            destination = destination + "/" + item.getDestination2();
        }
        if(CommonUtil.isNotEmpty(item.getDestination3())){
            destination = destination + "/" + item.getDestination3();
        }
        helper.setText(R.id.tv_origin, origin);
        helper.setText(R.id.tv_destination, destination);
        helper.setText(R.id.tv_model, item.getCar_long() + "|" + item.getCar_type());

        helper.setText(R.id.tv_adddate, "发布时间：" + item.getAdd_date());
        ImageView iv_driver = helper.getView(R.id.iv_driver);
        GlideLoaderUtil.showImgWithIcon(mContext, NetConfig.IMG_HEAD + item.getFphoto(), R.drawable.iman, R.drawable.iman, iv_driver);
        ImageView iv_call = helper.getView(R.id.iv_call_driver);
        iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowCallUtil.showCallDialog(mContext, item.getFmobile());
            }
        });
        String s = "可装重量："+item.getFweight()+"吨";
        if(CommonUtil.isNotEmpty(item.getFvolume())) {
            s = s + "  可装体积：" + item.getFvolume() + "m³";
        }
        helper.setText(R.id.tv_fweight,s);
        ImageView iv_pinzheng = helper.getView(R.id.iv_pinzheng);
        if(item.getFtype()==1){
            iv_pinzheng.setImageDrawable(mContext.getResources().getDrawable(R.drawable.zhengche));
        }else{
            iv_pinzheng.setImageDrawable(mContext.getResources().getDrawable(R.drawable.pinche));
        }
    }
}
