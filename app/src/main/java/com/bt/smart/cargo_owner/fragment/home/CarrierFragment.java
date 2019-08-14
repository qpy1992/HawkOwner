package com.bt.smart.cargo_owner.fragment.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bt.smart.cargo_owner.MyApplication;
import com.bt.smart.cargo_owner.NetConfig;
import com.bt.smart.cargo_owner.R;
import com.bt.smart.cargo_owner.adapter.CarrierAdapter;
import com.bt.smart.cargo_owner.messageInfo.CarrierInfo;
import com.bt.smart.cargo_owner.utils.HttpOkhUtils;
import com.bt.smart.cargo_owner.utils.MyFragmentManagerUtil;
import com.bt.smart.cargo_owner.utils.RequestParamsFM;
import com.bt.smart.cargo_owner.utils.SoftKeyboardUtils;
import com.bt.smart.cargo_owner.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Request;

public class CarrierFragment extends Fragment implements View.OnClickListener{
    private View mRootView;
    private ImageView img_back;
    private TextView tv_title;
    private Spinner sp_mob_carrier;
    private EditText et_carrierName;
    private Button btn_search_carrier;
    private RecyclerView lv_carrier;
    private SupplyGoodsFragment supplyGoodsFragment;
    private int sType = 0;//搜索条件，0_名称，1_电话，默认姓名搜索
    private static String TAG = "CarrierFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_carrier,null);
        initView();
        initData();
        return mRootView;
    }

    protected void initView(){
        img_back = mRootView.findViewById(R.id.img_back_a);
        tv_title = mRootView.findViewById(R.id.tv_title);
        sp_mob_carrier = mRootView.findViewById(R.id.sp_mob_carrier);
        et_carrierName = mRootView.findViewById(R.id.et_carrierName);
        btn_search_carrier = mRootView.findViewById(R.id.btn_seach_carrier);
        lv_carrier = mRootView.findViewById(R.id.lv_carrier);
    }

    protected void initData(){
        img_back.setVisibility(View.VISIBLE);
        img_back.setOnClickListener(this);
        tv_title.setText("承运商列表");
        final String[] sp_items = new String[]{"名称","电话"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,sp_items);
        sp_mob_carrier.setAdapter(adapter);
        sp_mob_carrier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        et_carrierName.setHint(getString(R.string.carrier_warn));
                        sType = 0;
                        break;
                    case 1:
                        et_carrierName.setHint(getString(R.string.carrier_warn1));
                        sType = 1;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_search_carrier.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back_a:
                MyFragmentManagerUtil.closeTopFragment(this);
                break;
            case R.id.btn_seach_carrier:
                searchCarriers(sType);
                break;
        }
    }

    protected void searchCarriers(int type){
        if(SoftKeyboardUtils.isSoftShowing(getActivity())){
            SoftKeyboardUtils.hideSystemSoftKeyboard(getActivity());
        }
        RequestParamsFM headparam = new RequestParamsFM();
        headparam.put("X-AUTH-TOKEN", MyApplication.userToken);
        RequestParamsFM params = new RequestParamsFM();
        params.put("ftype",0);
        String text = et_carrierName.getText().toString();
        switch (type){
            case 0:
                params.put("company_name",text);
                break;
            case 1:
                params.put("fmobile_carrier",text);
                break;
        }
        HttpOkhUtils.getInstance().doGetWithHeadParams(NetConfig.APPOINTDRIVER, headparam, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                Log.i(TAG,"网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code != 200) {
                    Log.i(TAG, "网络错误" + code);
                    return;
                }
                if (code != 200) {
                    Log.i(TAG, "网络错误" + code);
                    return;
                }
                Gson gson = new Gson();
                final CarrierInfo info = gson.fromJson(resbody,CarrierInfo.class);
                if(info.isOk()){
                    lv_carrier.setLayoutManager(new LinearLayoutManager(getContext()));
                    CarrierAdapter adapter = new CarrierAdapter(R.layout.item_carrier,getContext(),info.getData());
                    lv_carrier.setAdapter(adapter);
                    adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            supplyGoodsFragment.setAssignId(info.getData().get(position));
                            MyFragmentManagerUtil.closeTopFragment(CarrierFragment.this);
                        }
                    });
                }else{
                    ToastUtils.showToast(getContext(),"未搜寻到相关结果");
                }
            }
        });
    }

    public void setTopFragment(SupplyGoodsFragment fragment) {
        supplyGoodsFragment = fragment;
    }
}
