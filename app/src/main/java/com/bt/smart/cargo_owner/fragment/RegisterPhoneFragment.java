package com.bt.smart.cargo_owner.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bt.smart.cargo_owner.NetConfig;
import com.bt.smart.cargo_owner.R;
import com.bt.smart.cargo_owner.activity.LoginActivity;
import com.bt.smart.cargo_owner.messageInfo.CommenInfo;
import com.bt.smart.cargo_owner.messageInfo.CommonInfo;
import com.bt.smart.cargo_owner.messageInfo.RegisterInfo;
import com.bt.smart.cargo_owner.messageInfo.RuleContentInfo;
import com.bt.smart.cargo_owner.messageInfo.SMSInfo;
import com.bt.smart.cargo_owner.utils.CommonUtil;
import com.bt.smart.cargo_owner.utils.HttpOkhUtils;
import com.bt.smart.cargo_owner.utils.MyAlertDialog;
import com.bt.smart.cargo_owner.utils.MyTextUtils;
import com.bt.smart.cargo_owner.utils.ProgressDialogUtil;
import com.bt.smart.cargo_owner.utils.RequestParamsFM;
import com.bt.smart.cargo_owner.utils.ToastUtils;
import com.google.gson.Gson;
import java.io.IOException;
import okhttp3.Request;


