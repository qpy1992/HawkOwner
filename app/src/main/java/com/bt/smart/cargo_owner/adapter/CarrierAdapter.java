package com.bt.smart.cargo_owner.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bt.smart.cargo_owner.NetConfig;
import com.bt.smart.cargo_owner.R;
import com.bt.smart.cargo_owner.activity.homeAct.SupplyGoodsActivity;
import com.bt.smart.cargo_owner.messageInfo.CarrierInfo;
import com.bt.smart.cargo_owner.utils.GlideLoaderUtil;
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

public class CarrierAdapter extends BaseQuickAdapter<CarrierInfo.DataBean, BaseViewHolder> {
    private Context mContext;

    public CarrierAdapter(int layoutResId, Context context, List<CarrierInfo.DataBean> data) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final CarrierInfo.DataBean item) {
        helper.setText(R.id.tv_name_carrier,item.getFname());
        helper.setText(R.id.tv_mobile_carrier,item.getFmobile());
        ImageView img_headpic = helper.getView(R.id.img_headpic);
        GlideLoaderUtil.showImgWithIcon(mContext, NetConfig.IMG_HEAD + item.getFphoto(), R.drawable.iman, R.drawable.iman, img_headpic);
    }

}
