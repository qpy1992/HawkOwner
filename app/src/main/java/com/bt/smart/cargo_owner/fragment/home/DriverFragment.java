package com.bt.smart.cargo_owner.fragment.home;

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
import com.bt.smart.cargo_owner.utils.CommonUtil;
import com.bt.smart.cargo_owner.utils.HttpOkhUtils;
import com.bt.smart.cargo_owner.utils.MyFragmentManagerUtil;
import com.bt.smart.cargo_owner.utils.RequestParamsFM;
import com.bt.smart.cargo_owner.utils.SoftKeyboardUtils;
import com.bt.smart.cargo_owner.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import java.io.IOException;
import okhttp3.Request;


public class DriverFragment extends Fragment implements View.OnClickListener{
    private View mRootView;
    private ImageView img_back;
    private TextView tv_title,tv_history_driver;
    private Spinner sp_mob_name;
    private EditText et_driverName;
    private Button btn_search_driver;
    private RecyclerView lv_driver,lv_history_driver;
    private SupplyGoodsFragment supplyGoodsFragment;
    private int sType = 0;//搜索条件，0_姓名，1_手机号，默认姓名搜索
    private int mType;
    private static String TAG = "DriverFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_driver, null);
        initView();
        initData();
        return mRootView;
    }

    protected void initView(){
        img_back = mRootView.findViewById(R.id.img_back_a);
        tv_title = mRootView.findViewById(R.id.tv_title);
        tv_history_driver = mRootView.findViewById(R.id.tv_history_driver);
        sp_mob_name = mRootView.findViewById(R.id.sp_mob_name);
        et_driverName = mRootView.findViewById(R.id.et_driverName);
        btn_search_driver = mRootView.findViewById(R.id.btn_seach_driver);
        lv_driver = mRootView.findViewById(R.id.lv_driver);
        lv_history_driver = mRootView.findViewById(R.id.lv_history_driver);
    }

    protected void initData(){
        img_back.setVisibility(View.VISIBLE);
        img_back.setOnClickListener(this);
        if(mType==0){
            tv_title.setText("承运商列表");
            final String[] sp_items = new String[]{"名称","电话"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,sp_items);
            sp_mob_name.setAdapter(adapter);
            sp_mob_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position){
                        case 0:
                            et_driverName.setHint(getString(R.string.carrier_warn));
                            sType = 0;
                            break;
                        case 1:
                            et_driverName.setHint(getString(R.string.carrier_warn1));
                            sType = 1;
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }else{
            tv_title.setText("司机列表");
            final String[] sp_items = new String[]{"姓名","手机号"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,sp_items);
            sp_mob_name.setAdapter(adapter);
            sp_mob_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position){
                        case 0:
                            et_driverName.setHint(getString(R.string.driver_warn));
                            sType = 0;
                            break;
                        case 1:
                            et_driverName.setHint(getString(R.string.driver_warn1));
                            sType = 1;
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        btn_search_driver.setOnClickListener(this);
        setHistoryDrivers();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back_a:
                MyFragmentManagerUtil.closeTopFragment(this);
                break;
            case R.id.btn_seach_driver:
                searchDrivers(sType);
                break;
        }
    }

    protected void setHistoryDrivers(){
        RequestParamsFM headparam = new RequestParamsFM();
        headparam.put("X-AUTH-TOKEN", MyApplication.userToken);
        RequestParamsFM params = new RequestParamsFM();
        params.put("usercode",MyApplication.userCode);
        params.put("type",mType);
        HttpOkhUtils.getInstance().doGetWithHeadParams(NetConfig.COMMONUSE, headparam, params, new HttpOkhUtils.HttpCallBack() {
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
                Gson gson = new Gson();
                final CarrierInfo info = gson.fromJson(resbody,CarrierInfo.class);
                if(info.isOk()){
                    if(mType==0){
                        tv_history_driver.setText(getString(R.string.carriers));
                    }else{
                        tv_history_driver.setText(getString(R.string.drivers));
                    }
                    tv_history_driver.setVisibility(View.VISIBLE);
                    lv_history_driver.setLayoutManager(new LinearLayoutManager(getContext()));
                    CarrierAdapter adapter = new CarrierAdapter(R.layout.item_carrier,getContext(),info.getData());
                    lv_history_driver.setAdapter(adapter);
                    adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            supplyGoodsFragment.setAssignId(info.getData().get(position),sType);
                            MyFragmentManagerUtil.closeTopFragment(DriverFragment.this);
                        }
                    });
                }
            }
        });
    }

    protected void searchDrivers(int type){
        if(SoftKeyboardUtils.isSoftShowing(getActivity())){
            SoftKeyboardUtils.hideSystemSoftKeyboard(getActivity());
        }
        RequestParamsFM headparam = new RequestParamsFM();
        headparam.put("X-AUTH-TOKEN", MyApplication.userToken);
        RequestParamsFM params = new RequestParamsFM();
        params.put("ftype",mType);
        String text = et_driverName.getText().toString();
        if(!CommonUtil.isNotEmpty(text)){
            ToastUtils.showToast(getContext(),et_driverName.getHint()+"");
            return;
        }
        switch (type){
            case 0:
                if(mType==0){
                    params.put("company_name",text);
                }else{
                    params.put("fname_individual",text);
                }
                break;
            case 1:
                if(mType==0) {
                    params.put("fmobile_carrier",text);
                }else{
                    params.put("fmobile_individual", text);
                }
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
                Gson gson = new Gson();
                final CarrierInfo info = gson.fromJson(resbody,CarrierInfo.class);
                if(info.isOk()){
                    lv_driver.setLayoutManager(new LinearLayoutManager(getContext()));
                    CarrierAdapter adapter = new CarrierAdapter(R.layout.item_carrier,getContext(),info.getData());
                    lv_driver.setAdapter(adapter);
                    adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            supplyGoodsFragment.setAssignId(info.getData().get(position),sType);
                            MyFragmentManagerUtil.closeTopFragment(DriverFragment.this);
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

    public void setType(int type) {
        mType = type;
    }
}
