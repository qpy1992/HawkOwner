package com.bt.smart.cargo_owner.adapter;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bt.smart.cargo_owner.MyApplication;
import com.bt.smart.cargo_owner.NetConfig;
import com.bt.smart.cargo_owner.R;
import com.bt.smart.cargo_owner.fragment.mineOrders.SignOrderFragment;
import com.bt.smart.cargo_owner.messageInfo.OfferListInfo;
import com.bt.smart.cargo_owner.messageInfo.OrderDetailInfo;
import com.bt.smart.cargo_owner.messageInfo.SignInfo;
import com.bt.smart.cargo_owner.utils.HttpOkhUtils;
import com.bt.smart.cargo_owner.utils.ProgressDialogUtil;
import com.bt.smart.cargo_owner.utils.RequestParamsFM;
import com.bt.smart.cargo_owner.utils.ToastUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;

/**
 * @创建者 AndyYan
 * @创建时间 2019/1/9 8:59
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class DriverOfferAdapter extends BaseAdapter {
    private Context mContext;
    private OrderDetailInfo mOrderDetailInfo;
    private List<OfferListInfo.DataBean> mList;

    public DriverOfferAdapter(Context context, List<OfferListInfo.DataBean> list, OrderDetailInfo orderDetailInfo) {
        this.mContext = context;
        this.mList = list;
        this.mOrderDetailInfo = orderDetailInfo;
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
        final MyViewHolder viewholder;
        if (null == view) {
            viewholder = new MyViewHolder();
            view = View.inflate(mContext, R.layout.adapter_driver_offer, null);
            viewholder.tv_name = view.findViewById(R.id.tv_name);
            viewholder.tv_phone = view.findViewById(R.id.tv_phone);
            viewholder.tv_offer = view.findViewById(R.id.tv_offer);
            viewholder.tv_note = view.findViewById(R.id.tv_note);
            viewholder.tv_sure = view.findViewById(R.id.tv_sure);
            view.setTag(viewholder);
        } else {
            viewholder = (MyViewHolder) view.getTag();
        }
        viewholder.tv_name.setText(mList.get(i).getFname());
        viewholder.tv_phone.setText(mList.get(i).getFmobile());
        viewholder.tv_offer.setText("" + mList.get(i).getFfee());
        viewholder.tv_note.setText(mList.get(i).getFnote());
        viewholder.tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //确认选择
                choiceThisOne(i);
            }
        });
        return view;
    }

    private void choiceThisOne(int item) {
        //提交接受报价
        subAccept(item);
    }

    private void subAccept(final int item) {
        ProgressDialogUtil.startShow(mContext, "正在提交...");
        RequestParamsFM headParams = new RequestParamsFM();
        headParams.put("X-AUTH-TOKEN", MyApplication.userToken);
        RequestParamsFM params = new RequestParamsFM();
        params.put("order_id", mList.get(item).getOrder_id());
        params.put("driver_id", mList.get(item).getDriver_id());
        params.put("ffee", mList.get(item).getFfee());
        HttpOkhUtils.getInstance().doGetWithHeadParams(NetConfig.CONFIRMORDER, headParams, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ProgressDialogUtil.hideDialog();
                ToastUtils.showToast(mContext, "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                ProgressDialogUtil.hideDialog();
                if (200 != code) {
                    ToastUtils.showToast(mContext, "网络错误" + code);
                    return;
                }
                Gson gson = new Gson();
                SignInfo signInfo = gson.fromJson(resbody, SignInfo.class);
                ToastUtils.showToast(mContext, signInfo.getMessage());
                if (signInfo.isOk()) {
                    //弹出签署协议
                    SignOrderFragment orderDetailFt = new SignOrderFragment();
                    orderDetailFt.setDataList(mOrderDetailInfo, mList.get(item));
                    FragmentTransaction ftt = ((Activity) mContext).getFragmentManager().beginTransaction();
                    ftt.add(R.id.frame, orderDetailFt, "orderDetailFt");
                    ftt.commit();
//                    Intent intent = new Intent(mContext, SignOrderFragment.class);
//                    intent.putExtra("orderID", mList.get(item).getOrder_id());
//                    mContext.startActivity(intent);
                }
            }
        });
    }

    private class MyViewHolder {
        TextView tv_sure, tv_note, tv_offer, tv_phone, tv_name;
    }
}
