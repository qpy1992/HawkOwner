package com.bt.smart.cargo_owner.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bt.smart.cargo_owner.BaseActivity;
import com.bt.smart.cargo_owner.MainActivity;
import com.bt.smart.cargo_owner.MyApplication;
import com.bt.smart.cargo_owner.NetConfig;
import com.bt.smart.cargo_owner.R;
import com.bt.smart.cargo_owner.messageInfo.LoginInfo;
import com.bt.smart.cargo_owner.messageInfo.RuleContentInfo;
import com.bt.smart.cargo_owner.messageInfo.SMSInfo;
import com.bt.smart.cargo_owner.utils.CommonUtil;
import com.bt.smart.cargo_owner.utils.HttpOkhUtils;
import com.bt.smart.cargo_owner.utils.MyAlertDialog;
import com.bt.smart.cargo_owner.utils.ProgressDialogUtil;
import com.bt.smart.cargo_owner.utils.RequestParamsFM;
import com.bt.smart.cargo_owner.utils.SpUtils;
import com.bt.smart.cargo_owner.utils.ToastUtils;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Request;


/**
 * @创建者 AndyYan
 * @创建时间 2018/5/22 9:05
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back;
    private TextView tv_gcode;
    private EditText et_code;
    private EditText edit_num, edit_psd;
    private CheckBox ck_remPas;//记住密码
    private CheckBox cb_agree;//是否同意协议
    private Button bt_login;//登录按钮
    private TextView tv_fgt;//忘记密码
    private TextView tv_seragree;//服务协议
    private TextView tv_pripolicy;//隐私政策
    private boolean isRem = false;
    private boolean useYZCode;//是否用验证码登录
    private String mPhone;
    private String vCode;//记录获取的验证码
    private Handler handler;//用来刷新发送短息按钮
    private int count = 60;//验证码可重新点击发送时间间隔、单位秒
    private boolean isFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_actiivty);
        getView();
        setData();
    }

    private void getView() {
        img_back = (ImageView) findViewById(R.id.img_back);
        tv_gcode = (TextView) findViewById(R.id.tv_gcode);
        et_code = (EditText) findViewById(R.id.et_code);
        edit_num = (EditText) findViewById(R.id.edit_num);
        edit_psd = (EditText) findViewById(R.id.edit_psd);
        ck_remPas = (CheckBox) findViewById(R.id.ck_remPas);
//        ck_remPas.setChecked(true);
//        isRem = true;
        bt_login = (Button) findViewById(R.id.bt_login);
        cb_agree = findViewById(R.id.cb_agree);
        tv_fgt = (TextView) findViewById(R.id.tv_fgt);
        tv_seragree = (TextView) findViewById(R.id.tv_seragree);
        tv_pripolicy = (TextView) findViewById(R.id.tv_pripolicy);
    }

    private void setData() {
        UnderlineSpan underlineSpan = new UnderlineSpan();
        StyleSpan styleSpan_B = new StyleSpan(Typeface.BOLD);
        SpannableString spannableString = new SpannableString("服务协议");
        spannableString.setSpan(underlineSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(styleSpan_B, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_seragree.setText(spannableString);

        SpannableString spannableString2 = new SpannableString("隐私政策");
        spannableString2.setSpan(underlineSpan, 0, spannableString2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString2.setSpan(styleSpan_B, 0, spannableString2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_pripolicy.setText(spannableString2);

        tv_seragree.setOnClickListener(this);
        tv_pripolicy.setOnClickListener(this);

//        cb_agree.setChecked(true);
        Boolean isRemem = SpUtils.getBoolean(LoginActivity.this, "isRem", false);
        if (isRemem) {
            isRem = true;
            ck_remPas.setChecked(true);
            String name = SpUtils.getString(LoginActivity.this, "name");
            String psd = SpUtils.getString(LoginActivity.this, "psd");
            edit_num.setText(name);
            edit_num.setSelection(name.length());
            edit_psd.setText(psd);
            edit_psd.setSelection(psd.length());
            //            //直接登录
            //            loginToService(name, psd);
        }
        ck_remPas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isRem = b;
            }
        });
        img_back.setOnClickListener(this);
//        tv_yzmlogin.setOnClickListener(this);
        bt_login.setOnClickListener(this);
        tv_fgt.setOnClickListener(this);
        tv_gcode.setOnClickListener(this);
        tv_gcode.setVisibility(View.GONE);
        et_code.setVisibility(View.GONE);
        handler = new Handler();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            //            case R.id.tv_seragree://跳转服务协议
            //                Intent intentAgree = new Intent(this,WebRuleActivity.class);
            //                intentAgree.putExtra("rule_type", "0");
            //                startActivity(intentAgree);
            //                break;
            //            case R.id.tv_pripolicy://跳转隐私政策
            //                Intent intentRule = new Intent(this,WebRuleActivity.class);
            //                intentRule.putExtra("rule_type", "1");
            //                startActivity(intentRule);
            //                break;
            case R.id.tv_yzmlogin://切换验证码登录
//                if ("验证码登录".equals(tv_yzmlogin.getText())) {
//                    useYZCode = true;
//                    tv_yzmlogin.setText("密码登录");
//                    edit_psd.setHint("请输入验证码");
//                    edit_psd.setVisibility(View.GONE);
//                    tv_fgt.setVisibility(View.GONE);
//                    ck_remPas.setVisibility(View.GONE);
//                    tv_gcode.setVisibility(View.VISIBLE);
//                    et_code.setVisibility(View.VISIBLE);
//                } else {
//                    useYZCode = false;
//                    tv_yzmlogin.setText("验证码登录");
//                    edit_psd.setHint("请输入密码");
//                    edit_psd.setVisibility(View.VISIBLE);
//                    tv_fgt.setVisibility(View.VISIBLE);
//                    ck_remPas.setVisibility(View.VISIBLE);
//                    tv_gcode.setVisibility(View.GONE);
//                    et_code.setVisibility(View.GONE);
//                }
                break;
            case R.id.tv_gcode://发送验证码
                mPhone = String.valueOf(edit_num.getText()).trim();
                if ("".equals(mPhone) || mPhone.equals("手机号")) {
                    ToastUtils.showToast(this, "请输入手机号码");
                } else {
                    // 账号不匹配手机号格式（11位数字且以1开头）
                    if (mPhone.length() != 11) {
                        ToastUtils.showToast(this, "手机号码格式不正确");
                    } else {
                        //发送验证码
                        sendMsgFromIntnet();
                    }
                }
                break;
            case R.id.tv_fgt://跳转忘记密码界面
                Intent intent = new Intent(this, RegisterActivity.class);
                intent.putExtra("kind", "fgt");
                startActivity(intent);
                break;
            case R.id.bt_login://登录
                String phone = String.valueOf(edit_num.getText()).trim();
                if ("".equals(phone) || "手机号".equals(phone)) {
                    ToastUtils.showToast(LoginActivity.this, "请输入手机号");
                    return;
                } else {
                    // 账号不匹配手机号格式（11位数字且以1开头）
                    if (phone.length() != 11) {
                        ToastUtils.showToast(this, "手机号码格式不正确");
                        return;
                    }
                }

                if (useYZCode) {
//                    if (null == mPhone) {
//                        ToastUtils.showToast(this, "请获取验证码");
//                        return;
//                    }
//                    if (!phone.equals(mPhone)) {
//                        ToastUtils.showToast(this, "您修改了手机号码，请重新获取验证码");
//                        return;
//                    }
//                    String wrtcode = String.valueOf(et_code.getText()).trim();
//                    if ("".equals(wrtcode) || "请输入验证码".equals(wrtcode)) {
//                        ToastUtils.showToast(LoginActivity.this, "请输入验证码");
//                        return;
//                    }
//                    if (!wrtcode.equals(vCode)) {
//                        ToastUtils.showToast(LoginActivity.this, "验证码不正确");
//                        return;
//                    }
//                    //验证码登录
//                    loginWithYZCode(mPhone);
                } else {
                    String psd = String.valueOf(edit_psd.getText()).trim();
                    if ("".equals(psd) || "请输入密码".equals(psd)) {
                        ToastUtils.showToast(LoginActivity.this, "请输入密码");
                        return;
                    }
                    //是否记住账号密码
                    isNeedRem(phone, psd);
                    if(!cb_agree.isChecked()){
                        ToastUtils.showToast(LoginActivity.this, "请同意服务协议与隐私政策");
                        return;
                    }
                    //登录
                    loginToService(phone, psd);
                    // startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
                break;
            case R.id.tv_seragree:
                showSeragree();
                break;
            case R.id.tv_pripolicy:
                showPripolicy();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isFinish = true;
        handler = null;
    }

   /* private void loginWithYZCode(String phone) {
        RequestParamsFM params = new RequestParamsFM();
        params.put("fmobile", phone);
        HttpOkhUtils.getInstance().doPostBean(NetConfig.CodeLOGINURL, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ProgressDialogUtil.hideDialog();
                ToastUtils.showToast(LoginActivity.this, "网络连接错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                ProgressDialogUtil.hideDialog();
                if (code != 200) {
                    ToastUtils.showToast(LoginActivity.this, "网络错误" + code);
                    return;
                }
                Gson gson = new Gson();
                LoginInfo loginInfo = gson.fromJson(resbody, LoginInfo.class);
                ToastUtils.showToast(LoginActivity.this, loginInfo.getMessage());
                if (loginInfo.isOk()) {
                    MyApplication.userToken = loginInfo.getData().getToken();
                    MyApplication.userID = loginInfo.getData().getZRegister().getId();
                    MyApplication.userCode = loginInfo.getData().getZRegister().getUsercode();
                    MyApplication.userName = loginInfo.getData().getZRegister().getCompanyName();
                    MyApplication.userPhone = loginInfo.getData().getZRegister().getFmobile();
                    MyApplication.checkStatus = loginInfo.getData().getZRegister().getCheckStatus();
                    MyApplication.companyContract = loginInfo.getData().getZRegister().getCompanyContract();
                    MyApplication.userHeadPic = loginInfo.getData().getZRegister().getCompanyLicence();
                    MyApplication.userOrderNum = 0;
                    MyApplication.money = loginInfo.getData().getZRegister().getFaccount();
                    MyApplication.userType = loginInfo.getData().getZRegister().getFtype();
                    MyApplication.userFccountid = loginInfo.getData().getZRegister().getFaccountid();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }*/

    private void loginToService(String phone, final String psd) {
        ProgressDialogUtil.startShow(LoginActivity.this, "正在登录请稍后");
        RequestParamsFM params = new RequestParamsFM();
        params.put("fmobile", phone);
        params.put("password", psd);
        HttpOkhUtils.getInstance().doPost(NetConfig.LOGINURL, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ProgressDialogUtil.hideDialog();
                ToastUtils.showToast(LoginActivity.this, "网络连接错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                ProgressDialogUtil.hideDialog();
                if (code != 200) {
                    ToastUtils.showToast(LoginActivity.this, "网络错误" + code);
                    return;
                }
                Gson gson = new Gson();
                LoginInfo loginInfo = gson.fromJson(resbody, LoginInfo.class);
                ToastUtils.showToast(LoginActivity.this, loginInfo.getMessage());
                if (loginInfo.isOk()) {
                    MyApplication.userToken = loginInfo.getData().getToken();
                    MyApplication.userID = loginInfo.getData().getZRegister().getId();
                    MyApplication.userCode = loginInfo.getData().getZRegister().getUsercode();
                    MyApplication.userType = loginInfo.getData().getZRegister().getFtype();
                    if ("2".equals(MyApplication.userType)) {
                        MyApplication.userName = loginInfo.getData().getZRegister().getCompanyName();
                    } else {
                        MyApplication.userName = loginInfo.getData().getZRegister().getCompanyLxr();
                    }
                    MyApplication.userPhone = loginInfo.getData().getZRegister().getFmobile();
                    MyApplication.checkStatus = loginInfo.getData().getZRegister().getCheckStatus();
                    MyApplication.companyContract = loginInfo.getData().getZRegister().getCompanyContract();
                    MyApplication.userHeadPic = loginInfo.getData().getZRegister().getCompanyLicence();
                    MyApplication.money = loginInfo.getData().getZRegister().getFaccount();
                    MyApplication.userOrderNum = 0;
                    MyApplication.paccountid = loginInfo.getData().getZRegister().getPaccountid();
                    MyApplication.userFccountid = loginInfo.getData().getZRegister().getFaccountid();
                    if(loginInfo.getData().getZRegister().getCheckface()==1){
                        MyApplication.checkFace = true;
                    }else {
                        MyApplication.checkFace = false;
                    }
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }

    private void sendMsgFromIntnet() {
        RequestParamsFM params = new RequestParamsFM();
        params.put("fmobile", mPhone);
        HttpOkhUtils.getInstance().doPostBean(NetConfig.CHECKMESSAGE, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(LoginActivity.this, "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code != 200) {
                    ToastUtils.showToast(LoginActivity.this, "发送失败");
                    return;
                }
                Gson gson = new Gson();
                SMSInfo sendSMSInfo = gson.fromJson(resbody, SMSInfo.class);
                if (1 == sendSMSInfo.getResult()) {
                    ToastUtils.showToast(LoginActivity.this, "验证码发送成功");
                    vCode = sendSMSInfo.getCode();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            handler.postDelayed(this, 1000);//递归执行，一秒执行一次
                            if (!isFinish) {
                                if (count > 0) {
                                    count--;
                                    tv_gcode.setBackground(getResources().getDrawable(R.drawable.bg_roundcorder_gray));
                                    tv_gcode.setText(count + "秒后可重新发送");
                                    tv_gcode.setClickable(false);
                                } else {
                                    count = 60;
                                    tv_gcode.setBackground(getResources().getDrawable(R.drawable.bg_round_blue_50));
                                    tv_gcode.setText("发送验证码");
                                    tv_gcode.setClickable(true);
                                    handler.removeCallbacks(this);
                                }
                            }
                        }
                    }, 1000);    //第一次执行，一秒之后。第一次执行完就没关系了
                } else {
                    ToastUtils.showToast(LoginActivity.this, "验证码获取失败");
                }
            }
        });
    }

    private void isNeedRem(String name, String psd) {
        SpUtils.putBoolean(LoginActivity.this, "isRem", isRem);
        if (isRem) {
            SpUtils.putString(LoginActivity.this, "name", name);
            SpUtils.putString(LoginActivity.this, "psd", psd);
        }
    }

    protected void showSeragree(){
        HttpOkhUtils.getInstance().doGet(NetConfig.CONTENT+"/A0002", new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(LoginActivity.this, "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (200 != code) {
                    ToastUtils.showToast(LoginActivity.this, "网络错误" + code);
                    return;
                }
                Gson gson = new Gson();
                RuleContentInfo ruleContentInfo = gson.fromJson(resbody, RuleContentInfo.class);
//                ToastUtils.showToast(LoginActivity.this, ruleContentInfo.getMessage());
                if (ruleContentInfo.isOk()) {
                    new MyAlertDialog(LoginActivity.this)
                            .setTitleText(getString(R.string.seragree))
                            .setContentText(CommonUtil.toPlainText(ruleContentInfo.getData().getFcontent())).show();
                }
            }
        });
    }

    protected void showPripolicy(){
        HttpOkhUtils.getInstance().doGet(NetConfig.CONTENT + "/A0003", new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(LoginActivity.this, "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (200 != code) {
                    ToastUtils.showToast(LoginActivity.this, "网络错误" + code);
                    return;
                }
                Gson gson = new Gson();
                RuleContentInfo ruleContentInfo = gson.fromJson(resbody, RuleContentInfo.class);
//                ToastUtils.showToast(LoginActivity.this, ruleContentInfo.getMessage());
                if (ruleContentInfo.isOk()) {
                    new MyAlertDialog(LoginActivity.this)
                            .setTitleText(getString(R.string.pripolicy))
                            .setContentText(CommonUtil.toPlainText(ruleContentInfo.getData().getFcontent())).show();
                }
            }
        });
    }
}
