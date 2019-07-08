package com.bt.smart.cargo_owner.activity.userAct;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.bt.smart.cargo_owner.MyApplication;
import com.bt.smart.cargo_owner.NetConfig;
import com.bt.smart.cargo_owner.R;
import com.bt.smart.cargo_owner.adapter.BankCardAdapter;
import com.bt.smart.cargo_owner.messageInfo.BCardInfo;
import com.bt.smart.cargo_owner.messageInfo.UpPicInfo;
import com.bt.smart.cargo_owner.utils.HttpOkhUtils;
import com.bt.smart.cargo_owner.utils.MyAlertDialog;
import com.bt.smart.cargo_owner.utils.ProgressDialogUtil;
import com.bt.smart.cargo_owner.utils.RequestParamsFM;
import com.bt.smart.cargo_owner.utils.MyTextUtils;
import com.bt.smart.cargo_owner.utils.ToastUtils;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Request;

public class WithdrawActivity extends AppCompatActivity implements View.OnClickListener {
    TextView wd_back, tv_now, tv_all;
    EditText wd_et;
    Button btn_wd;
    private static String TAG = "WithdrawActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        setViews();
        setListeners();
    }

    protected void setViews() {
        wd_back = findViewById(R.id.wd_back);
        tv_now = findViewById(R.id.tv_now);
        tv_all = findViewById(R.id.tv_all);
        wd_et = findViewById(R.id.wd_et);
        btn_wd = findViewById(R.id.btn_wd);
        tv_now.setText("当前余额" + MyApplication.money + "元，");
    }

    protected void setListeners() {
        wd_back.setOnClickListener(this);
        tv_all.setOnClickListener(this);
        btn_wd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wd_back:
                finish();
                break;
            case R.id.tv_all:
                wd_et.setText("" + MyApplication.money);
                break;
            case R.id.btn_wd:
                String inputPrice = MyTextUtils.getEditTextContent(wd_et);
                if ("".equals(inputPrice)) {
                    ToastUtils.showToast(WithdrawActivity.this, "请输入要提现的金额！");
                } else if (Double.parseDouble(inputPrice) > MyApplication.money) {
                    ToastUtils.showToast(WithdrawActivity.this, "输入金额大于当前金额！");
                } else {
                    BankCard(MyApplication.userID);
                }
                break;
        }
    }

    private void BankCard(String id) {
        RequestParamsFM headParams = new RequestParamsFM();
        headParams.put("X-AUTH-TOKEN", MyApplication.userToken);
        RequestParamsFM params = new RequestParamsFM();
        params.put("fid", id);
        HttpOkhUtils.getInstance().doGetWithHeadParams(NetConfig.BANKCARD, headParams, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ProgressDialogUtil.hideDialog();
                ToastUtils.showToast(WithdrawActivity.this, "网络连接错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                ProgressDialogUtil.hideDialog();
                if (code != 200) {
                    ToastUtils.showToast(WithdrawActivity.this, "网络错误" + code);
                    return;
                }
                Gson gson = new Gson();
                final BCardInfo info = gson.fromJson(resbody, BCardInfo.class);
//                ToastUtils.showToast(WithdrawActivity.this, info.getMessage());
                if (!info.isOk()) {
                    new MyAlertDialog(WithdrawActivity.this, MyAlertDialog.WARNING_TYPE_1)
                            .setContentText("您还未提交任何银行卡，现在绑定？")
                            .setConfirmText("好的")
                            .setCancelText("算了")
                            .showCancelButton(true)
                            .setConfirmClickListener(new MyAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(MyAlertDialog sweetAlertDialog) {
                                    startActivity(new Intent(WithdrawActivity.this, BankCardActivity.class));
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .setCancelClickListener(null)
                            .show();
                } else {
                    //显示银行卡列表
                    if (info.getData().size() == 1) {
                        //只绑了一张银行卡，直接提现
                        withdraw(info.getData().get(0).getId(), wd_et.getText().toString());
                    } else {
                        //绑了多张银行卡，选择一张提现
                        BankCardAdapter adapter = new BankCardAdapter(WithdrawActivity.this, info.getData());
                        ListView lv = new ListView(WithdrawActivity.this);
                        lv.setAdapter(adapter);
                        final AlertDialog dialog = new AlertDialog.Builder(WithdrawActivity.this)
                                .setView(lv).show();
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                //提现
                                withdraw(info.getData().get(i).getId(), wd_et.getText().toString());
                            }
                        });
                    }
                }
            }
        });
    }

    protected void withdraw(String id, String amount) {
        RequestParamsFM headParams = new RequestParamsFM();
        headParams.put("X-AUTH-TOKEN", MyApplication.userToken);
        RequestParamsFM params = new RequestParamsFM();
        params.put("id", id);
        params.put("amount", amount);
        HttpOkhUtils.getInstance().doPostWithHeader(NetConfig.WITHDRAW, headParams, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(WithdrawActivity.this, "网络连接错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                Log.i(TAG, resbody);
                if (code != 200) {
                    ToastUtils.showToast(WithdrawActivity.this, "网络错误" + code);
                    return;
                }
                Gson gson = new Gson();
                UpPicInfo info = gson.fromJson(resbody, UpPicInfo.class);
                if (info.isOk()) {
                    ToastUtils.showToast(WithdrawActivity.this, "提现成功");
                } else {
                    ToastUtils.showToast(WithdrawActivity.this, "提现失败");

                }
            }
        });
    }
}