/**
 * @创建者 AndyYan
 * @创建时间 2018/10/30 17:03
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class RegisterPhoneFragment extends Fragment implements View.OnClickListener {
    private View mRootView;
    private ImageView img_back;
    private TextView tv_regis;//标题
    private RelativeLayout rlt_ftype;
    private CheckBox ck_per;//个人
    private CheckBox ck_com;//企业
    private EditText et_phone;
    private TextView tv_gcode,tv_seragree_reg,tv_pripolicy_reg;//点击获取验证码
    private EditText et_code;//填写验证码
    private EditText et_psd;//填写密码
    private EditText et_rec;//推荐人
    private CheckBox cb_agree_reg;//是否同意协议
    private TextView tv_submit;//确认提交

    private String mPhone;
    private String vCode;//记录获取的验证码
    private String mKind;//记录哪个页面跳转过来的。fgt是忘记密码，rgs是注册
    private Handler handler;//用来刷新发送短息按钮
    private int count = 60;//验证码可重新点击发送时间间隔、单位秒
    private boolean isFinish = false;//是否关闭页面

    private String roleType = "3";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = LayoutInflater.from(getActivity()).inflate(R.layout.frame_regis_phone, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        img_back = mRootView.findViewById(R.id.img_back);
        rlt_ftype = mRootView.findViewById(R.id.rlt_ftype);
        ck_per = mRootView.findViewById(R.id.ck_per);
        et_rec = mRootView.findViewById(R.id.et_rec);
        ck_com = mRootView.findViewById(R.id.ck_com);
        tv_regis = mRootView.findViewById(R.id.tv_regis);
        et_phone = mRootView.findViewById(R.id.et_phone);
        et_code = mRootView.findViewById(R.id.et_code);
        et_psd = mRootView.findViewById(R.id.et_psd);
        cb_agree_reg = mRootView.findViewById(R.id.cb_agree_reg);
        tv_gcode = mRootView.findViewById(R.id.tv_gcode);
        tv_submit = mRootView.findViewById(R.id.tv_submit);
        tv_seragree_reg = mRootView.findViewById(R.id.tv_seragree_reg);
        tv_pripolicy_reg = mRootView.findViewById(R.id.tv_pripolicy_reg);
    }

    private void initData() {
        UnderlineSpan underlineSpan = new UnderlineSpan();
        StyleSpan styleSpan_B = new StyleSpan(Typeface.BOLD);
        SpannableString spannableString = new SpannableString("服务协议");
        spannableString.setSpan(underlineSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(styleSpan_B, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_seragree_reg.setText(spannableString);

        SpannableString spannableString2 = new SpannableString("隐私政策");
        spannableString2.setSpan(underlineSpan, 0, spannableString2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString2.setSpan(styleSpan_B, 0, spannableString2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_pripolicy_reg.setText(spannableString2);
        handler = new Handler();
        img_back.setOnClickListener(this);
//        cb_agree_reg.setChecked(true);
        if ("fgt".equals(mKind)) {
            tv_regis.setText("忘记密码");
        } else {
            tv_regis.setText("注册");
            rlt_ftype.setVisibility(View.VISIBLE);
            et_rec.setVisibility(View.VISIBLE);
            //设置角色选择按钮
            setCheckState();
        }
        tv_gcode.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
        tv_seragree_reg.setOnClickListener(this);
        tv_pripolicy_reg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;
            case R.id.tv_gcode://获取验证码
                mPhone = String.valueOf(et_phone.getText()).trim();
                if ("".equals(mPhone) || mPhone.equals("手机号")) {
                    ToastUtils.showToast(getContext(), "请输入手机号码");
                } else {
                    // 账号不匹配手机号格式（11位数字且以1开头）
                    if (mPhone.length() != 11) {
                        ToastUtils.showToast(getContext(), "手机号码格式不正确");
                    } else {
                        //发送验证码
                        sendMsgFromIntnet();
                    }
                }
                break;
            case R.id.tv_submit:
                String newPhone = String.valueOf(et_phone.getText()).trim();
                if (null == mPhone) {
                    ToastUtils.showToast(getContext(), "请获取验证码");
                    return;
                }
                if (!newPhone.equals(mPhone)) {
                    ToastUtils.showToast(getContext(), "您修改了手机号码，请重新获取验证码");
                    return;
                }
                String wrtcode = String.valueOf(et_code.getText()).trim();
                String wrtpsd = String.valueOf(et_psd.getText()).trim();
                if ("".equals(wrtcode) || "请输入验证码".equals(wrtcode)) {
                    ToastUtils.showToast(getContext(), "请输入验证码");
                    return;
                }
                if (!wrtcode.equals(vCode)) {
                    ToastUtils.showToast(getContext(), "验证码不正确");
                    return;
                }
                if ("".equals(wrtpsd) || "请设置密码".equals(wrtpsd)) {
                    ToastUtils.showToast(getContext(), "请设置密码");
                    return;
                }
                if ("fgt".equals(mKind)) {//修改密码
                    changePsd(mPhone, wrtpsd);
                } else {//注册
                    if(!cb_agree_reg.isChecked()){
                        ToastUtils.showToast(getContext(),getString(R.string.check));
                        return;
                    }
                    //判断是否选择个人或企业
                    if (ck_per.isChecked()) {
                        roleType = "3";
                    } else {
                        roleType = "2";
                    }
                    String recommen = MyTextUtils.getEditTextContent(et_rec);
                    if ("推荐人".equals(recommen)) {
                        recommen = "";
                    }
                    registMember(mPhone, wrtpsd, recommen);
                }
                break;
            case R.id.tv_seragree_reg:
                showSeragree();
                break;
            case R.id.tv_pripolicy_reg:
                showPripolicy();
                break;
        }
    }

    private void setCheckState() {
        ck_per.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ck_per.setChecked(true);
            }
        });
        ck_per.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    roleType = "3";
                    ck_com.setChecked(false);
                }
            }
        });
        ck_com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ck_com.setChecked(true);
            }
        });
        ck_com.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    roleType = "2";
                    ck_per.setChecked(false);
                }
            }
        });
    }

    private void registMember(String phone, String wrtpsd, String recom) {//fmobile:手机号，password:密码，recommd:推荐人，ftype:类型
        RequestParamsFM params = new RequestParamsFM();
        params.put("ftype", roleType);
        params.put("fmobile", phone);
        params.put("password", wrtpsd);
        params.put("recommd", recom);
        ProgressDialogUtil.startShow(getContext(),"正在提交...");
        HttpOkhUtils.getInstance().doPostByUrl(NetConfig.REGISTERDRIVER, null, "zRegisterJson", params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ProgressDialogUtil.hideDialog();
                ToastUtils.showToast(getContext(), "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                ProgressDialogUtil.hideDialog();
                if (code != 200) {
                    ToastUtils.showToast(getContext(), "网络连接错误" + code);
                    return;
                }
                Gson gson = new Gson();
                RegisterInfo sendSMSInfo = gson.fromJson(resbody, RegisterInfo.class);
                if (sendSMSInfo.isOk()) {
                    isFinish = true;
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    getActivity().finish();
                }else{
                    ToastUtils.showToast(getContext(),sendSMSInfo.getMessage());
                }
            }
        });
    }

    private void changePsd(String phone, String wrtpsd) {
        RequestParamsFM params = new RequestParamsFM();
        params.put("fmobile", phone);
        params.put("fpassword", wrtpsd);
        HttpOkhUtils.getInstance().doPost(NetConfig.BACKFPASSWORDBYMOBILE, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(getContext(), "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code != 200) {
                    ToastUtils.showToast(getContext(), "网络连接错误" + code);
                    return;
                }
                Gson gson = new Gson();
                CommenInfo sendSMSInfo = gson.fromJson(resbody, CommenInfo.class);
                ToastUtils.showToast(getContext(), sendSMSInfo.getData().toString());
                if (sendSMSInfo.isOk()) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    isFinish = true;
                    getActivity().finish();
                }
            }
        });
    }

    private void sendMsgFromIntnet() {
        RequestParamsFM params = new RequestParamsFM();
        params.put("fmobile", mPhone);
        HttpOkhUtils.getInstance().doPost(NetConfig.CHECKMESSAGE, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(getContext(), "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code != 200) {
                    ToastUtils.showToast(getContext(), "发送失败");
                    return;
                }
                Gson gson = new Gson();
                SMSInfo sendSMSInfo = gson.fromJson(resbody, SMSInfo.class);
                if (1 == sendSMSInfo.getResult()) {
                    ToastUtils.showToast(getContext(), "验证码发送成功");
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
                    ToastUtils.showToast(getContext(), "验证码获取失败");
                }
            }
        });
    }

    protected void showSeragree(){
        HttpOkhUtils.getInstance().doGet(NetConfig.CONTENT+"/A0002", new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(getContext(), "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (200 != code) {
                    ToastUtils.showToast(getContext(), "网络错误" + code);
                    return;
                }
                Gson gson = new Gson();
                RuleContentInfo ruleContentInfo = gson.fromJson(resbody, RuleContentInfo.class);
//                ToastUtils.showToast(getContext(), ruleContentInfo.getMessage());
                if (ruleContentInfo.isOk()) {
                    new MyAlertDialog(getContext())
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
                ToastUtils.showToast(getContext(), "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (200 != code) {
                    ToastUtils.showToast(getContext(), "网络错误" + code);
                    return;
                }
                Gson gson = new Gson();
                RuleContentInfo ruleContentInfo = gson.fromJson(resbody, RuleContentInfo.class);
//                ToastUtils.showToast(getContext(), ruleContentInfo.getMessage());
                if (ruleContentInfo.isOk()) {
                    new MyAlertDialog(getContext())
                            .setTitleText(getString(R.string.pripolicy))
                            .setContentText(CommonUtil.toPlainText(ruleContentInfo.getData().getFcontent())).show();
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void setKind(String kind) {
        this.mKind = kind;
    }
}
