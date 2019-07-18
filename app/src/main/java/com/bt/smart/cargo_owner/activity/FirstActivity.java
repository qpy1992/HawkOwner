package com.bt.smart.cargo_owner.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bt.smart.cargo_owner.MainActivity;
import com.bt.smart.cargo_owner.MyApplication;
import com.bt.smart.cargo_owner.NetConfig;
import com.bt.smart.cargo_owner.R;
import com.bt.smart.cargo_owner.messageInfo.LoginInfo;
import com.bt.smart.cargo_owner.servicefile.GeTuiIntentService;
import com.bt.smart.cargo_owner.utils.HttpOkhUtils;
import com.bt.smart.cargo_owner.utils.ProgressDialogUtil;
import com.bt.smart.cargo_owner.utils.RequestParamsFM;
import com.bt.smart.cargo_owner.utils.SpUtils;
import com.bt.smart.cargo_owner.utils.ToastUtils;
import com.google.gson.Gson;
import com.igexin.sdk.PushManager;

import java.io.IOException;

import okhttp3.Request;

/**
 * @创建者 AndyYan
 * @创建时间 2018/10/29 15:37
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class FirstActivity extends Activity implements View.OnClickListener {
    private TextView tv_new;
    private TextView tv_old;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.first_actiivty);
        MyApplication.flag = 0;
        getView();
        setData();
    }

    private void getView() {
        tv_new = (TextView) findViewById(R.id.tv_new);
        tv_old = (TextView) findViewById(R.id.tv_old);
    }

    private void setData() {
        tv_new.setOnClickListener(this);
        tv_old.setOnClickListener(this);

        Boolean isRemem = SpUtils.getBoolean(this, "isRem", false);
        if (isRemem) {
            String name = SpUtils.getString(this, "name");
            String psd = SpUtils.getString(this, "psd");
            //直接登录
            loginToService(name, psd);
        }
    }

    @Override
    public void onClick(View view) {
        if (!isGetPermission()) {
            requestPermission();
            return;
        }
        switch (view.getId()) {
            case R.id.tv_new:
                //跳转注册界面
                Intent intent1 = new Intent(this, RegisterActivity.class);
                intent1.putExtra("kind", "rgs");
                startActivity(intent1);
                break;
            case R.id.tv_old:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if ((grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                PushManager.getInstance().initialize(this.getApplicationContext(), GeTuiIntentService.class);
                PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), GeTuiIntentService.class);
                PushManager.getInstance().bindAlias(this, MyApplication.userID);
            } else {
                PushManager.getInstance().initialize(this.getApplicationContext(), GeTuiIntentService.class);
                PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), GeTuiIntentService.class);
                PushManager.getInstance().bindAlias(this, MyApplication.userID);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private static final int REQUEST_PERMISSION = 101;

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA},
                REQUEST_PERMISSION);
    }

    private boolean isGetPermission() {
        PackageManager pkgManager = getPackageManager();
        // 读写 sd card 权限非常重要, android6.0默认禁止的, 建议初始化之前就弹窗让用户赋予该权限
        boolean sdCardWritePermission = pkgManager.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getPackageName()) == PackageManager.PERMISSION_GRANTED;
        // read phone state用于获取 imei 设备信息
        boolean phoneSatePermission = pkgManager.checkPermission(Manifest.permission.READ_PHONE_STATE, getPackageName()) == PackageManager.PERMISSION_GRANTED;
        return (sdCardWritePermission & phoneSatePermission);
    }

    private void loginToService(String phone, final String psd) {
        ProgressDialogUtil.startShow(FirstActivity.this, "正在登录请稍后");
        RequestParamsFM params = new RequestParamsFM();
        params.put("fmobile", phone);
        params.put("password", psd);
        HttpOkhUtils.getInstance().doPost(NetConfig.LOGINURL, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ProgressDialogUtil.hideDialog();
                ToastUtils.showToast(FirstActivity.this, "网络连接错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                ProgressDialogUtil.hideDialog();
                if (code != 200) {
                    ToastUtils.showToast(FirstActivity.this, "网络错误" + code);
                    return;
                }
                Gson gson = new Gson();
                LoginInfo loginInfo = gson.fromJson(resbody, LoginInfo.class);
                //ToastUtils.showToast(FirstActivity.this, loginInfo.getMessage());
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
                    MyApplication.userOrderNum = 0;
                    MyApplication.money = loginInfo.getData().getZRegister().getFaccount();
                    MyApplication.userFccountid = loginInfo.getData().getZRegister().getFaccountid();
                    startActivity(new Intent(FirstActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};


    public void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
