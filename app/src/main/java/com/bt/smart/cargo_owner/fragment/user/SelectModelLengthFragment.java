package com.bt.smart.cargo_owner.fragment.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bt.smart.cargo_owner.MyApplication;
import com.bt.smart.cargo_owner.NetConfig;
import com.bt.smart.cargo_owner.R;
import com.bt.smart.cargo_owner.adapter.RecyPlaceAdapter;
import com.bt.smart.cargo_owner.fragment.home.SupplyGoodsFragment;
import com.bt.smart.cargo_owner.messageInfo.ChioceAdapterContentInfo;
import com.bt.smart.cargo_owner.messageInfo.TsTypeInfo;
import com.bt.smart.cargo_owner.utils.HttpOkhUtils;
import com.bt.smart.cargo_owner.utils.MyTextUtils;
import com.bt.smart.cargo_owner.utils.MyFragmentManagerUtil;
import com.bt.smart.cargo_owner.utils.RequestParamsFM;
import com.bt.smart.cargo_owner.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * @创建者 AndyYan
 * @创建时间 2019/1/10 21:17
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class SelectModelLengthFragment extends Fragment implements View.OnClickListener {
    private View                           mRootView;
    private TextView                       tv_title;
    private ImageView                      img_back;
    private RecyclerView                   recy_length;
    private RecyclerView                   recy_model;
    private EditText                       et_length;
    private TextView                       tv_sureLength;
    private TextView                       tv_clear;
    private TextView                       tv_sure;
    private List<ChioceAdapterContentInfo> mLengthData;
    private List<ChioceAdapterContentInfo> mModelData;
    private RecyPlaceAdapter               placeAdapter;
    private RecyPlaceAdapter               modelAdapter;
    private SupplyGoodsFragment            supplyGoodsFragment;
    private static String TAG = "SelectModelLengthFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = LayoutInflater.from(getActivity()).inflate(R.layout.frame_select_model_length, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        img_back = mRootView.findViewById(R.id.img_back_a);
        tv_title = mRootView.findViewById(R.id.tv_title);
        recy_length = mRootView.findViewById(R.id.recy_length);
        et_length = mRootView.findViewById(R.id.et_length);
        tv_sureLength = mRootView.findViewById(R.id.tv_sureLength);
        recy_model = mRootView.findViewById(R.id.recy_model);
        tv_clear = mRootView.findViewById(R.id.tv_clear);
        tv_sure = mRootView.findViewById(R.id.tv_sure);

    }

    private void initData() {
        img_back.setVisibility(View.VISIBLE);
        tv_title.setText("车长车型");
        //设置车长数据
        setLengthData();
        //设置车型数据
        setModelData();

        img_back.setOnClickListener(this);
        tv_sureLength.setOnClickListener(this);
        tv_clear.setOnClickListener(this);
        tv_sure.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back_a:
                MyFragmentManagerUtil.closeTopFragment(this);
                break;
            case R.id.tv_sureLength://其他车长
                //添加其他车长到列表
                addOtherLength();
                break;
            case R.id.tv_clear://清空已选条件
                clearALLSelectData();
                break;
            case R.id.tv_sure://确定已选条件
                //传递选择条件、关闭页面
                makeSureTerm();
                break;
        }
    }

    private void setLengthData() {
        mLengthData = new ArrayList();
        RequestParamsFM headparam = new RequestParamsFM();
        headparam.put("X-AUTH-TOKEN", MyApplication.userToken);
        HttpOkhUtils.getInstance().doGetWithOnlyHeader(NetConfig.TSTYPE + "/2c90b4bf6c1ccde9016c1cdb66db000c", headparam, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                Log.i(TAG,"网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if(code!=200){
                    Log.i(TAG,"访问异常"+code);
                }
                Gson gson = new Gson();
                TsTypeInfo tsTypeInfo = gson.fromJson(resbody,TsTypeInfo.class);
                for(TsTypeInfo.DataBean bean : tsTypeInfo.getData()){
                    ChioceAdapterContentInfo contentInfo = new ChioceAdapterContentInfo();
                    contentInfo.setId(bean.getTypecode());
                    contentInfo.setCont(bean.getTypename());
                    contentInfo.setChioce(false);
                    mLengthData.add(contentInfo);
                }
                recy_length.setLayoutManager(new GridLayoutManager(getContext(), 4));
                placeAdapter = new RecyPlaceAdapter(R.layout.adpter_pop_city_place, getContext(), mLengthData);
                recy_length.setAdapter(placeAdapter);
                placeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        mLengthData.get(position).setChioce(!mLengthData.get(position).isChioce());
                        if (position == 0) {
                            for (ChioceAdapterContentInfo bean : mLengthData) {
                                bean.setChioce(false);
                            }
                            mLengthData.get(0).setChioce(true);
                        } else {
                            mLengthData.get(0).setChioce(false);
                        }
                        placeAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void setModelData() {
        mModelData = new ArrayList();
        RequestParamsFM headparam = new RequestParamsFM();
        headparam.put("X-AUTH-TOKEN", MyApplication.userToken);
        HttpOkhUtils.getInstance().doGetWithOnlyHeader(NetConfig.TSTYPE + "/2c90b4bf6c1ccde9016c1cdb2c4f000a", headparam, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                Log.i(TAG,"网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if(code!=200){
                    Log.i(TAG,"访问异常"+code);
                }
                Gson gson = new Gson();
                TsTypeInfo tsTypeInfo = gson.fromJson(resbody,TsTypeInfo.class);
                for(TsTypeInfo.DataBean bean : tsTypeInfo.getData()){
                    ChioceAdapterContentInfo contentInfo = new ChioceAdapterContentInfo();
                    contentInfo.setId(bean.getTypecode());
                    contentInfo.setCont(bean.getTypename());
                    contentInfo.setChioce(false);
                    mModelData.add(contentInfo);
                }
                recy_model.setLayoutManager(new GridLayoutManager(getContext(), 4));
                modelAdapter = new RecyPlaceAdapter(R.layout.adpter_pop_city_place, getContext(), mModelData);
                recy_model.setAdapter(modelAdapter);
                modelAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        mModelData.get(position).setChioce(!mModelData.get(position).isChioce());
                        if (position == 0) {
                            for (ChioceAdapterContentInfo bean : mModelData) {
                                bean.setChioce(false);
                            }
                            mModelData.get(0).setChioce(true);
                        } else {
                            mModelData.get(0).setChioce(false);
                        }
                        modelAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void clearALLSelectData() {
        for (ChioceAdapterContentInfo bean : mLengthData) {
            bean.setChioce(false);
        }
        placeAdapter.notifyDataSetChanged();
        for (ChioceAdapterContentInfo bean : mModelData) {
            bean.setChioce(false);
        }
        modelAdapter.notifyDataSetChanged();
    }

    private void addOtherLength() {
        if (!MyTextUtils.isEditTextEmpty(et_length, "请输入其他车长")) {
            String content = MyTextUtils.getEditTextContent(et_length);
            ChioceAdapterContentInfo contentInfo = new ChioceAdapterContentInfo();
            contentInfo.setCont(content + "米");
            contentInfo.setChioce(true);
            mLengthData.add(contentInfo);
            placeAdapter.notifyDataSetChanged();
        }
    }

    private boolean isChoiceLength;
    private boolean isChoiceModel;

    private void makeSureTerm() {
        for (ChioceAdapterContentInfo bean : mLengthData) {
            if (bean.isChioce()) {
                isChoiceLength = true;
            }
        }
        for (ChioceAdapterContentInfo bean : mModelData) {
            if (bean.isChioce()) {
                isChoiceModel = true;
            }
        }
        if (!isChoiceLength) {
            ToastUtils.showToast(getContext(), "请选择车长");
            return;
        }
        if (!isChoiceModel) {
            ToastUtils.showToast(getContext(), "请选择车型");
            return;
        }
        supplyGoodsFragment.setChioceTerm(mLengthData, mModelData);
        MyFragmentManagerUtil.closeTopFragment(this);
    }

    public void setTopFragment(SupplyGoodsFragment fragment) {
        supplyGoodsFragment = fragment;
    }
}
